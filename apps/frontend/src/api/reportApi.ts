import http from './http'

export const reportApi = {
  create: (data: { targetType: string; targetId: number; reason: string; detail?: string }) => http.post('/reports', data),
  adminList: (params?: any) => http.get('/admin/reports', { params }),
  adminHandle: (id: number, data: { action: string; resultNote?: string }) => http.post(`/admin/reports/${id}/handle`, data),
}
