export interface AuthUser {
  id: number;
  username: string;
  nickname: string;
  avatar: string;
  bio: string;
  role: string;
  status: string;
  createdAt: string;
}

interface AuthState {
  token: string;
  user: AuthUser;
  expiresAt: number;
}

const AUTH_STATE_KEY = 'fullstack_app_auth';

export function setAuthState(token: string, user: AuthUser, expiresAt: number) {
  const state: AuthState = { token, user, expiresAt };
  localStorage.setItem(AUTH_STATE_KEY, JSON.stringify(state));
}

export function clearAuthState() {
  localStorage.removeItem(AUTH_STATE_KEY);
}

export function getAuthState(): AuthState | null {
  const raw = localStorage.getItem(AUTH_STATE_KEY);
  if (!raw) {
    return null;
  }

  try {
    const state = JSON.parse(raw) as AuthState;
    if (!state.token || !state.user || state.expiresAt <= Date.now()) {
      clearAuthState();
      return null;
    }
    return state;
  } catch {
    clearAuthState();
    return null;
  }
}

export function getAuthToken() {
  return getAuthState()?.token ?? '';
}

export function getCurrentUser() {
  return getAuthState()?.user ?? null;
}

export function defaultPathForRole(role: string) {
  if (role === 'ADMIN') {
    return '/admin/users';
  }
  return '/portal/home';
}

export function hasRoutePermission(path: string, role: string) {
  if (path.startsWith('/admin')) {
    return role === 'ADMIN';
  }
  return true;
}
