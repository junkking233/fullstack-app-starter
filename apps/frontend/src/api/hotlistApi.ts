import http from './http'

export const hotlistApi = {
  list: (params?: { period?: string }) => http.get('/hotlist', { params }),
}
