<template>
  <div class="candidate-page">
    <div class="page-header">
      <h2>候选人管理</h2>
      <el-button type="primary" @click="showAddModal = true">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        新增候选人
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
        <el-option label="待筛选" :value="1"/>
        <el-option label="筛选中" :value="2"/>
        <el-option label="面试中" :value="3"/>
        <el-option label="已录用" :value="4"/>
        <el-option label="已入职" :value="5"/>
        <el-option label="已拒绝" :value="6"/>
      </el-select>
    </div>
    
    <el-table :data="tableData" border class="data-table" v-loading="loading">
      <el-table-column prop="name" label="姓名" min-width="100"/>
      <el-table-column prop="phone" label="手机号" min-width="120"/>
      <el-table-column prop="email" label="邮箱" min-width="150"/>
      <el-table-column prop="positionName" label="应聘职位" min-width="120"/>
      <el-table-column prop="education" label="学历" min-width="100"/>
      <el-table-column prop="experience" label="工作经验" min-width="100">
        <template #default="scope">
          {{ scope.row.experience ? scope.row.experience + '年' : '不限' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" min-width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="150">
        <template #default="scope">
          {{ formatDateTime(scope.row.createdAt || scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="followRecords" label="最新跟进" min-width="150">
        <template #default="scope">
          {{ scope.row.followRecords ? scope.row.followRecords.split('\n').slice(-1)[0] : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="200">
        <template #default="scope">
          <el-button size="small" @click="viewCandidate(scope.row)">查看详情</el-button>
          <el-button size="small" @click="editCandidate(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteCandidate(scope.row)">删除</el-button>
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
    
    <el-dialog :title="editForm.id ? '编辑候选人' : '新增候选人'" v-model="showAddModal" width="600px">
      <el-form :model="editForm" ref="formRef" label-width="100px">
        <el-form-item label="选择简历" v-if="!editForm.id">
          <el-select 
            v-model="selectedResume" 
            placeholder="选择简历自动回填（可选）"
            filterable
            allow-clear
            :filter-method="filterResumes"
            @change="handleResumeChange"
          >
            <el-option 
              v-for="resume in unconvertedResumes" 
              :key="resume.id" 
              :label="resume.name + ' - ' + (resume.phone || '无手机号')" 
              :value="resume"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="姓名" prop="name" required>
          <el-input v-model="editForm.name" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入手机号"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="应聘职位" prop="positionId" required>
          <el-select 
            v-model="editForm.positionId" 
            placeholder="请选择应聘职位"
            filterable
            @change="handlePositionChange"
          >
            <el-option 
              v-for="position in positions" 
              :key="position.id" 
              :label="position.name" 
              :value="position.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学历" prop="education">
          <el-select v-model="editForm.education" placeholder="请选择学历">
            <el-option label="高中" value="高中"/>
            <el-option label="大专" value="大专"/>
            <el-option label="本科" value="本科"/>
            <el-option label="硕士" value="硕士"/>
            <el-option label="博士" value="博士"/>
          </el-select>
        </el-form-item>
        <el-form-item label="工作经验" prop="experience">
          <el-input v-model.number="editForm.experience" placeholder="请输入工作经验（年）" type="number"/>
        </el-form-item>
        <el-form-item label="技能标签" prop="skills">
          <el-input v-model="editForm.skills" placeholder="多个技能用逗号分隔"/>
        </el-form-item>
        <el-form-item label="来源渠道" prop="source">
          <el-input v-model="editForm.source" placeholder="请输入来源渠道"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveCandidate">确定</el-button>
      </template>
    </el-dialog>
    
    <el-dialog title="候选人详情" v-model="showDetailModal" width="700px">
      <div v-if="detailData" class="detail-content">
        <div class="detail-row">
          <span class="detail-label">姓名</span>
          <span class="detail-value">{{ detailData.name }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">手机号</span>
          <span class="detail-value">{{ detailData.phone }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">邮箱</span>
          <span class="detail-value">{{ detailData.email }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">应聘职位</span>
          <span class="detail-value">{{ detailData.positionName }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">学历</span>
          <span class="detail-value">{{ detailData.education }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">工作经验</span>
          <span class="detail-value">{{ detailData.experience ? detailData.experience + '年' : '不限' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">技能标签</span>
          <span class="detail-value">{{ detailData.skills }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">来源渠道</span>
          <span class="detail-value">{{ detailData.source }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">状态</span>
          <el-tag :type="getStatusType(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </div>
        
        <div class="follow-section">
          <h4>跟进记录</h4>
          <div v-if="followRecords.length > 0" class="follow-list">
            <div v-for="record in followRecords" :key="record.id" class="follow-item">
              <div class="follow-header">
                <span class="follow-time">{{ record.createdAt }}</span>
                <span class="follow-operator">{{ record.operatorName }}</span>
              </div>
              <div class="follow-content">{{ record.content }}</div>
            </div>
          </div>
          <div v-else class="empty-tip">暂无跟进记录</div>
        </div>
        
        <div class="add-follow">
          <el-textarea v-model="followContent" placeholder="添加跟进记录" :rows="3" class="follow-input"></el-textarea>
          <el-button type="primary" @click="addFollowRecord" class="follow-btn">添加记录</el-button>
        </div>
        
        <div class="action-buttons">
          <el-button v-if="detailData.status === 0 || detailData.status === 1" @click="updateStatus(2)">进入筛选</el-button>
          <el-button v-if="detailData.status === 2" @click="updateStatus(3)">进入面试</el-button>
          <el-button v-if="detailData.status === 3" @click="updateStatus(4)">录用</el-button>
          <el-button v-if="detailData.status === 3" type="danger" @click="updateStatus(6)">拒绝</el-button>
          <el-button v-if="detailData.status === 4" @click="updateStatus(5)">确认入职</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
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
const unconvertedResumes = ref([])
const selectedResume = ref(null)
const resumeSearchKeyword = ref('')
const followRecords = ref([])
const followContent = ref('')
const positions = ref([])

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const editForm = reactive({
  id: null,
  name: '',
  phone: '',
  email: '',
  positionId: null,
  positionName: '',
  education: '',
  experience: null,
  skills: '',
  source: '',
  status: 1
})

function getStatusText(status) {
  const map = {
    0: '待筛选',
    1: '待筛选',
    2: '筛选中',
    3: '面试中',
    4: '已录用',
    5: '已入职',
    6: '已拒绝'
  }
  return map[status] || '未知'
}

function formatDateTime(dateStr) {
  if (!dateStr) return '-'
  const parts = dateStr.split('T')
  return parts[0] + ' ' + (parts[1] ? parts[1].substring(0, 5) : '')
}

function getStatusType(status) {
  const map = {
    0: 'info',
    1: 'info',
    2: 'primary',
    3: 'warning',
    4: 'success',
    5: 'success',
    6: 'danger'
  }
  return map[status] || 'info'
}

async function loadUnconvertedResumes() {
  try {
    const response = await api.resume.getUnconverted(resumeSearchKeyword.value)
    if (response.code === 200) {
      unconvertedResumes.value = response.data || []
    }
  } catch (error) {
    console.error('加载未转候选人简历失败', error)
  }
}

async function loadPositions() {
  try {
    const response = await api.position.list({ page: 1, size: 100 })
    if (response.code === 200) {
      positions.value = response.data.content || response.data.records || []
    }
  } catch (error) {
    console.error('加载职位列表失败', error)
  }
}

function handlePositionChange(positionId) {
  const position = positions.value.find(p => p.id === positionId)
  if (position) {
    editForm.positionName = position.name
  }
}

function filterResumes(query) {
  resumeSearchKeyword.value = query || ''
  loadUnconvertedResumes()
}

function handleResumeChange(resume) {
  if (resume) {
    editForm.resumeId = resume.id
    editForm.name = resume.name || ''
    editForm.phone = resume.phone || ''
    editForm.email = resume.email || ''
    editForm.education = resume.education || ''
    editForm.experience = resume.experience || null
    editForm.skills = resume.skills || ''
    editForm.source = resume.source || ''
  } else {
    editForm.resumeId = null
  }
}

function resetForm() {
  selectedResume.value = null
  resumeSearchKeyword.value = ''
  Object.assign(editForm, {
    id: null,
    name: '',
    phone: '',
    email: '',
    positionId: null,
    positionName: '',
    education: '',
    experience: null,
    skills: '',
    source: '',
    status: 1,
    resumeId: null
  })
}

async function loadData() {
  loading.value = true
  try {
    const response = await api.candidate.list({
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

function handleSizeChange(val) {
  pagination.pageSize = val
  loadData()
}

function handleCurrentChange(val) {
  pagination.pageNum = val
  loadData()
}

function editCandidate(row) {
  selectedResume.value = null
  Object.assign(editForm, row)
  showAddModal.value = true
}

function viewCandidate(row) {
  detailData.value = row
  loadFollowRecords(row.id)
  showDetailModal.value = true
}

async function loadFollowRecords(candidateId) {
  try {
    const response = await api.candidate.getFollowRecords(candidateId)
    if (response.code === 200) {
      followRecords.value = response.data
    }
  } catch (error) {
    ElMessage.error('加载跟进记录失败')
  }
}

async function addFollowRecord() {
  if (!followContent.value.trim()) {
    ElMessage.warning('请输入跟进内容')
    return
  }
  
  try {
    await api.candidate.addFollowRecord(detailData.value.id, followContent.value)
    ElMessage.success('添加成功')
    followContent.value = ''
    loadFollowRecords(detailData.value.id)
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

async function updateStatus(status) {
  try {
    await api.candidate.updateStatus(detailData.value.id, status)
    detailData.value.status = status
    ElMessage.success('状态更新成功')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function deleteCandidate(row) {
  try {
    await ElMessageBox.confirm('确定要删除该候选人吗？', '提示', {
      type: 'warning'
    })
    await api.candidate.delete(row.id)
    loadData()
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

function saveCandidate() {
  if (!editForm.name) {
    ElMessage.warning('请输入姓名')
    return
  }
  
  const data = { ...editForm }
  delete data.id
  
  if (editForm.id) {
    api.candidate.update(editForm.id, data).then(() => {
      ElMessage.success('更新成功')
      showAddModal.value = false
      loadData()
    }).catch(() => {
      ElMessage.error('更新失败')
    })
  } else {
    api.candidate.create(data).then(() => {
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
  loadPositions()
})

watch(showAddModal, (newVal) => {
  if (newVal && !editForm.id) {
    resetForm()
    loadUnconvertedResumes()
    loadPositions()
  }
})
</script>

<style scoped>
.candidate-page {
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

.follow-section {
  margin-top: 20px;
}

.follow-section h4 {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 12px;
}

.follow-list {
  max-height: 200px;
  overflow-y: auto;
}

.follow-item {
  padding: 12px;
  background-color: var(--color-bg-card);
  border-radius: 8px;
  margin-bottom: 10px;
}

.follow-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.follow-time {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.follow-operator {
  font-size: 12px;
  color: var(--color-accent);
}

.follow-content {
  font-size: 13px;
  color: var(--color-text-primary);
  line-height: 1.5;
}

.empty-tip {
  text-align: center;
  padding: 20px;
  color: var(--color-text-secondary);
}

.add-follow {
  margin-top: 20px;
}

.follow-input {
  margin-bottom: 12px;
}

.follow-btn {
  float: right;
}

.action-buttons {
  margin-top: 20px;
  display: flex;
  gap: 12px;
}
</style>