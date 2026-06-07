export interface User {
  id: number;
  username: string;
  password?: string;
  email: string;
  phone: string;
  role: 'ADMIN' | 'PARTNER' | 'USER' | string;
  status: number;
  createTime: string;
  updateTime: string;
}

export interface UserQuery {
  username?: string;
  email?: string;
  status?: number;
  page: number;
  pageSize: number;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}
