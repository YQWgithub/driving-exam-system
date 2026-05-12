<template>
  <div class="chapter-practice-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3>章节练习</h3>
        </div>
      </template>

      <div class="chapter-select" v-if="!practiceStarted">
        <el-form :inline="true">
          <el-form-item label="选择章节">
            <el-select v-model="selectedChapter" placeholder="请选择章节" style="width: 300px">
              <el-option
                v-for="chapter in chapters"
                :key="chapter.value"
                :label="chapter.label"
                :value="chapter.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="startPractice" :disabled="!selectedChapter">开始练习</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="practice-content" v-else>
        <div class="progress-info">
          <span>当前进度: {{ currentIndex + 1 }} / {{ questions.length }}</span>
          <el-button type="text" @click="exitPractice">退出练习</el-button>
        </div>

        <div class="question-item" v-if="currentQuestion">
          <div class="question-content">
            <p>{{ currentQuestion.content }}</p>
          </div>

          <div class="question-options">
            <div
              v-for="option in ['A', 'B', 'C', 'D']"
              :key="option"
              class="option-item"
              :class="{
                selected: userAnswers[currentQuestion.id] === option,
                correct: showAnswer && currentQuestion.answer === option,
                wrong: showAnswer && userAnswers[currentQuestion.id] === option && currentQuestion.answer !== option
              }"
              @click="selectOption(currentQuestion.id, option)"
            >
              <span class="option-label">{{ option }}</span>
              <span class="option-content">{{ getOptionContent(currentQuestion, option) }}</span>
            </div>
          </div>

          <div class="question-actions">
            <el-button @click="prevQuestion" :disabled="currentIndex === 0">上一题</el-button>
            <el-button type="primary" @click="submitAnswer" v-if="!showAnswer">提交答案</el-button>
            <el-button @click="nextQuestion" :disabled="currentIndex === questions.length - 1" v-else>下一题</el-button>
          </div>

          <div class="answer-analysis" v-if="showAnswer">
            <h4>答案解析</h4>
            <p>正确答案: {{ currentQuestion.answer }}</p>
            <p>{{ currentQuestion.analysis || '暂无解析' }}</p>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { questionApi } from '@/api/question'
import { wrongApi } from '@/api/wrong'

export default {
  name: 'ChapterPractice',
  setup() {
    const chapters = ref([
      { value: '道路交通安全法律法规', label: '道路交通安全法律法规' },
      { value: '道路交通信号', label: '道路交通信号' },
      { value: '安全行车、文明驾驶', label: '安全行车、文明驾驶' },
      { value: '机动车驾驶操作相关基础知识', label: '机动车驾驶操作相关基础知识' },
      { value: '恶劣气象和复杂道路条件下的驾驶常识', label: '恶劣气象和复杂道路条件下的驾驶常识' },
      { value: '紧急情况避险', label: '紧急情况避险' },
      { value: '交通事故处置', label: '交通事故处置' }
    ])

    const selectedChapter = ref('')
    const practiceStarted = ref(false)
    const questions = ref([])
    const currentIndex = ref(0)
    const userAnswers = ref({})
    const showAnswer = ref(false)

    const currentQuestion = computed(() => {
      return questions.value[currentIndex.value]
    })

    const getOptionContent = (question, option) => {
      if (!question || !question.options) return ''
      try {
        const opts = JSON.parse(question.options)
        return opts[option] || ''
      } catch {
        return ''
      }
    }

    const startPractice = async () => {
      if (!selectedChapter.value) return
      
      try {
        const res = await questionApi.getByChapter(selectedChapter.value)
        if (res && res.length > 0) {
          questions.value = res
        } else {
          ElMessage.warning('该章节暂无题目')
          return
        }
      } catch (error) {
        console.error('加载题目失败:', error)
        ElMessage.error('加载题目失败，请检查网络连接')
        return
      }
      
      practiceStarted.value = true
      currentIndex.value = 0
      userAnswers.value = {}
      showAnswer.value = false
    }

    const selectOption = (questionId, option) => {
      if (!showAnswer.value) {
        userAnswers.value[questionId] = option
      }
    }

    const submitAnswer = () => {
      showAnswer.value = true
      
      const userAnswer = userAnswers.value[currentQuestion.value.id]
      if (userAnswer && userAnswer !== currentQuestion.value.answer) {
        const user = JSON.parse(localStorage.getItem('user') || '{}')
        const userId = user.id || 1
        
        wrongApi.addWrongQuestion({
          userId: userId,
          questionId: currentQuestion.value.id,
          wrongAnswer: userAnswer,
          correctAnswer: currentQuestion.value.answer,
          questionContent: currentQuestion.value.content,
          options: currentQuestion.value.options
        }).catch(err => console.error('录入错题失败:', err))
      }
    }

    const prevQuestion = () => {
      if (currentIndex.value > 0) {
        currentIndex.value--
        showAnswer.value = false
      }
    }

    const nextQuestion = () => {
      if (currentIndex.value < questions.value.length - 1) {
        currentIndex.value++
        showAnswer.value = false
      }
    }

    const exitPractice = () => {
      practiceStarted.value = false
      questions.value = []
      currentIndex.value = 0
      userAnswers.value = {}
      showAnswer.value = false
    }

    return {
      chapters,
      selectedChapter,
      practiceStarted,
      questions,
      currentIndex,
      currentQuestion,
      userAnswers,
      showAnswer,
      startPractice,
      getOptionContent,
      selectOption,
      submitAnswer,
      prevQuestion,
      nextQuestion,
      exitPractice
    }
  }
}
</script>

<style scoped>
.chapter-practice-container {
  padding: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.chapter-select {
  padding: 40px 0;
  text-align: center;
}

.practice-content {
  padding: 20px 0;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e4e7ed;
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

.option-item.selected {
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

.option-item.selected .option-label {
  background: #409eff;
  color: white;
}

.option-item.correct .option-label {
  background: #67c23a;
  color: white;
}

.option-item.wrong .option-label {
  background: #f56c6c;
  color: white;
}

.option-content {
  font-size: 16px;
  line-height: 1.5;
}

.question-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.answer-analysis {
  margin-top: 30px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.answer-analysis h4 {
  margin: 0 0 10px 0;
  color: #409eff;
}

.answer-analysis p {
  margin: 5px 0;
  color: #606266;
}
</style>
