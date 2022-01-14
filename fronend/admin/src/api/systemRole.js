import request from '@/utils/request'
const baseUrl = '/role'

export default {
  create(data) {
    return request({
      url: baseUrl + '/create',
      method: 'post',
      data
    })
  },
  update(id, data) {
    return request({
      url: baseUrl + '/update/' + id,
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
  },
  updateStatus(id, status) {
    return request({
      url: baseUrl + '/updateStatus/' + id,
      method: 'post',
      params: { status }
    })
  },
  listResource(id) {
    return request({
      url: baseUrl + '/listResource/' + id,
      method: 'get'
    })
  },
  allocResource(params) {
    return request({
      url: baseUrl + '/allocResource',
      method: 'post',
      params
    })
  }
}
