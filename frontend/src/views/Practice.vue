<template>
  <div class="practice-container">
    <el-container>
      <el-header height="60px" class="header">
        <div class="logo">章节练习</div>
        <el-button type="text" @click="goBack">返回首页</el-button>
      </el-header>
      
      <el-main>
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <el-select v-model="selectedCategory" placeholder="选择分类">
                <el-option
                  v-for="category in categories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
              <el-select v-model="selectedType" placeholder="选择题型">
                <el-option label="全部" value="" />
                <el-option label="单选题" value="single" />
                <el-option label="多选题" value="multiple" />
                <el-option label="判断题" value="judge" />
              </el-select>
              <el-button type="primary" @click="loadQuestions">开始练习</el-button>
            </div>
          </template>
          
          <div v-if="questions.length === 0" class="empty-state">
            <el-icon class="empty-icon"><Document /></el-icon>
            <p>请选择分类开始练习</p>
          </div>
          
          <div v-else>
            <div v-for="(question, index) in questions" :key="question.id" class="question-item">
              <div class="question-header">
                <span class="question-number">{{ index + 1 }}. {{ question.type }}</span>
                <span class="difficulty" :class="`difficulty-${question.difficulty}`">
                  难度: {{ question.difficulty }}星
                </span>
              </div>
              
              <div class="question-content">
                <p>{{ question.content }}</p>
                <img v-if="question.imageUrl" :src="question.imageUrl" class="question-image" />
              </div>
              
              <div class="question-options" v-if="question.options">
                <div
                  v-for="(option, key) in JSON.parse(question.options)"
                  :key="key"
                  class="option-item"
                  :class="{ active: selectedOptions[question.id] === key, correct: showAnalysis && key === question.answer, wrong: showAnalysis && selectedOptions[question.id] === key && selectedOptions[question.id] !== question.answer }"
                  @click="selectOption(question.id, key)"
                >
                  <span class="option-label">{{ key }}</span>
                  <span class="option-content">{{ option }}</span>
                </div>
              </div>
              
              <div class="question-analysis" v-if="showAnalysis">
                <h4>解析：</h4>
                <p>{{ question.analysis }}</p>
              </div>
              
              <div class="question-actions">
                <el-button type="primary" @click="showAnalysis = true">查看解析</el-button>
                <el-button @click="nextQuestion(index)">下一题</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { Document } from '@element-plus/icons-vue'
import request from '@/utils/request'

export default {
  name: 'Practice',
  components: {
    Document
  },
  setup() {
    const router = useRouter()
    const categories = ref([])
    const questions = ref([])
    const selectedCategory = ref('')
    const selectedType = ref('')
    const selectedOptions = ref({})
    const showAnalysis = ref(false)
    
    const goBack = () => {
      router.push('/home')
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
      if (!selectedCategory.value) {
        return
      }
      
      try {
        const res = await request({
          url: `/question/list/category/${selectedCategory.value}`,
          method: 'get'
        })
        questions.value = selectedType.value
          ? (res || []).filter(question => question.type === selectedType.value)
          : (res || [])
        selectedOptions.value = {}
        showAnalysis.value = false
      } catch (error) {
        console.error('加载题目失败:', error)
      }
    }
    
    const selectOption = (questionId, option) => {
      selectedOptions.value[questionId] = option
    }
    
    const nextQuestion = (currentIndex) => {
      if (currentIndex < questions.value.length - 1) {
        showAnalysis.value = false
      }
    }
    
    onMounted(() => {
      loadCategories()
    })
    
    return {
      categories,
      questions,
      selectedCategory,
      selectedType,
      selectedOptions,
      showAnalysis,
      goBack,
      loadQuestions,
      selectOption,
      nextQuestion
    }
  }
}
</script>

<style scoped>
.practice-container {
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

.card-header {
  display: flex;
  align-items: center;
  gap: 20px;
}

.empty-state {
  text-align: center;
  padding: 100px 0;
}

.empty-icon {
  font-size: 64px;
  color: #ccc;
  margin-bottom: 20px;
}

.question-item {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  background: white;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.question-number {
  font-weight: bold;
  font-size: 16px;
}

.difficulty {
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.difficulty-1, .difficulty-2 { color: #67c23a; background: #f0f9eb; }
.difficulty-3 { color: #e6a23c; background: #fdf6ec; }
.difficulty-4, .difficulty-5 { color: #f56c6c; background: #fef0f0; }

.question-content {
  margin-bottom: 20px;
}

.question-content p {
  line-height: 1.5;
  margin-bottom: 10px;
}

.question-image {
  max-width: 100%;
  max-height: 300px;
  margin: 10px 0;
}

.question-options {
  margin-bottom: 20px;
}

.option-item {
  padding: 10px 15px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
}

.option-item:hover {
  border-color: #409eff;
}

.option-item.active {
  border-color: #409eff;
  background: #ecf5ff;
}

.option-item.correct {
  border-color: #67c23a;
  background: #f0f9eb;
}

.option-item.wrong {
  border-color: #f56c6c;
  background: #fef0f0;
}

.option-label {
  width: 20px;
  margin-right: 10px;
  font-weight: bold;
}

.question-analysis {
  background: #f9f9f9;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.question-actions {
  display: flex;
  gap: 10px;
}
</style>
