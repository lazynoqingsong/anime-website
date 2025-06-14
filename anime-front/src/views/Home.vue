<template>
  <div class="page-container">
    <div class="scroll-container">
      <el-row :gutter="20" class="content-wrapper">
        <el-col :span="13" :offset="3">
          <div class="content-box">
            <div class="section-header">
              <h4>最近更新</h4>
              <el-button type="info" plain size="small" @click="goToWeekly">更多>></el-button>
            </div>
            <div class="anime-grid">
              <router-link 
                v-for="anime in limitedAnimeList" 
                :key="anime.id"
                :to="`/anime/${anime.id}`"
                class="anime-card"
              >
                <div class="anime-cover">
                  <img 
                    :src="getCoverImage(anime.cover_image)" 
                    :alt="anime.title"
                    @error="handleImageError"
                  >
                  <div class="episode-tag">{{ anime.latest_episode || '暂无更新' }}</div>
                </div>
                <h4 class="anime-title">{{ anime.title }}</h4>
              </router-link>
            </div>
          </div>
          <DayRecommend v-if="userStore.isLoggedIn" />
        </el-col>
        <el-col :span="5">
          <div class="schedule-box">
            <h4>本周放送列表</h4>
            <el-tabs v-model="activeDay" class="weekly-tabs">
              <el-tab-pane 
                v-for="day in weekDays" 
                :key="day" 
                :label="day" 
                :name="day"
              >
                <div class="schedule-list">
                  <div 
                    v-for="anime in weeklySchedule[day]" 
                    :key="anime.id"
                    class="schedule-item"
                  >
                    <span class="time">{{ formatTime(anime.broadcast_time) }}</span>
                    <router-link 
                      :to="`/anime/${anime.id}`" 
                      class="title"
                    >
                      {{ anime.title }}
                    </router-link>
                   
                    <span v-if="anime.status === '完结'" class="status">{{ anime.status }}</span>
                    <span v-else class="episode">{{ anime.latest_episode }}</span>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
          <div class="recent-box">
            <h4 style="color: rgb(255, 255, 255);">最近更新</h4>
            <ul class="recent-list">
              <li v-for="anime in latestUpdates" :key="anime.id">
                <router-link :to="`/anime/${anime.id}`" class="recent-title">{{ anime.title }}</router-link>
                <span class="date">{{ formatDate(anime.update_time) }}</span>
              </li>
            </ul>
          </div>
        </el-col>
      </el-row>
    </div>
    <BackToTop />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import BackToTop from '@/components/BackToTop.vue'
import { formatDate } from '@/utils/format'
import DayRecommend from '@/components/DayRecommend.vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 动漫列表数据
const animeList = ref([])

// 获取动漫数据
const fetchAnimes = async () => {
  try {
    const response = await axios.get('http://localhost:8080/anime/recent')
    if (response.data.code === 200) {
      animeList.value = response.data.data
      console.log('获取到的动漫数据:', response.data.data)
    }
  } catch (error) {
    console.error('获取动漫数据失败:', error)
  }
}

const updateList = ref([])

// 周播放数据
const weekDays = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
const activeDay = ref('周一')
const weeklySchedule = ref({})

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  // 处理类似 "11:00:00" 格式的时间
  return time.substring(0, 5)  // 只显示小时和分钟
}

// 获取周播放表数据
const fetchWeeklySchedule = async () => {
  try {
    const response = await axios.get('http://localhost:8080/anime/weekly')
    if (response.data.code === 200) {
      weeklySchedule.value = response.data.data
      console.log('获取到的周播放表数据:', response.data.data)
    } else {
      console.error('获取周播放表失败:', response.data.msg)
      weeklySchedule.value = {} // 设置空对象作为默认值
    }
  } catch (error) {
    console.error('获取周播放表失败:', error)
    weeklySchedule.value = {} // 设置空对象作为默认值
  }
}

const getCoverImage = (path) => {
  if (!path) {
    return 'http://localhost:8080/images/avatar.jpg'
  }
  // 直接返回完整的URL
  return `http://localhost:8080/images/${path}`
}

const handleImageError = (e) => {
  e.target.src = 'http://localhost:8080/images/avatar.jpg'
  console.log('图片加载失败，使用默认图片')
}

// 限制显示数量的计算属性
const limitedAnimeList = computed(() => {
  return animeList.value.slice(0, 10)
})

// 添加跳转到周更新页面的方法
const goToWeekly = () => {
  router.push('/weekly')
}

// 最新更新列表数据
const latestUpdates = ref([])

// 获取最新更新数据
const fetchLatestUpdates = async () => {
  try {
    const response = await axios.get('http://localhost:8080/anime/latest-updates')
    if (response.data.code === 200) {
      latestUpdates.value = response.data.data
    }
  } catch (error) {
    console.error('获取最新更新列表失败:', error)
  }
}

onMounted(() => {
  fetchAnimes()
  fetchWeeklySchedule()
  fetchLatestUpdates()
})
</script>

<style scoped>
/* 内容盒子通用样式 */
.content-box, .schedule-box, .recent-box {
  background-color: #2d2d2d;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 20px;
}

/* 区块标题样式 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.section-header h4 {
  color: #fff;
  font-size: 16px;
  margin: 0;
}

/* 动漫网格布局 */
.anime-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 15px;
}

/* 动漫卡片样式 */
.anime-card {
  text-decoration: none;
  transition: transform 0.2s ease;
}

.anime-card:hover {
  transform: scale(1.05);
}

/* 动漫封面样式 */
.anime-cover {
  position: relative;
  aspect-ratio: 3/4;
  border-radius: 4px;
  overflow: hidden;
}

.anime-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.anime-card:hover .anime-cover img {
  transform: scale(1.1);
}

/* 集数标签样式 */
.episode-tag {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 4px 8px;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  font-size: 12px;
}

/* 动漫标题样式 */
.anime-title {
  margin: 8px 0 0;
  font-size: 14px;
  color: #fff;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 周播放列表样式 */
.weekly-tabs :deep(.el-tabs__item) {
  color: #999;
  font-size: 14px;
}

.weekly-tabs :deep(.el-tabs__item.is-active) {
  color: #fff;
}

.schedule-list {
  margin-top: 10px;
}

.schedule-item {
  padding: 10px 0;
  border-bottom: 1px solid #3d3d3d;
}

.schedule-item:last-child {
  border-bottom: none;
}

.schedule-item .time {
  color: #dc3545;
  font-size: 13px;
  display: block;
}

.schedule-item .title {
  color: #fff;
  font-size: 14px;
  display: block;
  margin: 4px 0;
  text-decoration: none;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.schedule-item .title:hover {
  color: #409EFF;
}
.recent-title:hover { 
  color: #409EFF;
}
.schedule-item .episode,
.schedule-item .status {
  font-size: 12px;
  color: #666;
  margin-right: 10px;
}

/* 最近更新列表样式 */
.recent-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.recent-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #3d3d3d;
}

.recent-list li:last-child {
  border-bottom: none;
}

.recent-list a {
  color: #fff;
  text-decoration: none;
  font-size: 14px;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.recent-list .date {
  color: #666;
  font-size: 12px;
  margin-left: 10px;
}
</style>
