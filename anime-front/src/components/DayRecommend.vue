<template>
  <div class="recommend-container">
    <div class="recommend-header">
      <h2>今日推荐</h2>
      <el-button type="info" plain size="small" @click="goToRecommendations">更多>></el-button>
  
    </div>

    <div class="recommend-grid">
      <div v-for="anime in recommendations.slice(0, 5)" 
           :key="anime.id" 
           class="anime-card"
           @click="goToDetail(anime.id)">
        <div class="cover-wrapper">
          <img :src="getCoverImage(anime.cover_image)" 
               :alt="anime.title"
               @error="handleImageError">
          <div class="episode-tag" v-if="anime.latest_episode">
            {{ anime.latest_episode }}
          </div>
        </div>
        <div class="anime-info">
          <div class="title">{{ anime.title }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import axios from 'axios'

const router = useRouter()
const userStore = useUserStore()
const recommendations = ref([])

// 获取每日推荐
const fetchRecommendations = async () => {
  if (!userStore.isLoggedIn) return
  
  try {
    const response = await axios.get('http://localhost:8080/recommendations/daily', {
      params: {
        userId: userStore.userId,
        limit: 10
      }
    })
    if (response.data.code === 200) {
      recommendations.value = response.data.data
    }
  } catch (error) {
    console.error('获取每日推荐失败:', error)
  }
}

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

onMounted(() => {
  fetchRecommendations()
})

const goToRecommendations = () => {
  router.push('/recommendations')
}
</script>

<style scoped>
.recommend-container {
  margin-bottom: 20px;
  background-color: #2d2d2d;
  border-radius: 8px;
  padding: 20px;
}

.recommend-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.recommend-header h2 {
  color: #fff;
  font-size: 20px;
  margin: 0;
}

.more-link {
  color: #409EFF;
  text-decoration: none;
  font-size: 14px;
}

.more-link:hover {
  text-decoration: underline;
}

.recommend-grid {
  display: flex;
  justify-content: flex-start;
  gap: 16px;
  width: 100%;
}

.anime-card {
  width: 140px;
  cursor: pointer;
  transition: transform 0.2s;
  flex-shrink: 0;
}

.anime-card:hover {
  transform: scale(1.05);
}

.cover-wrapper {
  position: relative;
  width: 140px;
  height: 188px;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}

.cover-wrapper img {
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
  text-align: center;
}

.anime-info {
  width: 140px;
}

.title {
  color: #fff;
  font-size: 14px;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}

.tags {
  color: #909399;
  font-size: 12px;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}

.recommendation-score {
  color: #67C23A;
  font-size: 12px;
  text-align: right;
  padding-right: 4px;
}
</style> 