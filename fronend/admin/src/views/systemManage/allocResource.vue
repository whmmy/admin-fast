<template>
  <el-card class="form-container" shadow="never">
    <div v-for="(cate,index) in getResourceByCate(0)" :key="'cate'+cate.id" :class="index===0?'top-line':null" style="margin: 20px 0">
      <el-row class="table-layout" style="background: #F2F6FC;">
        <el-checkbox
          v-model="cate.checked"
          @change="handleCheckChange(cate)"
        >
          {{ cate.name }}
        </el-checkbox>
      </el-row>
      <el-row v-for="resource in getResourceByCate(cate.id)" :key="resource.id" class="table-layout">
        <div>
          <el-checkbox
            v-model="resource.checked"
            @change="handleCheckChange(resource)"
          >
            {{ resource.name }}
          </el-checkbox>
          <el-row v-if=" getResourceByCate(resource.id).length > 0" class="table-layout">
            <el-col v-for="resBtn in getResourceByCate(resource.id)" :key="resBtn.id" :span="8" style="margin: 4px 0">
              <el-checkbox v-model="resBtn.checked" @change="handleCheckChange(resBtn)">
                {{ resBtn.name }}
              </el-checkbox>
            </el-col>
          </el-row>
        </div>
      </el-row>
    </div>
    <div style="margin-top: 20px" align="center">
      <el-button type="primary" @click="handleSave()">保存</el-button>
      <el-button @click="handleClear()">清空</el-button>
    </div>

  </el-card>
</template>

<script>
import resourceApi from '@/api/systemUserResource'
import roleApi from '@/api/systemRole'

export default {
  name: 'AllocResource',
  data() {
    return {
      roleId: null,
      allResource: null
    }
  },
  created() {
    this.roleId = this.$route.query.roleId
    if (!this.roleId) {
      // this.$message.success('非正确进入，返回角色管理')
      this.$router.replace({ path: '/systemManage/roleManager' })
      return
    }
    this.getAllResourceList()
  },
  methods: {
    getAllResourceList() {
      resourceApi.listAll().then(response => {
        this.allResource = response.value.list
        for (let i = 0; i < this.allResource.length; i++) {
          this.allResource[i].checked = false
        }
        this.getResourceByRole(this.roleId)
      })
    },
    getResourceByCate(categoryId) {
      const cateResource = []
      if (this.allResource == null) return null
      for (let i = 0; i < this.allResource.length; i++) {
        const resource = this.allResource[i]
        if (resource.categoryId === categoryId) {
          cateResource.push(resource)
        }
      }
      return cateResource
    },
    getResourceByRole(roleId) {
      roleApi.listResource(roleId).then(response => {
        const allocResource = response.value.resList
        this.allResource.forEach(item => {
          item.checked = this.getResourceChecked(item.id, allocResource)
        })
        this.$forceUpdate()
      })
    },
    getResourceChecked(resourceId, allocResource) {
      if (allocResource == null || allocResource.length === 0) return false
      for (let i = 0; i < allocResource.length; i++) {
        if (allocResource[i].id === resourceId) {
          return true
        }
      }
      return false
    },
    isIndeterminate(categoryId) {
      const cateResources = this.getResourceByCate(categoryId)
      if (cateResources == null) return false
      let checkedCount = 0
      for (let i = 0; i < cateResources.length; i++) {
        if (cateResources[i].checked === true) {
          checkedCount++
        }
      }
      return !(checkedCount === 0 || checkedCount === cateResources.length)
    },
    isAllChecked(categoryId) {
      const cateResources = this.getResourceByCate(categoryId)
      if (cateResources == null) return false
      let checkedCount = 0
      for (let i = 0; i < cateResources.length; i++) {
        if (cateResources[i].checked === true) {
          checkedCount++
        }
      }
      if (checkedCount === 0) {
        return false
      }
      return checkedCount === cateResources.length
    },
    isChecked(categoryId) {
      const cateResources = this.getResourceByCate(categoryId)
      if (cateResources == null) return false
      for (let i = 0; i < cateResources.length; i++) {
        if (cateResources[i].checked === true) {
          return true
        }
      }

      return false
    },
    handleSave() {
      this.$confirm('是否分配资源？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const checkedResourceIds = new Set()
        if (this.allResource != null && this.allResource.length > 0) {
          this.allResource.forEach(item => {
            if (item.checked) {
              checkedResourceIds.add(item.id)
            }
          })
        }
        const params = new URLSearchParams()
        params.append('roleId', this.roleId)
        params.append('resourceIds', Array.from(checkedResourceIds))
        roleApi.allocResource(params).then(response => {
          this.$message({
            message: '分配成功',
            type: 'success',
            duration: 1000
          })
          this.$router.back()
        })
      })
    },
    handleClear() {
      this.allResource.forEach(item => {
        item.checked = false
      })
      this.$forceUpdate()
    },
    handleCheckAllChange(cate) {
      const cateResources = this.getResourceByCate(cate.id)
      for (let i = 0; i < cateResources.length; i++) {
        cateResources[i].checked = cate.checked
      }
      this.$forceUpdate()
    },
    handleCheckChange(resource) {
      this.allResource.forEach(item => {
        if (item.id === resource.categoryId) {
          if (resource.checked) {
            item.checked = true
            if (resource.type === 3) {
              this.handleCheckChange(item)
            }
          }
        }
      })
      this.$forceUpdate()
    }
  }
}
</script>

<style scoped>
  .table-layout {
    padding: 20px;
    border-left: 1px solid #DCDFE6;
    border-right: 1px solid #DCDFE6;
    border-bottom: 1px solid #DCDFE6;
  }

  .top-line {
    border-top: 1px solid #DCDFE6;
  }
</style>
