<template>
  <div class="page-container">
    <div class="scroll-container">
      <div class="content-wrapper">
        <!-- 动漫详情区域 -->
        <div class="anime-detail" v-if="animeInfo">
          <!-- 左侧封面图 -->
          <div class="cover-section">
            <img 
              :src="getCoverImage(animeInfo.cover_image)" 
              :alt="animeInfo.title"
              @error="handleImageError"
            >
            <!-- 添加收藏按钮 -->
            <div v-if="userStore.isLoggedIn" class="favorite-btn" @click="toggleFavorite">
              <el-icon :class="{ 'is-favorite': isFavorited }">
                <Star v-if="isFavorited" />
                <StarFilled v-else />
              </el-icon>
              <span>{{ isFavorited ? '已收藏' : '收藏' }}</span>
            </div>
          </div>
                
          <!-- 右侧信息区域 -->
          <div class="info-section">
            <h1 class="title">{{ animeInfo.title }}</h1>
            
            <!-- 基本信息 -->
            <div class="meta-info">
              <div class="info-item">
                <span class="label">动画种类:</span>
                <span>{{ animeInfo.format || '未知' }}</span>
              </div>
              <div class="info-item">
                <span class="label">首播时间:</span>
                <span>{{ animeInfo.release_date || '未知' }}</span>
              </div>
              <div class="info-item">
                <span class="label">播放状态:</span>
                <span>{{ animeInfo.status || '未知' }}</span>
              </div>
              <div class="info-item">
                <span class="label">剧情类型:</span>
                <span>{{ animeInfo.genres || '未知' }}</span>
              </div>
              <div class="info-item">
                <span class="label">制作公司:</span>
                <span>{{ animeInfo.studio || '未知' }}</span>
              </div>
              <div class="info-item">
                <span class="label">更新时间:</span>
                <span>{{ animeInfo.broadcast_day }} {{ formatTime(animeInfo.broadcast_time) }}</span>
              </div>
            </div>

            <!-- 简介 -->
            <div class="description">
              <h3>作品简介</h3>
              <p>{{ animeInfo.description || '暂无简介' }}</p>
            </div>
          </div>
        </div>

        <!-- 剧集列表 -->
        <div class="episodes-section" v-if="episodes.length">
          <h2>剧集列表</h2>
          <div class="episode-grid">
            <div 
              v-for="episode in episodes" 
              :key="episode.id"
              class="episode-item"
              @click="goToPlay(episode.episode_number)"
            >
              第{{ episode.episode_number }}集
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
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import BackToTop from '@/components/BackToTop.vue'
import { Star, StarFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const animeInfo = ref(null)
const episodes = ref([])
const userStore = useUserStore()
const isFavorited = ref(false)

// 获取动漫详情
const fetchAnimeDetail = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/anime/${route.params.id}`)
    if (response.data.code === 200) {
      animeInfo.value = response.data.data
    }
  } catch (error) {
    console.error('获取动漫详情失败:', error)
  }
}

// 获取剧集列表
const fetchEpisodes = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/anime/${route.params.id}/episodes`)
    if (response.data.code === 200) {
      episodes.value = response.data.data
    }
  } catch (error) {
    console.error('获取剧集列表失败:', error)
  }
}

// 获取封面图片
const getCoverImage = (path) => {
  if (!path) return 'http://localhost:8080/images/avatar.jpg'
  return `http://localhost:8080/images/${path}`
}

// 处理图片加载失败
const handleImageError = (e) => {
  e.target.src = 'http://localhost:8080/images/avatar.jpg'
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return time.substring(0, 5)
}

// 添加跳转到播放页的方法
const goToPlay = (episodeNumber) => {
  router.push(`/anime/${route.params.id}/play/${episodeNumber}`)
}

// 检查是否已收藏
const checkFavorite = async () => {
  if (!userStore.isLoggedIn) return
  
  try {
    const response = await axios.get('http://localhost:8080/favorite/check', {
      params: {
        userId: userStore.userId,
        animeId: route.params.id
      }
    })
    if (response.data.code === 200) {
      isFavorited.value = response.data.data
    }
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 切换收藏状态
const toggleFavorite = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    if (isFavorited.value) {
      // 取消收藏
      const response = await axios.delete('http://localhost:8080/favorite', {
        params: {
          userId: userStore.userId,
          animeId: route.params.id
        }
      })
      if (response.data.code === 200) {
        isFavorited.value = false
        ElMessage.success('取消收藏成功')
      }
    } else {
      // 添加收藏
      const response = await axios.post('http://localhost:8080/favorite', null, {
        params: {
          userId: userStore.userId,
          animeId: route.params.id
        }
      })
      if (response.data.code === 200) {
        isFavorited.value = true
        ElMessage.success('收藏成功')
      }
    }
  } catch (error) {
    console.error('操作收藏失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

onMounted(() => {
  fetchAnimeDetail()
  fetchEpisodes()
  checkFavorite()
})
</script>

<style scoped>
.anime-detail {
  display: flex;
  gap: 30px;
  padding: 20px;
  background-color: #2d2d2d;
  border-radius: 4px;
}

.cover-section {
  flex-shrink: 0;
  width: 240px;
  position: relative;
}

.cover-section img {
  width: 100%;
  height: auto;
  border-radius: 4px;
}

.info-section {
  flex-grow: 1;
}

.title {
  color: #fff;
  font-size: 24px;
  margin-bottom: 20px;
}

.meta-info {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 30px;
}

.info-item {
  color: #fff;
  font-size: 14px;
}

.label {
  color: #909399;
  margin-right: 10px;
}

.description {
  color: #fff;
}

.description h3 {
  font-size: 16px;
  margin-bottom: 10px;
}

.description p {
  color: #909399;
  line-height: 1.6;
}

.episodes-section {
  margin-top: 30px;
  padding: 20px;
  background-color: #2d2d2d;
  border-radius: 4px;
}

.episodes-section h2 {
  color: #fff;
  font-size: 18px;
  margin-bottom: 20px;
}

.episode-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 15px;
}

.episode-item {
  padding: 10px;
  text-align: center;
  background-color: #363636;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.episode-item:hover {
  background-color: #409EFF;
  transform: scale(1.05);
}

.favorite-btn {
  position: absolute;
  right: 10px;
  bottom: 10px;
  background: rgba(0, 0, 0, 0.6);
  padding: 8px 12px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  transition: all 0.3s;
  color: #fff;
}

.favorite-btn:hover {
  background: rgba(0, 0, 0, 0.8);
}

.favorite-btn .el-icon {
  font-size: 18px;
  color: #fff;
}

.favorite-btn .is-favorite {
  color: #f56c6c;
}
</style> 