<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { cityApi, commentApi, favoriteApi, matchApi, stadiumApi, teamApi } from '@/api/worldcupApi';
import type { City, Match, MatchComment, Stadium, Team } from '@/types/worldcup';
import { getCurrentUser } from '@/utils/auth';

const route = useRoute();
const router = useRouter();
const match = ref<Match>();
const teams = ref<Team[]>([]);
const cities = ref<City[]>([]);
const stadiums = ref<Stadium[]>([]);
const comments = ref<MatchComment[]>([]);
const content = ref('');
const favorited = ref(false);

onMounted(load);

async function load() {
  const id = Number(route.params.id);
  const [detail, teamPage, cityList, stadiumList, commentList] = await Promise.all([
    matchApi.get(id),
    teamApi.list({ page: 1, pageSize: 100 }),
    cityApi.list(),
    stadiumApi.list(),
    commentApi.byMatch(id),
  ]);
  match.value = detail;
  teams.value = teamPage.records;
  cities.value = cityList;
  stadiums.value = stadiumList;
  comments.value = commentList;
  if (getCurrentUser()) {
    favorited.value = (await favoriteApi.status('MATCH', id)).favorited;
  }
}
function teamName(id?: number) { return teams.value.find((item) => item.id === id)?.nameCn || '待定'; }
function cityName(id?: number) { return cities.value.find((item) => item.id === id)?.nameCn || '-'; }
function stadiumName(id?: number) { return stadiums.value.find((item) => item.id === id)?.nameCn || '-'; }
async function toggleFavorite() {
  if (!match.value) return;
  if (!getCurrentUser()) {
    ElMessage.warning('请先登录');
    router.push({ path: '/login', query: { redirect: route.fullPath } });
    return;
  }
  if (favorited.value) {
    await favoriteApi.delete('MATCH', match.value.id);
    favorited.value = false;
    ElMessage.success('已取消收藏');
  } else {
    await favoriteApi.create('MATCH', match.value.id);
    favorited.value = true;
    ElMessage.success('收藏成功');
  }
}
async function submitComment() {
  if (!match.value || !content.value.trim()) return;
  if (!getCurrentUser()) {
    ElMessage.warning('请先登录');
    router.push({ path: '/login', query: { redirect: route.fullPath } });
    return;
  }
  await commentApi.create({ matchId: match.value.id, content: content.value.trim() });
  content.value = '';
  ElMessage.success('评论已提交，等待审核');
}
</script>

<template>
  <el-card v-if="match">
    <template #header>
      <div class="header">
        <span>Match {{ match.matchNo }} · {{ match.stage }}</span>
        <el-button :type="favorited ? 'default' : 'primary'" @click="toggleFavorite">
          {{ favorited ? '取消收藏' : '收藏比赛' }}
        </el-button>
      </div>
    </template>
    <div class="score-board">
      <strong>{{ teamName(match.homeTeamId) }}</strong>
      <span>{{ match.homeScore ?? '-' }} : {{ match.awayScore ?? '-' }}</span>
      <strong>{{ teamName(match.awayTeamId) }}</strong>
    </div>
    <el-descriptions :column="2" border>
      <el-descriptions-item label="小组">{{ match.groupName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="状态">{{ match.status }}</el-descriptions-item>
      <el-descriptions-item label="比赛时间">{{ match.matchTime }}</el-descriptions-item>
      <el-descriptions-item label="场馆">{{ cityName(match.cityId) }} · {{ stadiumName(match.stadiumId) }}</el-descriptions-item>
    </el-descriptions>
    <section class="comment-box">
      <h3>比赛评论</h3>
      <el-input v-model="content" type="textarea" :rows="3" placeholder="发表比赛评论，提交后等待管理员审核" />
      <el-button type="primary" class="submit" @click="submitComment">发表评论</el-button>
      <el-divider />
      <div v-for="comment in comments" :key="comment.id" class="comment">{{ comment.content }}</div>
      <el-empty v-if="comments.length === 0" description="暂无已通过评论" />
    </section>
  </el-card>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.score-board {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  gap: 20px;
  align-items: center;
  margin-bottom: 20px;
  padding: 28px;
  text-align: center;
  background: #f1f5f9;
  border-radius: 8px;
}
.score-board strong {
  font-size: 24px;
}
.score-board span {
  font-size: 34px;
  font-weight: 800;
}
.comment-box {
  margin-top: 24px;
}
.submit {
  margin-top: 10px;
}
.comment {
  padding: 12px 0;
  border-bottom: 1px solid #e5e7eb;
}
</style>
