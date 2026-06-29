
<template>
  <div class="position-page">
    <div class="page-header">
      <h2>职位管理</h2>
      <el-button type="primary" @click="showAddModal = true">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        新增职位
      </el-button>
    </div>
    
    <div class="search-bar">
      <el-input 
        v-model="searchKeyword" 
        placeholder="搜索职位名称"
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
      <el-table-column prop="name" label="职位名称" min-width="150"/>
      <el-table-column prop="departmentName" label="所属部门" min-width="120"/>
      <el-table-column prop="location" label="工作地点" min-width="100"/>
      <el-table-column prop="education" label="学历要求" min-width="100"/>
      <el-table-column prop="experience" label="经验要求" min-width="100">
        <template #default="scope">
          {{ scope.row.experience ? scope.row.experience + '年' : '不限' }}
        </template>
      </el-table-column>
      <el-table-column prop="recruitmentCount" label="招聘人数" min-width="100"/>
      <el-table-column prop="status" label="状态" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
            {{ scope.row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="150"/>
      <el-table-column label="操作" min-width="180">
        <template #default="scope">
          <el-button size="small" @click="editPosition(scope.row)">编辑</el-button>
          <el-button 
            size="small" 
            :type="scope.row.status === 1 ? 'warning' : 'success'" 
            @click="toggleStatus(scope.row)"
          >
            {{ scope.row.status === 1 ? '下架' : '上架' }}
          </el-button>
          <el-button size="small" type="danger" @click="deletePosition(scope.row)">删除</el-button>
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
    
    <el-dialog :title="editForm.id ? '编辑职位' : '新增职位'" v-model="showAddModal" width="600px">
      <el-form :model="editForm" ref="formRef" label-width="100px">
        <el-form-item label="职位名称" prop="name" required>
          <el-input v-model="editForm.name" placeholder="请输入职位名称"/>
        </el-form-item>
        <el-form-item label="所属部门" prop="departmentName" required>
          <el-input v-model="editForm.departmentName" placeholder="请输入部门名称"/>
        </el-form-item>
        <el-form-item label="工作地点" prop="location">
          <el-input v-model="editForm.location" placeholder="请输入工作地点"/>
        </el-form-item>
        <el-form-item label="学历要求" prop="education">
          <el-select v-model="editForm.education" placeholder="请选择学历要求">
            <el-option label="高中" value="高中"/>
            <el-option label="大专" value="大专"/>
            <el-option label="本科" value="本科"/>
            <el-option label="硕士" value="硕士"/>
            <el-option label="博士" value="博士"/>
          </el-select>
        </el-form-item>
        <el-form-item label="经验要求" prop="experience">
          <el-input v-model.number="editForm.experience" placeholder="请输入经验要求（年）" type="number"/>
        </el-form-item>
        <el-form-item label="招聘人数" prop="recruitmentCount">
          <el-input v-model.number="editForm.recruitmentCount" placeholder="请输入招聘人数" type="number"/>
        </el-form-item>
        <el-form-item label="岗位职责" prop="jobDescription">
          <el-textarea v-model="editForm.jobDescription" placeholder="请输入岗位职责" :rows="3"/>
        </el-form-item>
        <el-form-item label="任职要求" prop="requirements">
          <el-textarea v-model="editForm.requirements" placeholder="请输入任职要求" :rows="3"/>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="editForm.tags" placeholder="多个标签用逗号分隔"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="savePosition">确定</el-button>
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
const formRef = ref(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const editForm = reactive({
  id: null,
  name: '',
  departmentId: null,
  departmentName: '',
  location: '',
  education: '',
  experience: null,
  recruitmentCount: 1,
  jobDescription: '',
  requirements: '',
  tags: '',
  status: 1
})

async function loadData() {
  loading.value = true
  try {
    const response = await api.position.list({
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

function editPosition(row) {
  Object.assign(editForm, row)
  showAddModal.value = true
}

async function toggleStatus(row) {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    await api.position.updateStatus(row.id, newStatus)
    row.status = newStatus
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function deletePosition(row) {
  try {
    await ElMessageBox.confirm('确定要删除该职位吗？', '提示', {
      type: 'warning'
    })
    await api.position.delete(row.id)
    loadData()
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

function savePosition() {
  if (!editForm.name) {
    ElMessage.warning('请输入职位名称')
    return
  }
  
  const data = { ...editForm }
  delete data.id
  
  if (editForm.id) {
    api.position.update(editForm.id, data).then(() => {
      ElMessage.success('更新成功')
      showAddModal.value = false
      loadData()
    }).catch(() => {
      ElMessage.error('更新失败')
    })
  } else {
    api.position.create(data).then(() => {
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
})
</script>

<style scoped>
.position-page {
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
</style>
