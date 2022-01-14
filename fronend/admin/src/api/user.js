import request from '@/utils/request'
import rsa from '@/utils/rsa/RSA'
import BigInt from '@/utils/rsa/BigInt'
import md5 from '@/utils/rsa/md5'
export default {
  fetchList(query) {
    return request({
      url: '/user/page',
      method: 'get',
      params: {
        keyword: query.keyword,
        role: query.role,
        pageSize: query.limit,
        pageNum: query.page
      }
    })
  },
  createSystemuser(data, exponent, modulus) {
    var temp = Object.assign({}, data)
    BigInt.setMaxDigits(130)
    var publicKey = new rsa.RSAKeyPair(exponent, '', modulus)
    temp.password = rsa.encryptedString(publicKey, encodeURIComponent(md5.hex_md5(temp.password).toLowerCase()))
    temp.passwordConfirm = rsa.encryptedString(publicKey, encodeURIComponent(md5.hex_md5(temp.passwordConfirm).toLowerCase()))
    return request({
      url: '/user/add/' + modulus,
      method: 'post',
      data: temp
    })
  },
  updateSystemuser(data, exponent, modulus) {
    var temp = Object.assign({}, data)
    if (temp.password) {
      BigInt.setMaxDigits(130)
      var publicKey = new rsa.RSAKeyPair(exponent, '', modulus)
      temp.password = rsa.encryptedString(publicKey, encodeURIComponent(md5.hex_md5(temp.password).toLowerCase()))
      temp.passwordConfirm = rsa.encryptedString(publicKey, encodeURIComponent(md5.hex_md5(temp.passwordConfirm).toLowerCase()))
    }
    return request({
      url: '/user/edit/' + modulus,
      method: 'post',
      data: temp
    })
  },
  delSystemUser(id) {
    return request({
      url: '/system/del',
      method: 'post',
      params: {
        id: id
      }
    })
  },
  updateRole(params) {
    return request({
      url: '/user/role/update',
      method: 'post',
      params
    })
  },
  getRoleList(userId) {
    return request({
      url: '/user/role/' + userId,
      method: 'get'
    })
  },
  listAllRole() {
    return request({
      url: '/user/roleListAllCanAllo',
      method: 'get'
    })
  }

}
