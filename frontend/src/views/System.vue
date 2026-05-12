<template>
  <div class="system-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <h3>系统管理</h3>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="题库管理" name="questions">
          <div class="toolbar">
            <el-button type="primary" @click="showAddDialog">添加题目</el-button>
            <el-button @click="loadQuestions" :loading="loading">刷新</el-button>
          </div>
          
          <el-table :data="questions" style="width: 100%" v-loading="loading">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="content" label="题目内容" show-overflow-tooltip />
            <el-table-column prop="chapter" label="章节" width="180" />
            <el-table-column prop="answer" label="答案" width="80" />
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="editQuestion(row)">编辑</el-button>
                <el-button type="danger" size="small" @click="deleteQuestion(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="用户管理" name="users">
          <el-table :data="users" style="width: 100%">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="role" label="角色" width="100">
              <template #default="{ row }">
                <el-tag :type="row.role === 1 ? 'danger' : 'success'">
                  {{ row.role === 1 ? '管理员' : '学员' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'">
                  {{ row.status === 1 ? '正常' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑题目' : '添加题目'" width="600px">
      <el-form :model="questionForm" label-width="80px">
        <el-form-item label="题目内容">
          <el-input v-model="questionForm.content" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="选项A">
          <el-input v-model="questionForm.optionA" />
        </el-form-item>
        <el-form-item label="选项B">
          <el-input v-model="questionForm.optionB" />
        </el-form-item>
        <el-form-item label="选项C">
          <el-input v-model="questionForm.optionC" />
        </el-form-item>
        <el-form-item label="选项D">
          <el-input v-model="questionForm.optionD" />
        </el-form-item>
        <el-form-item label="正确答案">
          <el-select v-model="questionForm.answer" style="width: 100%">
            <el-option label="A" value="A" />
            <el-option label="B" value="B" />
            <el-option label="C" value="C" />
            <el-option label="D" value="D" />
          </el-select>
        </el-form-item>
        <el-form-item label="章节">
          <el-select v-model="questionForm.chapter" style="width: 100%">
            <el-option label="道路交通安全法律法规" value="道路交通安全法律法规" />
            <el-option label="道路交通信号" value="道路交通信号" />
            <el-option label="安全行车、文明驾驶" value="安全行车、文明驾驶" />
            <el-option label="机动车驾驶操作相关基础知识" value="机动车驾驶操作相关基础知识" />
            <el-option label="恶劣气象和复杂道路条件下的驾驶常识" value="恶劣气象和复杂道路条件下的驾驶常识" />
            <el-option label="紧急情况避险" value="紧急情况避险" />
            <el-option label="交通事故处置" value="交通事故处置" />
          </el-select>
        </el-form-item>
        <el-form-item label="解析">
          <el-input v-model="questionForm.analysis" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveQuestion">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { questionApi } from '@/api/question'
import { userApi } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'System',
  setup() {
    const activeTab = ref('questions')
    const questions = ref([])
    const users = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const isEdit = ref(false)
    const questionForm = ref({
      id: null,
      content: '',
      optionA: '',
      optionB: '',
      optionC: '',
      optionD: '',
      answer: 'A',
      chapter: '',
      analysis: '',
      categoryId: 1,
      type: 'single'
    })

    const parseOptions = (options) => {
      try {
        const parsed = JSON.parse(options || '{}')
        return {
          optionA: parsed.A || '',
          optionB: parsed.B || '',
          optionC: parsed.C || '',
          optionD: parsed.D || ''
        }
      } catch {
        return { optionA: '', optionB: '', optionC: '', optionD: '' }
      }
    }

    const buildOptions = (form) => {
      return JSON.stringify({
        A: form.optionA,
        B: form.optionB,
        C: form.optionC,
        D: form.optionD
      })
    }

    const loadQuestions = () => {
      loading.value = true
      const startTime = Date.now()
      questionApi.getList().then(res => {
        if (import.meta.env.DEV) {
          console.debug('【题库数据】耗时:', Date.now() - startTime, 'ms, 数据:', res)
        }
        questions.value = res || []
      }).catch(err => {
        console.error('加载题目失败:', err)
        questions.value = []
      }).finally(() => {
        loading.value = false
      })
    }

    const loadUsers = () => {
      userApi.getUserList().then(res => {
        users.value = res || []
      }).catch(err => {
        console.error('加载用户失败:', err)
        users.value = []
      })
    }

    const showAddDialog = () => {
      isEdit.value = false
      questionForm.value = {
        id: null,
        content: '',
        optionA: '',
        optionB: '',
        optionC: '',
        optionD: '',
        answer: 'A',
        chapter: '',
        analysis: '',
        categoryId: 1,
        type: 'single'
      }
      dialogVisible.value = true
    }

    const editQuestion = (row) => {
      isEdit.value = true
      const opts = parseOptions(row.options)
      questionForm.value = {
        id: row.id,
        content: row.content,
        optionA: opts.optionA,
        optionB: opts.optionB,
        optionC: opts.optionC,
        optionD: opts.optionD,
        answer: row.answer,
        chapter: row.chapter,
        analysis: row.analysis,
        categoryId: row.categoryId || 1,
        type: row.type || 'single'
      }
      dialogVisible.value = true
    }

    const saveQuestion = () => {
      const data = {
        id: questionForm.value.id,
        content: questionForm.value.content,
        options: buildOptions(questionForm.value),
        answer: questionForm.value.answer,
        chapter: questionForm.value.chapter,
        analysis: questionForm.value.analysis,
        categoryId: questionForm.value.categoryId,
        type: questionForm.value.type
      }
      
      if (isEdit.value) {
        questionApi.updateQuestion(data).then(() => {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          loadQuestions()
        }).catch(() => {
          ElMessage.error('保存失败')
        })
      } else {
        questionApi.addQuestion(data).then(() => {
          ElMessage.success('添加成功')
          dialogVisible.value = false
          loadQuestions()
        }).catch(() => {
          ElMessage.error('保存失败')
        })
      }
    }

    const deleteQuestion = (row) => {
      ElMessageBox.confirm('确定删除该题目？', '提示', { type: 'warning' }).then(() => {
        questionApi.deleteQuestion(row.id).then(() => {
          ElMessage.success('删除成功')
          loadQuestions()
        }).catch(() => {
          ElMessage.error('删除失败')
        })
      }).catch(() => {})
    }

    onMounted(() => {
      loadQuestions()
      loadUsers()
    })

    return {
      activeTab,
      questions,
      users,
      loading,
      dialogVisible,
      isEdit,
      questionForm,
      loadQuestions,
      showAddDialog,
      editQuestion,
      saveQuestion,
      deleteQuestion
    }
  }
}
</script>

<style scoped>
.system-container {
  padding: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.toolbar {
  margin-bottom: 20px;
}
</style>
