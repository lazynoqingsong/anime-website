<template>
  <div class="page-container">
    <div class="scroll-container">
      <div class="content-wrapper">
        <div class="ranking-container">
          <el-tabs v-model="activeTab" class="ranking-tabs" @tab-change="handleTabChange">
            <el-tab-pane label="周榜" name="weekly">
              <div class="ranking-list-container">
                <div class="ranking-header">
                  <h2>周榜 TOP20</h2>
                  <span class="ranking-update-time">{{ updateTimeText }}</span>
                </div>
                <div class="ranking-list" v-loading="loading.weekly">
                  <template v-if="rankings.weekly && rankings.weekly.length > 0">
                    <div 
                      v-for="(anime, index) in rankings.weekly" 
                      :key="anime.id" 
                      class="ranking-item"
                      :class="{'top-1': index === 0, 'top-2': index === 1, 'top-3': index === 2, 'watched': anime.isWatched}"
                      @click="goToDetail(anime.id)"
                    >
                      <div class="rank-number">{{ index + 1 }}</div>
                      <div class="anime-cover">
                        <img :src="getCoverImage(anime.coverImage)" :alt="anime.title" @error="handleImageError">
                      </div>
                      <div class="anime-info">
                        <div class="anime-title">{{ anime.title }}</div>
                        <div class="anime-heat">
                          热度: {{ anime.heatScore.toFixed(1) }}
                        </div>
                      </div>
                    </div>
                  </template>
                  <el-empty v-else description="暂无排名数据" />
                </div>
              </div>
            </el-tab-pane>

            <el-tab-pane label="月榜" name="monthly">
              <div class="ranking-list-container">
                <div class="ranking-header">
                  <h2>月榜 TOP20</h2>
                  <span class="ranking-update-time">{{ updateTimeText }}</span>
                </div>
                <div class="ranking-list" v-loading="loading.monthly">
                  <template v-if="rankings.monthly && rankings.monthly.length > 0">
                    <div 
                      v-for="(anime, index) in rankings.monthly" 
                      :key="anime.id" 
                      class="ranking-item"
                      :class="{'top-1': index === 0, 'top-2': index === 1, 'top-3': index === 2, 'watched': anime.isWatched}"
                      @click="goToDetail(anime.id)"
                    >
                      <div class="rank-number">{{ index + 1 }}</div>
                      <div class="anime-cover">
                        <img :src="getCoverImage(anime.coverImage)" :alt="anime.title" @error="handleImageError">
                      </div>
                      <div class="anime-info">
                        <div class="anime-title">{{ anime.title }}</div>
                        <div class="anime-heat">
                          热度: {{ anime.heatScore.toFixed(1) }}
                        </div>
                      </div>
                    </div>
                  </template>
                  <el-empty v-else description="暂无排名数据" />
                </div>
              </div>
            </el-tab-pane>

            <el-tab-pane label="总榜" name="total">
              <div class="ranking-list-container">
                <div class="ranking-header">
                  <h2>总榜 TOP20</h2>
                  <span class="ranking-update-time">{{ updateTimeText }}</span>
                </div>
                <div class="ranking-list" v-loading="loading.total">
                  <template v-if="rankings.total && rankings.total.length > 0">
                    <div 
                      v-for="(anime, index) in rankings.total" 
                      :key="anime.id" 
                      class="ranking-item"
                      :class="{'top-1': index === 0, 'top-2': index === 1, 'top-3': index === 2, 'watched': anime.isWatched}"
                      @click="goToDetail(anime.id)"
                    >
                      <div class="rank-number">{{ index + 1 }}</div>
                      <div class="anime-cover">
                        <img :src="getCoverImage(anime.coverImage)" :alt="anime.title" @error="handleImageError">
                      </div>
                      <div class="anime-info">
                        <div class="anime-title">{{ anime.title }}</div>
                        <div class="anime-heat">
                          热度: {{ anime.heatScore.toFixed(1) }}
                        </div>
                      </div>
                    </div>
                  </template>
                  <el-empty v-else description="暂无排名数据" />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import axios from 'axios'
import { formatDate } from '@/utils/format'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('weekly')
const updateTime = ref(new Date())

const rankings = reactive({
  weekly: [],
  monthly: [],
  total: []
})

const loading = reactive({
  weekly: false,
  monthly: false,
  total: false
})

// 格式化更新时间
const updateTimeText = computed(() => {
  return `更新时间: ${formatDate(updateTime.value)}`
})

// 获取封面图片
const getCoverImage = (path) => {
  if (!path) return 'http://localhost:8080/images/default.jpg'
  return `http://localhost:8080/images/${path}`
}

// 处理图片加载失败
const handleImageError = (e) => {
  e.target.src = 'http://localhost:8080/images/default.jpg'
}

// 跳转到详情页
const goToDetail = (animeId) => {
  router.push(`/anime/${animeId}`)
}

// 获取排行榜数据
const fetchRankings = async (type) => {
  loading[type] = true
  try {
    const response = await axios.get(`http://localhost:8080/anime/ranking/${type}`, {
      params: {
        userId: userStore.userId || 0
      }
    })
    
    if (response.data.code === 200) {
      rankings[type] = response.data.data
      updateTime.value = new Date()
    }
  } catch (error) {
    console.error(`获取${type}排行榜失败:`, error)
  } finally {
    loading[type] = false
  }
}

// 监听标签页变化
const handleTabChange = (tab) => {
  if (tab === 'weekly' && rankings.weekly.length === 0) {
    fetchRankings('weekly')
  } else if (tab === 'monthly' && rankings.monthly.length === 0) {
    fetchRankings('monthly')
  } else if (tab === 'total' && rankings.total.length === 0) {
    fetchRankings('total')
  }
}

onMounted(() => {
  // 加载当前标签页的数据
  handleTabChange(activeTab.value)
})
</script>

<style scoped>
.ranking-container {
  max-width: 1200px;
  margin: 0 auto;
  background-color: #2d2d2d;
  border-radius: 8px;
  padding: 20px;
}

.ranking-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.ranking-header h2 {
  color: #fff;
  margin: 0;
}

.ranking-update-time {
  color: #aaa;
  font-size: 14px;
}

.ranking-list {
  min-height: 500px;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #3a3a3a;
  transition: all 0.3s ease;
}

.ranking-item:hover {
  background-color: #363636;
}

.rank-number {
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background-color: #444;
  color: #fff;
  font-weight: bold;
  margin-right: 15px;
}

.top-1 .rank-number {
  background-color: #FFD700; /* 金色 */
  color: #333;
}

.top-2 .rank-number {
  background-color: #C0C0C0; /* 银色 */
  color: #333;
}

.top-3 .rank-number {
  background-color: #CD7F32; /* 铜色 */
  color: #333;
}

.anime-cover {
  width: 60px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.anime-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.anime-info {
  flex: 1;
}

.anime-title {
  color: #fff;
  font-size: 16px;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.anime-heat {
  color: #ff9800;
  font-size: 14px;
}

.watched {
  background-color: rgba(64, 158, 255, 0.1);
}

.watched::after {
  content: '看过';
  display: inline-block;
  background-color: #409EFF;
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
  margin-left: 10px;
}

:deep(.el-tabs__item) {
  color: #fff;
}

:deep(.el-tabs__item.is-active) {
  color: #409EFF;
}

:deep(.el-tabs__active-bar) {
  background-color: #409EFF;
}
</style>