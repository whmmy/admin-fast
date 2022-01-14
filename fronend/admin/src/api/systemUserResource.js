import request from '@/utils/request'
const baseUrl = '/resource'

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
      url: baseUrl + '/delete/' + id,
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
      url: baseUrl + '/listAll',
      method: 'get'
    })
  }
}
