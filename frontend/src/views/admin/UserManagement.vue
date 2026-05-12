<template>
  <div class="admin-container">
    <el-container>
      <el-header height="60px" class="header">
        <div class="logo">用户管理</div>
        <div class="user-info">
          <span>{{ user?.username }}</span>
          <el-button type="text" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      
      <el-container>
        <el-aside width="200px" class="sidebar">
          <el-menu
            default-active="3"
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
            <div class="content-header">
              <h2>用户管理</h2>
            </div>
            
            <el-card shadow="hover">
              <el-form :model="searchForm" class="search-form">
                <el-form-item label="用户名">
                  <el-input v-model="searchForm.username" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item label="角色">
                  <el-select v-model="searchForm.role" placeholder="选择角色">
                    <el-option label="全部" value="" />
                    <el-option label="学员" value="0" />
                    <el-option label="管理员" value="1" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="searchUsers">搜索</el-button>
                  <el-button @click="resetSearch">重置</el-button>
                </el-form-item>
              </el-form>
              
              <el-table :data="users" style="width: 100%">
                <el-table-column prop="id" label="用户ID" width="80" />
                <el-table-column prop="username" label="用户名" width="150" />
                <el-table-column prop="phone" label="手机号" width="150" />
                <el-table-column prop="email" label="邮箱" width="200" />
                <el-table-column label="角色" width="100">
                  <template #default="scope">
                    <el-tag :type="scope.row.role === 1 ? 'warning' : 'success'">
                      {{ scope.row.role === 1 ? '管理员' : '学员' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="状态" width="100">
                  <template #default="scope">
                    <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                      {{ scope.row.status === 1 ? '正常' : '禁用' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="创建时间" width="180">
                  <template #default="scope">
                    {{ formatDate(scope.row.createTime) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="scope">
                    <el-button 
                      type="primary" 
                      size="small" 
                      @click="toggleStatus(scope.row.id, scope.row.status)"
                    >
                      {{ scope.row.status === 1 ? '禁用' : '启用' }}
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { ref, reactive, onMounted } from 'vue'
import { Setting, Document, User, Tools } from '@element-plus/icons-vue'
import request from '@/utils/request'

export default {
  name: 'UserManagement',
  components: {
    Setting,
    Document,
    User,
    Tools
  },
  setup() {
    const router = useRouter()
    const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
    const users = ref([])
    
    const searchForm = reactive({
      username: '',
      role: ''
    })
    
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
    
    const loadUsers = async () => {
      try {
        const res = await request({
          url: '/user/list',
          method: 'get'
        })
        users.value = res || []
      } catch (error) {
        console.error('加载用户失败:', error)
      }
    }
    
    const searchUsers = async () => {
      try {
        let url = '/user/list'
        if (searchForm.role) {
          url = `/user/list/role/${searchForm.role}`
        }
        
        const res = await request({
          url: url,
          method: 'get'
        })
        users.value = res || []
      } catch (error) {
        console.error('搜索用户失败:', error)
      }
    }
    
    const resetSearch = () => {
      searchForm.username = ''
      searchForm.role = ''
      loadUsers()
    }
    
    const toggleStatus = async (userId, currentStatus) => {
      try {
        await request({
          url: `/user/status/${userId}/${currentStatus === 1 ? 0 : 1}`,
          method: 'put'
        })
        loadUsers()
      } catch (error) {
        console.error('修改状态失败:', error)
      }
    }
    
    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString()
    }
    
    onMounted(() => {
      loadUsers()
    })
    
    return {
      user,
      users,
      searchForm,
      handleLogout,
      handleMenuSelect,
      searchUsers,
      resetSearch,
      toggleStatus,
      formatDate
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

.content-header {
  margin-bottom: 20px;
}

.content-header h2 {
  color: #333;
}

.search-form {
  display: flex;
  margin-bottom: 20px;
  gap: 10px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}
</style>
