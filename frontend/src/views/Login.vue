<template>
  <div class="login-container">
    <div class="login-form">
      <h2>驾驶证模拟考试系统</h2>
      <el-form :model="loginForm" :rules="rules" @keyup.enter="handleLogin">
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model.trim="loginForm.username"
            clearable
            autocomplete="off"
            placeholder="请输入用户名"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            clearable
            show-password
            autocomplete="off"
            type="password"
            placeholder="请输入密码"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleLogin" :loading="loading">
            登录
          </el-button>
        </el-form-item>

        <el-form-item class="form-footer">
          <span>还没有账号？</span>
          <el-link type="primary" @click="goToRegister">立即注册</el-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { userApi } from '@/api/user'

export default {
  name: 'LoginView',
  setup() {
    const router = useRouter()
    const loading = ref(false)
    const loginForm = reactive({
      username: '',
      password: ''
    })

    const rules = {
      username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
    }

    const handleLogin = async () => {
      if (!loginForm.username || !loginForm.password) {
        ElMessage.warning('请输入用户名和密码')
        return
      }
      loading.value = true
      try {
        const res = await userApi.login(loginForm)

        if (!res?.token || !res?.user?.id) {
          throw new Error('登录响应数据不完整')
        }
        
        localStorage.setItem('token', res.token)
        localStorage.setItem('userId', res.user.id)
        localStorage.setItem('user', JSON.stringify(res.user))
        
        ElMessage.success('登录成功')
        router.push('/home')
      } catch (err) {
        console.error('登录失败:', err)
        ElMessage.error(err.message || '登录失败，请重试')
      } finally {
        loading.value = false
      }
    }

    const goToRegister = () => {
      router.push('/register')
    }

    return {
      loginForm,
      rules,
      loading,
      handleLogin,
      goToRegister
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-form {
  width: 420px;
  padding: 32px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
}

.login-form h2 {
  margin: 0 0 24px;
  text-align: center;
}

.form-footer {
  display: flex;
  justify-content: center;
  gap: 8px;
}
</style>
