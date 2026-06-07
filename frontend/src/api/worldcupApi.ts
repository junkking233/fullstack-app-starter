import http from '@/api/http';
import type {
  City,
  CityDetail,
  DataMaintenance,
  Favorite,
  Match,
  MatchComment,
  PageResult,
  PublicSummary,
  Stadium,
  StadiumDetail,
  Standing,
  Team,
} from '@/types/worldcup';

export const teamApi = {
  list(params: Record<string, unknown>): Promise<PageResult<Team>> {
    return http.get('/teams', { params });
  },
  get(id: number): Promise<Team> {
    return http.get(`/teams/${id}`);
  },
  create(data: Partial<Team>): Promise<Team> {
    return http.post('/teams', data);
  },
  update(id: number, data: Partial<Team>): Promise<Team> {
    return http.put(`/teams/${id}`, data);
  },
  delete(id: number): Promise<void> {
    return http.delete(`/teams/${id}`);
  },
};

export const cityApi = {
  list(params?: Record<string, unknown>): Promise<City[]> {
    return http.get('/cities', { params });
  },
  get(id: number): Promise<CityDetail> {
    return http.get(`/cities/${id}`);
  },
};

export const stadiumApi = {
  list(params?: Record<string, unknown>): Promise<Stadium[]> {
    return http.get('/stadiums', { params });
  },
  get(id: number): Promise<StadiumDetail> {
    return http.get(`/stadiums/${id}`);
  },
};

export const matchApi = {
  list(params: Record<string, unknown>): Promise<PageResult<Match>> {
    return http.get('/matches', { params });
  },
  get(id: number): Promise<Match> {
    return http.get(`/matches/${id}`);
  },
  create(data: Partial<Match>): Promise<Match> {
    return http.post('/matches', data);
  },
  update(id: number, data: Partial<Match>): Promise<Match> {
    return http.put(`/matches/${id}`, data);
  },
  delete(id: number): Promise<void> {
    return http.delete(`/matches/${id}`);
  },
};

export const standingApi = {
  list(params?: Record<string, unknown>): Promise<Standing[]> {
    return http.get('/standings', { params });
  },
  recalculate(): Promise<Standing[]> {
    return http.post('/standings/recalculate');
  },
  update(id: number, data: Partial<Standing>): Promise<Standing> {
    return http.put(`/standings/${id}`, data);
  },
};

export const bracketApi = {
  list(): Promise<Match[]> {
    return http.get('/bracket');
  },
  update(id: number, data: Partial<Match>): Promise<Match> {
    return http.put(`/bracket/matches/${id}`, data);
  },
};

export const commentApi = {
  byMatch(matchId: number): Promise<MatchComment[]> {
    return http.get(`/comments/match/${matchId}`);
  },
  my(): Promise<MatchComment[]> {
    return http.get('/comments/my');
  },
  create(data: { matchId: number; content: string }): Promise<MatchComment> {
    return http.post('/comments', data);
  },
  list(params: Record<string, unknown>): Promise<PageResult<MatchComment>> {
    return http.get('/comments', { params });
  },
  review(id: number, auditStatus: 'APPROVED' | 'REJECTED'): Promise<MatchComment> {
    return http.put(`/comments/${id}/review`, { auditStatus });
  },
  delete(id: number): Promise<void> {
    return http.delete(`/comments/${id}`);
  },
};

export const favoriteApi = {
  my(): Promise<Favorite[]> {
    return http.get('/favorites/my');
  },
  status(objectType: 'TEAM' | 'MATCH', objectId: number): Promise<{ favorited: boolean }> {
    return http.get('/favorites/status', { params: { objectType, objectId } });
  },
  create(objectType: 'TEAM' | 'MATCH', objectId: number): Promise<Favorite> {
    return http.post('/favorites', { objectType, objectId });
  },
  delete(objectType: 'TEAM' | 'MATCH', objectId: number): Promise<void> {
    return http.delete('/favorites', { params: { objectType, objectId } });
  },
};

export const publicStatsApi = {
  summary(): Promise<PublicSummary> {
    return http.get('/charts/public-summary');
  },
};

export const dataMaintenanceApi = {
  list(params: Record<string, unknown>): Promise<PageResult<DataMaintenance>> {
    return http.get('/data-maintenance', { params });
  },
  create(data: Partial<DataMaintenance>): Promise<DataMaintenance> {
    return http.post('/data-maintenance', data);
  },
};
