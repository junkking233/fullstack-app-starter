import http from './http'

export const tagApi = {
  list: () => http.get('/tags'),
  recommend: (keyword: string) => http.get('/tags/recommend', { params: { keyword } }),
  create: (name: string) => http.post('/admin/tags', { name }),
  update: (id: number, name: string) => http.put(`/admin/tags/${id}`, null, { params: { name } }),
  delete: (id: number) => http.delete(`/admin/tags/${id}`)
}
