import http from './http'

export const sensitiveWordApi = {
  list: () => http.get('/admin/sensitive-words'),
  create: (word: string) => http.post('/admin/sensitive-words', { word }),
  updateStatus: (id: number, status: string) => http.put(`/admin/sensitive-words/${id}/status`, null, { params: { status } }),
  delete: (id: number) => http.delete(`/admin/sensitive-words/${id}`)
}
