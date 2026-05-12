<template>
  <div class="home-container">
    <el-container>
      <el-header height="60px" class="header">
        <div class="logo">驾驶证模拟考试系统</div>
        <div class="user-info">
          <span>{{ user?.username }}</span>
          <el-button type="text" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      
      <el-main>
        <div class="welcome">
          <h1>欢迎回来，{{ user?.username }}！</h1>
          <p>选择下面的功能开始你的学习之旅</p>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card shadow="hover" class="function-card" @click="goToChapterPractice">
              <template #header>
                <div class="card-header">
                  <span>章节练习</span>
                </div>
              </template>
              <div class="card-content">
                <el-icon class="card-icon"><Reading /></el-icon>
                <p>按章节顺序练习题目</p>
                <p>查看详细解析</p>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="8">
            <el-card shadow="hover" class="function-card" @click="goToExam">
              <template #header>
                <div class="card-header">
                  <span>模拟考试</span>
                </div>
              </template>
              <div class="card-content">
                <el-icon class="card-icon"><Timer /></el-icon>
                <p>全真模拟考试</p>
                <p>计时答题，即时判分</p>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="8">
            <el-card shadow="hover" class="function-card" @click="goToWrong">
              <template #header>
                <div class="card-header">
                  <span>错题本</span>
                </div>
              </template>
              <div class="card-content">
                <el-icon class="card-icon"><Warning /></el-icon>
                <p>自动收录答错题目</p>
                <p>针对性练习</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <el-card shadow="hover" class="function-card" @click="goToScore">
              <template #header>
                <div class="card-header">
                  <span>成绩查询</span>
                </div>
              </template>
              <div class="card-content">
                <el-icon class="card-icon"><Document /></el-icon>
                <p>查看历史考试成绩</p>
                <p>分析学习情况</p>
              </div>
            </el-card>
          </el-col>
          
          <el-col :span="12">
            <el-card shadow="hover" class="function-card" @click="goToSystem" v-if="user?.role === 1">
              <template #header>
                <div class="card-header">
                  <span>系统管理</span>
                </div>
              </template>
              <div class="card-content">
                <el-icon class="card-icon"><Setting /></el-icon>
                <p>题库管理</p>
                <p>用户管理</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { Reading, Timer, Warning, Document, Setting } from '@element-plus/icons-vue'

export default {
  name: 'Home',
  components: {
    Reading,
    Timer,
    Warning,
    Document,
    Setting
  },
  setup() {
    const router = useRouter()
    const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
    
    const handleLogout = () => {
      localStorage.clear()
      router.push('/login')
    }
    
    const goToChapterPractice = () => {
      router.push('/chapter-practice')
    }
    
    const goToExam = () => {
      router.push('/exam')
    }
    
    const goToWrong = () => {
      router.push('/wrong')
    }
    
    const goToScore = () => {
      router.push('/score')
    }
    
    const goToSystem = () => {
      router.push('/admin')
    }
    
    onMounted(() => {
      if (!user.value) {
        router.push('/login')
      }
    })
    
    return {
      user,
      handleLogout,
      goToChapterPractice,
      goToExam,
      goToWrong,
      goToScore,
      goToSystem
    }
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.welcome {
  text-align: center;
  margin: 40px 0;
}

.welcome h1 {
  font-size: 32px;
  color: #333;
  margin-bottom: 10px;
}

.welcome p {
  font-size: 16px;
  color: #666;
}

.function-card {
  cursor: pointer;
  transition: all 0.3s ease;
}

.function-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.card-header {
  font-size: 16px;
  font-weight: bold;
}

.card-content {
  text-align: center;
  padding: 40px 0;
}

.card-icon {
  font-size: 48px;
  color: #409eff;
  margin-bottom: 20px;
}

.card-content p {
  margin: 5px 0;
  color: #666;
}
</style>
