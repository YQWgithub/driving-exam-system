import request from '@/utils/request'

export const wrongApi = {
  getWrongQuestions(userId) {
    return request({
      url: '/wrong/list',
      method: 'get',
      params: { userId }
    })
  },

  addWrongQuestion(data) {
    return request({
      url: '/wrong/add',
      method: 'post',
      data
    })
  },

  deleteWrongQuestion(userId, questionId) {
    return request({
      url: '/wrong/remove',
      method: 'delete',
      params: { userId, questionId }
    })
  },

  deleteWrongQuestionById(id) {
    return request({
      url: `/wrong/delete/${id}`,
      method: 'delete'
    })
  },

  clearWrongQuestions(userId) {
    return request({
      url: '/wrong/clear',
      method: 'delete',
      params: { userId }
    })
  }
}
