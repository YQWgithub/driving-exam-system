<template>
  <div class="admin-container">
    <el-container>
      <el-header height="60px" class="header">
        <div class="logo">系统管理</div>
        <div class="user-info">
          <span>{{ user?.username }}</span>
          <el-button type="text" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      
      <el-container>
        <el-aside width="200px" class="sidebar">
          <el-menu
            default-active="1"
            class="el-menu-vertical-demo"
            @select="handleMenuSelect"
          >
            <el-menu-item index="1">
              <el-icon><Setting /></el-icon>
              <span>控制台</span>
            </el-menu-item>
            <el-menu-item index="2">
              <el-icon><Document /></el-icon>
              <span>题库管理</span>
            </el-menu-item>
            <el-menu-item index="3">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="4">
              <el-icon><Tools /></el-icon>
              <span>系统配置</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        
        <el-main>
          <div class="admin-content">
            <h2>管理员控制台</h2>
            <el-row :gutter="20">
              <el-col :span="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="stat-item">
                    <div class="stat-number">{{ userCount }}</div>
                    <div class="stat-label">用户总数</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="stat-item">
                    <div class="stat-number">{{ questionCount }}</div>
                    <div class="stat-label">题目总数</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="stat-item">
                    <div class="stat-number">{{ examCount }}</div>
                    <div class="stat-label">考试次数</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="stat-item">
                    <div class="stat-number">{{ categoryCount }}</div>
                    <div class="stat-label">分类总数</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { Setting, Document, User, Tools } from '@element-plus/icons-vue'
import request from '@/utils/request'

export default {
  name: 'AdminDashboard',
  components: {
    Setting,
    Document,
    User,
    Tools
  },
  setup() {
    const router = useRouter()
    const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
    const userCount = ref(0)
    const questionCount = ref(0)
    const examCount = ref(0)
    const categoryCount = ref(0)
    
    const handleLogout = () => {
      localStorage.clear()
      router.push('/login')
    }
    
    const handleMenuSelect = (index) => {
      switch (index) {
        case '1':
          router.push('/admin')
          break
        case '2':
          router.push('/admin/questions')
          break
        case '3':
          router.push('/admin/users')
          break
        case '4':
          router.push('/admin/config')
          break
      }
    }
    
    const loadStats = async () => {
      try {
        const userRes = await request({
          url: '/user/list',
          method: 'get'
        })
        userCount.value = (userRes || []).length
        
        const questionRes = await request({
          url: '/question/list',
          method: 'get'
        })
        questionCount.value = (questionRes || []).length
        
        const categoryRes = await request({
          url: '/category/list',
          method: 'get'
        })
        categoryCount.value = (categoryRes || []).length

        const examRes = await request({
          url: '/exam/all',
          method: 'get'
        })
        examCount.value = (examRes || []).length
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    }
    
    onMounted(() => {
      loadStats()
    })
    
    return {
      user,
      userCount,
      questionCount,
      examCount,
      categoryCount,
      handleLogout,
      handleMenuSelect
    }
  }
}
</script>

<style scoped>
.admin-container {
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
  font-size: 18px;
  font-weight: bold;
  color: #409eff;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.sidebar {
  background: white;
  box-shadow: 2px 0 4px rgba(0, 0, 0, 0.1);
}

.admin-content {
  padding: 20px;
}

.admin-content h2 {
  margin-bottom: 20px;
  color: #333;
}

.stat-card {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}
</style>
