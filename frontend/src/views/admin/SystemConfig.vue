<template>
  <div class="admin-container">
    <el-container>
      <el-header height="60px" class="header">
        <div class="logo">系统配置</div>
        <div class="user-info">
          <span>{{ user?.username }}</span>
          <el-button type="text" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      
      <el-container>
        <el-aside width="200px" class="sidebar">
          <el-menu
            default-active="4"
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
              <h2>系统配置</h2>
              <el-button type="primary" @click="showAddDialog = true">添加配置</el-button>
            </div>
            
            <el-card shadow="hover">
              <el-table :data="configs" style="width: 100%">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column prop="paramKey" label="参数键" width="200" />
                <el-table-column prop="paramValue" label="参数值" width="200" />
                <el-table-column prop="description" label="描述" min-width="300" />
                <el-table-column label="操作" width="150">
                  <template #default="scope">
                    <el-button type="primary" size="small" @click="editConfig(scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="deleteConfig(scope.row.id)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-main>
      </el-container>
    </el-container>
    
    <el-dialog v-model="showAddDialog" :title="editMode ? '编辑配置' : '添加配置'" width="500px">
      <el-form :model="configForm" :rules="rules" ref="configForm">
        <el-form-item label="参数键" prop="paramKey">
          <el-input v-model="configForm.paramKey" placeholder="请输入参数键" />
        </el-form-item>
        <el-form-item label="参数值" prop="paramValue">
          <el-input v-model="configForm.paramValue" placeholder="请输入参数值" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="configForm.description" type="textarea" rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddDialog = false">取消</el-button>
          <el-button type="primary" @click="saveConfig">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { ref, reactive, onMounted } from 'vue'
import { Setting, Document, User, Tools } from '@element-plus/icons-vue'
import request from '@/utils/request'

export default {
  name: 'SystemConfig',
  components: {
    Setting,
    Document,
    User,
    Tools
  },
  setup() {
    const router = useRouter()
    const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
    const configs = ref([])
    const showAddDialog = ref(false)
    const editMode = ref(false)
    
    const configForm = reactive({
      id: null,
      paramKey: '',
      paramValue: '',
      description: ''
    })
    
    const rules = {
      paramKey: [{ required: true, message: '请输入参数键', trigger: 'blur' }],
      paramValue: [{ required: true, message: '请输入参数值', trigger: 'blur' }],
      description: [{ required: true, message: '请输入描述', trigger: 'blur' }]
    }
    
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
    
    const loadConfigs = async () => {
      try {
        const res = await request({
          url: '/config/list',
          method: 'get'
        })
        configs.value = res || []
      } catch (error) {
        console.error('加载配置失败:', error)
      }
    }
    
    const editConfig = (config) => {
      Object.assign(configForm, config)
      editMode.value = true
      showAddDialog.value = true
    }
    
    const deleteConfig = async (id) => {
      try {
        await request({
          url: `/config/delete/${id}`,
          method: 'delete'
        })
        loadConfigs()
      } catch (error) {
        console.error('删除配置失败:', error)
      }
    }
    
    const saveConfig = async () => {
      try {
        if (editMode.value) {
          await request({
            url: '/config/update',
            method: 'put',
            data: configForm
          })
        } else {
          await request({
            url: '/config/add',
            method: 'post',
            data: configForm
          })
        }
        showAddDialog.value = false
        loadConfigs()
        resetForm()
      } catch (error) {
        console.error('保存配置失败:', error)
      }
    }
    
    const resetForm = () => {
      Object.assign(configForm, {
        id: null,
        paramKey: '',
        paramValue: '',
        description: ''
      })
      editMode.value = false
    }
    
    onMounted(() => {
      loadConfigs()
    })
    
    return {
      user,
      configs,
      showAddDialog,
      editMode,
      configForm,
      rules,
      handleLogout,
      handleMenuSelect,
      editConfig,
      deleteConfig,
      saveConfig
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.content-header h2 {
  color: #333;
}
</style>
