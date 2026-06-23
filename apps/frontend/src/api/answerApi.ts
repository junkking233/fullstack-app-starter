import http from './http'

export const answerApi = {
  create: (questionId: number, data: { content: string }) =>
    http.post(`/questions/${questionId}/answers`, data),
  list: (questionId: number, params?: any) =>
    http.get(`/questions/${questionId}/answers`, { params }),
  update: (id: number, data: { content: string }) =>
    http.put(`/answers/${id}`, data),
  delete: (id: number) => http.delete(`/answers/${id}`),
  accept: (id: number) => http.post(`/answers/${id}/accept`),
}
