import http from './http'

export const favoriteApi = {
  toggle: (questionId: number) => http.post('/favorites/toggle', { questionId }),
  check: (questionId: number) => http.get('/favorites/check', { params: { questionId } }),
  my: (params?: any) => http.get('/favorites/my', { params }),
}
