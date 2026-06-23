import http from './http'

export const commentApi = {
  list: (answerId: number) => http.get(`/answers/${answerId}/comments`),
  create: (answerId: number, data: { content: string; parentId?: number }) =>
    http.post(`/answers/${answerId}/comments`, data),
  delete: (id: number) => http.delete(`/comments/${id}`),
}

export const adminCommentApi = {
  list: (params?: any) => http.get('/admin/comments', { params }),
  delete: (id: number) => http.delete(`/admin/comments/${id}`),
}
