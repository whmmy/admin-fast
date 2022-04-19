<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="输入关键词"
        style="width: 200px; margin-right: 10px;"
        class="filter-item"
        clearable
        @keyup.enter.native="handleFilter"
      />

      <el-button
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >查询</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-plus"
        @click="handleCreate"
      >增加</el-button>
    </div>
    <div class="tableContainer">
      <el-table
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        height="100%"
        style="width: 100%;"
      >
        <el-table-column label="序号" align="center" min-width="100">
          <template slot-scope="{row}">
            <span>{{ row.id }}</span>
          </template>
        </el-table-column>

        <el-table-column label="角色名称" align="center" min-width="120">
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="角色编码" align="center" min-width="120">
          <template slot-scope="{row}">
            <span>{{ row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column label="描述" align="center" min-width="150">
          <template slot-scope="{row}">
            <span>{{ row.description }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="200px">
          <template slot-scope="{row}">
            <template v-if="row.code != 'admin'">
              <el-button type="text" size="small" @click="handleSelectResource(row)">分配资源</el-button>
              <el-button type="text" size="small" @click="handleUpdate(row)">编辑</el-button>
              <el-button v-if="row.status == 1" size="small" type="text" @click="handleUpdateStatus(row,0)">禁用</el-button>
              <el-button v-if="row.status == 0" size="small" type="text" @click="handleUpdateStatus(row,1)">启用</el-button>
            </template>
            <template v-else>
              超级管理无法修改
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.pageNum"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="45%">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="right"
        label-width="20%"
        style="width: 100%;"
      >
        <el-form-item label="角色名" prop="name">
          <el-input v-model="temp.name" style="width:84%;" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="temp.code" style="width:84%;" />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="temp.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" style="width:84%;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="createData()">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="editFormVisible" width="45%">
      <el-form
        ref="editForm"
        :rules="rules"
        :model="editTemp"
        label-position="right"
        label-width="20%"
        style="width: 100%;"
      >
        <el-form-item label="角色名" prop="name">
          <el-input v-model="editTemp.name" style="width:84%;" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="editTemp.code" style="width:84%;" />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="editTemp.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" style="width:84%;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import roleApi from '@/api/systemRole'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const roleOptions = [
  { key: 1, display_name: '系统管理员' },
  { key: 2, display_name: '普通用户' },
  { key: 3, display_name: '活码用户' }
]

const roleKeyValue = roleOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'Systemuser',
  components: { Pagination },
  filters: {
    roleFilter(type) {
      return roleKeyValue[type]
    },
    parseTime
  },
  data() {
    return {
      role: '',
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        keyword: '',
        type: undefined,
        role: ''
      },
      roleOptions,
      temp: {
        userName: '',
        role: '',
        password: '',
        passwordConfirm: ''
      },
      editTemp: {},
      dialogFormVisible: false,
      editFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增'
      },
      rules: {
        name: [{ required: true, message: '请输入角色名', trigger: 'blur' }],
        code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
      },
      downloadLoading: false,
      isDisabled: false
    }
  },
  mounted() {
  },
  created() {
    this.getList()
  },
  methods: {
    getList(pagination) {
      this.listLoading = true
      roleApi.list(this.listQuery).then(response => {
        this.total = response.value.page.total
        this.list = response.value.page.list
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    resetTemp() {
      // console.log(this.$refs['role'])
      this.temp = {
        userName: '',
        role: '',
        password: '',
        passwordConfirm: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          roleApi.create(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    handleUpdate(row) {
      this.editTemp = {
        id: row.id,
        name: row.name,
        code: row.code,
        description: row.description
      }
      this.editFormVisible = true
      this.dialogStatus = 'update'
      this.isDisabled = true
    },
    updateData() {
      this.$refs['editForm'].validate(valid => {
        if (valid) {
          const tempData = Object.assign({}, this.editTemp)
          roleApi.update(tempData.id, tempData).then(() => {
            for (const v of this.list) {
              if (v.id === this.editTemp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.editTemp)
                break
              }
            }
            this.getList()
            this.editFormVisible = false
            this.$notify({
              title: '成功',
              message: '修改成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        roleApi.delete(row.id).then(res => {
          this.getList()
          this.$message.success('删除成功！')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    handleUpdateStatus(row, status) {
      this.$confirm('此操作将改变角色状态, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        roleApi.updateStatus(row.id, status).then(res => {
          this.getList()
          this.$message.success('操作成功！')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
      })
    },
    handleSelectResource(row) {
      this.$router.push({ path: '/systemManage/allocResource', query: { roleId: row.id }})
    }
  }
}

</script>

<style>
	.filter-container {
		padding-bottom: 10px;
	}
</style>
