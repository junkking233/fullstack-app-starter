import http from '@/api/http';
import type { User, UserQuery, PageResult } from '@/types/user';

export const userApi = {
  list(params: UserQuery): Promise<PageResult<User>> {
    return http.get<never, PageResult<User>>('/users', { params });
  },

  getById(id: number): Promise<User> {
    return http.get<never, User>(`/users/${id}`);
  },

  create(data: Partial<User>): Promise<User> {
    return http.post<never, User>('/users', data);
  },

  update(id: number, data: Partial<User>): Promise<User> {
    return http.put<never, User>(`/users/${id}`, data);
  },

  delete(id: number): Promise<void> {
    return http.delete<never, void>(`/users/${id}`);
  },
};
