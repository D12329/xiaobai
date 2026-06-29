<template>
  <div class="department-management">
    <div class="page-header">
      <h2>部门管理</h2>
      <el-button type="primary" @click="showAddModal = true">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        新增部门
      </el-button>
    </div>
    
    <div class="search-bar">
      <el-input 
        v-model="searchKeyword" 
        placeholder="搜索部门名称"
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
      <el-table-column prop="name" label="部门名称" min-width="150"/>
      <el-table-column prop="parentName" label="上级部门" min-width="150">
        <template #default="scope">
          {{ scope.row.parentName || '无' }}
        </template>
      </el-table-column>
      <el-table-column prop="code" label="部门编码" min-width="120"/>
      <el-table-column prop="description" label="部门描述" min-width="200"/>
      <el-table-column prop="sortOrder" label="排序" min-width="80"/>
      <el-table-column prop="status" label="状态" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="150"/>
      <el-table-column label="操作" min-width="180">
        <template #default="scope">
          <el-button size="small" @click="editDepartment(scope.row)">编辑</el-button>
          <el-button 
            size="small" 
            :type="scope.row.status === 1 ? 'warning' : 'success'" 
            @click="toggleStatus(scope.row)"
          >
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button size="small" type="danger" @click="deleteDepartment(scope.row)">删除</el-button>
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
    
    <el-dialog :title="editForm.id ? '编辑部门' : '新增部门'" :visible.sync="showAddModal" width="500px">
      <el-form :model="editForm" ref="formRef" label-width="100px">
        <el-form-item label="部门名称" prop="name" required>
          <el-input v-model="editForm.name" placeholder="请输入部门名称"/>
        </el-form-item>
        <el-form-item label="上级部门" prop="parentId">
          <el-select v-model="editForm.parentId" placeholder="请选择上级部门">
            <el-option label="无" :value="null"/>
            <el-option 
              v-for="dept in parentDepartments" 
              :key="dept.id" 
              :label="dept.name" 
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门编码" prop="code">
          <el-input v-model="editForm.code" placeholder="请输入部门编码"/>
        </el-form-item>
        <el-form-item label="部门描述" prop="description">
          <el-textarea v-model="editForm.description" placeholder="请输入部门描述" :rows="2"/>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input v-model.number="editForm.sortOrder" placeholder="请输入排序号" type="number"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveDepartment">确定</el-button>
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

const parentDepartments = ref([])

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const editForm = reactive({
  id: null,
  name: '',
  parentId: null,
  parentName: '',
  code: '',
  description: '',
  sortOrder: 0,
  status: 1
})

async function loadData() {
  loading.value = true
  try {
    const response = await api.department.list({
      page: pagination.pageNum,
      size: pagination.pageSize,
      keyword: searchKeyword.value
    })
    if (response.code === 200) {
      tableData.value = response.data.content || response.data
      pagination.total = response.data.totalElements || tableData.value.length
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

async function loadParentDepartments() {
  try {
    const response = await api.department.list()
    if (response.code === 200) {
      parentDepartments.value = response.data.content || response.data
    }
  } catch (error) {
    ElMessage.error('加载部门列表失败')
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

function editDepartment(row) {
  Object.assign(editForm, row)
  showAddModal.value = true
}

async function toggleStatus(row) {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    const data = { ...row, status: newStatus }
    await api.department.update(row.id, data)
    row.status = newStatus
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function deleteDepartment(row) {
  try {
    await ElMessageBox.confirm('确定要删除该部门吗？', '提示', {
      type: 'warning'
    })
    await api.department.delete(row.id)
    loadData()
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

function saveDepartment() {
  if (!editForm.name) {
    ElMessage.warning('请输入部门名称')
    return
  }
  
  const parent = parentDepartments.value.find(p => p.id === editForm.parentId)
  if (parent) {
    editForm.parentName = parent.name
  } else {
    editForm.parentName = ''
  }
  
  const data = { ...editForm }
  delete data.id
  
  if (editForm.id) {
    api.department.update(editForm.id, data).then(() => {
      ElMessage.success('更新成功')
      showAddModal.value = false
      loadData()
    }).catch(() => {
      ElMessage.error('更新失败')
    })
  } else {
    api.department.create(data).then(() => {
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
  loadParentDepartments()
})
</script>

<style scoped>
.department-management {
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