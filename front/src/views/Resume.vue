
<template>
  <div class="resume-page">
    <div class="page-header">
      <h2>简历管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="showAddModal = true">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
          录入简历
        </el-button>
        <el-button @click="showUploadModal = true">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
            <polyline points="17 8 12 3 7 8"/>
            <line x1="12" y1="3" x2="12" y2="15"/>
          </svg>
          上传简历
        </el-button>
      </div>
    </div>
    
    <div class="search-bar">
      <el-input 
        v-model="searchKeyword" 
        placeholder="搜索姓名或手机号"
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
    </div>
    
    <el-table :data="tableData" border class="data-table" v-loading="loading">
      <el-table-column prop="name" label="姓名" min-width="100"/>
      <el-table-column prop="phone" label="手机号" min-width="120"/>
      <el-table-column prop="email" label="邮箱" min-width="150"/>
      <el-table-column prop="education" label="学历" min-width="100"/>
      <el-table-column prop="experience" label="工作经验" min-width="100">
        <template #default="scope">
          {{ scope.row.experience ? scope.row.experience + '年' : '不限' }}
        </template>
      </el-table-column>
      <el-table-column prop="skills" label="技能" min-width="200">
        <template #default="scope">
          <el-tag v-for="skill in (scope.row.skills?.split(',') || [])" :key="skill" size="small">
            {{ skill }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="source" label="来源渠道" min-width="120"/>
      <el-table-column prop="status" label="状态" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
            {{ scope.row.status === 1 ? '有效' : '无效' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="150"/>
      <el-table-column label="操作" min-width="150">
        <template #default="scope">
          <el-button size="small" @click="editResume(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteResume(scope.row)">删除</el-button>
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
    
    <el-dialog :title="editForm.id ? '编辑简历' : '录入简历'" v-model="showAddModal" width="500px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="姓名" prop="name" required>
          <el-input v-model="editForm.name" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入手机号"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" placeholder="请输入邮箱"/>
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
        <el-form-item label="技能" prop="skills">
          <el-input v-model="editForm.skills" placeholder="多个技能用逗号分隔"/>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="editForm.tags" placeholder="多个标签用逗号分隔"/>
        </el-form-item>
        <el-form-item label="来源渠道" prop="source">
          <el-input v-model="editForm.source" placeholder="请输入来源渠道"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveResume">确定</el-button>
      </template>
    </el-dialog>
    
    <el-dialog title="上传简历" v-model="showUploadModal" width="400px">
      <div class="upload-area">
        <div 
          class="upload-drop" 
          :class="{ dragging: isDragging }"
          @dragover.prevent="isDragging = true"
          @dragleave="isDragging = false"
          @drop.prevent="handleDrop"
          @click="triggerFileInput"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
            <polyline points="17 8 12 3 7 8"/>
            <line x1="12" y1="3" x2="12" y2="15"/>
          </svg>
          <p>点击或拖拽文件到此处上传</p>
          <p class="hint">支持 PDF、DOC、DOCX 格式</p>
        </div>
        <input 
          ref="fileInput" 
          type="file" 
          accept=".pdf,.doc,.docx" 
          class="file-input"
          @change="handleFileSelect"
        />
      </div>
      <template #footer>
        <el-button @click="showUploadModal = false">取消</el-button>
        <el-button type="primary" :loading="uploading" @click="handleUpload">
          上传
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const tableData = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const showAddModal = ref(false)
const showUploadModal = ref(false)
const isDragging = ref(false)
const uploading = ref(false)
const fileInput = ref(null)

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
  education: '',
  experience: null,
  skills: '',
  tags: '',
  source: '',
  status: 1
})

const selectedFile = ref(null)

async function loadData() {
  loading.value = true
  try {
    const response = await api.resume.list({
      page: pagination.pageNum,
      size: pagination.pageSize,
      keyword: searchKeyword.value
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

function editResume(row) {
  Object.assign(editForm, row)
  showAddModal.value = true
}

async function deleteResume(row) {
  try {
    await ElMessageBox.confirm('确定要删除该简历吗？', '提示', {
      type: 'warning'
    })
    await api.resume.delete(row.id)
    loadData()
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

function saveResume() {
  if (!editForm.name) {
    ElMessage.warning('请输入姓名')
    return
  }
  
  const data = { ...editForm }
  delete data.id
  
  if (editForm.id) {
    api.resume.update(editForm.id, data).then(() => {
      ElMessage.success('更新成功')
      showAddModal.value = false
      loadData()
    }).catch(() => {
      ElMessage.error('更新失败')
    })
  } else {
    api.resume.create(data).then(() => {
      ElMessage.success('创建成功')
      showAddModal.value = false
      loadData()
    }).catch(() => {
      ElMessage.error('创建失败')
    })
  }
}

function triggerFileInput() {
  fileInput.value?.click()
}

function handleDrop(e) {
  isDragging.value = false
  const files = e.dataTransfer?.files
  if (files && files.length > 0) {
    selectedFile.value = files[0]
  }
}

function handleFileSelect(e) {
  const files = e.target?.files
  if (files && files.length > 0) {
    selectedFile.value = files[0]
  }
}

async function handleUpload() {
  if (!selectedFile.value) {
    ElMessage.warning('请选择文件')
    return
  }
  
  uploading.value = true
  
  const formData = new FormData()
  formData.append('file', selectedFile.value)
  
  try {
    const response = await api.resume.upload(formData)
    if (response.code === 200) {
      ElMessage.success('上传成功')
      showUploadModal.value = false
      selectedFile.value = null
      loadData()
    }
  } catch (error) {
    ElMessage.error('上传失败')
  } finally {
    uploading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.resume-page {
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

.header-actions {
  display: flex;
  gap: 12px;
}

.search-bar {
  display: flex;
  gap: 12px;
}

.search-input {
  width: 300px;
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

.upload-area {
  padding: 20px;
}

.upload-drop {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  border: 2px dashed var(--color-border);
  border-radius: 12px;
  cursor: pointer;
  color: var(--color-text-secondary);
  transition: all 0.2s;
}

.upload-drop:hover,
.upload-drop.dragging {
  border-color: var(--color-accent);
  background-color: var(--color-accent-light);
}

.upload-drop svg {
  margin-bottom: 12px;
}

.upload-drop p {
  margin: 4px 0;
  font-size: 14px;
}

.upload-drop .hint {
  font-size: 12px;
  color: var(--color-text-muted);
}

.file-input {
  display: none;
}
</style>
