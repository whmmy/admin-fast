import request from '@/utils/request'
const baseUrl = '/company'

export default {
  create(data) {
    return request({
      url: baseUrl + '/create',
      method: 'post',
      data
    })
  },
  update(data) {
    return request({
      url: baseUrl + '/update',
      method: 'post',
      data
    })
  },
  delete(id) {
    return request({
      url: baseUrl + '/softdel?id=' + id,
      method: 'post'
    })
  },
  list(params) {
    return request({
      url: baseUrl + '/list',
      method: 'get',
      params
    })
  },
  listAll() {
    return request({
      url: baseUrl + '/all',
      method: 'get'
    })
  },
  editConfig(data) {
    return request({
      url: baseUrl + '/edit/config',
      method: 'post',
      data
    })
  },
  readConfig() {
    return request({
      url: baseUrl + '/read/config',
      method: 'get'
    })
  }
}
