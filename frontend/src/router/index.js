import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('@/views/Register.vue') },
  { path: '/home', name: 'Home', component: () => import('@/views/Home.vue'), meta: { requiresAuth: true, title: '首页' } },
  { path: '/practice', name: 'Practice', component: () => import('@/views/Practice.vue'), meta: { requiresAuth: true, title: '章节练习' } },
  { path: '/chapter-practice', name: 'ChapterPractice', component: () => import('@/views/ChapterPractice.vue'), meta: { requiresAuth: true, title: '章节练习' } },
  { path: '/exam', name: 'Exam', component: () => import('@/views/Exam.vue'), meta: { requiresAuth: true, title: '模拟考试' } },
  { path: '/score', name: 'Score', component: () => import('@/views/Score.vue'), meta: { requiresAuth: true, title: '成绩查询' } },
  { path: '/exam-history', name: 'ExamHistory', component: () => import('@/views/ExamHistory.vue'), meta: { requiresAuth: true, title: '考试历史' } },
  { path: '/wrong', name: 'Wrong', component: () => import('@/views/Wrong.vue'), meta: { requiresAuth: true, title: '错题本' } },
  { path: '/wrong-questions', name: 'WrongQuestions', component: () => import('@/views/WrongQuestions.vue'), meta: { requiresAuth: true, title: '错题本' } },
  { path: '/system', name: 'System', component: () => import('@/views/System.vue'), meta: { requiresAuth: true, requiresAdmin: true, title: '系统管理' } },
  { path: '/admin', name: 'AdminDashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { requiresAuth: true, requiresAdmin: true, title: '管理员控制台' } },
  { path: '/admin/questions', name: 'QuestionManagement', component: () => import('@/views/admin/QuestionManagement.vue'), meta: { requiresAuth: true, requiresAdmin: true, title: '题库管理' } },
  { path: '/admin/users', name: 'UserManagement', component: () => import('@/views/admin/UserManagement.vue'), meta: { requiresAuth: true, requiresAdmin: true, title: '用户管理' } },
  { path: '/admin/config', name: 'SystemConfig', component: () => import('@/views/admin/SystemConfig.vue'), meta: { requiresAuth: true, requiresAdmin: true, title: '系统配置' } },
  { path: '/:pathMatch(.*)*', redirect: '/home' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const getCurrentUser = () => {
  try {
    return JSON.parse(localStorage.getItem('user') || 'null')
  } catch {
    localStorage.removeItem('user')
    return null
  }
}

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }

  if (to.meta.requiresAdmin) {
    const user = getCurrentUser()
    if (Number(user?.role) !== 1) {
      next('/home')
      return
    }
  }

  next()
})

export default router
