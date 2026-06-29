<template>
  <div class="offer-page">
    <div class="page-header">
      <h2>Offer管理</h2>
      <el-button type="primary" @click="showAddModal = true">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        发放Offer
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
        <el-option label="待确认" :value="0"/>
        <el-option label="已接受" :value="1"/>
        <el-option label="已拒绝" :value="2"/>
        <el-option label="已过期" :value="3"/>
      </el-select>
    </div>
    
    <el-table :data="tableData" border class="data-table" v-loading="loading">
      <el-table-column prop="candidateName" label="候选人" min-width="100"/>
      <el-table-column prop="positionName" label="应聘职位" min-width="120"/>
      <el-table-column prop="salary" label="薪资" min-width="120">
        <template #default="scope">
          {{ scope.row.salary ? scope.row.salary + 'K/月' : '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="salaryType" label="薪资类型" min-width="100"/>
      <el-table-column prop="sendTime" label="发放日期" min-width="150">
        <template #default="scope">
          {{ formatDate(scope.row.sendTime || scope.row.createdAt || scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="startDate" label="入职日期" min-width="150">
        <template #default="scope">
          {{ scope.row.startDate ? scope.row.startDate.split('T')[0] : '-' }}
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
      <el-table-column label="操作" min-width="200">
        <template #default="scope">
          <el-button size="small" @click="viewOffer(scope.row)">查看详情</el-button>
          <el-button size="small" v-if="scope.row.status === 0" type="warning" @click="sendNotification(scope.row)">发送通知</el-button>
          <el-button size="small" v-if="scope.row.status === 0" type="success" @click="confirmAccept(scope.row)">确认接受</el-button>
          <el-button size="small" type="danger" @click="deleteOffer(scope.row)">删除</el-button>
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
    
    <el-dialog :title="editForm.id ? '编辑Offer' : '发放Offer'" v-model="showAddModal" width="600px">
      <el-form :model="editForm" ref="formRef" label-width="100px">
        <el-form-item label="候选人" prop="candidateId" required>
          <el-select v-model="editForm.candidateId" placeholder="请选择候选人">
            <el-option 
              v-for="candidate in candidates" 
              :key="candidate.id" 
              :label="candidate.name + (candidate.positionName ? ' - ' + candidate.positionName : '')" 
              :value="candidate.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="薪资（K/月）" prop="salary" required>
          <el-input v-model.number="editForm.salary" placeholder="请输入薪资" type="number"/>
        </el-form-item>
        <el-form-item label="职级" prop="positionLevel">
          <el-input v-model="editForm.positionLevel" placeholder="请输入职级"/>
        </el-form-item>
        <el-form-item label="入职日期" prop="startDate">
          <el-date-picker v-model="editForm.startDate" type="date" placeholder="选择入职日期"/>
        </el-form-item>
        <el-form-item label="截止日期" prop="expireDate" required>
          <el-date-picker v-model="editForm.expireDate" type="date" placeholder="选择截止日期"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-textarea v-model="editForm.remark" placeholder="请输入备注信息" :rows="2"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveOffer">确定</el-button>
      </template>
    </el-dialog>
    
    <el-dialog title="Offer详情" v-model="showDetailModal" width="700px">
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
          <span class="detail-label">薪资</span>
          <span class="detail-value">{{ detailData.salary ? detailData.salary + 'K/月' : '-' }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">职级</span>
          <span class="detail-value">{{ detailData.positionLevel }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">发放日期</span>
          <span class="detail-value">{{ detailData.sendTime }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">截止日期</span>
          <span class="detail-value">{{ detailData.expireDate }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">入职日期</span>
          <span class="detail-value">{{ detailData.startDate }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">状态</span>
          <el-tag :type="getStatusType(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </div>
        
        <div v-if="['pending', 'sent', '0', 0].includes(detailData.status)" class="action-buttons">
          <el-button type="primary" @click="confirmOfferAccept">确认接受</el-button>
          <el-button type="danger" @click="rejectOffer">拒绝</el-button>
        </div>
        <div class="detail-row">
          <span class="detail-label">备注</span>
          <span class="detail-value">{{ detailData.remark }}</span>
        </div>
        
        <div v-if="detailData.status === 1" class="onboarding-section">
          <h4>入职资料审核</h4>
          <div class="document-list">
            <div class="doc-item">
              <span class="doc-name">身份证复印件</span>
              <el-switch :checked="detailData.idCardUploaded" @change="updateDocument('idCard')"/>
            </div>
            <div class="doc-item">
              <span class="doc-name">学历证书</span>
              <el-switch :checked="detailData.eduCertificateUploaded" @change="updateDocument('eduCertificate')"/>
            </div>
            <div class="doc-item">
              <span class="doc-name">离职证明</span>
              <el-switch :checked="detailData.resignationUploaded" @change="updateDocument('resignation')"/>
            </div>
            <div class="doc-item">
              <span class="doc-name">体检报告</span>
              <el-switch :checked="detailData.medicalReportUploaded" @change="updateDocument('medicalReport')"/>
            </div>
          </div>
          <el-button v-if="allDocumentsUploaded" type="primary" @click="completeOnboarding">完成入职</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
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
  salary: null,
  positionLevel: '',
  startDate: '',
  expireDate: '',
  remark: '',
  status: 'pending'
})

function getStatusText(status) {
  const map = {
    'draft': '草稿',
    'pending': '待确认',
    'sent': '待确认',
    'accepted': '已接受',
    'rejected': '已拒绝',
    'cancelled': '已取消',
    '0': '待确认',
    '1': '已接受',
    '2': '已拒绝',
    '3': '已取消'
  }
  return map[status] || '未知'
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  return dateStr.split('T')[0]
}

function formatDateTime(dateStr) {
  if (!dateStr) return '-'
  const parts = dateStr.split('T')
  return parts[0] + ' ' + (parts[1] ? parts[1].substring(0, 5) : '')
}

function getStatusType(status) {
  const map = {
    'draft': 'info',
    'pending': 'info',
    'sent': 'info',
    'accepted': 'success',
    'rejected': 'danger',
    'cancelled': 'warning',
    '0': 'info',
    '1': 'success',
    '2': 'danger',
    '3': 'warning'
  }
  return map[status] || 'info'
}

const allDocumentsUploaded = computed(() => {
  if (!detailData.value) return false
  return detailData.value.idCardUploaded && 
         detailData.value.eduCertificateUploaded && 
         detailData.value.resignationUploaded && 
         detailData.value.medicalReportUploaded
})

async function loadData() {
  loading.value = true
  try {
    const response = await api.offer.list({
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
    const response = await api.candidate.list({ page: 1, size: 100 })
    if (response.code === 200) {
      candidates.value = (response.data.content || response.data.records || []).filter(c => c.status !== 6)
    }
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

function viewOffer(row) {
  detailData.value = row
  showDetailModal.value = true
}

async function sendNotification(row) {
  try {
    await api.offer.sendNotification(row.id)
    ElMessage.success('通知发送成功')
  } catch (error) {
    ElMessage.error('发送失败')
  }
}

async function confirmAccept(row) {
  try {
    await api.offer.confirmAccept(row.id)
    row.status = 1
    ElMessage.success('已确认接受')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function confirmOfferAccept() {
  try {
    await api.offer.updateStatus(detailData.value.id, 1)
    detailData.value.status = 1
    ElMessage.success('已确认接受')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function rejectOffer() {
  try {
    await api.offer.updateStatus(detailData.value.id, 2)
    detailData.value.status = 2
    ElMessage.success('已拒绝')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function updateDocument(docType) {
  try {
    await api.offer.updateDocument(detailData.value.id, docType)
    ElMessage.success('更新成功')
    loadData()
    viewOffer(tableData.value.find(o => o.id === detailData.value.id))
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

async function completeOnboarding() {
  try {
    await api.offer.completeOnboarding(detailData.value.id)
    ElMessage.success('入职完成')
    showDetailModal.value = false
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function deleteOffer(row) {
  try {
    await ElMessageBox.confirm('确定要删除该Offer吗？', '提示', {
      type: 'warning'
    })
    await api.offer.delete(row.id)
    loadData()
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

function saveOffer() {
  if (!editForm.candidateId || !editForm.salary || !editForm.expireDate) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  const candidate = candidates.value.find(c => c.id === editForm.candidateId)
  if (candidate) {
    editForm.candidateName = candidate.name
    editForm.positionId = candidate.positionId
    editForm.positionName = candidate.positionName
  }
  
  const data = { ...editForm }
  delete data.id
  
  if (editForm.id) {
    api.offer.update(editForm.id, data).then(() => {
      ElMessage.success('更新成功')
      showAddModal.value = false
      loadData()
    }).catch(() => {
      ElMessage.error('更新失败')
    })
  } else {
    api.offer.create(data).then(() => {
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
</script>

<style scoped>
.offer-page {
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

.onboarding-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--color-border);
}

.onboarding-section h4 {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 16px;
}

.document-list {
  background-color: var(--color-bg-card);
  border-radius: 8px;
  padding: 16px;
}

.doc-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--color-border);
}

.doc-item:last-child {
  border-bottom: none;
}

.doc-name {
  color: var(--color-text-primary);
}

.action-buttons {
  display: flex;
  gap: 12px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--color-border);
}
</style>