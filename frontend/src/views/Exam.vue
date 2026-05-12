<template>
  <div class="exam-container">
    <el-container>
      <el-header height="60px" class="header">
        <div class="logo">模拟考试</div>
        <div class="exam-info" v-if="examStarted">
          <span class="timer">剩余时间: {{ formatTime(remainingTime) }}</span>
          <el-button type="danger" @click="handleSubmit">交卷</el-button>
        </div>
      </el-header>
      
      <el-main>
        <el-card shadow="hover" v-if="!examStarted" class="start-card">
          <template #header>
            <div class="card-header">
              <h3>开始模拟考试</h3>
            </div>
          </template>
          
          <div class="exam-intro">
            <p>考试规则：</p>
            <ul>
              <li>共 20 道题目</li>
              <li>考试时间 30 分钟</li>
              <li>满分 100 分</li>
              <li>90 分及格</li>
            </ul>
          </div>
          
          <el-button type="primary" size="large" @click="startExam" :loading="loading">
            开始考试
          </el-button>
        </el-card>
        
        <div v-else class="exam-content">
          <el-card shadow="hover" class="question-card">
            <template #header>
              <div class="card-header">
                <span>第 {{ currentQuestionIndex + 1 }} / {{ questions.length }} 题</span>
              </div>
            </template>
            
            <div v-if="currentQuestion" class="question-item">
              <div class="question-content">
                <p>{{ currentQuestion.content }}</p>
              </div>
              
              <div class="question-options">
                <div
                  v-for="option in ['A', 'B', 'C', 'D']"
                  :key="option"
                  class="option-item"
                  :class="{ active: answers[currentQuestion.id] === option }"
                  @click="selectOption(currentQuestion.id, option)"
                >
                  <span class="option-label">{{ option }}</span>
                  <span class="option-content">{{ getOptionContent(currentQuestion, option) }}</span>
                </div>
              </div>
              
              <div class="question-actions">
                <el-button @click="prevQuestion" :disabled="currentQuestionIndex === 0">上一题</el-button>
                <el-button @click="nextQuestion" :disabled="currentQuestionIndex === questions.length - 1">下一题</el-button>
              </div>
            </div>
          </el-card>
          
          <div class="question-nav">
            <div class="nav-title">答题卡</div>
            <div class="nav-grid">
              <div
                v-for="(question, index) in questions"
                :key="question.id"
                class="question-nav-item"
                :class="{
                  answered: answers[question.id],
                  current: index === currentQuestionIndex
                }"
                @click="goToQuestion(index)"
              >
                {{ index + 1 }}
              </div>
            </div>
          </div>
        </div>
        
        <el-dialog v-model="showResult" title="考试结果" width="500px" :close-on-click-modal="false">
          <div class="exam-result">
            <div class="result-score">
              <span class="score-value">{{ examResult.score }}</span>
              <span class="score-unit">分</span>
            </div>
            <div class="result-detail">
              <p>答对: {{ examResult.correctCount }} / {{ examResult.totalQuestions }} 题</p>
              <p :class="examResult.score >= 90 ? 'pass' : 'fail'">
                {{ examResult.score >= 90 ? '恭喜通过！' : '未及格，请继续努力！' }}
              </p>
            </div>
          </div>
          <template #footer>
            <el-button type="primary" @click="resetExam">重新考试</el-button>
            <el-button @click="goHome">返回首页</el-button>
          </template>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { ref, computed, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { examApi } from '@/api/exam'
import { wrongApi } from '@/api/wrong'

export default {
  name: 'Exam',
  setup() {
    const router = useRouter()
    const questions = ref([])
    const examStarted = ref(false)
    const currentQuestionIndex = ref(0)
    const answers = ref({})
    const showResult = ref(false)
    const examResult = ref({ score: 0, correctCount: 0, totalQuestions: 0 })
    const timer = ref(null)
    const remainingTime = ref(30 * 60)
    const loading = ref(false)
    
    const currentQuestion = computed(() => {
      return questions.value[currentQuestionIndex.value]
    })
    
    const formatTime = (seconds) => {
      const minutes = Math.floor(seconds / 60)
      const secs = seconds % 60
      return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    }
    
    const getOptionContent = (question, option) => {
      if (!question || !question.options) return ''
      try {
        const opts = JSON.parse(question.options)
        return opts[option] || ''
      } catch {
        return ''
      }
    }
    
    const startExam = async () => {
      loading.value = true
      try {
        const res = await examApi.getRandomQuestions(20)
        if (res && res.length > 0) {
          questions.value = res
        } else {
          ElMessage.error('题库中暂无题目')
          return
        }
        examStarted.value = true
        startTimer()
      } catch (error) {
        console.error('加载试题失败:', error)
        ElMessage.error('加载试题失败，请检查网络连接')
      } finally {
        loading.value = false
      }
    }
    
    const startTimer = () => {
      timer.value = setInterval(() => {
        if (remainingTime.value > 0) {
          remainingTime.value--
        } else {
          clearInterval(timer.value)
          handleSubmit()
        }
      }, 1000)
    }
    
    const selectOption = (questionId, option) => {
      answers.value[questionId] = option
    }
    
    const prevQuestion = () => {
      if (currentQuestionIndex.value > 0) {
        currentQuestionIndex.value--
      }
    }
    
    const nextQuestion = () => {
      if (currentQuestionIndex.value < questions.value.length - 1) {
        currentQuestionIndex.value++
      }
    }
    
    const goToQuestion = (index) => {
      currentQuestionIndex.value = index
    }
    
    const handleSubmit = async () => {
      if (timer.value) {
        clearInterval(timer.value)
      }
      
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      const userId = user.id || 1
      const answerList = questions.value.map(q => ({
        questionId: q.id,
        answer: answers.value[q.id] || ''
      }))
      
      try {
        const res = await examApi.submitExam({
          userId: userId,
          answers: answerList
        })
        examResult.value = {
          score: res.score,
          correctCount: res.correctCount,
          totalQuestions: res.totalQuestions
        }
        
        questions.value.forEach(q => {
          const userAnswer = answers.value[q.id]
          if (userAnswer && userAnswer !== q.answer) {
            wrongApi.addWrongQuestion({
              userId: userId,
              questionId: q.id,
              wrongAnswer: userAnswer,
              correctAnswer: q.answer,
              questionContent: q.content,
              options: q.options
            }).catch(err => console.error('录入错题失败:', err))
          }
        })
        
        showResult.value = true
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('提交失败，请检查网络连接')
      }
    }
    
    const resetExam = () => {
      showResult.value = false
      examStarted.value = false
      questions.value = []
      answers.value = {}
      currentQuestionIndex.value = 0
      remainingTime.value = 30 * 60
    }
    
    const goHome = () => {
      router.push('/home')
    }
    
    onUnmounted(() => {
      if (timer.value) {
        clearInterval(timer.value)
      }
    })
    
    return {
      questions,
      examStarted,
      currentQuestionIndex,
      currentQuestion,
      answers,
      showResult,
      examResult,
      remainingTime,
      loading,
      formatTime,
      getOptionContent,
      startExam,
      selectOption,
      prevQuestion,
      nextQuestion,
      goToQuestion,
      handleSubmit,
      resetExam,
      goHome
    }
  }
}
</script>

<style scoped>
.exam-container {
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

.exam-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.timer {
  font-size: 16px;
  font-weight: bold;
  color: #f56c6c;
}

.start-card {
  max-width: 500px;
  margin: 100px auto;
  text-align: center;
}

.card-header {
  font-size: 16px;
  font-weight: bold;
}

.exam-intro {
  text-align: left;
  margin-bottom: 30px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.exam-intro p {
  font-weight: bold;
  margin-bottom: 10px;
}

.exam-intro ul {
  list-style: none;
  padding: 0;
}

.exam-intro li {
  padding: 5px 0;
  color: #666;
}

.exam-content {
  display: flex;
  gap: 20px;
  padding: 20px;
}

.question-card {
  flex: 1;
}

.question-item {
  padding: 20px;
}

.question-content {
  margin-bottom: 30px;
}

.question-content p {
  font-size: 18px;
  line-height: 1.8;
}

.question-options {
  margin-bottom: 30px;
}

.option-item {
  padding: 15px 20px;
  border: 2px solid #e4e7ed;
  border-radius: 8px;
  margin-bottom: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
}

.option-item:hover {
  border-color: #409eff;
  background: #ecf5ff;
}

.option-item.active {
  border-color: #409eff;
  background: #ecf5ff;
}

.option-label {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: #e4e7ed;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
  font-weight: bold;
}

.option-item.active .option-label {
  background: #409eff;
  color: white;
}

.option-content {
  font-size: 16px;
  line-height: 1.5;
}

.question-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
}

.question-nav {
  width: 200px;
  background: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: fit-content;
}

.nav-title {
  font-weight: bold;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e7ed;
}

.nav-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
}

.question-nav-item {
  width: 32px;
  height: 32px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.question-nav-item:hover {
  border-color: #409eff;
}

.question-nav-item.answered {
  background: #67c23a;
  color: white;
  border-color: #67c23a;
}

.question-nav-item.current {
  border-color: #409eff;
  background: #409eff;
  color: white;
}

.exam-result {
  text-align: center;
  padding: 30px;
}

.result-score {
  margin-bottom: 20px;
}

.score-value {
  font-size: 60px;
  font-weight: bold;
  color: #409eff;
}

.score-unit {
  font-size: 24px;
  color: #666;
}

.result-detail {
  font-size: 16px;
  color: #666;
}

.result-detail p {
  margin: 10px 0;
}

.pass {
  color: #67c23a;
  font-weight: bold;
  font-size: 18px;
}

.fail {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}
</style>
