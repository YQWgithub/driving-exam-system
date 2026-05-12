import request from '@/utils/request'

export const userApi = {
  login(data) {
    return request({
      url: '/user/login',
      method: 'post',
      data
    })
  },

  register(data) {
    return request({
      url: '/user/register',
      method: 'post',
      data
    })
  },

  getUserInfo(id) {
    return request({
      url: `/user/info/${id}`,
      method: 'get'
    })
  },

  updateUser(data) {
    return request({
      url: '/user/update',
      method: 'put',
      data
    })
  },

  checkUsername(username) {
    return request({
      url: `/user/check/username/${username}`,
      method: 'get'
    })
  },

  checkPhone(phone) {
    return request({
      url: `/user/check/phone/${phone}`,
      method: 'get'
    })
  },

  checkEmail(email) {
    return request({
      url: `/user/check/email/${email}`,
      method: 'get'
    })
  },

  getUserList() {
    return request({
      url: '/user/list',
      method: 'get'
    })
  }
}
