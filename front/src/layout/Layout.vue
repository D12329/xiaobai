
<template>
  <div class="layout-container">
    <aside class="sidebar" :class="{ collapsed: isCollapsed }">
      <div class="sidebar-header">
        <div class="logo" v-if="!isCollapsed">
          <span class="logo-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
          </span>
          <span class="logo-text">招聘系统</span>
        </div>
        <div class="logo-mini" v-else>
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapsed"
        mode="vertical"
        class="sidebar-menu"
        router
      >
        <el-menu-item index="/dashboard">
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="3" width="7" height="7"/>
              <rect x="14" y="3" width="7" height="7"/>
              <rect x="14" y="14" width="7" height="7"/>
              <rect x="3" y="14" width="7" height="7"/>
            </svg>
          </template>
          <span>数据看板</span>
        </el-menu-item>
        
        <el-menu-item index="/positions">
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
            </svg>
          </template>
          <span>职位管理</span>
        </el-menu-item>
        
        <el-menu-item index="/resumes">
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <polyline points="14 2 14 8 20 8"/>
              <line x1="16" y1="13" x2="8" y2="13"/>
              <line x1="16" y1="17" x2="8" y2="17"/>
              <polyline points="10 9 9 9 8 9"/>
            </svg>
          </template>
          <span>简历管理</span>
        </el-menu-item>
        
        <el-menu-item index="/candidates">
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
              <circle cx="9" cy="7" r="4"/>
              <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
              <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
            </svg>
          </template>
          <span>候选人管理</span>
        </el-menu-item>
        
        <el-menu-item index="/interviews">
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
              <line x1="16" y1="2" x2="16" y2="6"/>
              <line x1="8" y1="2" x2="8" y2="6"/>
              <line x1="3" y1="10" x2="21" y2="10"/>
            </svg>
          </template>
          <span>面试管理</span>
        </el-menu-item>
        
        <el-menu-item index="/offers">
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
              <path d="M12 12v4"/>
            </svg>
          </template>
          <span>Offer管理</span>
        </el-menu-item>
        
        <el-menu-item index="/match">
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M12 4.5a2.5 2.5 0 0 0-4.96-.46 2.5 2.5 0 0 0-1.98 3 2.5 2.5 0 0 0 1.48 4.58 2.5 2.5 0 0 0-0.52 1.27V19a2.5 2.5 0 0 0 2.5 2.5h1.5a2 2 0 0 0 2-2v-1.25a2.5 2.5 0 0 0 .5-4.95 2.5 2.5 0 0 0 2.96-.4 2.5 2.5 0 0 0 1.44-4.22A2.5 2.5 0 0 0 12 4.5z"/>
              <path d="M15.7 8.3a2.5 2.5 0 0 0-3.4 0 2.5 2.5 0 0 0 0 3.4 2.5 2.5 0 0 0 3.4 0 2.5 2.5 0 0 0 0-3.4z"/>
            </svg>
          </template>
          <span>AI智能匹配</span>
        </el-menu-item>
        
        <el-menu-item index="/tracking">
          <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12 6 12 12 16 14"/>
            </svg>
          </template>
          <span>流程追踪</span>
        </el-menu-item>
        
        <el-sub-menu index="settings">
          <template #title>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z"/>
              <circle cx="12" cy="12" r="3"/>
            </svg>
            <span>系统设置</span>
          </template>
          <el-menu-item index="/settings/users">用户管理</el-menu-item>
          <el-menu-item index="/settings/departments">部门管理</el-menu-item>
          <el-menu-item index="/settings/dict">数据字典</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </aside>
    
    <div class="main-content">
      <header class="header">
        <div class="header-left">
          <button class="toggle-btn" @click="toggleCollapse">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="3" y1="12" x2="21" y2="12"/>
              <line x1="3" y1="6" x2="21" y2="6"/>
              <line x1="3" y1="18" x2="21" y2="18"/>
            </svg>
          </button>
          <div class="breadcrumb">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </div>
        
        <div class="header-right">
          <div class="user-info">
            <span class="user-name">{{ userName }}</span>
            <button class="logout-btn" @click="handleLogout">
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
                <polyline points="16 17 21 12 16 7"/>
                <line x1="21" y1="12" x2="9" y2="12"/>
              </svg>
              <span>退出</span>
            </button>
          </div>
        </div>
      </header>
      
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const isCollapsed = ref(false)

const activeMenu = computed(() => route.path)

const currentPageTitle = computed(() => {
  const titles = {
    '/dashboard': '数据看板',
    '/positions': '职位管理',
    '/resumes': '简历管理',
    '/candidates': '候选人管理',
    '/interviews': '面试管理',
    '/offers': 'Offer管理',
    '/match': 'AI智能匹配',
    '/tracking': '流程追踪',
    '/settings/users': '用户管理',
    '/settings/departments': '部门管理',
    '/settings/dict': '数据字典'
  }
  return titles[route.path] || '首页'
})

const userName = computed(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    try {
      const user = JSON.parse(userStr)
      return user.realName || '用户'
    } catch {
      return '用户'
    }
  }
  return '用户'
})

function toggleCollapse() {
  isCollapsed.value = !isCollapsed.value
}

async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    ElMessage.success('退出成功')
    router.push('/login')
  } catch {
  }
}
</script>

<style scoped>
.layout-container {
  display: flex;
  min-height: 100vh;
  background-color: var(--color-bg-primary);
}

.sidebar {
  width: 240px;
  background-color: var(--color-bg-secondary);
  border-right: 1px solid var(--color-border);
  transition: width 0.3s ease;
  flex-shrink: 0;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  display: flex;
  align-items: center;
  height: 60px;
  padding: 0 20px;
  border-bottom: 1px solid var(--color-border);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--color-accent);
}

.logo-icon {
  font-size: 24px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
}

.logo-mini {
  display: flex;
  align-items: center;
  color: var(--color-accent);
  font-size: 24px;
}

.sidebar-menu {
  height: calc(100% - 60px);
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
  background-color: var(--color-bg-secondary);
  border-bottom: 1px solid var(--color-border);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.toggle-btn {
  background: none;
  border: none;
  color: var(--color-text-secondary);
  cursor: pointer;
  padding: 8px;
  border-radius: 6px;
  transition: background-color 0.2s;
}

.toggle-btn:hover {
  background-color: var(--color-bg-tertiary);
}

.breadcrumb {
  font-size: 14px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  font-size: 14px;
  color: var(--color-text-secondary);
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  background: none;
  border: none;
  color: var(--color-text-secondary);
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.2s;
}

.logout-btn:hover {
  background-color: var(--color-bg-tertiary);
  color: var(--color-danger);
}

.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>
