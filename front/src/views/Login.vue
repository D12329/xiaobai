
<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
          </div>
          <h1>企业招聘后台管理系统</h1>
          <p>Enterprise Recruitment Management System</p>
        </div>
        
        <el-form :model="form" ref="formRef" class="login-form">
          <el-form-item prop="username" label="用户名">
            <el-input 
              v-model="form.username" 
              placeholder="请输入用户名"
              prefix-icon="User"
            />
          </el-form-item>
          
          <el-form-item prop="password" label="密码">
            <el-input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码"
              prefix-icon="Lock"
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="login-footer">
          <p>默认账户：admin / admin</p>
          <p>HR账户：hr001 / admin</p>
        </div>
      </div>
    </div>
    
    <div class="background-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
      <div class="grid-pattern"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  username: '',
  password: ''
})

const formRef = ref(null)
const loading = ref(false)

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  
  loading.value = true
  
  try {
    const success = await authStore.login(form.username, form.password)
    if (success) {
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } else {
      ElMessage.error('用户名或密码错误')
    }
  } catch (error) {
    ElMessage.error('登录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--color-bg-primary);
  position: relative;
  overflow: hidden;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.circle-1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, var(--color-accent) 0%, transparent 70%);
  top: -100px;
  right: -100px;
}

.circle-2 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, var(--color-accent-blue) 0%, transparent 70%);
  bottom: -50px;
  left: -50px;
}

.circle-3 {
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, var(--color-info) 0%, transparent 70%);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.grid-pattern {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    linear-gradient(rgba(0, 212, 170, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 212, 170, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
}

.login-wrapper {
  position: relative;
  z-index: 1;
}

.login-card {
  width: 420px;
  padding: 40px;
  background: rgba(22, 27, 34, 0.95);
  border: 1px solid var(--color-border);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  box-shadow: 0 0 40px rgba(0, 212, 170, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
  color: var(--color-accent);
}

.login-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0 0 8px 0;
}

.login-header p {
  font-size: 14px;
  color: var(--color-text-muted);
  margin: 0;
}

.login-form {
  margin-bottom: 24px;
}

.login-form .el-form-item {
  margin-bottom: 20px;
}

.login-form .el-form-item__label {
  color: var(--color-text-secondary) !important;
  font-weight: 500;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
}

.login-footer {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid var(--color-border);
}

.login-footer p {
  font-size: 12px;
  color: var(--color-text-muted);
  margin: 4px 0;
}
</style>
