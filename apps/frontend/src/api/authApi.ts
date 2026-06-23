import http from '@/api/http';
import type { AuthUser } from '@/utils/auth';

export interface LoginResponse {
  token: string;
  user: AuthUser;
  expiresAt: number;
}

export const authApi = {
  login(data: { username: string; password: string }): Promise<LoginResponse> {
    return http.post<never, LoginResponse>('/auth/login', data);
  },

  register(data: { username: string; password: string; nickname?: string }): Promise<void> {
    return http.post<never, void>('/auth/register', data);
  },

  me(): Promise<AuthUser> {
    return http.get<never, AuthUser>('/auth/me');
  },

  changePassword(data: { oldPassword: string; newPassword: string }): Promise<void> {
    return http.put<never, void>('/auth/password', data);
  },

  logout(): Promise<void> {
    return http.post('/auth/logout');
  },
};
