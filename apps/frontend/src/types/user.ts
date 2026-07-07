export interface User {
  id: number;
  username: string;
  password?: string;
  nickname?: string;
  avatar?: string;
  bio?: string;
  role: 'ADMIN' | 'USER' | string;
  status: 'active' | 'banned' | string;
  createdAt?: string;
  updatedAt?: string;
}

export interface UserQuery {
  username?: string;
  nickname?: string;
  status?: string;
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
