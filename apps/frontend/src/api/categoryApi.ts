import http from './http'

export const categoryApi = {
  list: () => http.get('/categories'),
  getById: (id: number) => http.get(`/categories/${id}`),
  create: (data: { name: string; parentId?: number; sortOrder?: number }) => http.post('/admin/categories', data),
  update: (id: number, data: { name: string; sortOrder?: number }) => http.put(`/admin/categories/${id}`, data),
  delete: (id: number) => http.delete(`/admin/categories/${id}`)
}
