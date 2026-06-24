import { post } from './http'

interface LoginParams {
  username: string
  password: string
}

interface LoginResult {
  token: string
  user: {
    id: number
    username: string
    role: string
    nickname: string
  }
}

export function login(data: LoginParams) {
  return post<LoginResult>('/auth/login', data)
}
