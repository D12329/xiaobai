<template>
  <div class="interview-page">
    <div class="page-header">
      <h2>面试管理</h2>
      <el-button type="primary" @click="showAddModal = true">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        安排面试
      </el-button>
    </div>
    
    <div class="search-bar">
      <el-input 
        v-model="searchKeyword" 
        placeholder="搜索候选人姓名"
        class="search-input"
        @keyup.enter="loadData"
      >
        <template #append>
          <button @click="loadData" class="search-btn">
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="11" cy="11" r="8"/>
              <line x1="21" y1="21" x2="16.65" y2="16.65"/>
            </svg>
          </button>
        </template>
      </el-input>
      
      <el-select v-model="statusFilter" placeholder="筛选状态" class="status-filter" @change="loadData">
        <el-option label="全部" value=""/>
        <el-option label="待面试" :value="0"/>
        <el-option label="已完成" :value="1"/>
        <el-option label="逾期" :value="2"/>
      </el-select>
    </div>
    
    <el-table :data="tableData" border class="data-table" v-loading="loading">
      <el-table-column prop="candidateName" label="候选人" min-width="100"/>
      <el-table-column prop="positionName" label="应聘职位" min-width="120"/>
      <el-table-column prop="interviewerName" label="面试官" min-width="120"/>
      <el-table-column prop="interviewTime" label="面试日期" min-width="150">
        <template #default="scope">
          {{ formatDate(scope.row.interviewTime || scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="interviewTime" label="面试时间" min-width="120">
        <template #default="scope">
          {{ formatTime(scope.row.interviewTime || scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="location" label="面试地点" min-width="120"/>
      <el-table-column prop="status" label="状态" min-width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="安排时间" min-width="150">
        <template #default="scope">
          {{ formatDateTime(scope.row.createdAt || scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="200">
        <template #default="scope">
          <el-button size="small" @click="viewInterview(scope.row)">查看详情</el-button>
          <el-button size="small" v-if="scope.row.status === 0" @click="editInterview(scope.row)">编辑</el-button>
          <el-button size="small" v-if="scope.row.status === 0" type="warning" @click="sendNotification(scope.row)">发送通知</el-button>
          <el-button size="small" type="danger" v-if="scope.row.candidateId" @click="deleteInterview(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <div class="pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>
    
    <el-dialog :title="editForm.id ? '编辑面试安排' : '安排面试'" v-model="showAddModal" width="600px">
      <el-form :model="editForm" ref="formRef" label-width="100px">
        <el-form-item label="候选人" prop="candidateId" required>
          <el-select v-model="editForm.candidateId" placeholder="请选择候选人">
            <el-option 
              v-for="candidate in candidates" 
              :key="candidate.id" 
              :label="candidate.name + ' - ' + candidate.positionName" 
              :value="candidate.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="面试官" prop="interviewerId" required>
          <el-select v-model="editForm.interviewerId" placeholder="请选择面试官">
            <el-option 
              v-for="user in interviewers" 
              :key="user.id" 
              :label="user.name" 
              :value="user.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="面试日期" prop="interviewDate" required>
          <el-date-picker v-model="editForm.interviewDate" type="date" placeholder="选择日期"/>
        </el-form-item>
        <el-form-item label="面试时间" prop="interviewTime" required>
          <el-time-picker v-model="editForm.interviewTime" placeholder="选择时间"/>
        </el-form-item>
        <el-form-item label="面试地点" prop="location">
          <el-input v-model="editForm.location" placeholder="请输入面试地点"/>
        </el-form-item>
        <el-form-item label="面试类型" prop="type">
          <el-select v-model="editForm.type" placeholder="请选择面试类型">
            <el-option label="初试" value="初试"/>
            <el-option label="复试" value="复试"/>
            <el-option label="终面" value="终面"/>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-textarea v-model="editForm.remark" placeholder="请输入备注信息" :rows="2"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveInterview">确定</el-button>
      </template>
    </el-dialog>
    
    <el-dialog title="面试详情与评价" v-model="showDetailModal" width="700px">
      <div v-if="detailData" class="detail-content">
        <div class="detail-row">
          <span class="detail-label">候选人</span>
          <span class="detail-value">{{ detailData.candidateName }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">应聘职位</span>
          <span class="detail-value">{{ detailData.positionName }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">面试官</span>
          <span class="detail-value">{{ detailData.interviewerName }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">面试日期</span>
          <span class="detail-value">{{ detailData.interviewDate }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">面试时间</span>
          <span class="detail-value">{{ detailData.interviewTime }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">面试地点</span>
          <span class="detail-value">{{ detailData.location }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">面试类型</span>
          <span class="detail-value">{{ detailData.type }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">状态</span>
          <el-tag :type="getStatusType(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </div>
        
        <div v-if="isInterviewPending" class="action-buttons">
          <el-button type="primary" @click="startInterview">开始面试</el-button>
          <el-button type="danger" @click="cancelInterview">取消面试</el-button>
        </div>
        
        <div v-if="isInterviewCompleted" class="evaluation-section">
          <h4>面试评价</h4>
          <div class="evaluation-content">
            <div class="evaluation-item">
              <span class="eval-label">综合评分</span>
              <el-rate v-model="evaluationForm.score" :disabled="true" show-score text-color="#00d4aa"/>
            </div>
            <div class="evaluation-item">
              <span class="eval-label">评价结果</span>
              <el-tag :type="evaluationForm.result === 'pass' ? 'success' : 'danger'">
                {{ evaluationForm.result === 'pass' ? '通过' : '不通过' }}
              </el-tag>
            </div>
            <div class="evaluation-item">
              <span class="eval-label">评价内容</span>
              <p>{{ evaluationForm.comment }}</p>
            </div>
          </div>
        </div>
        
        <div v-if="isInterviewPending" class="add-evaluation">
          <h4>添加面试评价</h4>
          <el-form :model="evaluationForm" label-width="80px">
            <el-form-item label="综合评分">
              <el-rate v-model="evaluationForm.score" show-score text-color="#00d4aa"/>
            </el-form-item>
            <el-form-item label="评价结果">
              <el-select v-model="evaluationForm.result" placeholder="请选择评价结果">
                <el-option label="通过" value="pass"/>
                <el-option label="不通过" value="fail"/>
              </el-select>
            </el-form-item>
            <el-form-item label="评价内容">
              <el-textarea v-model="evaluationForm.comment" placeholder="请输入评价内容" :rows="3"/>
            </el-form-item>
            <el-button type="primary" @click="submitEvaluation">提交评价</el-button>
          </el-form>
        </div>
        
        <div v-if="isInterviewCompleted" class="action-buttons">
          <el-button type="primary" @click="markPass">通过</el-button>
          <el-button type="danger" @click="markFail">不通过</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const tableData = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const statusFilter = ref('')
const showAddModal = ref(false)
const showDetailModal = ref(false)
const formRef = ref(null)
const detailData = ref(null)

const candidates = ref([])
const interviewers = ref([])

const isInterviewPending = computed(() => {
  if (!detailData.value) return false
  const status = detailData.value.status
  return status === 'pending' || status === 'scheduled' || status === '0' || status === 0
})

const isInterviewCompleted = computed(() => {
  if (!detailData.value) return false
  const status = detailData.value.status
  return status === 'completed' || status === '1' || status === 1
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const editForm = reactive({
  id: null,
  candidateId: null,
  candidateName: '',
  positionId: null,
  positionName: '',
  interviewerId: null,
  interviewerName: '',
  interviewDate: '',
  interviewTime: '',
  location: '',
  type: '初试',
  remark: '',
  status: 0
})

const evaluationForm = reactive({
  score: 0,
  result: '',
  comment: ''
})

function getStatusText(status) {
  const map = {
    'scheduled': '待面试',
    'completed': '已完成',
    'cancelled': '已取消',
    'pending': '待面试',
    'passed': '通过',
    'failed': '不通过',
    '0': '待面试',
    '1': '已完成',
    '2': '已取消',
    '3': '通过',
    '4': '不通过'
  }
  return map[status] || '未知'
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  return dateStr.split('T')[0]
}

function formatTime(dateStr) {
  if (!dateStr) return '-'
  const timePart = dateStr.split('T')[1]
  return timePart ? timePart.substring(0, 5) : '-'
}

function formatDateTime(dateStr) {
  if (!dateStr) return '-'
  const parts = dateStr.split('T')
  return parts[0] + ' ' + (parts[1] ? parts[1].substring(0, 5) : '')
}

function getStatusType(status) {
  const map = {
    'scheduled': 'info',
    'completed': 'success',
    'cancelled': 'danger',
    'pending': 'info',
    'passed': 'success',
    'failed': 'danger',
    '0': 'info',
    '1': 'success',
    '2': 'danger',
    '3': 'success',
    '4': 'danger'
  }
  return map[status] || 'info'
}

async function loadData() {
  loading.value = true
  try {
    const response = await api.interview.list({
      page: pagination.pageNum,
      size: pagination.pageSize,
      keyword: searchKeyword.value,
      status: statusFilter.value || undefined
    })
    if (response.code === 200) {
      tableData.value = response.data.content || response.data.records || []
      pagination.total = response.data.totalElements || response.data.total || 0
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

async function loadOptions() {
  try {
    const [candidateRes] = await Promise.all([
      api.candidate.list({ page: 1, size: 100 })
    ])
    if (candidateRes.code === 200) {
      candidates.value = (candidateRes.data.content || candidateRes.data.records || []).filter(c => c.status !== 6)
    }
    
    interviewers.value = [
      { id: 3, name: '李面试官' },
      { id: 2, name: '张HR' }
    ]
  } catch (error) {
    ElMessage.error('加载选项失败')
  }
}

function handleSizeChange(val) {
  pagination.pageSize = val
  loadData()
}

function handleCurrentChange(val) {
  pagination.pageNum = val
  loadData()
}

function editInterview(row) {
  Object.assign(editForm, row)
  showAddModal.value = true
}

function viewInterview(row) {
  detailData.value = row
  if (row.status === 1) {
    loadEvaluation(row.id)
  } else {
    evaluationForm.score = 0
    evaluationForm.result = ''
    evaluationForm.comment = ''
  }
  showDetailModal.value = true
}

async function loadEvaluation(interviewId) {
  try {
    const response = await api.interview.getEvaluation(interviewId)
    if (response.code === 200) {
      const evalData = response.data
      evaluationForm.score = evalData.score || 0
      evaluationForm.result = evalData.result || ''
      evaluationForm.comment = evalData.comment || ''
    }
  } catch (error) {
    ElMessage.error('加载评价失败')
  }
}

async function submitEvaluation() {
  if (!evaluationForm.score || !evaluationForm.result) {
    ElMessage.warning('请填写完整评价信息')
    return
  }
  
  try {
    await api.interview.submitEvaluation(detailData.value.id, {
      score: evaluationForm.score,
      result: evaluationForm.result,
      comment: evaluationForm.comment
    })
    ElMessage.success('评价提交成功')
    detailData.value.status = 1
    loadData()
  } catch (error) {
    ElMessage.error('提交失败')
  }
}

async function startInterview() {
  try {
    await api.interview.updateStatus(detailData.value.id, 1)
    detailData.value.status = 1
    ElMessage.success('面试已开始')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function cancelInterview() {
  try {
    await ElMessageBox.confirm('确定要取消该面试吗？', '提示', {
      type: 'warning'
    })
    await api.interview.updateStatus(detailData.value.id, 2)
    detailData.value.status = 2
    ElMessage.success('面试已取消')
    loadData()
    showDetailModal.value = false
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

async function markPass() {
  try {
    await api.interview.updateStatus(detailData.value.id, 3)
    detailData.value.status = 3
    ElMessage.success('已标记为通过')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function markFail() {
  try {
    await api.interview.updateStatus(detailData.value.id, 4)
    detailData.value.status = 4
    ElMessage.success('已标记为不通过')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function sendNotification(row) {
  try {
    await api.interview.sendNotification(row.id)
    ElMessage.success('通知发送成功')
  } catch (error) {
    ElMessage.error('发送失败')
  }
}

async function deleteInterview(row) {
  try {
    await ElMessageBox.confirm('确定要删除该面试安排吗？', '提示', {
      type: 'warning'
    })
    await api.interview.delete(row.id)
    loadData()
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

function saveInterview() {
  if (!editForm.candidateId || !editForm.interviewerId || !editForm.interviewDate || !editForm.interviewTime) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  const candidate = candidates.value.find(c => c.id === editForm.candidateId)
  if (candidate) {
    editForm.candidateName = candidate.name
    editForm.positionId = candidate.positionId
    editForm.positionName = candidate.positionName
  }
  
  const interviewer = interviewers.value.find(u => u.id === editForm.interviewerId)
  if (interviewer) {
    editForm.interviewerName = interviewer.name
  }
  
  const dateStr = editForm.interviewDate instanceof Date ? editForm.interviewDate.toISOString().split('T')[0] : editForm.interviewDate
  const timeStr = editForm.interviewTime instanceof Date ? editForm.interviewTime.toTimeString().split(' ')[0].substring(0, 5) : editForm.interviewTime
  
  const data = { ...editForm }
  data.startTime = `${dateStr}T${timeStr}:00`
  delete data.id
  delete data.interviewDate
  delete data.interviewTime
  
  if (editForm.id) {
    api.interview.update(editForm.id, data).then(() => {
      ElMessage.success('更新成功')
      showAddModal.value = false
      loadData()
    }).catch(() => {
      ElMessage.error('更新失败')
    })
  } else {
    Promise.all([
      api.interview.create(data),
      api.candidate.updateStatus(editForm.candidateId, 3)
    ]).then(() => {
      ElMessage.success('创建成功')
      showAddModal.value = false
      loadData()
    }).catch(() => {
      ElMessage.error('创建失败')
    })
  }
}

onMounted(() => {
  loadData()
  loadOptions()
})

watch(showAddModal, (newVal) => {
  if (newVal) {
    editForm.candidateId = null
    editForm.candidateName = ''
    editForm.positionId = null
    editForm.positionName = ''
    loadOptions()
  }
})
</script>

<style scoped>
.interview-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.search-bar {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-input {
  width: 300px;
}

.status-filter {
  width: 150px;
}

.search-btn {
  background: none;
  border: none;
  color: var(--color-text-secondary);
  padding: 8px;
  cursor: pointer;
}

.data-table {
  flex: 1;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  padding-top: 10px;
}

.detail-content {
  padding: 10px 0;
}

.detail-row {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid var(--color-border);
}

.detail-label {
  width: 100px;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.detail-value {
  flex: 1;
  color: var(--color-text-primary);
}

.evaluation-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--color-border);
}

.evaluation-section h4 {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 16px;
}

.evaluation-content {
  padding: 16px;
  background-color: var(--color-bg-card);
  border-radius: 8px;
}

.evaluation-item {
  margin-bottom: 12px;
}

.evaluation-item:last-child {
  margin-bottom: 0;
}

.eval-label {
  display: block;
  font-size: 13px;
  color: var(--color-text-secondary);
  margin-bottom: 8px;
}

.evaluation-item p {
  margin: 0;
  color: var(--color-text-primary);
  line-height: 1.5;
}

.add-evaluation {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--color-border);
}

.add-evaluation h4 {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 16px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--color-border);
}
</style>