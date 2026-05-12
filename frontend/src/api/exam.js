import request from '@/utils/request'

export const examApi = {
  getRandomQuestions(limit = 20) {
    return request({
      url: '/exam/random',
      method: 'get',
      params: { limit }
    })
  },

  submitExam(data) {
    return request({
      url: '/exam/submit',
      method: 'post',
      data
    })
  },

  startExam(data) {
    return request({
      url: '/exam/start',
      method: 'post',
      data
    })
  },

  saveAnswer(data) {
    return request({
      url: '/exam/answer',
      method: 'post',
      data
    })
  },

  getExamRecord(id) {
    return request({
      url: `/exam/record/${id}`,
      method: 'get'
    })
  },

  getExamRecords(userId) {
    return request({
      url: '/exam/list',
      method: 'get',
      params: { userId }
    })
  },

  getAnswerDetails(examRecordId) {
    return request({
      url: `/exam/answer/${examRecordId}`,
      method: 'get'
    })
  },

  calculateScore(examRecordId) {
    return request({
      url: `/exam/score/${examRecordId}`,
      method: 'get'
    })
  }
}
