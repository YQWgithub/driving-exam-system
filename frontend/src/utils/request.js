import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const res = response.data

    if (import.meta.env.DEV) {
      console.debug('【接口响应】', response.config.url, res)
    }

    if (res && Object.prototype.hasOwnProperty.call(res, 'code') && res.code !== 200) {
      return Promise.reject(new Error(res.message || '请求失败'))
    }

    if (res && Object.prototype.hasOwnProperty.call(res, 'data')) {
      return res.data
    }
    return res
  },
  error => {
    const message = error.response?.data?.message || error.message || '网络错误'
    console.error('网络错误:', message)
    error.message = message
    return Promise.reject(error)
  }
)

export default request
