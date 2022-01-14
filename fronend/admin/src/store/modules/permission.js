import { asyncRouterMap, constantRoutes } from '@/router/index'

export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

// 判断是否有权限访问该菜单
function hasPermission(role, route) {
  if (route.name) {
    const currMenu = getMenu(route.name, role)// 在menus中 查找name为route.name的成员
    if (currMenu != null) { // 存在target
      // 设置菜单的标题、图标和可见性
      if (currMenu.name != null && currMenu.name !== '') {
        route.meta.title = currMenu.name
      }
      if (currMenu.sort != null && currMenu.sort !== '') {
        route.sort = currMenu.sort
      }
      return true
    } else { // 不存在target
      route.sort = 0
      if (route.hidden !== undefined && route.hidden === true) {
        route.sort = -1
        return true
      } else {
        return false
      }
    }
  } else { // 不存在name的route
    return true
  }
}

// 根据路由名称获取菜单
function getMenu(name, roles) {
  for (let i = 0; i < roles.length; i++) {
    const role = roles[i]
    if (name === role.code) {
      return role
    }
  }
  return null
}

// 对菜单进行排序
// eslint-disable-next-line
function sortRouters(accessedRouters) {
  for (let i = 0; i < accessedRouters.length; i++) {
    const router = accessedRouters[i]
    if (router.children && router.children.length > 0) {
      router.children.sort(compare('sort'))
    }
  }
  accessedRouters.sort(compare('sort'))
}

// 降序比较函数
function compare(p) {
  return function(m, n) {
    const a = m[p]
    const b = n[p]
    return b - a
  }
}

const permission = {
  state: {
    routers: constantRoutes,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRoutes.concat(routers)
    }
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {
        const { role } = data
        const accessedRoutes = filterAsyncRoutes(asyncRouterMap, role)
        console.log(accessedRoutes)
        // 对菜单进行排序
        sortRouters(accessedRoutes)
        commit('SET_ROUTERS', accessedRoutes)
        resolve(accessedRoutes)
      })
    }
  }
}

export default permission

