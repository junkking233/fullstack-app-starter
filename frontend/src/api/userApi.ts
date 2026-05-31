import axios from 'axios';
import type { User, UserQuery, PageResult } from '@/types/user';

interface ApiResult<T> {
  code: number;
  message: string;
  data: T;
  timestamp: string;
}

const http = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

http.interceptors.response.use(
  (res) => {
    const body = res.data as ApiResult<unknown>;
    if (body && typeof body === 'object' && 'code' in body) {
      if (body.code === 200) {
        return body.data;
      }
      return Promise.reject(new Error(body.message || '请求失败'));
    }
    return res.data;
  },
  (err) => {
    return Promise.reject(err);
  },
);

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
