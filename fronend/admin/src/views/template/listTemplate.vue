<template>
  <div class="app-container">
    <div class="filter-container" style="margin-bottom: 10px">
      <el-input
        v-model="listQuery.keyword"
        placeholder="输入关键词"
        style="width: 200px; margin-right: 10px;"
        class="filter-item"
        clearable
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
        type="success"
        icon="el-icon-plus"
        @click="handleCreate"
      >增加</el-button>
    </div>

    <el-table
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="编号" align="center" width="100px">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="名称" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250px">
        <template slot-scope="{row}">
          <el-button type="primary" size="small" @click="edit(row)">修改</el-button>
          <el-button type="danger" size="small" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.pageNum"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <el-dialog class="dialog" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="50%" @close="handleClose">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="right"
        label-width="15%"
        style="width: 100%;"
      >
        <el-form-item label="名称" prop="name" required>
          <el-input
            v-model="temp.name"
            placeholder="请输入标题"
            style="width: 85%;"
            clearable
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus == 'create'" type="primary" @click="createConfig()">创建</el-button>
        <el-button v-if="dialogStatus == 'update'" type="primary" @click="editConfig()">更新</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script>
import templateApi from '@/api/template'
import Pagination from '@/components/Pagination'
export default {
  components: {
    Pagination
  },
  filters: {
  },
  data() {
    return {
      list: [],
      total: 0,
      listQuery: {
        keyword: '',
        pageNum: 1,
        pageSize: 10
      },
      textMap: {
        create: '新增',
        update: '修改'
      },
      dialogStatus: '',
      dialogFormVisible: false,
      temp: {
        name: ''
      },
      rules: {
        name: [{ required: true, message: '请输入标题', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.handleFilter()
  },
  mounted() {
  },
  methods: {
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    getList() {
      templateApi.list(this.listQuery).then(res => {
        const pageData = res.value.page
        this.list = pageData.list
        this.total = pageData.total
      })
    },
    handleCreate() {
      if (this.$refs['dataForm']) {
        this.$refs['dataForm'].clearValidate()
      }
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createConfig() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          templateApi.create(this.temp).then(res => {
            this.$message.success('保存成功')
            this.dialogFormVisible = false
            this.getList()
          })
        }
      })
    },
    editConfig() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          templateApi.update(this.temp).then(res => {
            this.$message.success('修改成功')
            this.dialogFormVisible = false
            this.getList()
          })
        }
      })
    },
    handleClose() {
      this.temp = {
        name: ''
      }
      this.$refs['dataForm'].clearValidate()
    },
    edit(row) {
      this.dialogStatus = 'update'
      this.temp = row
      this.dialogFormVisible = true
    },
    del(row) {
      this.$confirm('是否删除该项目?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        templateApi.delete(row.id).then(res => {
          this.getList()
        })
      }).catch(() => {})
    }
  }
}
</script>
<style lang="scss" scoped>

</style>
