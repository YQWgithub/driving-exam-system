<template>
  <div class="register-container">
    <div class="register-form">
      <h2>用户注册</h2>
      <el-form :model="registerForm" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" prefix-icon="User"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码" prefix-icon="Lock" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" style="width: 100%" :loading="loading">注册</el-button>
        </el-form-item>
        <el-form-item style="text-align: center">
          <span>已有账号？</span>
          <el-link type="primary" @click="goToLogin">立即登录</el-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { userApi } from '@/api/user'
import { useRouter } from 'vue-router'
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'Register',
  setup() {
    const router = useRouter()
    const formRef = ref(null)
    const loading = ref(false)
    
    const registerForm = reactive({
      username: '',
      password: '',
      confirmPassword: ''
    })
    
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== registerForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码长度至少 6 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' }
      ]
    }
    
    const handleRegister = async () => {
      if (!formRef.value) return
      
      await formRef.value.validate(async (valid) => {
        if (!valid) return
        
        loading.value = true
        try {
          await userApi.register({
            username: registerForm.username,
            password: registerForm.password,
            role: 0
          })
          ElMessage.success('注册成功，请登录')
          router.push('/login')
        } catch (error) {
          console.error('注册失败:', error)
          ElMessage.error(error.message || '注册失败')
        } finally {
          loading.value = false
        }
      })
    }
    
    const goToLogin = () => {
      router.push('/login')
    }
    
    return {
      formRef,
      registerForm,
      rules,
      loading,
      handleRegister,
      goToLogin
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-form {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.register-form h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
</style>
