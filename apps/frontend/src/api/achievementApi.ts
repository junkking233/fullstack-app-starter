import http from './http'

export const achievementApi = {
  my: () => http.get('/achievements/my'),
  leaderboard: (params?: any) => http.get('/achievements/leaderboard', { params }),
  userAchievements: (userId: number) => http.get(`/users/${userId}/achievements`),
}
