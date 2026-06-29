import axios from 'axios'

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

instance.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

const api = {
  auth: {
    login: (data) => instance.post('/auth/login', data),
    register: (data) => instance.post('/auth/register', data),
    getUserInfo: () => instance.get('/auth/userinfo'),
    logout: () => instance.post('/auth/logout'),
    changePassword: (data) => instance.post('/auth/change-password', data)
  },
  user: {
    list: (params) => instance.get('/users', { params }),
    get: (id) => instance.get(`/users/${id}`),
    create: (data) => instance.post('/users', data),
    update: (id, data) => instance.put(`/users/${id}`, data),
    delete: (id) => instance.delete(`/users/${id}`),
    updateStatus: (id, status) => instance.put(`/candidates/${id}/status`, null, { params: { status } }),
    listRoles: () => instance.get('/users/roles'),
    listDepartments: () => instance.get('/departments')
  },
  position: {
    list: (params) => instance.get('/positions', { params }),
    get: (id) => instance.get(`/positions/${id}`),
    create: (data) => instance.post('/positions', data),
    update: (id, data) => instance.put(`/positions/${id}`, data),
    delete: (id) => instance.delete(`/positions/${id}`),
    updateStatus: (id, status) => instance.put(`/positions/${id}/status`, null, { params: { status } })
  },
  resume: {
    list: (params) => instance.get('/resumes', { params }),
    get: (id) => instance.get(`/resumes/${id}`),
    create: (data) => instance.post('/resumes', data),
    update: (id, data) => instance.put(`/resumes/${id}`, data),
    delete: (id) => instance.delete(`/resumes/${id}`),
    upload: (formData) => instance.post('/resumes/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } }),
    getUnconverted: (keyword) => instance.get('/resumes/unconverted', { params: { keyword } })
  },
  candidate: {
    list: (params) => instance.get('/candidates', { params }),
    get: (id) => instance.get(`/candidates/${id}`),
    create: (data) => instance.post('/candidates', data),
    update: (id, data) => instance.put(`/candidates/${id}`, data),
    delete: (id) => instance.delete(`/candidates/${id}`),
    updateStatus: (id, status) => instance.put(`/candidates/${id}/status`, null, { params: { status } }),
    addFollow: (id, record) => instance.post(`/candidates/${id}/follow`, { record }),
    addFollowRecord: (id, record) => instance.post(`/candidates/${id}/follow`, { record }),
    getFollowRecords: (id) => instance.get(`/candidates/${id}/follow`)
  },
  interview: {
    list: (params) => instance.get('/interviews', { params }),
    get: (id) => instance.get(`/interviews/${id}`),
    create: (data) => instance.post('/interviews', data),
    update: (id, data) => instance.put(`/interviews/${id}`, data),
    delete: (id) => instance.delete(`/interviews/${id}`),
    updateStatus: (id, status) => instance.put(`/interviews/${id}/status`, null, { params: { status } }),
    submitEvaluation: (id, evaluation, score) => instance.put(`/interviews/${id}/evaluation`, { evaluation, score }),
    getEvaluation: (id) => instance.get(`/interviews/${id}/evaluation`),
    sendNotification: (id) => instance.post(`/interviews/${id}/notification`)
  },
  offer: {
    list: (params) => instance.get('/offers', { params }),
    get: (id) => instance.get(`/offers/${id}`),
    create: (data) => instance.post('/offers', data),
    update: (id, data) => instance.put(`/offers/${id}`, data),
    delete: (id) => instance.delete(`/offers/${id}`),
    updateStatus: (id, status) => instance.put(`/offers/${id}/status`, null, { params: { status } }),
    sendNotification: (id) => instance.post(`/offers/${id}/notification`),
    confirmAccept: (id) => instance.put(`/offers/${id}/confirm`),
    updateDocument: (id, docType) => instance.put(`/offers/${id}/document`, null, { params: { docType } }),
    completeOnboarding: (id) => instance.put(`/offers/${id}/onboarding`)
  },
  dashboard: {
    statistics: () => instance.get('/dashboard/statistics'),
    positionDemand: () => instance.get('/dashboard/position-demand'),
    channelDistribution: () => instance.get('/dashboard/channel-distribution'),
    recruitmentCycle: () => instance.get('/dashboard/recruitment-cycle'),
    pipelineStats: () => instance.get('/dashboard/pipeline-stats'),
    alerts: () => instance.get('/dashboard/alerts'),
    cycleStats: () => instance.get('/dashboard/cycle-stats'),
    positionStats: () => instance.get('/dashboard/position-stats')
  },
  match: {
    parse: (resumeId) => instance.post(`/match/parse/${resumeId}`),
    matchPosition: (positionId, resumeIds, threshold) => instance.post(`/match/position/${positionId}`, { resumeIds, threshold }),
    getResult: (candidateId) => instance.get(`/match/result/${candidateId}`)
  },
  dict: {
    list: (params) => instance.get('/dict', { params }),
    get: (id) => instance.get(`/dict/${id}`),
    create: (data) => instance.post('/dict', data),
    update: (id, data) => instance.put(`/dict/${id}`, data),
    delete: (id) => instance.delete(`/dict/${id}`),
    getByType: (type) => instance.get(`/dict/type/${type}`)
  },
  department: {
    list: (params) => instance.get('/departments', { params }),
    get: (id) => instance.get(`/departments/${id}`),
    create: (data) => instance.post('/departments', data),
    update: (id, data) => instance.put(`/departments/${id}`, data),
    delete: (id) => instance.delete(`/departments/${id}`)
  },
  role: {
    list: (params) => instance.get('/roles', { params }),
    getAll: () => instance.get('/roles/all'),
    get: (id) => instance.get(`/roles/${id}`)
  }
}

export default api