<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { cityApi, stadiumApi, teamApi } from '@/api/worldcupApi';
import type { City, CityDetail, Match, Stadium, Team } from '@/types/worldcup';

const cities = ref<City[]>([]);
const stadiums = ref<Stadium[]>([]);
const details = ref<Record<number, CityDetail>>({});
const teams = ref<Team[]>([]);

onMounted(async () => {
  [cities.value, stadiums.value, teams.value] = await Promise.all([
    cityApi.list(),
    stadiumApi.list(),
    teamApi.list({ page: 1, pageSize: 100 }).then((page) => page.records),
  ]);
  const entries = await Promise.all(cities.value.map(async (city) => [city.id, await cityApi.get(city.id)] as const));
  details.value = Object.fromEntries(entries);
});
function cityStadiums(cityId: number) {
  return stadiums.value.filter((stadium) => stadium.cityId === cityId);
}
function cityMatches(cityId: number): Match[] {
  return details.value[cityId]?.matches || [];
}
function teamName(id?: number) {
  return teams.value.find((team) => team.id === id)?.nameCn || '待定';
}
</script>

<template>
  <el-card>
    <template #header>主办城市与场馆</template>
    <div class="city-grid">
      <el-card v-for="city in cities" :key="city.id" class="city-card">
        <div class="city-head">
          <div>
            <h3>{{ city.nameCn }}</h3>
            <p>{{ city.nameEn }} · {{ city.country }}</p>
          </div>
          <el-tag>{{ details[city.id]?.matchCount || 0 }} 场</el-tag>
        </div>
        <p>{{ city.description }}</p>
        <el-divider />
        <div v-for="stadium in cityStadiums(city.id)" :key="stadium.id" class="stadium">
          <strong>{{ stadium.nameCn }}</strong>
          <span>{{ stadium.nameEn }} · {{ stadium.capacity || '-' }} seats</span>
        </div>
        <el-collapse class="match-collapse">
          <el-collapse-item title="承办比赛" :name="city.id">
            <div v-for="match in cityMatches(city.id)" :key="match.id" class="match-row">
              <strong>Match {{ match.matchNo }}</strong>
              <span>{{ teamName(match.homeTeamId) }} vs {{ teamName(match.awayTeamId) }}</span>
            </div>
            <el-empty v-if="cityMatches(city.id).length === 0" description="暂无比赛" />
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </div>
  </el-card>
</template>

<style scoped>
.city-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
}
.city-card h3 {
  margin: 0 0 4px;
}
.city-card p, .stadium span {
  color: #64748b;
}
.city-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 10px;
}
.stadium {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.match-collapse {
  margin-top: 12px;
}
.match-row {
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding: 6px 0;
}
.match-row span {
  color: #64748b;
  font-size: 13px;
}
</style>
