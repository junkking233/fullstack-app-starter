<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { UserFilled, Edit } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { authApi } from '@/api/authApi';
import { achievementApi } from '@/api/achievementApi';
import { clearAuthState } from '@/utils/auth';

interface ProfileInfo {
  id: number;
  username: string;
  nickname: string;
  role: string;
  status: string;
  exp?: number;
  level?: number;
  bio?: string;
  avatar?: string;
  questionCount?: number;
  answerCount?: number;
}

const router = useRouter();
const profile = ref<ProfileInfo | null>(null);
const loading = ref(false);
const editMode = ref(false);
const editingNickname = ref(false);
const editingBio = ref(false);

const nicknameForm = ref({ nickname: '' });
const bioForm = ref({ bio: '' });
const passwordForm = ref({ oldPassword: '', newPassword: '' });

const activeTab = ref('questions');
const achievements = ref<any[]>([]);

const levelThresholds = [0, 100, 300, 600, 1000, 2000, 4000, 7000, 12000, 20000];
const levelNames: Record<number, string> = {
  1: '初学者', 2: '入门', 3: '进阶', 4: '熟练', 5: '精通',
  6: '专家', 7: '大师', 8: '宗师', 9: '传说', 10: '至尊'
};

const expProgress = computed(() => {
  const exp = profile.value?.exp ?? 0;
  const level = profile.value?.level ?? 1;
  if (level >= 10) return 100;
  const current = levelThresholds[level - 1] || 0;
  const next = levelThresholds[level] || 20000;
  return Math.min(100, Math.round(((exp - current) / (next - current)) * 100));
});

const expToNext = computed(() => {
  const exp = profile.value?.exp ?? 0;
  const level = profile.value?.level ?? 1;
  if (level >= 10) return 0;
  return (levelThresholds[level] || 20000) - exp;
});

async function loadAchievements() {
  try {
    const data: any = await achievementApi.my();
    achievements.value = Array.isArray(data) ? data : [];
  } catch { achievements.value = []; }
}

async function loadProfile() {
  loading.value = true;
  try {
    const data = await authApi.me() as unknown as ProfileInfo;
    profile.value = data;
    nicknameForm.value.nickname = data.nickname || '';
    bioForm.value.bio = data.bio || '';
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载个人信息失败');
  } finally {
    loading.value = false;
  }
}

function toggleEditMode() {
  editMode.value = !editMode.value;
}

async function saveNickname() {
  if (!nicknameForm.value.nickname) {
    ElMessage.warning('昵称不能为空');
    return;
  }
  try {
    ElMessage.success('昵称已更新');
    editingNickname.value = false;
    if (profile.value) {
      profile.value.nickname = nicknameForm.value.nickname;
    }
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '更新失败');
  }
}

async function saveBio() {
  try {
    ElMessage.success('个人简介已更新');
    editingBio.value = false;
    if (profile.value) {
      profile.value.bio = bioForm.value.bio;
    }
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '更新失败');
  }
}

async function changePassword() {
  if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword) {
    ElMessage.warning('请填写完整密码信息');
    return;
  }
  try {
    await authApi.changePassword({
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword,
    });
    ElMessage.success('密码修改成功');
    passwordForm.value = { oldPassword: '', newPassword: '' };
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '密码修改失败');
  }
}

function logout() {
  clearAuthState();
  router.push('/login');
}

onMounted(() => {
  loadProfile();
  loadAchievements();
});
</script>

<template>
  <div class="profile-page">
    <!-- Header -->
    <header class="profile-header">
      <div class="header-inner">
        <router-link to="/" class="logo">知问社区</router-link>
        <el-button text type="danger" size="small" @click="logout">退出登录</el-button>
      </div>
    </header>

    <main class="profile-main" v-loading="loading">
      <!-- Profile Card -->
      <div class="profile-card">
        <div class="profile-top">
          <el-avatar :size="72" class="profile-avatar">
            <el-icon :size="36"><UserFilled /></el-icon>
          </el-avatar>
          <div class="profile-info">
            <h2>{{ profile?.nickname || profile?.username }}</h2>
            <p class="profile-username">@{{ profile?.username }}</p>
            <div class="profile-stats-row">
              <span class="stat-item">
                <el-tag :type="(profile?.level ?? 1) >= 5 ? 'warning' : 'info'" size="small">
                  Lv.{{ profile?.level ?? 1 }} {{ levelNames[profile?.level ?? 1] || '' }}
                </el-tag>
              </span>
              <span class="stat-item">{{ profile?.exp ?? 0 }} 经验</span>
            </div>
            <div class="exp-bar">
              <el-progress
                :percentage="expProgress"
                :stroke-width="8"
                :color="(profile?.level ?? 1) >= 7 ? '#e6a23c' : '#409eff'"
              />
              <span v-if="expToNext > 0" class="exp-hint">距下一级还需 {{ expToNext }} 经验</span>
              <span v-else class="exp-hint">已达最高等级</span>
            </div>
            <p v-if="profile?.bio" class="profile-bio">{{ profile.bio }}</p>
          </div>
          <div class="profile-actions" v-if="!editMode">
            <el-button :icon="Edit" size="small" @click="toggleEditMode">编辑资料</el-button>
          </div>
        </div>

        <div class="profile-stats">
          <div class="stat-card" @click="activeTab = 'questions'">
            <div class="stat-num">{{ profile?.questionCount ?? 0 }}</div>
            <div class="stat-label">提问</div>
          </div>
          <div class="stat-card" @click="activeTab = 'answers'">
            <div class="stat-num">{{ profile?.answerCount ?? 0 }}</div>
            <div class="stat-label">回答</div>
          </div>
          <div class="stat-card">
            <div class="stat-num">0</div>
            <div class="stat-label">收藏</div>
          </div>
          <div class="stat-card">
            <div class="stat-num">0</div>
            <div class="stat-label">关注</div>
          </div>
        </div>
      </div>

      <!-- Edit Mode -->
      <div class="edit-section" v-if="editMode">
        <el-card class="edit-card">
          <template #header>
            <div class="edit-card-header">
              <span>编辑资料</span>
              <el-button text size="small" @click="toggleEditMode">取消</el-button>
            </div>
          </template>

          <div class="edit-field">
            <label>昵称</label>
            <template v-if="editingNickname">
              <el-input v-model="nicknameForm.nickname" size="default" style="width: 240px" />
              <el-button type="primary" size="small" @click="saveNickname" style="margin-left: 8px">保存</el-button>
              <el-button size="small" @click="editingNickname = false">取消</el-button>
            </template>
            <template v-else>
              <span class="edit-value">{{ profile?.nickname || '未设置' }}</span>
              <el-button text type="primary" size="small" @click="editingNickname = true">修改</el-button>
            </template>
          </div>

          <div class="edit-field">
            <label>个人简介</label>
            <template v-if="editingBio">
              <el-input
                v-model="bioForm.bio"
                type="textarea"
                :rows="3"
                placeholder="介绍一下你自己"
              />
              <div class="edit-field-actions">
                <el-button type="primary" size="small" @click="saveBio">保存</el-button>
                <el-button size="small" @click="editingBio = false">取消</el-button>
              </div>
            </template>
            <template v-else>
              <span class="edit-value">{{ profile?.bio || '未设置' }}</span>
              <el-button text type="primary" size="small" @click="editingBio = true">修改</el-button>
            </template>
          </div>
        </el-card>

        <el-card class="edit-card">
          <template #header>
            <span>修改密码</span>
          </template>
          <el-form :model="passwordForm" label-width="80px" size="default">
            <el-form-item label="原密码">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password style="width: 240px" />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="passwordForm.newPassword" type="password" show-password style="width: 240px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="changePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>

      <!-- Content Tabs -->
      <el-card class="content-card" v-if="!editMode">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="我的提问" name="questions">
            <div class="tab-placeholder">
              <p>暂无提问记录</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我的回答" name="answers">
            <div class="tab-placeholder">
              <p>暂无回答记录</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我的收藏" name="favorites">
            <div class="tab-placeholder">
              <p>暂无收藏内容</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我的关注" name="follows">
            <div class="tab-placeholder">
              <p>暂无关注</p>
            </div>
          </el-tab-pane>
          <el-tab-pane label="成就徽章" name="achievements">
            <div v-if="achievements.length === 0" class="tab-placeholder">
              <p>暂未解锁任何成就</p>
            </div>
            <div v-else class="badge-wall">
              <div v-for="a in achievements" :key="a.code" class="badge-item">
                <div class="badge-icon">🏅</div>
                <div class="badge-name">{{ a.name }}</div>
                <div class="badge-time">{{ new Date(a.unlockedAt).toLocaleDateString('zh-CN') }}</div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </main>
  </div>
</template>

<style scoped>
.profile-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: var(--c-bg);
}

.profile-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: var(--c-surface);
  border-bottom: 1px solid var(--c-line);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 960px;
  margin: 0 auto;
  padding: 0 24px;
  height: 60px;
}

.logo {
  font-size: 20px;
  font-weight: 800;
  color: var(--c-primary);
  text-decoration: none;
}

.profile-main {
  max-width: 960px;
  width: 100%;
  margin: 0 auto;
  padding: 24px;
}

.profile-card {
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: 28px;
  margin-bottom: 20px;
}

.profile-top {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  margin-bottom: 24px;
}

.profile-avatar {
  background: var(--c-primary-bg);
  color: var(--c-primary);
  flex-shrink: 0;
}

.profile-info h2 {
  margin: 0 0 4px;
  font-size: 22px;
  font-weight: 700;
}

.profile-username {
  margin: 0 0 8px;
  color: var(--c-muted);
  font-size: 14px;
}

.profile-stats-row {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
}

.stat-item {
  color: var(--c-muted);
  font-size: 13px;
}

.profile-bio {
  margin: 8px 0 0;
  color: var(--c-body);
  font-size: 14px;
  line-height: 1.6;
}

.exp-bar {
  margin-top: 8px;
}

.exp-hint {
  font-size: 12px;
  color: var(--c-muted);
  margin-top: 4px;
  display: block;
}

.profile-actions {
  margin-left: auto;
}

.profile-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid var(--c-line);
}

.stat-card {
  text-align: center;
  cursor: pointer;
  padding: 8px;
  border-radius: var(--radius-sm);
  transition: background var(--transition-fast);
}

.stat-card:hover {
  background: var(--c-bg);
}

.stat-num {
  font-size: 22px;
  font-weight: 700;
  color: var(--c-primary);
}

.stat-label {
  margin-top: 4px;
  color: var(--c-muted);
  font-size: 13px;
}

.edit-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 20px;
}

.edit-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.edit-field {
  padding: 12px 0;
  border-bottom: 1px solid var(--c-line-light);
}

.edit-field:last-child {
  border-bottom: none;
}

.edit-field label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--c-ink-light);
}

.edit-value {
  color: var(--c-body);
  font-size: 14px;
}

.edit-field-actions {
  margin-top: 8px;
}

.tab-placeholder {
  text-align: center;
  padding: 48px 0;
  color: var(--c-muted);
  font-size: 14px;
}

.badge-wall {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 16px;
  padding: 8px 0;
}

.badge-item {
  text-align: center;
  padding: 16px 12px;
  background: var(--c-bg);
  border-radius: var(--radius-md);
  border: 1px solid var(--c-line);
  transition: all var(--transition-fast);
}

.badge-item:hover {
  border-color: var(--c-primary);
  box-shadow: var(--shadow-sm);
}

.badge-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.badge-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--c-ink-light);
  margin-bottom: 4px;
}

.badge-time {
  font-size: 11px;
  color: var(--c-muted);
}

@media (max-width: 768px) {
  .profile-top {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .profile-actions {
    margin-left: 0;
  }

  .profile-stats {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
