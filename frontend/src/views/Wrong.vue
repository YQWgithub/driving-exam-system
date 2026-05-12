<template>
  <div class="wrong-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3>错题本</h3>
          <div class="header-actions">
            <span class="count">共 {{ wrongQuestions.length }} 道错题</span>
            <el-button v-if="wrongQuestions.length > 0" type="danger" size="small" @click="clearAll">清空错题</el-button>
          </div>
        </div>
      </template>

      <div v-loading="loading">
        <div v-if="wrongQuestions.length > 0" class="question-list">
          <div v-for="item in wrongQuestions" :key="item.id" class="wrong-item">
            <div class="question-header">
              <span class="question-id">题目 #{{ item.questionId }}</span>
              <el-tag type="danger" size="small">错题</el-tag>
            </div>
            
            <div class="question-content">
              <p>{{ item.content }}</p>
            </div>

            <div class="question-options">
              <div
                v-for="option in ['A', 'B', 'C', 'D']"
                :key="option"
                class="option-item"
                :class="{
                  correct: item.answer === option,
                  wrong: item.wrongAnswer === option && item.answer !== option
                }"
              >
                <span class="option-label">{{ option }}</span>
                <span class="option-content">{{ getOptionContent(item, option) }}</span>
                <el-tag v-if="item.answer === option" type="success" size="small">正确答案</el-tag>
              </div>
            </div>

            <div v-if="item.analysis" class="question-analysis">
              <strong>解析：</strong>{{ item.analysis }}
            </div>

            <div class="question-footer">
              <span class="time">错误时间: {{ formatTime(item.createTime) }}</span>
              <el-button type="danger" size="small" @click="deleteWrong(item)">移除</el-button>
            </div>
          </div>
        </div>

        <el-empty v-else description="暂无错题" />
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { wrongApi } from '@/api/wrong'

export default {
  name: 'Wrong',
  setup() {
    const wrongQuestions = ref([])
    const loading = ref(false)

    const formatTime = (time) => {
      if (!time) return '-'
      const date = new Date(time)
      return date.toLocaleString('zh-CN')
    }

    const getOptionContent = (item, option) => {
      if (!item || !item.options) return ''
      try {
        const opts = JSON.parse(item.options)
        return opts[option] || ''
      } catch {
        return ''
      }
    }

    const getUserId = () => {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        try {
          const user = JSON.parse(userStr)
          return user.id || 1
        } catch {
          return 1
        }
      }
      return parseInt(localStorage.getItem('userId')) || 1
    }

    const loadWrongQuestions = () => {
      loading.value = true
      const userId = getUserId()
      
      wrongApi.getWrongQuestions(userId).then(res => {
        wrongQuestions.value = res || []
      }).catch(err => {
        console.error('加载错题失败:', err)
        wrongQuestions.value = []
      }).finally(() => {
        loading.value = false
      })
    }

    const deleteWrong = (item) => {
      wrongApi.deleteWrongQuestionById(item.id).then(() => {
        wrongQuestions.value = wrongQuestions.value.filter(q => q.id !== item.id)
        ElMessage.success('删除成功')
      }).catch(err => {
        console.error('删除失败:', err)
        ElMessage.error('删除失败')
      })
    }

    const clearAll = () => {
      ElMessageBox.confirm('确定要清空所有错题吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const userId = getUserId()
        wrongApi.clearWrongQuestions(userId).then(() => {
          wrongQuestions.value = []
          ElMessage.success('清空成功')
        }).catch(err => {
          console.error('清空失败:', err)
          ElMessage.error('清空失败')
        })
      }).catch(() => {})
    }

    onMounted(() => {
      loadWrongQuestions()
    })

    return {
      wrongQuestions,
      loading,
      formatTime,
      getOptionContent,
      deleteWrong,
      clearAll
    }
  }
}
</script>

<style scoped>
.wrong-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.count {
  color: #909399;
  font-size: 14px;
}

.question-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.wrong-item {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  background: #fafafa;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.question-id {
  font-weight: bold;
  color: #606266;
}

.question-content {
  margin-bottom: 15px;
}

.question-content p {
  font-size: 16px;
  line-height: 1.6;
  margin: 0;
}

.question-options {
  margin-bottom: 15px;
}

.option-item {
  padding: 10px 15px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  background: white;
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
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #e4e7ed;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 10px;
  font-weight: bold;
  font-size: 12px;
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
  flex: 1;
  font-size: 14px;
}

.question-analysis {
  padding: 10px 15px;
  background: #f4f4f5;
  border-radius: 4px;
  margin-bottom: 15px;
  font-size: 14px;
  color: #606266;
}

.question-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #e4e7ed;
}

.time {
  color: #909399;
  font-size: 12px;
}
</style>
