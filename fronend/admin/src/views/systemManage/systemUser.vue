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
        v-if="haveRole('managerUserEdit')"
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-plus"
        @click="handleCreate"
      >增加</el-button>
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
          <span>{{ row.userId }}</span>
        </template>
      </el-table-column>

      <el-table-column label="用户名" align="center">
        <template slot-scope="{row}">
          <span>{{ row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属公司" align="center">
        <template slot-scope="{row}">
          <span>{{ row.companyId == 0 ? '全局账号':row.companyName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button v-if="haveRole('managerUserEdit')" size="small" @click="handleSelectRole(row)">分配角色</el-button>
          <el-button type="primary" size="small" @click="handleUpdate(row)">编辑</el-button>
          <el-button
            v-if="haveRole('managerUserEdit')"
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
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
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
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="temp.userName" style="width:84%;" />
        </el-form-item>

        <el-form-item v-if="globals.currentUser.companyId == 0" label="所属公司">
          <el-select v-model="temp.companyId" placeholder="请选择" size="small" style="width: 80%">
            <el-option label="全局账号" :value="0" />
            <el-option v-for="item in allCompanyList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="temp.phone" type="text" style="width:84%;" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="temp.email" type="text" style="width:84%;" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="temp.password" type="password" style="width:84%;" />
        </el-form-item>

        <el-form-item label="确认密码" prop="passwordConfirm">
          <el-input v-model="temp.passwordConfirm" type="password" style="width:84%;" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="temp.remark" type="text" style="width:84%;" />
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
        :rules="editRules"
        :model="editTemp"
        label-position="right"
        label-width="20%"
        style="width: 100%;"
      >
        <el-form-item label="用户名">
          <el-input v-model="editTemp.userName" :disabled="true" style="width:84%;" />
        </el-form-item>
        <el-form-item v-if="globals.currentUser.companyId == 0" label="所属公司">
          <el-select v-model="editTemp.companyId" placeholder="请选择" size="small" style="width: 80%">
            <el-option label="全局账号" :value="0" />
            <el-option v-for="item in allCompanyList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-input v-model="editTemp.phone" style="width:84%;" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editTemp.email" style="width:84%;" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="editTemp.password" type="password" style="width:84%;" />
        </el-form-item>

        <el-form-item label="确认密码" prop="passwordConfirm">
          <el-input v-model="editTemp.passwordConfirm" type="password" style="width:84%;" />
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="editTemp.remark" style="width:84%;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editFormVisible = false">取消</el-button>
        <el-button v-if="haveRole('managerUserEdit')" type="primary" @click="updateData()">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="分配角色" :visible.sync="allocDialogVisible" width="30%">
      <el-select v-model="allocRoleIds" multiple placeholder="请选择" size="small" style="width: 80%">
        <el-option v-for="item in showRoleList" :key="item.id" :label="item.name" :value="item.id" :disabled="item.sort == 0" />
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="allocDialogVisible = false">取 消</el-button>
        <el-button type="primary" size="small" @click="handleAllocDialogConfirm()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import userApi from '@/api/user'
import authApi from '@/api/auth'
import { parseTime } from '@/utils'
import { haveRole } from '@/utils/auth'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import userCompanyIdApi from '@/api/userCompany'
export default {
  name: 'Systemuser',
  components: { Pagination },
  filters: {
    parseTime
  },
  data() {
    var validatePass = (rule, value, callback) => {
      if (value !== this.temp.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    var validateEditPass = (rule, value, callback) => {
      if (value !== this.editTemp.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      role: '',
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        keyword: '',
        type: undefined,
        role: ''
      },
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
        // role: [{ required: true, message: '请选择角色', trigger: 'blur' }],
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          {
            pattern: /^[a-zA-Z\d]{3,18}$/,
            message: '用户名必须为3-18位字母或数字',
            trigger: ['change', 'blur']
          }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 18, message: '密码长度在 6 到 18 位', trigger: 'blur' }
        ],
        passwordConfirm: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ]
      },
      editRules: {
        // role: [{ required: true, message: '请选择角色', trigger: 'blur' }],
        password: [
          {
            min: 6,
            max: 18,
            message: '密码长度在 6 到 18 位',
            trigger: ['blur', 'change']
          }
        ],
        passwordConfirm: [
          { validator: validateEditPass, trigger: ['blur', 'change'] }
        ]
      },
      downloadLoading: false,
      isDisabled: false,
      showRoleList: [],
      allRoleList: [],
      allocDialogVisible: false,
      allocRoleIds: [],
      allCompanyList: []
    }
  },
  computed: {
    globals() {
      return this.$store.state.user.globals
    }
  },
  mounted() {
  },
  created() {
    this.getList()
    this.getAllRoleList()
    this.getAllCompany()
    console.log(this.globals)
  },
  methods: {
    getList(pagination) {
      this.listLoading = true
      userApi.fetchList(this.listQuery).then(response => {
        this.total = response.value.page.total
        this.list = response.value.page.list
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
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
          authApi.keypair().then(res => {
            userApi.createSystemuser(this.temp, res.exponent, res.modulus).then(() => {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.editTemp = {
        userId: row.userId,
        userName: row.userName,
        companyId: row.companyId,
        phone: row.phone,
        email: row.email,
        remark: row.remark,
        role: row.role
      }
      this.editFormVisible = true
      this.dialogStatus = 'update'
      this.isDisabled = true
    },
    updateData() {
      this.$refs['editForm'].validate(valid => {
        if (valid) {
          // 设置有效期（把时间选择器的时间数据处理后给temp）

          const tempData = Object.assign({}, this.editTemp)
          // console.log(tempData, "tempData");
          // tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          authApi.keypair().then(res => {
            userApi.updateSystemuser(tempData, res.exponent, res.modulus).then(() => {
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
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userApi.delSystemUser(row.userId).then(res => {
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
    getAllRoleList() {
      userApi.listAllRole().then(res => {
        this.allRoleList = res.value.roleList
      })
    },
    getAllCompany() {
      userCompanyIdApi.listAll().then(res => {
        this.allCompanyList = res.value.list
      })
    },
    handleSelectRole(row) {
      this.allocAdminId = row.userId
      this.allocDialogVisible = true
      this.getRoleListByAdmin(row.userId)
    },
    getRoleListByAdmin(userId) {
      userApi.getRoleList(userId).then(response => {
        const allocRoleList = response.value.roleList
        this.allocRoleIds = []
        if (allocRoleList != null && allocRoleList.length > 0) {
          for (let i = 0; i < allocRoleList.length; i++) {
            this.allocRoleIds.push(allocRoleList[i].id)
          }
        }
        // 将显示当前已经拥有的角色和属于当前公司的角色
        this.showRoleList = this.allRoleList.filter(a => {
          const isInArray = (array, key) => {
            for (var i = 0; i < array.length; i++) {
              if (array[i] === key) {
                return true
              }
            }
            return false
          }
          return a.sort === 1 || isInArray(this.allocRoleIds, a.id)
        })
        console.log(this.showRoleList)
      })
    },
    handleAllocDialogConfirm() {
      this.$confirm('是否要确认?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const params = new URLSearchParams()
        params.append('userId', this.allocAdminId)
        params.append('roleIds', this.allocRoleIds)
        userApi.updateRole(params).then(response => {
          this.$message({
            message: '分配成功！',
            type: 'success'
          })
          this.allocDialogVisible = false
        })
      })
    },
    haveRole(role) {
      return haveRole(role)
    }
  }
}

</script>

<style>
	.filter-container {
		padding-bottom: 10px;
	}
</style>
