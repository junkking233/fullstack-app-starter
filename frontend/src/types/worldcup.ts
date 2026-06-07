export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export interface Team {
  id: number;
  nameCn: string;
  nameEn: string;
  country: string;
  confederation: string;
  groupName: string;
  flagUrl?: string;
  description?: string;
  source?: string;
  sourceUpdatedAt?: string;
}

export interface City {
  id: number;
  nameCn: string;
  nameEn: string;
  country: string;
  description?: string;
  imageUrl?: string;
  source?: string;
}

export interface Stadium {
  id: number;
  nameCn: string;
  nameEn: string;
  cityId: number;
  capacity?: number;
  description?: string;
  imageUrl?: string;
  source?: string;
}

export interface Match {
  id: number;
  matchNo: number;
  stage: string;
  groupName?: string;
  homeTeamId?: number;
  awayTeamId?: number;
  matchTime?: string;
  cityId?: number;
  stadiumId?: number;
  homeScore?: number;
  awayScore?: number;
  status: string;
  winnerTeamId?: number;
  source?: string;
}

export interface Standing {
  id: number;
  groupName: string;
  teamId: number;
  played: number;
  wins: number;
  draws: number;
  losses: number;
  goalsFor: number;
  goalsAgainst: number;
  goalDiff: number;
  points: number;
  rankNo?: number;
  qualifyStatus: string;
}

export interface MatchComment {
  id: number;
  matchId: number;
  userId: number;
  content: string;
  auditStatus: string;
  createTime: string;
}

export interface Favorite {
  id: number;
  userId: number;
  objectType: 'TEAM' | 'MATCH';
  objectId: number;
  createTime: string;
}

export interface PublicSummary {
  totalTeams: number;
  totalMatches: number;
  totalCities: number;
  finishedMatches: number;
  upcomingMatches: number;
  topFavoriteTeams: { name: string; value: number }[];
  topFavoriteMatches: { name: string; value: number }[];
}

export interface CityDetail {
  city: City;
  stadiums: Stadium[];
  matches: Match[];
  matchCount: number;
}

export interface StadiumDetail {
  stadium: Stadium;
  matches: Match[];
  matchCount: number;
}

export interface DataMaintenance {
  id: number;
  dataType: string;
  source?: string;
  operatorId?: number;
  actionType: string;
  remark?: string;
  createTime?: string;
}
