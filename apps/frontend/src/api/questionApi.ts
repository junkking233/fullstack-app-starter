import http from './http'

export const questionApi = {
  create: (data: any) => http.post('/questions', data),
  list: (params?: any) => http.get('/questions', { params }),
  detail: (id: number) => http.get(`/questions/${id}`),
  update: (id: number, data: any) => http.put(`/questions/${id}`, data),
  delete: (id: number) => http.delete(`/questions/${id}`),
  search: (params?: any) => http.get('/questions/search', { params }),
  related: (id: number, limit?: number) => http.get(`/questions/${id}/related`, { params: { limit: limit || 5 } }),
}

export const reviewApi = {
  questionList: (params?: any) => http.get('/admin/review/questions', { params }),
  approveQuestion: (id: number) => http.post(`/admin/review/questions/${id}/approve`),
  rejectQuestion: (id: number, reason: string) =>
    http.post(`/admin/review/questions/${id}/reject`, { reason }),
  answerList: (params?: any) => http.get('/admin/review/answers', { params }),
  approveAnswer: (id: number) => http.post(`/admin/review/answers/${id}/approve`),
  rejectAnswer: (id: number, reason: string) =>
    http.post(`/admin/review/answers/${id}/reject`, { reason }),
}

export const adminQuestionApi = {
  pin: (id: number) => http.put(`/admin/questions/${id}/pin`),
  feature: (id: number) => http.put(`/admin/questions/${id}/feature`),
}
