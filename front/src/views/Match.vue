<template>
  <div class="match-page">
    <div class="page-header">
      <h2>AI智能简历匹配</h2>
      <div class="header-actions">
        <el-button type="primary" @click="startMatching">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="5 3 19 12 5 21 5 3"/>
          </svg>
          开始匹配
        </el-button>
      </div>
    </div>
    
    <div class="match-config">
      <div class="config-item">
        <label>选择职位</label>
        <el-select v-model="selectedPositionId" placeholder="请选择职位" @change="loadResumes">
          <el-option 
            v-for="pos in positions" 
            :key="pos.id" 
            :label="pos.name" 
            :value="pos.id"
          />
        </el-select>
      </div>
      <div class="config-item">
        <label>匹配阈值</label>
        <el-slider 
          v-model="matchThreshold" 
          :min="0" 
          :max="100" 
          :step="5"
          show-input
        />
      </div>
      <div class="config-item">
        <label>学历优先</label>
        <el-switch v-model="educationPriority"/>
      </div>
      <div class="config-item">
        <label>经验优先</label>
        <el-switch v-model="experiencePriority"/>
      </div>
    </div>
    
    <div v-if="matching" class="matching-indicator">
      <el-spinner size="large" />
      <p>AI正在分析简历中...</p>
    </div>
    
    <div v-else-if="matchResults.length > 0" class="match-results">
      <div class="results-header">
        <span>匹配结果</span>
        <span class="result-count">共 {{ matchResults.length }} 个匹配</span>
      </div>
      
      <div class="results-grid">
        <div 
          v-for="result in matchResults" 
          :key="result.resumeId" 
          class="match-card"
          :class="{ 'high-match': result.matchScore >= 80, 'medium-match': result.matchScore >= 60 && result.matchScore < 80 }"
        >
          <div class="card-header">
            <div class="candidate-info">
              <h3>{{ result.candidateName }}</h3>
              <span class="position">{{ result.positionName }}</span>
            </div>
            <div class="match-score" :style="{ color: getScoreColor(result.matchScore) }">
              {{ result.matchScore }}%
            </div>
          </div>
          
          <div class="match-details">
            <div class="detail-row">
              <span class="detail-label">学历</span>
              <span class="detail-value">{{ result.education }}</span>
              <span :class="result.educationMatch ? 'match-tag' : 'mismatch-tag'">
                {{ result.educationMatch ? '匹配' : '不匹配' }}
              </span>
            </div>
            <div class="detail-row">
              <span class="detail-label">经验</span>
              <span class="detail-value">{{ result.experience || '不限' }}年</span>
              <span :class="result.experienceMatch ? 'match-tag' : 'mismatch-tag'">
                {{ result.experienceMatch ? '匹配' : '不匹配' }}
              </span>
            </div>
          </div>
          
          <div class="skills-section">
            <div class="skills-header">
              <span>技能匹配</span>
              <span class="skill-match-rate">{{ result.skillMatchCount }}/{{ result.requiredSkillCount }}</span>
            </div>
            <div class="skills-list">
              <span 
                v-for="(skill, index) in result.skills" 
                :key="index"
                :class="skill.match ? 'skill-match' : 'skill-mismatch'"
              >
                {{ skill.name }}
              </span>
            </div>
          </div>
          
          <div class="match-analysis">
            <h4>匹配分析</h4>
            <p>{{ result.analysis }}</p>
          </div>
          
          <div class="card-actions">
            <el-button size="small" @click="viewResume(result.resumeId)">查看简历</el-button>
            <el-button size="small" type="primary" @click="addToCandidate(result)">加入候选人</el-button>
          </div>
        </div>
      </div>
    </div>
    
    <div v-else-if="!matching" class="empty-state">
      <div class="empty-icon">
        <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
          <circle cx="12" cy="7" r="4"/>
          <path d="M16 11.78A7 7 0 0 1 8 11.78"/>
        </svg>
      </div>
      <h3>智能匹配</h3>
      <p>选择职位后点击开始匹配，AI将自动分析简历并推荐高匹配候选人</p>
    </div>
    
    <el-dialog title="简历详情" v-model="showResumeModal" width="700px">
      <div v-if="selectedResume" class="resume-detail">
        <div class="resume-header">
          <h3>{{ selectedResume.name }}</h3>
          <span class="resume-position">{{ selectedResume.position }}</span>
        </div>
        
        <div class="resume-section">
          <h4>基本信息</h4>
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">学历</span>
              <span class="info-value">{{ selectedResume.education }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">经验</span>
              <span class="info-value">{{ selectedResume.experience }}年</span>
            </div>
            <div class="info-item">
              <span class="info-label">年龄</span>
              <span class="info-value">{{ selectedResume.age }}岁</span>
            </div>
            <div class="info-item">
              <span class="info-label">电话</span>
              <span class="info-value">{{ selectedResume.phone }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">邮箱</span>
              <span class="info-value">{{ selectedResume.email }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">来源</span>
              <span class="info-value">{{ selectedResume.source }}</span>
            </div>
          </div>
        </div>
        
        <div class="resume-section">
          <h4>技能标签</h4>
          <div class="skills-badge">
            <span v-for="skill in selectedResume.skills" :key="skill" class="skill-badge">{{ skill }}</span>
          </div>
        </div>
        
        <div class="resume-section">
          <h4>工作经历</h4>
          <div v-for="(exp, index) in selectedResume.experiences" :key="index" class="experience-item">
            <div class="exp-header">
              <span class="exp-company">{{ exp.company }}</span>
              <span class="exp-date">{{ exp.startDate }} - {{ exp.endDate }}</span>
            </div>
            <div class="exp-position">{{ exp.position }}</div>
            <div class="exp-desc">{{ exp.description }}</div>
          </div>
        </div>
        
        <div class="resume-section">
          <h4>项目经验</h4>
          <div v-for="(project, index) in selectedResume.projects" :key="index" class="project-item">
            <div class="project-header">
              <span class="project-name">{{ project.name }}</span>
              <span class="project-date">{{ project.startDate }} - {{ project.endDate }}</span>
            </div>
            <div class="project-role">角色：{{ project.role }}</div>
            <div class="project-desc">{{ project.description }}</div>
            <div class="project-skills">技术栈：{{ project.skills }}</div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

const positions = ref([])
const matchResults = ref([])
const selectedPositionId = ref(null)
const matchThreshold = ref(60)
const educationPriority = ref(false)
const experiencePriority = ref(false)
const matching = ref(false)
const showResumeModal = ref(false)
const selectedResume = ref(null)

async function loadPositions() {
  try {
    const response = await api.position.list({ page: 1, size: 100 })
    if (response.code === 200) {
      positions.value = (response.data.content || response.data.records || []).filter(p => p.status === 1)
    }
  } catch (error) {
    ElMessage.error('加载职位失败')
  }
}

function loadResumes() {
  matchResults.value = []
}

async function startMatching() {
  if (!selectedPositionId.value) {
    ElMessage.warning('请先选择职位')
    return
  }
  
  matching.value = true
  
  try {
    const resumeRes = await api.resume.list({ page: 1, size: 100 })
    const resumes = resumeRes.code === 200 ? 
      (resumeRes.data.content || resumeRes.data.records || []) : []
    
    if (resumes.length === 0) {
      ElMessage.info('暂无简历数据')
      matching.value = false
      return
    }
    
    const resumeIds = resumes.map(r => r.id)
    
    const response = await api.match.matchPosition(selectedPositionId.value, resumeIds, matchThreshold.value)
    
    if (response.code === 200) {
      const results = response.data || []
      
      matchResults.value = results.map(result => {
        const resume = resumes.find(r => r.id === result.resumeId)
        const pos = positions.value.find(p => p.id === selectedPositionId.value)
        return {
          resumeId: result.resumeId,
          candidateName: result.candidateName || resume?.name || '未知',
          positionName: result.positionName || pos?.name || '未知',
          matchScore: result.matchScore || 0,
          education: result.education || resume?.education || '',
          experience: result.experience || resume?.experience || 0,
          educationMatch: (result.matchScore || 0) >= 50,
          experienceMatch: (result.matchScore || 0) >= 50,
          skillMatchCount: result.matchScore || 0,
          requiredSkillCount: 100,
          skills: ((result.skills || resume?.skills) || '').split(',').map(skill => ({
            name: skill.trim(),
            match: true
          })).filter(s => s.name),
          analysis: result.analysis || generateAnalysis({ totalScore: result.matchScore || 0 })
        }
      })
      
      if (matchResults.value.length === 0) {
        ElMessage.info('未找到符合条件的匹配结果')
      }
    }
  } catch (error) {
    console.error('匹配失败:', error)
    ElMessage.error('匹配失败')
  } finally {
    matching.value = false
  }
}

function generateAnalysis(result) {
  const score = result.totalScore || 0
  if (score >= 80) {
    return '该候选人与职位高度匹配，建议优先安排面试。'
  } else if (score >= 60) {
    return '该候选人基本符合职位要求，可安排面试进一步评估。'
  } else if (score >= 40) {
    return '该候选人部分条件匹配，建议综合评估后决定是否面试。'
  } else {
    return '该候选人与职位匹配度较低，建议考虑其他候选人。'
  }
}

function getScoreColor(score) {
  if (score >= 80) return '#00d4aa'
  if (score >= 60) return '#f59e0b'
  return '#ef4444'
}

async function viewResume(resumeId) {
  try {
    const response = await api.resume.get(resumeId)
    if (response.code === 200) {
      selectedResume.value = response.data
      showResumeModal.value = true
    }
  } catch (error) {
    ElMessage.error('加载简历失败')
  }
}

async function addToCandidate(result) {
  try {
    await api.candidate.create({
      name: result.candidateName,
      positionName: result.positionName,
      education: result.education,
      experience: result.experience,
      skills: result.skills.map(s => s.name).join(','),
      source: 'AI匹配',
      status: 0
    })
    ElMessage.success('已加入候选人')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  loadPositions()
})
</script>

<style scoped>
.match-page {
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

.match-config {
  display: flex;
  gap: 24px;
  align-items: center;
  padding: 20px;
  background-color: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  flex-wrap: wrap;
}

.config-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.config-item label {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.config-item .el-select {
  width: 200px;
}

.config-item .el-slider {
  width: 200px;
}

.matching-indicator {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px;
}

.matching-indicator p {
  margin-top: 16px;
  color: var(--color-text-secondary);
}

.match-results {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.results-header span {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.result-count {
  font-size: 14px;
  font-weight: normal;
  color: var(--color-text-secondary);
}

.results-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(450px, 1fr));
  gap: 16px;
}

.match-card {
  background-color: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 20px;
  transition: all 0.3s ease;
}

.match-card:hover {
  border-color: var(--color-accent);
  box-shadow: 0 0 20px rgba(0, 212, 170, 0.1);
}

.match-card.high-match {
  border-color: rgba(0, 212, 170, 0.3);
  background: linear-gradient(135deg, rgba(0, 212, 170, 0.05) 0%, transparent 100%);
}

.match-card.medium-match {
  border-color: rgba(245, 158, 11, 0.3);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.candidate-info h3 {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0 0 4px 0;
}

.candidate-info .position {
  font-size: 14px;
  color: var(--color-text-secondary);
}

.match-score {
  font-size: 32px;
  font-weight: 700;
}

.match-details {
  display: flex;
  gap: 20px;
  margin-bottom: 16px;
}

.detail-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.detail-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  width: 40px;
}

.detail-value {
  font-size: 14px;
  color: var(--color-text-primary);
}

.match-tag {
  font-size: 12px;
  padding: 2px 8px;
  background-color: rgba(0, 212, 170, 0.15);
  color: var(--color-accent);
  border-radius: 4px;
}

.mismatch-tag {
  font-size: 12px;
  padding: 2px 8px;
  background-color: rgba(239, 68, 68, 0.15);
  color: #ef4444;
  border-radius: 4px;
}

.skills-section {
  margin-bottom: 16px;
}

.skills-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.skills-header span {
  font-size: 13px;
  color: var(--color-text-secondary);
}

.skill-match-rate {
  color: var(--color-accent);
  font-weight: 600;
}

.skills-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.skill-match {
  font-size: 12px;
  padding: 4px 10px;
  background-color: rgba(0, 212, 170, 0.15);
  color: var(--color-accent);
  border-radius: 4px;
}

.skill-mismatch {
  font-size: 12px;
  padding: 4px 10px;
  background-color: rgba(156, 163, 175, 0.15);
  color: var(--color-text-secondary);
  border-radius: 4px;
}

.match-analysis {
  padding: 12px;
  background-color: rgba(59, 130, 246, 0.08);
  border-radius: 8px;
  margin-bottom: 16px;
}

.match-analysis h4 {
  font-size: 13px;
  font-weight: 600;
  color: var(--color-accent-blue);
  margin: 0 0 8px 0;
}

.match-analysis p {
  font-size: 13px;
  color: var(--color-text-secondary);
  line-height: 1.5;
  margin: 0;
}

.card-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px;
}

.empty-icon {
  color: var(--color-text-secondary);
  margin-bottom: 16px;
}

.empty-state h3 {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0 0 8px 0;
}

.empty-state p {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin: 0;
}

.resume-detail {
  padding: 10px 0;
}

.resume-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--color-border);
}

.resume-header h3 {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0;
}

.resume-position {
  font-size: 14px;
  color: var(--color-accent);
}

.resume-section {
  margin-bottom: 20px;
}

.resume-section h4 {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 0 0 12px 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-label {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.info-value {
  font-size: 14px;
  color: var(--color-text-primary);
}

.skills-badge {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.skill-badge {
  font-size: 12px;
  padding: 4px 12px;
  background-color: rgba(0, 212, 170, 0.15);
  color: var(--color-accent);
  border-radius: 4px;
}

.experience-item,
.project-item {
  padding: 12px;
  background-color: var(--color-bg-card);
  border-radius: 8px;
  margin-bottom: 10px;
}

.experience-item:last-child,
.project-item:last-child {
  margin-bottom: 0;
}

.exp-header,
.project-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}

.exp-company,
.project-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.exp-date,
.project-date {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.exp-position,
.project-role {
  font-size: 13px;
  color: var(--color-accent);
  margin-bottom: 8px;
}

.exp-desc,
.project-desc {
  font-size: 13px;
  color: var(--color-text-secondary);
  line-height: 1.5;
}

.project-skills {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-top: 8px;
}
</style>