import http from '@/api/http';

export interface ChartSeries {
  name: string;
  data: number[];
  color?: string;
}

export interface LineChartData {
  xData: string[];
  series: ChartSeries[];
}

export interface PieChartItem {
  name: string;
  value: number;
}

export const chartApi = {
  dashboardStats(): Promise<{
    totalUsers: number;
    totalTeams: number;
    totalMatches: number;
    totalCities: number;
    pendingComments: number;
    favorites: number;
  }> {
    return http.get('/charts/dashboard-stats');
  },
  matchStageDistribution(): Promise<PieChartItem[]> {
    return http.get('/charts/match-stage-distribution');
  },
  cityMatchCount(): Promise<PieChartItem[]> {
    return http.get('/charts/city-match-count');
  },
  groupPoints(): Promise<LineChartData> {
    return http.get('/charts/group-points');
  },
  topFavoriteTeams(): Promise<PieChartItem[]> {
    return http.get('/charts/top-favorite-teams');
  },
  topFavoriteMatches(): Promise<PieChartItem[]> {
    return http.get('/charts/top-favorite-matches');
  },
};
