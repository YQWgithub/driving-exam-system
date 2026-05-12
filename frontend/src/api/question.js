import request from '@/utils/request'

export const questionApi = {
  getByChapter(chapter) {
    return request({
      url: '/question/list/chapter',
      method: 'get',
      params: { chapter }
    })
  },

  getList() {
    return request({
      url: '/question/list',
      method: 'get'
    })
  },

  getByCategory(categoryId) {
    return request({
      url: `/question/list/category/${categoryId}`,
      method: 'get'
    })
  },

  getByType(type) {
    return request({
      url: `/question/list/type/${type}`,
      method: 'get'
    })
  },

  getByDifficulty(difficulty) {
    return request({
      url: `/question/list/difficulty/${difficulty}`,
      method: 'get'
    })
  },

  addQuestion(data) {
    return request({
      url: '/question/add',
      method: 'post',
      data
    })
  },

  updateQuestion(data) {
    return request({
      url: '/question/update',
      method: 'put',
      data
    })
  },

  deleteQuestion(id) {
    return request({
      url: `/question/delete/${id}`,
      method: 'delete'
    })
  },

  getQuestionInfo(id) {
    return request({
      url: `/question/info/${id}`,
      method: 'get'
    })
  },

  getRandomQuestions(params) {
    return request({
      url: '/question/random',
      method: 'get',
      params
    })
  },

  countQuestions(params) {
    return request({
      url: '/question/count',
      method: 'get',
      params
    })
  }
}
