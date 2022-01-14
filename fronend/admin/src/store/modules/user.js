import authApi from '@/api/auth'
import { setToken, removeToken, removeGlobal, setName, setGlobal, getGlobal } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: '',
    name: '',
    avatar: '',
    roles: [],
    globals: {}
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_GLOBALS: (state, globals) => {
    console.log('Begin set globals')
    state.globals = globals
    if (globals && globals.currentUser) {
      state.token = globals.currentUser.token
      setToken(globals.currentUser.token)
      state.name = globals.currentUser.username
    } else {
      state.token = ''
      state.name = ''
      state.roles = []
    }
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      authApi.keypair().then(resKey => {
        authApi.login(username, password, resKey.modulus, resKey.exponent).then(loginRes => {
          const data = loginRes.value
          setToken(data.token)
          // commit('SET_TOKEN', data.token)
          setName(username)
          var globals = {}
          globals.currentUser = {}
          globals.currentUser.username = username
          globals.currentUser.token = data.token
          globals.currentUser.companyId = data.companyId
          commit('SET_GLOBALS', globals)
          commit('SET_AVATAR', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif')
          setGlobal(globals)
          console.log('login called')
          resolve()
        }).catch(error => {
          console.log(error)
          reject(error)
        })
      }).catch(error => {
        console.log(error)
        reject(error)
      })
    })
  },
  updateStoreFromCookie({ commit }) {
    return new Promise((resolve, reject) => {
      const globalInfoFromCookie = getGlobal() ? JSON.parse(getGlobal()) : {}
      commit('SET_GLOBALS', globalInfoFromCookie)
      commit('SET_AVATAR', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif')
      resolve()
    })
  },
  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      authApi.getInfo().then(response => {
        const data = response.value

        if (data.role && data.role.length > 0) { // 验证返回的roles是否是一个非空数组
          commit('SET_ROLES', data.role)
        } else {
          reject('getInfo: roles must be a non-null array !')
        }

        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      authApi.logout(state.token).then(() => {
        commit('SET_GLOBALS', '')
        commit('SET_ROLES', [])
        removeGlobal()
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      commit('SET_GLOBALS', '')
      removeGlobal()
      resolve()
    })
  },
  // 前端 登出
  FedLogOut({ commit, dispatch }) {
    return new Promise((resolve, reject) => {
      commit('SET_GLOBALS', '')
      removeToken()
      removeGlobal()
      resolve()
      resetRouter()
      dispatch('tagsView/delAllViews', null, { root: true })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

