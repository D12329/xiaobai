
<template>
  <div class="dashboard">
    <div class="stats-grid">
      <div class="stat-card card-glow cursor-pointer" @click="navigateTo('/positions')">
        <div class="stat-icon positions">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M22 12h-4l-3 9L9 3l-3 9H2"/>
          </svg>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalPositions || 0 }}</div>
          <div class="stat-label">招聘职位</div>
        </div>
        <div class="stat-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 18l6-6-6-6"/>
          </svg>
        </div>
      </div>
      
      <div class="stat-card card-glow cursor-pointer" @click="navigateTo('/resumes')">
        <div class="stat-icon resumes">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14 2 14 8 20 8"/>
            <line x1="16" y1="13" x2="8" y2="13"/>
            <line x1="16" y1="17" x2="8" y2="17"/>
            <polyline points="10 9 9 9 8 9"/>
          </svg>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalResumes || 0 }}</div>
          <div class="stat-label">简历总数</div>
        </div>
        <div class="stat-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 18l6-6-6-6"/>
          </svg>
        </div>
      </div>
      
      <div class="stat-card card-glow cursor-pointer" @click="navigateTo('/candidates')">
        <div class="stat-icon candidates">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
            <circle cx="9" cy="7" r="4"/>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
          </svg>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalCandidates || 0 }}</div>
          <div class="stat-label">候选人</div>
        </div>
        <div class="stat-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 18l6-6-6-6"/>
          </svg>
        </div>
      </div>
      
      <div class="stat-card card-glow cursor-pointer" @click="navigateTo('/interviews')">
        <div class="stat-icon interviews">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
            <line x1="16" y1="2" x2="16" y2="6"/>
            <line x1="8" y1="2" x2="8" y2="6"/>
            <line x1="3" y1="10" x2="21" y2="10"/>
          </svg>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalInterviews || 0 }}</div>
          <div class="stat-label">面试安排</div>
        </div>
        <div class="stat-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 18l6-6-6-6"/>
          </svg>
        </div>
      </div>
      
      <div class="stat-card card-glow cursor-pointer" @click="navigateTo('/offers')">
        <div class="stat-icon offers">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
            <path d="M12 12v4"/>
          </svg>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalOffers || 0 }}</div>
          <div class="stat-label">Offer发放</div>
        </div>
        <div class="stat-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 18l6-6-6-6"/>
          </svg>
        </div>
      </div>
      
      <div class="stat-card card-glow cursor-pointer" @click="navigateTo('/tracking')">
        <div class="stat-icon employees">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalEmployees || 0 }}</div>
          <div class="stat-label">入职员工</div>
        </div>
        <div class="stat-arrow">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 18l6-6-6-6"/>
          </svg>
        </div>
      </div>
    </div>
    
    <div class="charts-row">
      <div class="chart-card">
        <div class="chart-header">
          <h3>职位招聘需求</h3>
        </div>
        <div ref="positionChart" class="chart-container"></div>
      </div>
      
      <div class="chart-card">
        <div class="chart-header">
          <h3>简历来源渠道</h3>
        </div>
        <div ref="channelChart" class="chart-container"></div>
      </div>
    </div>
    
    <div class="charts-row">
      <div class="chart-card full-width">
        <div class="chart-header">
          <h3>招聘周期分析</h3>
        </div>
        <div ref="cycleChart" class="chart-container large"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()
const statistics = ref({})
const positionChart = ref(null)
const channelChart = ref(null)
const cycleChart = ref(null)

function navigateTo(path) {
  router.push(path)
}

let positionChartInstance = null
let channelChartInstance = null
let cycleChartInstance = null

async function fetchData() {
  const [stats, positionStats, channelStats, cycleStats] = await Promise.all([
    api.dashboard.statistics(),
    api.dashboard.positionDemand(),
    api.dashboard.channelDistribution(),
    api.dashboard.recruitmentCycle()
  ])
  
  if (stats.code === 200) {
    statistics.value = stats.data
  }
  
  if (positionStats.code === 200) {
    renderPositionChart(positionStats.data)
  }
  
  if (channelStats.code === 200) {
    renderChannelChart(channelStats.data)
  }
  
  if (cycleStats.code === 200) {
    renderCycleChart(cycleStats.data)
  }
}

function renderPositionChart(data) {
  if (!positionChart.value) return
  
  if (!positionChartInstance) {
    positionChartInstance = echarts.init(positionChart.value)
  }
  
  const positionDemand = data.positionDemand || {}
  const names = Object.keys(positionDemand)
  const counts = Object.values(positionDemand)
  
  positionChartInstance.setOption({
    color: ['#00d4aa', '#3b82f6', '#58a6ff', '#8b5cf6'],
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
      data: names,
      axisLine: { lineStyle: { color: '#30363d' } },
      axisLabel: { color: '#8b949e' }
    },
    yAxis: {
      type: 'value',
      axisLine: { lineStyle: { color: '#30363d' } },
      axisLabel: { color: '#8b949e' },
      splitLine: { lineStyle: { color: '#21262d' } }
    },
    series: [{
      type: 'bar',
      data: counts,
      barWidth: '50%',
      itemStyle: {
        borderRadius: [6, 6, 0, 0]
      }
    }]
  })
}

function renderChannelChart(data) {
  if (!channelChart.value) return
  
  if (!channelChartInstance) {
    channelChartInstance = echarts.init(channelChart.value)
  }
  
  const channels = data.channelDistribution || {}
  const names = Object.keys(channels)
  const values = Object.values(channels)
  
  channelChartInstance.setOption({
    color: ['#00d4aa', '#3b82f6', '#58a6ff', '#8b5cf6', '#f59e0b', '#ec4899'],
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
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
        formatter: '{b}\n{d}%'
      },
      labelLine: {
        lineStyle: { color: '#8b949e' }
      },
      data: names.map((name, index) => ({
        name,
        value: values[index]
      }))
    }]
  })
}

function renderCycleChart(data) {
  if (!cycleChart.value) return
  
  if (!cycleChartInstance) {
    cycleChartInstance = echarts.init(cycleChart.value)
  }
  
  const monthlyTrend = data.monthlyTrend || {}
  const months = Object.keys(monthlyTrend)
  const candidateCounts = months.map(m => monthlyTrend[m]?.candidates || 0)
  
  cycleChartInstance.setOption({
    color: ['#00d4aa', '#3b82f6'],
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross', label: { backgroundColor: '#1c2128' } }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: months,
      axisLine: { lineStyle: { color: '#30363d' } },
      axisLabel: { color: '#8b949e' }
    },
    yAxis: {
      type: 'value',
      name: '数量',
      nameTextStyle: { color: '#8b949e' },
      axisLine: { lineStyle: { color: '#30363d' } },
      axisLabel: { color: '#8b949e' },
      splitLine: { lineStyle: { color: '#21262d' } }
    },
    series: [{
      name: '候选人',
      type: 'line',
      smooth: true,
      data: candidateCounts,
      lineStyle: { width: 3 },
      itemStyle: {
        color: '#00d4aa'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(0, 212, 170, 0.3)' },
          { offset: 1, color: 'rgba(0, 212, 170, 0.05)' }
        ])
      },
      symbol: 'circle',
      symbolSize: 8
    }, {
      name: '简历',
      type: 'line',
      smooth: true,
      data: months.map(m => monthlyTrend[m]?.resumes || 0),
      lineStyle: { width: 3 },
      itemStyle: {
        color: '#3b82f6'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(59, 130, 246, 0.3)' },
          { offset: 1, color: 'rgba(59, 130, 246, 0.05)' }
        ])
      },
      symbol: 'circle',
      symbolSize: 8
    }]
  })
}

onMounted(() => {
  fetchData()
  
  window.addEventListener('resize', () => {
    positionChartInstance?.resize()
    channelChartInstance?.resize()
    cycleChartInstance?.resize()
  })
})
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background-color: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: 12px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon.positions {
  background-color: rgba(0, 212, 170, 0.15);
  color: var(--color-accent);
}

.stat-icon.resumes {
  background-color: rgba(59, 130, 246, 0.15);
  color: var(--color-accent-blue);
}

.stat-icon.candidates {
  background-color: rgba(88, 166, 255, 0.15);
  color: var(--color-info);
}

.stat-icon.interviews {
  background-color: rgba(139, 92, 246, 0.15);
  color: #8b5cf6;
}

.stat-icon.offers {
  background-color: rgba(245, 158, 11, 0.15);
  color: #f59e0b;
}

.stat-icon.employees {
  background-color: rgba(63, 185, 80, 0.15);
  color: var(--color-success);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.stat-label {
  font-size: 14px;
  color: var(--color-text-secondary);
}

.stat-arrow {
  color: var(--color-text-secondary);
  opacity: 0;
  transition: opacity 0.2s;
}

.cursor-pointer {
  cursor: pointer;
}

.cursor-pointer:hover .stat-arrow {
  opacity: 1;
}

.cursor-pointer:hover {
  border-color: var(--color-accent);
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 212, 170, 0.15);
}

.charts-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 16px;
}

.charts-row .full-width {
  grid-column: 1 / -1;
}

.chart-card {
  background-color: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 20px;
}

.chart-header {
  margin-bottom: 20px;
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

.chart-container.large {
  height: 300px;
}
</style>
