<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.nameKeyword"
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
        v-if="listQuery.categoryId != 0 "
        class="filter-item"
        style="margin-left: 10px;"
        icon="el-icon-back"
        @click="handleBack"
      >返回上级</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="序号" align="center">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column label="资源/菜单名称" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="资源类型" align="center">
        <template slot-scope="{row}">
          <span>{{ row.type | resourceTypeFilter }}</span>
        </template>
      </el-table-column>

      <el-table-column label="资源编码" align="center">
        <template slot-scope="{row}">
          <span>{{ row.code === ""?'-': row.code }}</span>
        </template>
      </el-table-column>

      <el-table-column label="排序" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sort }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button size="small" @click="showNextLevel(row)">查看下级</el-button>
          <el-button type="primary" size="small" @click="handleUpdate(row)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.pageNum"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="editFormVisible" width="45%">
      <el-form
        ref="editForm"
        :rules="editRules"
        :model="editTemp"
        label-position="right"
        label-width="20%"
        style="width: 100%;"
      >
        <el-form-item label="资源/菜单名称">
          <el-input v-model="editTemp.name" style="width:84%;" />
        </el-form-item>

        <el-form-item label="资源类型" prop="type">
          <el-select
            v-model="editTemp.type"
            class="filter-item"
            placeholder="请选择"
            style="width:84%;"
            disabled
          >
            <el-option
              v-for="item in roleOptions"
              :key="item.key"
              :label="item.display_name"
              :value="item.key"
              :disabled="item.disabled"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="路由名称" prop="code">
          <el-input v-model="editTemp.code" style="width:84%;" disabled />
        </el-form-item>

        <el-form-item label="排序" prop="sort">
          <!-- <el-input v-model="editTemp.sort" style="width:84%;" /> -->
          <el-input-number v-model="editTemp.sort" :min="0" :max="99" label="仅支持 0-99" />
        </el-form-item>

        <el-form-item label="资源描述" prop="description">
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
import resourceApi from '@/api/systemUserResource'

import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

const roleOptions = [
  { key: 1, display_name: '资源模块' },
  { key: 2, display_name: '资源菜单' },
  { key: 3, display_name: '资源按钮' }
]

const roleKeyValue = roleOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'Systemuser',
  components: { Pagination },
  filters: {
    resourceTypeFilter(type) {
      return roleKeyValue[type]
    }
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
        nameKeyword: '',
        type: undefined,
        categoryId: 0,
        role: ''
      },
      roleOptions,
      editTemp: {},
      editFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增'
      },
      editRules: {
        name: [{ required: true, message: '输入资源/菜单名称', trigger: 'blur' }]
      },
      historyCategoryId: 0
    }
  },
  mounted() {
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      resourceApi.list(this.listQuery).then(response => {
        this.total = response.value.page.total
        this.list = response.value.page.list
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleBack() {
      this.listQuery.categoryId = this.historyCategoryId
      this.historyCategoryId = 0
      this.getList()
    },
    showNextLevel(row) {
      console.log(row)
      this.historyCategoryId = this.listQuery.categoryId
      this.listQuery.categoryId = row.id
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleUpdate(row) {
      this.editTemp = {
        id: row.id,
        type: row.type,
        name: row.name,
        code: row.code,
        description: row.description,
        sort: row.sort
      }
      this.editFormVisible = true
      this.dialogStatus = 'update'
    },
    updateData() {
      this.$refs['editForm'].validate(valid => {
        if (valid) {
          const tempData = Object.assign({}, this.editTemp)
          resourceApi.update(tempData).then(() => {
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
      this.$confirm('此操作将删除,此为关键项请确认无影响后删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        resourceApi.delete(row.id).then(res => {
          this.getList()
          this.$message.success('删除成功！')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}

</script>

<style>
	.filter-container {
		padding-bottom: 10px;
	}
</style>
