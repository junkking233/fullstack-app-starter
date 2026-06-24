interface AuthState {
  token: string
  user: {
    id: number
    username: string
    role: string
    nickname: string
  } | null
}

export function getAuthState(): AuthState | null {
  const token = uni.getStorageSync('token')
  const userStr = uni.getStorageSync('user')
  if (!token) return null
  let user = null
  try {
    user = userStr ? JSON.parse(userStr) : null
  } catch {
    user = null
  }
  return { token, user }
}

export function setAuthState(token: string, user: AuthState['user']) {
  uni.setStorageSync('token', token)
  uni.setStorageSync('user', JSON.stringify(user))
}

export function clearAuthState() {
  uni.removeStorageSync('token')
  uni.removeStorageSync('user')
}

export function isLoggedIn(): boolean {
  return !!uni.getStorageSync('token')
}
