import request from '@/utils/request'
import rsa from '@/utils/rsa/RSA'
import BigInt from '@/utils/rsa/BigInt'
import md5 from '@/utils/rsa/md5'
export default {
  keypair() {
    return request({
      url: '/auth/keypair',
      method: 'get'
    })
  },
  login(username, password, modulus, exponent) {
    BigInt.setMaxDigits(130)
    var publicKey = new rsa.RSAKeyPair(exponent, '', modulus)
    var pwd = rsa.encryptedString(publicKey, encodeURIComponent(md5.hex_md5(password).toLowerCase()))

    return request({
      url: '/auth/login/' + modulus,
      method: 'post',
      data: {
        'userName': username,
        'password': pwd
      }
    })
  },
  logout() {
    return request({
      url: '/auth/logout/',
      method: 'post'
    })
  },
  getInfo() {
    return request({
      url: '/user/info',
      method: 'get'
    })
  }
}
