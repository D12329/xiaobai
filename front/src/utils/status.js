// 统一状态字典工具
// 避免在每个页面重复定义 getStatusText/getStatusType

export const CANDIDATE_STATUS = {
  0: { text: '待筛选', type: 'info' },
  1: { text: '待筛选', type: 'info' },
  2: { text: '筛选中', type: 'warning' },
  3: { text: '面试中', type: 'primary' },
  4: { text: '已录用', type: 'success' },
  5: { text: '已入职', type: 'success' },
  6: { text: '已拒绝', type: 'danger' }
}

export const INTERVIEW_STATUS = {
  pending: { text: '待面试', type: 'info' },
  scheduled: { text: '待面试', type: 'info' },
  completed: { text: '已完成', type: 'success' },
  cancelled: { text: '已取消', type: 'danger' }
}

export const OFFER_STATUS = {
  draft: { text: '草稿', type: 'info' },
  pending: { text: '待确认', type: 'info' },
  sent: { text: '待确认', type: 'info' },
  accepted: { text: '已接受', type: 'success' },
  rejected: { text: '已拒绝', type: 'danger' },
  cancelled: { text: '已取消', type: 'warning' }
}

export const CANDIDATE_STATUS_OPTIONS = [
  { label: '待筛选', value: 1 },
  { label: '筛选中', value: 2 },
  { label: '面试中', value: 3 },
  { label: '已录用', value: 4 },
  { label: '已入职', value: 5 },
  { label: '已拒绝', value: 6 }
]

export const INTERVIEW_STATUS_OPTIONS = [
  { label: '待面试', value: 'pending' },
  { label: '已完成', value: 'completed' },
  { label: '已取消', value: 'cancelled' }
]

export const OFFER_STATUS_OPTIONS = [
  { label: '草稿', value: 'draft' },
  { label: '待确认', value: 'pending' },
  { label: '已接受', value: 'accepted' },
  { label: '已拒绝', value: 'rejected' }
]

export function getStatusText(status, dict) {
  return (dict[status] && dict[status].text) || '未知'
}

export function getStatusType(status, dict) {
  return (dict[status] && dict[status].type) || 'info'
}
