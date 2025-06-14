<template>
  <div class="my-favorites">
    <div class="favorites-header">
      <h2>我的收藏 ({{ total }}部)</h2>
    </div>
    
    <div class="favorites-list" v-loading="loading">
      <template v-if="favorites.length > 0">
        <div v-for="favorite in favorites" :key="favorite.id" class="favorite-item">
          <div class="anime-cover">
            <img 
              :src="getCoverImage(favorite.cover_image)" 
              :alt="favorite.anime_title"
              @error="handleImageError"
            >
            <div class="episode-tag">{{ favorite.latest_episode || '暂无更新' }}</div>
          </div>
          
          <div class="anime-info">
            <h3 class="title">{{ favorite.anime_title }}</h3>
            <p class="description">{{ favorite.description || '暂无简介' }}</p>
            <div class="status">状态：{{ favorite.status }}</div>
            <div class="action-buttons">
              <el-button 
                type="primary" 
                size="small"
                @click="goToDetail(favorite.anime_id)"
              >
                <el-icon><VideoPlay /></el-icon>
                立即观看
              </el-button>
              <el-button 
                type="danger" 
                size="small"
                @click="handleRemoveFavorite(favorite.anime_id)"
              >
                <el-icon><Delete /></el-icon>
                取消收藏
              </el-button>
            </div>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination-container" v-if="total > 0">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            :background="true"
            layout="prev, pager, next, jumper"
            @current-change="handlePageChange"
          />
        </div>
      </template>
      <el-empty v-else description="暂无收藏" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { VideoPlay, Delete } from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const userStore = useUserStore()

const favorites = ref([])
const total = ref(0)
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(5)

// 获取收藏列表
const fetchFavorites = async () => {
  if (!userStore.userId) return
  
  loading.value = true
  try {
    const response = await axios.get('http://localhost:8080/favorite', {
      params: {
        userId: userStore.userId,
        page: currentPage.value,
        pageSize: pageSize.value
      }
    })
    
    if (response.data.code === 200) {
      favorites.value = response.data.data.list
      total.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取收藏列表失败:', error)
    ElMessage.error('获取收藏列表失败')
  } finally {
    loading.value = false
  }
}

// 处理页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchFavorites()
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

// 跳转到详情页
const goToDetail = (animeId) => {
  router.push(`/anime/${animeId}`)
}

// 取消收藏
const handleRemoveFavorite = (animeId) => {
  ElMessageBox.confirm(
    '确定要取消收藏这部动漫吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await axios.delete('http://localhost:8080/favorite', {
        params: {
          userId: userStore.userId,
          animeId: animeId
        }
      })
      
      if (response.data.code === 200) {
        ElMessage.success('取消收藏成功')
        fetchFavorites() // 刷新列表
      }
    } catch (error) {
      console.error('取消收藏失败:', error)
      ElMessage.error('取消收藏失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchFavorites()
})

// 暴露给父组件的属性和方法
defineExpose({
  total,
  fetchFavorites
})
</script>

<style scoped>
.my-favorites {
  background: #2c2c2c;
  border-radius: 8px;
  padding: 20px;
}

.favorites-header {
  margin-bottom: 20px;
}

.favorites-header h2 {
  font-size: 18px;
  margin: 0;
  color: #fff;
}

.favorites-list {
  min-height: 200px;
}

.favorite-item {
  display: flex;
  gap: 20px;
  padding: 15px;
  background: #363636;
  border-radius: 8px;
  margin-bottom: 15px;
}

.anime-cover {
  position: relative;
  width: 120px;
  flex-shrink: 0;
}

.anime-cover img {
  width: 100%;
  height: 160px;
  border-radius: 4px;
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
  flex: 1;
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 16px;
  color: #fff;
  margin: 0 0 10px;
}

.description {
  
  color: #909399;
  font-size: 14px;
  line-height: 1.5;
  margin: 0 0 10px;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-clamp: 2;
}

.status {
  color: #909399;
  font-size: 14px;
  margin-bottom: 10px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.action-buttons .el-button {
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

:deep(.el-pagination) {
  --el-pagination-bg-color: transparent;
  --el-pagination-hover-color: #409EFF;
  --el-pagination-button-color: #fff;
  --el-text-color-primary: #fff;
}

:deep(.el-pagination .el-pager li) {
  background: #363636;
}

:deep(.el-pagination .el-pager li.is-active) {
  background: #409EFF;
}
</style> 