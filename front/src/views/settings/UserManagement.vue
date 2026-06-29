<template>
  <div class="user-management">
    <div class="page-header">
      <h2>用户管理</h2>
      <el-button type="primary" @click="showAddModal = true">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        新增用户
      </el-button>
    </div>
    
    <div class="search-bar">
      <el-input 
        v-model="searchKeyword" 
        placeholder="搜索用户名"
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
      
      <el-select v-model="roleFilter" placeholder="筛选角色" class="role-filter" @change="loadData">
        <el-option label="全部" value=""/>
        <el-option label="管理员" value="admin"/>
        <el-option label="HR" value="hr"/>
        <el-option label="面试官" value="interviewer"/>
      </el-select>
    </div>
    
    <el-table :data="tableData" border class="data-table" v-loading="loading">
      <el-table-column prop="username" label="用户名" min-width="120"/>
      <el-table-column prop="name" label="姓名" min-width="100"/>
      <el-table-column prop="email" label="邮箱" min-width="150"/>
      <el-table-column prop="phone" label="手机号" min-width="120"/>
      <el-table-column prop="roleName" label="角色" min-width="100">
        <template #default="scope">
          <el-tag :type="getRoleType(scope.row.roleName)">
            {{ scope.row.roleName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="departmentName" label="所属部门" min-width="120"/>
      <el-table-column prop="status" label="状态" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="150"/>
      <el-table-column label="操作" min-width="200">
        <template #default="scope">
          <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
          <el-button 
            size="small" 
            :type="scope.row.status === 1 ? 'warning' : 'success'" 
            @click="toggleStatus(scope.row)"
          >
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button size="small" type="danger" @click="deleteUser(scope.row)">删除</el-button>
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
    
    <el-dialog :title="editForm.id ? '编辑用户' : '新增用户'" v-model="showAddModal" width="600px">
      <el-form :model="editForm" ref="formRef" label-width="100px">
        <el-form-item label="用户名" prop="username" required>
          <el-input v-model="editForm.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="姓名" prop="name" required>
          <el-input v-model="editForm.name" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="密码" prop="password" :required="!editForm.id">
          <el-input v-model="editForm.password" type="password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入手机号"/>
        </el-form-item>
        <el-form-item label="角色" prop="roleId" required>
          <el-select v-model="editForm.roleId" placeholder="请选择角色">
            <el-option 
              v-for="role in roles" 
              :key="role.id" 
              :label="role.name" 
              :value="role.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="departmentId">
          <el-select v-model="editForm.departmentId" placeholder="请选择部门">
            <el-option 
              v-for="dept in departments" 
              :key="dept.id" 
              :label="dept.name" 
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveUser">确定</el-button>
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
const roleFilter = ref('')
const showAddModal = ref(false)
const formRef = ref(null)

const roles = ref([])
const departments = ref([])

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const editForm = reactive({
  id: null,
  username: '',
  name: '',
  password: '',
  email: '',
  phone: '',
  roleId: null,
  roleName: '',
  departmentId: null,
  departmentName: '',
  status: 1
})

function getRoleType(roleName) {
  const map = {
    '管理员': 'danger',
    'HR': 'primary',
    '面试官': 'warning'
  }
  return map[roleName] || 'info'
}

async function loadData() {
  loading.value = true
  try {
    const response = await api.user.list({
      page: pagination.pageNum,
      size: pagination.pageSize,
      keyword: searchKeyword.value,
      role: roleFilter.value || undefined
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
    const [roleRes, deptRes] = await Promise.all([
      api.role.getAll(),
      api.department.list()
    ])
    if (roleRes.code === 200) {
      roles.value = roleRes.data
    }
    if (deptRes.code === 200) {
      departments.value = deptRes.data.content || deptRes.data
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

function editUser(row) {
  Object.assign(editForm, row)
  showAddModal.value = true
}

async function toggleStatus(row) {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    await api.user.updateStatus(row.id, newStatus)
    row.status = newStatus
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function deleteUser(row) {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      type: 'warning'
    })
    await api.user.delete(row.id)
    loadData()
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

function saveUser() {
  if (!editForm.username || !editForm.name || !editForm.roleId) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  if (!editForm.id && !editForm.password) {
    ElMessage.warning('请输入密码')
    return
  }
  
  const role = roles.value.find(r => r.id === editForm.roleId)
  if (role) {
    editForm.roleName = role.name
  }
  
  const dept = departments.value.find(d => d.id === editForm.departmentId)
  if (dept) {
    editForm.departmentName = dept.name
  }
  
  const data = { ...editForm }
  delete data.id
  
  if (editForm.id) {
    api.user.update(editForm.id, data).then(() => {
      ElMessage.success('更新成功')
      showAddModal.value = false
      loadData()
    }).catch(() => {
      ElMessage.error('更新失败')
    })
  } else {
    api.user.create(data).then(() => {
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
.user-management {
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

.role-filter {
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
</style>