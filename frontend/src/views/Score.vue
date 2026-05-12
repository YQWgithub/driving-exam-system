<template>
  <div class="score-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3>考试记录</h3>
        </div>
      </template>

      <el-table :data="records" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="记录ID" width="80" />
        <el-table-column label="得分" width="120">
          <template #default="{ row }">
            <span :class="row.score >= 90 ? 'pass' : 'fail'">{{ row.score }} / {{ row.total }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isPass === 1 ? 'success' : 'danger'">
              {{ row.isPass === 1 ? '通过' : '未通过' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="考试时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && records.length === 0" description="暂无考试记录" />
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { examApi } from '@/api/exam'

export default {
  name: 'Score',
  setup() {
    const records = ref([])
    const loading = ref(false)

    const formatTime = (time) => {
      if (!time) return '-'
      const date = new Date(time)
      return date.toLocaleString('zh-CN')
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

    const loadRecords = () => {
      loading.value = true
      const userId = getUserId()
      
      const startTime = Date.now()
      examApi.getExamRecords(userId).then(res => {
        if (import.meta.env.DEV) {
          console.debug('【成绩记录数据】耗时:', Date.now() - startTime, 'ms, 数据:', res)
        }
        records.value = res || []
      }).catch(err => {
        console.error('加载记录失败:', err)
        records.value = []
      }).finally(() => {
        loading.value = false
      })
    }

    const viewDetail = (row) => {
      alert(`考试记录 #${row.id}\n得分: ${row.score}/${row.total}\n状态: ${row.isPass === 1 ? '通过' : '未通过'}`)
    }

    onMounted(() => {
      loadRecords()
    })

    return {
      records,
      loading,
      formatTime,
      viewDetail
    }
  }
}
</script>

<style scoped>
.score-container {
  padding: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.pass {
  color: #67c23a;
  font-weight: bold;
}

.fail {
  color: #f56c6c;
  font-weight: bold;
}
</style>
