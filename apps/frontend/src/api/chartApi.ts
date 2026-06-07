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
  userGrowth(): Promise<LineChartData> {
    return http.get('/charts/user-growth');
  },
  roleDistribution(): Promise<PieChartItem[]> {
    return http.get('/charts/role-distribution');
  },
  taskTrend(): Promise<LineChartData> {
    return http.get('/charts/task-trend');
  },
  serviceDistribution(): Promise<PieChartItem[]> {
    return http.get('/charts/service-distribution');
  },
  satisfactionTrend(): Promise<LineChartData> {
    return http.get('/charts/satisfaction-trend');
  },
  dashboardStats(): Promise<{
    totalUsers: number;
    activeUsers: number;
    apiRequests: number;
    pendingTasks: number;
  }> {
    return http.get('/charts/dashboard-stats');
  },
};
