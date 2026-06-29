
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/',
      name: 'Layout',
      component: () => import('@/layout/Layout.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: '/dashboard',
          name: 'Dashboard',
          component: () => import('@/views/Dashboard.vue'),
          meta: { title: '数据看板' }
        },
        {
          path: '/positions',
          name: 'Positions',
          component: () => import('@/views/Position.vue'),
          meta: { title: '职位管理' }
        },
        {
          path: '/resumes',
          name: 'Resumes',
          component: () => import('@/views/Resume.vue'),
          meta: { title: '简历管理' }
        },
        {
          path: '/candidates',
          name: 'Candidates',
          component: () => import('@/views/Candidate.vue'),
          meta: { title: '候选人管理' }
        },
        {
          path: '/interviews',
          name: 'Interviews',
          component: () => import('@/views/Interview.vue'),
          meta: { title: '面试管理' }
        },
        {
          path: '/offers',
          name: 'Offers',
          component: () => import('@/views/Offer.vue'),
          meta: { title: 'Offer管理' }
        },
        {
          path: '/match',
          name: 'Match',
          component: () => import('@/views/Match.vue'),
          meta: { title: 'AI智能匹配' }
        },
        {
          path: '/tracking',
          name: 'Tracking',
          component: () => import('@/views/Tracking.vue'),
          meta: { title: '流程追踪' }
        },
        {
          path: '/settings/users',
          name: 'UserManagement',
          component: () => import('@/views/settings/UserManagement.vue'),
          meta: { title: '用户管理' }
        },
        {
          path: '/settings/departments',
          name: 'DepartmentManagement',
          component: () => import('@/views/settings/DepartmentManagement.vue'),
          meta: { title: '部门管理' }
        },
        {
          path: '/settings/dict',
          name: 'DictManagement',
          component: () => import('@/views/settings/DictManagement.vue'),
          meta: { title: '数据字典' }
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const user = localStorage.getItem('user')
  const isLoggedIn = !!(token && user)

  if (to.path === '/login') {
    if (isLoggedIn) {
      next('/dashboard')
    } else {
      next()
    }
  } else {
    if (isLoggedIn) {
      next()
    } else {
      next('/login')
    }
  }
})

router.afterEach((to) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - 企业招聘后台管理系统'
  }
})

export default router
