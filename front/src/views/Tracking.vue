<template>
  <div class="tracking-page">
    <div class="page-header">
      <h2>招聘流程追踪</h2>
      <div class="header-actions">
        <el-button type="primary" @click="refreshData">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="23 4 23 10 17 10"/>
            <polyline points="1 20 1 14 7 14"/>
            <path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"/>
          </svg>
          刷新数据
        </el-button>
      </div>
    </div>
    
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-value">{{ stats.totalPositions }}</span>
        <span class="stat-label">招聘职位</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ stats.totalCandidates }}</span>
        <span class="stat-label">候选人总数</span>
      </div>
      <div class="stat-item warning">
        <span class="stat-value">{{ stats.overdueCount }}</span>
        <span class="stat-label">超时预警</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ stats.averageDays }}天</span>
        <span class="stat-label">平均周期</span>
      </div>
    </div>
    
    <div class="timeline-container">
      <div class="timeline-header">
        <h3>招聘进度总览</h3>
        <el-select v-model="selectedPosition" placeholder="选择职位">
          <el-option label="全部职位" value=""/>
          <el-option 
            v-for="pos in positions" 
            :key="pos.id" 
            :label="pos.name" 
            :value="pos.id"
          />
        </el-select>
      </div>
      
      <div class="flow-chart">
        <div class="flow-column">
          <div class="column-header">
            <span class="column-title">待筛选</span>
            <span class="column-count">{{ pipeline.pending }}</span>
          </div>
          <div class="column-content">
            <div 
              v-for="candidate in pipelineData.pending" 
              :key="candidate.id" 
              class="candidate-card"
              draggable="true"
              @dragstart="handleDragStart($event, candidate, 'pending')"
              @dragover.prevent
              @drop="handleDrop($event, 'pending')"
            >
              <div class="card-name">{{ candidate.name }}</div>
              <div class="card-position">{{ candidate.positionName }}</div>
              <div class="card-time">等待 {{ candidate.waitDays }} 天</div>
            </div>
            <div v-if="pipelineData.pending.length === 0" class="empty-column">
              暂无候选人
            </div>
          </div>
        </div>
        
        <div class="flow-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M5 12h14"/>
            <path d="m12 5 7 7-7 7"/>
          </svg>
        </div>
        
        <div class="flow-column">
          <div class="column-header">
            <span class="column-title">面试中</span>
            <span class="column-count">{{ pipeline.interviewing }}</span>
          </div>
          <div class="column-content">
            <div 
              v-for="candidate in pipelineData.interviewing" 
              :key="candidate.id" 
              class="candidate-card"
              draggable="true"
              @dragstart="handleDragStart($event, candidate, 'interviewing')"
              @dragover.prevent
              @drop="handleDrop($event, 'interviewing')"
            >
              <div class="card-name">{{ candidate.name }}</div>
              <div class="card-position">{{ candidate.positionName }}</div>
              <div class="card-time">面试 {{ candidate.interviewCount }} 轮</div>
            </div>
            <div v-if="pipelineData.interviewing.length === 0" class="empty-column">
              暂无候选人
            </div>
          </div>
        </div>
        
        <div class="flow-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M5 12h14"/>
            <path d="m12 5 7 7-7 7"/>
          </svg>
        </div>
        
        <div class="flow-column">
          <div class="column-header">
            <span class="column-title">录用</span>
            <span class="column-count success">{{ pipeline.offered }}</span>
          </div>
          <div class="column-content">
            <div 
              v-for="candidate in pipelineData.offered" 
              :key="candidate.id" 
              class="candidate-card"
              draggable="true"
              @dragstart="handleDragStart($event, candidate, 'offered')"
              @dragover.prevent
              @drop="handleDrop($event, 'offered')"
            >
              <div class="card-name">{{ candidate.name }}</div>
              <div class="card-position">{{ candidate.positionName }}</div>
              <div class="card-time">Offer {{ candidate.waitDays }} 天</div>
            </div>
            <div v-if="pipelineData.offered.length === 0" class="empty-column">
              暂无候选人
            </div>
          </div>
        </div>
        
        <div class="flow-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M5 12h14"/>
            <path d="m12 5 7 7-7 7"/>
          </svg>
        </div>
        
        <div class="flow-column">
          <div class="column-header">
            <span class="column-title">入职</span>
            <span class="column-count success">{{ pipeline.hired }}</span>
          </div>
          <div class="column-content">
            <div 
              v-for="candidate in pipelineData.hired" 
              :key="candidate.id" 
              class="candidate-card"
            >
              <div class="card-name">{{ candidate.name }}</div>
              <div class="card-position">{{ candidate.positionName }}</div>
              <div class="card-time">{{ candidate.joinDate }}</div>
            </div>
            <div v-if="pipelineData.hired.length === 0" class="empty-column">
              暂无员工
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="charts-section">
      <div class="chart-card">
        <div class="chart-header">
          <h3>各环节耗时统计</h3>
        </div>
        <div ref="timeChart" class="chart-container"></div>
      </div>
      
      <div class="chart-card">
        <div class="chart-header">
          <h3>职位招聘进度</h3>
        </div>
        <div ref="progressChart" class="chart-container"></div>
      </div>
    </div>
    
    <div class="alert-section">
      <div class="alert-header">
        <h3>超时预警</h3>
        <span class="alert-count">共 {{ alerts.length }} 条预警</span>
      </div>
      <div class="alert-list">
        <div 
          v-for="alert in alerts" 
          :key="alert.id" 
          class="alert-item"
          :class="{ 'high-priority': alert.priority === 'high' }"
        >
          <div class="alert-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
              <path d="M12 9v4"/>
              <path d="M12 17h.01"/>
            </svg>
          </div>
          <div class="alert-content">
            <div class="alert-title">{{ alert.title }}</div>
            <div class="alert-desc">{{ alert.description }}</div>
          </div>
          <div class="alert-meta">
            <span class="alert-time">{{ alert.time }}</span>
            <el-button size="mini" @click="handleAlert(alert)">处理</el-button>
          </div>
        </div>
        <div v-if="alerts.length === 0" class="empty-alerts">
          暂无超时预警
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import api from '@/api'

const stats = reactive({
  totalPositions: 0,
  totalCandidates: 0,
  overdueCount: 0,
  averageDays: 0
})

const positions = ref([])
const selectedPosition = ref('')
const pipeline = reactive({
  pending: 0,
  interviewing: 0,
  offered: 0,
  hired: 0
})

const pipelineData = reactive({
  pending: [],
  interviewing: [],
  offered: [],
  hired: []
})

const alerts = ref([])

const timeChart = ref(null)
const progressChart = ref(null)
let timeChartInstance = null
let progressChartInstance = null
let refreshTimer = null

const draggedItem = ref(null)
const draggedFrom = ref('')

async function loadData() {
  try {
    const [statsRes, positionsRes, pipelineRes, alertsRes, timeStats, progressStats] = await Promise.all([
      api.dashboard.statistics(),
      api.position.list({ page: 1, size: 50 }),
      api.dashboard.pipelineStats(),
      api.dashboard.alerts(),
      api.dashboard.cycleStats(),
      api.dashboard.positionStats()
    ])
    
    if (statsRes.code === 200) {
      stats.totalPositions = statsRes.data.totalPositions || 0
      stats.totalCandidates = statsRes.data.totalCandidates || 0
      stats.averageDays = statsRes.data.averageCycleDays || 0
    }
    
    if (positionsRes.code === 200) {
      positions.value = positionsRes.data.content || positionsRes.data.records || []
    }
    
    if (pipelineRes.code === 200) {
      const data = pipelineRes.data
      pipeline.pending = data.pending || 0
      pipeline.interviewing = data.interviewing || 0
      pipeline.offered = data.offered || 0
      pipeline.hired = data.hired || 0
      
      pipelineData.pending = (data.pendingList || []).map(c => ({
        id: c.id,
        name: c.name,
        positionName: c.positionName || '未知职位',
        waitDays: c.waitDays || 1
      }))
      pipelineData.interviewing = (data.interviewingList || []).map(c => ({
        id: c.id,
        name: c.name,
        positionName: c.positionName || '未知职位',
        interviewCount: c.interviewCount || 1
      }))
      pipelineData.offered = (data.offeredList || []).map(c => ({
        id: c.id,
        name: c.name,
        positionName: c.positionName || '未知职位',
        waitDays: c.waitDays || 1
      }))
      pipelineData.hired = (data.hiredList || []).map(c => ({
        id: c.id,
        name: c.name,
        positionName: c.positionName || '未知职位',
        joinDate: c.joinDate || '-'
      }))
    }
    
    if (alertsRes.code === 200) {
      alerts.value = alertsRes.data.alerts || []
      stats.overdueCount = alerts.value.length
    }
    
    if (timeStats.code === 200) {
      renderTimeChart(timeStats.data)
    }
    
    if (progressStats.code === 200) {
      renderProgressChart(progressStats.data)
    }
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

function renderTimeChart(data) {
  if (!timeChart.value) return
  
  if (!timeChartInstance) {
    timeChartInstance = echarts.init(timeChart.value)
  }
  
  const cycleData = data.cycleData || []
  const stages = cycleData.map(d => d.stage)
  const avgDays = cycleData.map(d => d.avgDays)
  
  timeChartInstance.setOption({
    color: ['#00d4aa'],
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: stages,
      axisLine: { lineStyle: { color: '#30363d' } },
      axisLabel: { color: '#8b949e' }
    },
    yAxis: {
      type: 'value',
      name: '平均天数',
      nameTextStyle: { color: '#8b949e' },
      axisLine: { lineStyle: { color: '#30363d' } },
      axisLabel: { color: '#8b949e' },
      splitLine: { lineStyle: { color: '#21262d' } }
    },
    series: [{
      type: 'bar',
      data: avgDays,
      barWidth: '50%',
      itemStyle: {
        borderRadius: [6, 6, 0, 0]
      }
    }]
  })
}

function renderProgressChart(data) {
  if (!progressChart.value) return
  
  if (!progressChartInstance) {
    progressChartInstance = echarts.init(progressChart.value)
  }
  
  const positions = data.positions || []
  const names = positions.map(p => p.name)
  const counts = positions.map(p => p.count)
  
  progressChartInstance.setOption({
    color: ['#3b82f6', '#00d4aa', '#f59e0b', '#8b5cf6'],
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}人'
    },
    legend: {
      show: false
    },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 8,
        borderColor: '#1c2128',
        borderWidth: 2
      },
      label: {
        show: true,
        color: '#e6edf3',
        formatter: '{b}\n{c}人'
      },
      labelLine: {
        lineStyle: { color: '#8b949e' }
      },
      data: names.map((name, index) => ({
        name,
        value: counts[index]
      }))
    }]
  })
}

function handleDragStart(event, candidate, from) {
  draggedItem.value = candidate
  draggedFrom.value = from
  event.dataTransfer.effectAllowed = 'move'
}

async function handleDrop(event, to) {
  if (!draggedItem.value || draggedFrom.value === to) return
  
  try {
    const statusMap = {
      pending: 1,
      interviewing: 2,
      offered: 3,
      hired: 4
    }
    
    await api.candidate.updateStatus(draggedItem.value.id, statusMap[to])
    ElMessage.success('状态更新成功')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    draggedItem.value = null
    draggedFrom.value = ''
  }
}

function handleAlert(alert) {
  ElMessage.info(`已处理预警: ${alert.title}`)
  alerts.value = alerts.value.filter(a => a.id !== alert.id)
  stats.overdueCount = alerts.value.length
}

function refreshData() {
  loadData()
}

function startAutoRefresh() {
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
  refreshTimer = setInterval(() => {
    loadData()
  }, 30000)
}

function stopAutoRefresh() {
  if (refreshTimer) {
    clearInterval(refreshTimer)
    refreshTimer = null
  }
}

watch(selectedPosition, () => {
  loadData()
})

onMounted(() => {
  loadData()
  startAutoRefresh()
  
  window.addEventListener('resize', () => {
    timeChartInstance?.resize()
    progressChartInstance?.resize()
  })
})

onUnmounted(() => {
  stopAutoRefresh()
})
</script>

<style scoped>
.tracking-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
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

.stats-bar {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 16px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
  background-color: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: 12px;
}

.stat-item.warning .stat-value {
  color: #f59e0b;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-accent);
}

.stat-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin-top: 4px;
}

.timeline-container {
  background-color: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 20px;
}

.timeline-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.timeline-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.flow-chart {
  display: flex;
  align-items: stretch;
  gap: 8px;
}

.flow-column {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  overflow: hidden;
}

.column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background-color: rgba(0, 0, 0, 0.2);
  border-bottom: 1px solid var(--color-border);
}

.column-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.column-count {
  font-size: 16px;
  font-weight: 700;
  color: var(--color-text-secondary);
}

.column-count.success {
  color: var(--color-accent);
}

.column-content {
  flex: 1;
  padding: 12px;
  overflow-y: auto;
  max-height: 400px;
}

.candidate-card {
  padding: 12px;
  background-color: var(--color-bg-primary);
  border: 1px solid var(--color-border);
  border-radius: 8px;
  margin-bottom: 10px;
  cursor: move;
  transition: all 0.2s ease;
}

.candidate-card:hover {
  border-color: var(--color-accent);
  transform: translateY(-2px);
}

.card-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.card-position {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-bottom: 4px;
}

.card-time {
  font-size: 11px;
  color: var(--color-accent-blue);
}

.empty-column {
  text-align: center;
  padding: 20px;
  color: var(--color-text-secondary);
  font-size: 13px;
}

.flow-arrow {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-secondary);
}

.charts-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 16px;
}

.chart-card {
  background-color: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 20px;
}

.chart-header {
  margin-bottom: 16px;
}

.chart-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.chart-container {
  height: 250px;
}

.alert-section {
  background-color: rgba(245, 158, 11, 0.05);
  border: 1px solid rgba(245, 158, 11, 0.2);
  border-radius: 12px;
  padding: 20px;
}

.alert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.alert-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.alert-count {
  font-size: 13px;
  color: #f59e0b;
}

.alert-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.alert-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background-color: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: 8px;
}

.alert-item.high-priority {
  border-color: #f59e0b;
  background-color: rgba(245, 158, 11, 0.1);
}

.alert-icon {
  color: #f59e0b;
}

.alert-content {
  flex: 1;
}

.alert-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.alert-desc {
  font-size: 13px;
  color: var(--color-text-secondary);
}

.alert-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.alert-time {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.empty-alerts {
  text-align: center;
  padding: 20px;
  color: var(--color-text-secondary);
}
</style>