import Cookies from 'js-cookie'
import store from '../store'
const TokenKey = 'vue_admin_template_token'
const NameKey = 'vue_admin_template_name'
const globalsKey = 'globals'

function formateCookieStr(str) {
  if (str) {
    if (str.length > 0) {
      if (str.substr(0, 1) === '"') {
        return str.substr(1, str.length - 1)
      }
    }
  }
  return str
}
export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function setName(name) {
  return Cookies.set(NameKey, name)
}

export function getName() {
  // .replace(/"/g, '')
  return formateCookieStr(Cookies.get(NameKey))
}

export function setGlobal(global) {
  return Cookies.set(globalsKey, global)
}

export function getGlobal() {
  return formateCookieStr(Cookies.get(globalsKey))
}

export function removeGlobal() {
  console.log('Begin remove global from cookie')
  return Cookies.remove(globalsKey)
}

export function haveRole(roleCode) {
  const roles = store.getters.role
  for (const role of roles) {
    if (role.code === roleCode) {
      return true
    }
  }
  return false
}
