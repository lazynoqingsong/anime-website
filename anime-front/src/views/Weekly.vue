<template>
  <div class="page-container">
    <div class="scroll-container">
      <div class="content-wrapper">


        <!-- 每日更新区域 -->
        <div class="weekly-content">
          <div 
            v-for="day in weekDays" 
            :key="day" 
            class="day-section"
          >
            <!-- 今天更新标题 -->
            <div class="day-title">
              {{ day }}（{{ getUpdateCount(day) }}部）
            </div>
            
            <!-- 动漫列表 -->
            <div class="anime-list">
              <div 
                v-for="anime in weeklySchedule[day]" 
                :key="anime.id"
                class="anime-card"
                @click="goToDetail(anime.id)"
              >
                <div class="anime-cover">
                  <img 
                    :src="getCoverImage(anime.cover_image)" 
                    :alt="anime.title"
                    @error="handleImageError"
                  >
                  <div class="episode-tag">{{ anime.latest_episode || '暂无更新' }}</div>
                </div>
                <div class="anime-info">
                  <div class="title">{{ anime.title }}</div>
                  <div class="time">{{ formatTime(anime.broadcast_time) }}</div>
                </div>
              </div>
              <el-empty 
                v-if="!weeklySchedule[day]?.length" 
                description="暂无更新" 
                :image-size="100"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <BackToTop />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import BackToTop from '@/components/BackToTop.vue'

const router = useRouter()
const weekDays = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
const weeklySchedule = ref({})

// 获取封面图片
const getCoverImage = (path) => {

  if (!path) {
    console.log('图片路径为空，使用默认图片')
    return 'http://localhost:8080/images/avatar.jpg'
  }
  const imageUrl = `http://localhost:8080/images/${path}`
 
  return imageUrl
}

// 处理图片加载失败
const handleImageError = (e) => {
  console.log('图片加载失败:', e.target.src)
  e.target.src = 'http://localhost:8080/images/avatar.jpg'
}

// 获取当天更新数量
const getUpdateCount = (day) => {
  return weeklySchedule.value[day]?.length || 0
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return time.substring(0, 5)  // 只显示小时和分钟
}

// 跳转到详情页
const goToDetail = (animeId) => {
  router.push(`/anime/${animeId}`)
}

// 获取周播放表数据
const fetchWeeklySchedule = async () => {
  try {
    const response = await axios.get('http://localhost:8080/anime/weekly')
    if (response.data.code === 200) {
  
      weeklySchedule.value = response.data.data
    }
  } catch (error) {
    console.error('获取周播放表失败:', error)
  }
}

onMounted(() => {
  fetchWeeklySchedule()
})
</script>

<style scoped>
.weekly-header {
  margin-bottom: 20px;
  padding: 0 20px;
}

.weekly-header h2 {
  color: #fff;
  font-size: 24px;
  margin: 0;
}

.weekly-content {
  padding: 0 20px;
}

.day-section {
  margin-bottom: 30px;
  background-color: #2d2d2d;
  border-radius: 4px;
  padding: 15px;
}

.day-title {
  color: #fff;
  font-size: 16px;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #363636;
  border-radius: 4px;
}

/* 列表布局 */
.anime-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 20px;
}

.anime-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.anime-card:hover {
  transform: scale(1.05);
}

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
}

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

.anime-info {
  padding: 8px 0;
}

.anime-info .title {
  color: #fff;
  font-size: 14px;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.anime-info .time {
  color: #409EFF;
  font-size: 12px;
}

/* 空状态样式 */
:deep(.el-empty__description) {
  color: #909399;
}
</style>