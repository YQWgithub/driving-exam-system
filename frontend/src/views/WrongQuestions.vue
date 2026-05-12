<template>
  <div class="wrong-container">
    <el-container>
      <el-header height="60px" class="header">
        <div class="logo">错题本</div>
        <el-button type="text" @click="goBack">返回首页</el-button>
      </el-header>
      
      <el-main>
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>我的错题 (共 {{ wrongQuestions.length }} 道)</span>
            </div>
          </template>
          
          <div v-if="wrongQuestions.length === 0" class="empty-state">
            <el-icon class="empty-icon"><Warning /></el-icon>
            <p>暂无错题</p>
          </div>
          
          <div v-else>
            <div v-for="(question, index) in wrongQuestions" :key="question.id" class="question-item">
              <div class="question-header">
                <span class="question-number">{{ index + 1 }}. {{ question.type || '单选题' }}</span>
                <span class="difficulty" :class="`difficulty-${question.difficulty || 1}`">
                  难度: {{ question.difficulty || 1 }}星
                </span>
              </div>
              
              <div class="question-content">
                <p>{{ question.content }}</p>
                <img v-if="question.imageUrl" :src="question.imageUrl" class="question-image" />
              </div>
              
              <div class="question-options" v-if="question.options">
                <div
                  v-for="(option, key) in parseOptions(question.options)"
                  :key="key"
                  class="option-item"
                  :class="{ correct: key === question.answer }"
                >
                  <span class="option-label">{{ key }}</span>
                  <span class="option-content">{{ option }}</span>
                </div>
              </div>
              
              <div class="question-analysis" v-if="question.analysis">
                <h4>解析：</h4>
                <p>{{ question.analysis }}</p>
              </div>
              
              <div class="question-actions">
                <el-button type="danger" @click="removeWrongQuestion(question)">移除错题</el-button>
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
import { Warning } from '@element-plus/icons-vue'
import { wrongApi } from '@/api/wrong'

export default {
  name: 'WrongQuestions',
  components: {
    Warning
  },
  setup() {
    const router = useRouter()
    const wrongQuestions = ref([])
    
    const goBack = () => {
      router.push('/home')
    }
    
    const parseOptions = (options) => {
      if (!options) return {}
      try {
        return JSON.parse(options)
      } catch {
        return {}
      }
    }
    
    const loadWrongQuestions = async () => {
      try {
        const user = JSON.parse(localStorage.getItem('user') || '{}')
        const userId = user.id || 1
        const res = await wrongApi.getWrongQuestions(userId)
        wrongQuestions.value = res || []
      } catch (error) {
        console.error('加载错题失败:', error)
        wrongQuestions.value = []
      }
    }
    
    const removeWrongQuestion = async (question) => {
      try {
        await wrongApi.deleteWrongQuestionById(question.id)
        wrongQuestions.value = wrongQuestions.value.filter(q => q.id !== question.id)
      } catch (error) {
        console.error('移除错题失败:', error)
      }
    }
    
    onMounted(() => {
      loadWrongQuestions()
    })
    
    return {
      wrongQuestions,
      goBack,
      parseOptions,
      removeWrongQuestion
    }
  }
}
</script>

<style scoped>
.wrong-container {
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
  font-size: 16px;
  font-weight: bold;
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
  display: flex;
  align-items: center;
}

.option-item.correct {
  border-color: #67c23a;
  background: #f0f9eb;
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
  text-align: right;
}
</style>
