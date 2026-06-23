import http from './http'

export const followApi = {
  toggle: (followedId: number) => http.post('/follows/toggle', { followedId }),
  check: (followedId: number) => http.get('/follows/check', { params: { followedId } }),
  followers: (userId: number, params?: any) => http.get(`/follows/${userId}/followers`, { params }),
  following: (userId: number, params?: any) => http.get(`/follows/${userId}/following`, { params }),
}
