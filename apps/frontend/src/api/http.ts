import axios, { type AxiosError, type InternalAxiosRequestConfig } from 'axios';
import { clearAuthState, getAuthToken } from '@/utils/auth';

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

http.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = getAuthToken();
  if (token) {
    config.headers.set('Authorization', `Bearer ${token}`);
  }
  return config;
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
  (err: AxiosError<ApiResult<unknown>>) => {
    if (err.response?.status === 401) {
      clearAuthState();
    }
    const message = err.response?.data?.message || err.message || '请求失败';
    return Promise.reject(new Error(message));
  },
);

export default http;
