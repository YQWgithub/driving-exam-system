<template>
  <div class="exam-history-container">
    <el-container>
      <el-header height="60px" class="header">
        <div class="logo">成绩查询</div>
        <el-button type="text" @click="goBack">返回首页</el-button>
      </el-header>
      
      <el-main>
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>历史考试记录</span>
            </div>
          </template>
          
          <div v-if="examRecords.length === 0" class="empty-state">
            <el-icon class="empty-icon"><Document /></el-icon>
            <p>暂无考试记录</p>
          </div>
          
          <el-table :data="examRecords" style="width: 100%" v-else>
            <el-table-column prop="id" label="考试ID" width="80" />
            <el-table-column prop="paperId" label="试卷ID" width="80" />
            <el-table-column label="开始时间">
              <template #default="scope">
                {{ formatDate(scope.row.startTime) }}
              </template>
            </el-table-column>
            <el-table-column label="结束时间">
              <template #default="scope">
                {{ formatDate(scope.row.endTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="score" label="得分" width="80" />
            <el-table-column label="是否及格" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.isPass ? 'success' : 'danger'">
                  {{ scope.row.isPass ? '及格' : '不及格' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button type="text" @click="viewDetails(scope.row.id)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        
        <el-dialog v-model="showDetails" title="考试详情" width="800px">
          <div v-if="selectedRecord">
            <div class="record-info">
              <div class="info-item">
                <span>考试ID:</span>
                <span>{{ selectedRecord.id }}</span>
              </div>
              <div class="info-item">
                <span>开始时间:</span>
                <span>{{ formatDate(selectedRecord.startTime) }}</span>
              </div>
              <div class="info-item">
                <span>结束时间:</span>
                <span>{{ formatDate(selectedRecord.endTime) }}</span>
              </div>
              <div class="info-item">
                <span>得分:</span>
                <span class="score">{{ selectedRecord.score }}</span>
              </div>
              <div class="info-item">
                <span>是否及格:</span>
                <span class="pass" :class="selectedRecord.isPass ? 'pass' : 'fail'">
                  {{ selectedRecord.isPass ? '及格' : '不及格' }}
                </span>
              </div>
            </div>
            
            <el-divider>答题详情</el-divider>
            
            <div v-if="answerDetails.length === 0">
              <p>暂无答题记录</p>
            </div>
            <div v-else>
              <div v-for="(detail, index) in answerDetails" :key="detail.id" class="answer-item">
                <div class="answer-header">
                  <span class="answer-number">{{ index + 1 }}.</span>
                  <span class="answer-status" :class="detail.isCorrect ? 'correct' : 'wrong'">
                    {{ detail.isCorrect ? '正确' : '错误' }}
                  </span>
                </div>
                <div class="answer-content">
                  <p>用户答案: {{ detail.userAnswer }}</p>
                  <p v-if="detail.question">正确答案: {{ detail.question.answer }}</p>
                </div>
              </div>
            </div>
          </div>
        </el-dialog>
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
  name: 'ExamHistory',
  components: {
    Document
  },
  setup() {
    const router = useRouter()
    const examRecords = ref([])
    const selectedRecord = ref(null)
    const answerDetails = ref([])
    const showDetails = ref(false)
    
    const goBack = () => {
      router.push('/home')
    }
    
    const loadExamRecords = async () => {
      try {
        const user = JSON.parse(localStorage.getItem('user') || '{}')
        const userId = user.id || Number(localStorage.getItem('userId')) || 1
        const res = await request({
          url: '/exam/list',
          method: 'get',
          params: { userId }
        })
        examRecords.value = res || []
      } catch (error) {
        console.error('加载考试记录失败:', error)
      }
    }
    
    const viewDetails = async (recordId) => {
      try {
        const res = await request({
          url: `/exam/record/${recordId}`,
          method: 'get'
        })
        selectedRecord.value = res
        
        const detailsRes = await request({
          url: `/exam/answer/${recordId}`,
          method: 'get'
        })
        
        const details = detailsRes || []
        for (const detail of details) {
          const questionRes = await request({
            url: `/question/info/${detail.questionId}`,
            method: 'get'
          })
          detail.question = questionRes
        }
        answerDetails.value = details
        showDetails.value = true
      } catch (error) {
        console.error('加载考试详情失败:', error)
      }
    }
    
    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString()
    }
    
    const getStatusType = (status) => {
      switch (status) {
        case 0: return 'warning'
        case 1: return 'success'
        case 2: return 'danger'
        default: return 'info'
      }
    }
    
    const getStatusText = (status) => {
      switch (status) {
        case 0: return '进行中'
        case 1: return '已交卷'
        case 2: return '超时'
        default: return '未知'
      }
    }
    
    onMounted(() => {
      loadExamRecords()
    })
    
    return {
      examRecords,
      selectedRecord,
      answerDetails,
      showDetails,
      goBack,
      viewDetails,
      formatDate,
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style scoped>
.exam-history-container {
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

.record-info {
  margin-bottom: 20px;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.score {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.pass {
  color: #67c23a;
  font-weight: bold;
}

.fail {
  color: #f56c6c;
  font-weight: bold;
}

.answer-item {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  background: white;
}

.answer-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.answer-number {
  font-weight: bold;
}

.answer-status {
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.answer-status.correct {
  color: #67c23a;
  background: #f0f9eb;
}

.answer-status.wrong {
  color: #f56c6c;
  background: #fef0f0;
}
</style>
