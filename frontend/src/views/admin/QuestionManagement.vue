<template>
  <div class="admin-container">
    <el-container>
      <el-header height="60px" class="header">
        <div class="logo">题库管理</div>
        <div class="user-info">
          <span>{{ user?.username }}</span>
          <el-button type="text" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      
      <el-container>
        <el-aside width="200px" class="sidebar">
          <el-menu
            default-active="2"
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
              <h2>题库管理</h2>
              <el-button type="primary" @click="showAddDialog = true">添加题目</el-button>
            </div>
            
            <el-card shadow="hover">
              <el-form :model="searchForm" class="search-form">
                <el-form-item label="分类">
                  <el-select v-model="searchForm.categoryId" placeholder="选择分类">
                    <el-option
                      v-for="category in categories"
                      :key="category.id"
                      :label="category.name"
                      :value="category.id"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="题型">
                  <el-select v-model="searchForm.type" placeholder="选择题型">
                    <el-option label="全部" value="" />
                    <el-option label="单选题" value="single" />
                    <el-option label="多选题" value="multiple" />
                    <el-option label="判断题" value="judge" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="searchQuestions">搜索</el-button>
                  <el-button @click="resetSearch">重置</el-button>
                </el-form-item>
              </el-form>
              
              <el-table :data="questions" style="width: 100%">
                <el-table-column prop="id" label="题目ID" width="80" />
                <el-table-column prop="type" label="题型" width="100" />
                <el-table-column label="题目内容" min-width="300">
                  <template #default="scope">
                    {{ scope.row.content.substring(0, 50) }}{{ scope.row.content.length > 50 ? '...' : '' }}
                  </template>
                </el-table-column>
                <el-table-column prop="difficulty" label="难度" width="80" />
                <el-table-column prop="chapter" label="章节" width="120" />
                <el-table-column label="操作" width="150">
                  <template #default="scope">
                    <el-button type="primary" size="small" @click="editQuestion(scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="deleteQuestion(scope.row.id)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-main>
      </el-container>
    </el-container>
    
    <el-dialog v-model="showAddDialog" :title="editMode ? '编辑题目' : '添加题目'" width="600px">
      <el-form :model="questionForm" :rules="rules" ref="questionForm">
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="questionForm.categoryId" placeholder="选择分类">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="题型" prop="type">
          <el-select v-model="questionForm.type" placeholder="选择题型">
            <el-option label="单选题" value="single" />
            <el-option label="多选题" value="multiple" />
            <el-option label="判断题" value="judge" />
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容" prop="content">
          <el-input v-model="questionForm.content" type="textarea" rows="3" placeholder="请输入题目内容" />
        </el-form-item>
        <el-form-item label="图片URL" prop="imageUrl">
          <el-input v-model="questionForm.imageUrl" placeholder="请输入图片URL" />
        </el-form-item>
        <el-form-item label="选项" prop="options">
          <el-input v-model="questionForm.options" type="textarea" rows="4" placeholder='请输入选项，格式：{"A":"选项A","B":"选项B"}' />
        </el-form-item>
        <el-form-item label="正确答案" prop="answer">
          <el-input v-model="questionForm.answer" placeholder="请输入正确答案" />
        </el-form-item>
        <el-form-item label="解析" prop="analysis">
          <el-input v-model="questionForm.analysis" type="textarea" rows="3" placeholder="请输入解析" />
        </el-form-item>
        <el-form-item label="难度" prop="difficulty">
          <el-select v-model="questionForm.difficulty" placeholder="选择难度">
            <el-option label="1星" value="1" />
            <el-option label="2星" value="2" />
            <el-option label="3星" value="3" />
            <el-option label="4星" value="4" />
            <el-option label="5星" value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="章节" prop="chapter">
          <el-input v-model="questionForm.chapter" placeholder="请输入章节" />
        </el-form-item>
        <el-form-item label="知识点" prop="knowledgePoint">
          <el-input v-model="questionForm.knowledgePoint" placeholder="请输入知识点" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddDialog = false">取消</el-button>
          <el-button type="primary" @click="saveQuestion">保存</el-button>
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
  name: 'QuestionManagement',
  components: {
    Setting,
    Document,
    User,
    Tools
  },
  setup() {
    const router = useRouter()
    const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
    const categories = ref([])
    const questions = ref([])
    const showAddDialog = ref(false)
    const editMode = ref(false)
    
    const searchForm = reactive({
      categoryId: '',
      type: ''
    })
    
    const questionForm = reactive({
      id: null,
      categoryId: '',
      type: '',
      content: '',
      imageUrl: '',
      options: '',
      answer: '',
      analysis: '',
      difficulty: 3,
      chapter: '',
      knowledgePoint: ''
    })
    
    const rules = {
      categoryId: [{ required: true, message: '请选择分类', trigger: 'blur' }],
      type: [{ required: true, message: '请选择题型', trigger: 'blur' }],
      content: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
      options: [{ required: true, message: '请输入选项', trigger: 'blur' }],
      answer: [{ required: true, message: '请输入正确答案', trigger: 'blur' }],
      analysis: [{ required: true, message: '请输入解析', trigger: 'blur' }]
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
    
    const loadCategories = async () => {
      try {
        const res = await request({
          url: '/category/list',
          method: 'get'
        })
        categories.value = res || []
      } catch (error) {
        console.error('加载分类失败:', error)
      }
    }
    
    const loadQuestions = async () => {
      try {
        const res = await request({
          url: '/question/list',
          method: 'get'
        })
        questions.value = res || []
      } catch (error) {
        console.error('加载题目失败:', error)
      }
    }
    
    const searchQuestions = async () => {
      try {
        let url = '/question/list'
        if (searchForm.categoryId) {
          url = `/question/list/category/${searchForm.categoryId}`
        } else if (searchForm.type) {
          url = `/question/list/type/${searchForm.type}`
        }
        
        const res = await request({
          url: url,
          method: 'get'
        })
        questions.value = res || []
      } catch (error) {
        console.error('搜索题目失败:', error)
      }
    }
    
    const resetSearch = () => {
      searchForm.categoryId = ''
      searchForm.type = ''
      loadQuestions()
    }
    
    const editQuestion = (question) => {
      Object.assign(questionForm, question)
      editMode.value = true
      showAddDialog.value = true
    }
    
    const deleteQuestion = async (id) => {
      try {
        await request({
          url: `/question/delete/${id}`,
          method: 'delete'
        })
        loadQuestions()
      } catch (error) {
        console.error('删除题目失败:', error)
      }
    }
    
    const saveQuestion = async () => {
      try {
        if (editMode.value) {
          await request({
            url: '/question/update',
            method: 'put',
            data: questionForm
          })
        } else {
          await request({
            url: '/question/add',
            method: 'post',
            data: questionForm
          })
        }
        showAddDialog.value = false
        loadQuestions()
        resetForm()
      } catch (error) {
        console.error('保存题目失败:', error)
      }
    }
    
    const resetForm = () => {
      Object.assign(questionForm, {
        id: null,
        categoryId: '',
        type: '',
        content: '',
        imageUrl: '',
        options: '',
        answer: '',
        analysis: '',
        difficulty: 3,
        chapter: '',
        knowledgePoint: ''
      })
      editMode.value = false
    }
    
    onMounted(() => {
      loadCategories()
      loadQuestions()
    })
    
    return {
      user,
      categories,
      questions,
      showAddDialog,
      editMode,
      searchForm,
      questionForm,
      rules,
      handleLogout,
      handleMenuSelect,
      searchQuestions,
      resetSearch,
      editQuestion,
      deleteQuestion,
      saveQuestion
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

.search-form {
  display: flex;
  margin-bottom: 20px;
  gap: 10px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}
</style>
