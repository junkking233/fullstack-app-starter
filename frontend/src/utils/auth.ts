import { computed, ref } from 'vue';

export interface AuthUser {
  id: number;
  username: string;
  email: string;
  role: string;
  status: number;
}

interface AuthState {
  token: string;
  user: AuthUser;
  expiresAt: number;
}

const AUTH_STATE_KEY = 'springboot_vue_starter_auth';

function readStoredAuthState(): AuthState | null {
  const raw = sessionStorage.getItem(AUTH_STATE_KEY) ?? localStorage.getItem(AUTH_STATE_KEY);
  if (!raw) {
    return null;
  }

  try {
    const state = JSON.parse(raw) as AuthState;
    if (!state.token || !state.user || state.expiresAt <= Date.now()) {
      return null;
    }
    return state;
  } catch {
    return null;
  }
}

const authState = ref<AuthState | null>(readStoredAuthState());

function syncStoredAuthState() {
  const storedState = readStoredAuthState();
  authState.value = storedState;
  if (!storedState) {
    localStorage.removeItem(AUTH_STATE_KEY);
    sessionStorage.removeItem(AUTH_STATE_KEY);
  }
  return storedState;
}

export function setAuthState(token: string, user: AuthUser, expiresAt: number, persistent = false) {
  const state: AuthState = { token, user, expiresAt };
  const storage = persistent ? localStorage : sessionStorage;
  const otherStorage = persistent ? sessionStorage : localStorage;
  storage.setItem(AUTH_STATE_KEY, JSON.stringify(state));
  otherStorage.removeItem(AUTH_STATE_KEY);
  authState.value = state;
}

export function clearAuthState() {
  localStorage.removeItem(AUTH_STATE_KEY);
  sessionStorage.removeItem(AUTH_STATE_KEY);
  authState.value = null;
}

export function getAuthState(): AuthState | null {
  return syncStoredAuthState();
}

export function getAuthToken() {
  return getAuthState()?.token ?? '';
}

export function getCurrentUser() {
  return getAuthState()?.user ?? null;
}

export function useCurrentUser() {
  syncStoredAuthState();
  return computed(() => authState.value?.user ?? null);
}

window.addEventListener('storage', (event) => {
  if (event.key === AUTH_STATE_KEY) {
    syncStoredAuthState();
  }
});

export function defaultPathForRole(role: string) {
  if (role === 'ADMIN') {
    return '/admin/dashboard';
  }
  return '/portal/home';
}

export function hasRoutePermission(path: string, role: string) {
  if (path.startsWith('/admin')) {
    return role === 'ADMIN';
  }
  return true;
}
