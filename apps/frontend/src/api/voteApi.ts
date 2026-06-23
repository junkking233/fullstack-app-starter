import http from './http'

export const voteApi = {
  toggle: (data: { targetType: string; targetId: number }) => http.post('/votes/toggle', data),
  check: (params: { targetType: string; targetId: number }) => http.get('/votes/check', { params }),
}
