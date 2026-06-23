import http from './http'

export const notificationApi = {
  list: (params?: any) => http.get('/notifications', { params }),
  unreadCount: () => http.get('/notifications/unread-count'),
  markRead: (id: number) => http.put(`/notifications/${id}/read`),
  markAllRead: () => http.put('/notifications/read-all'),
}
