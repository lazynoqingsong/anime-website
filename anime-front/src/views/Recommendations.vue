<template>
  <div class="page-container">
    <div class="scroll-container">
      <div class="content-wrapper">
        <div class="recommendations-container">
          <!-- 页面标题 -->
          <div class="page-header">
            <h2>今日推荐</h2>
          </div>

          <!-- 推荐列表 -->
          <div class="recommendations-grid" v-loading="loading">
            <template v-if="recommendations.length > 0">
              <div v-for="anime in recommendations" 
                   :key="anime.id" 
                   class="anime-card"
                   @click="goToDetail(anime.id)">
                <div class="cover-wrapper">
                  <img :src="getCoverImage(anime.cover_image)" 
                       :alt="anime.title"
                       @error="handleImageError">
                </div>
                <div class="anime-info">
                  <div class="title">{{ anime.title }}</div>
                  <div class="recommendation-score" v-if="anime.score">
                    推荐指数: {{ (anime.score * 100).toFixed(1) }}%
                  </div>
                </div>
              </div>
            </template>
            <el-empty v-else description="暂无推荐" />
          </div>

          <!-- 分页器 -->
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
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const recommendations = ref([])
const currentPage = ref(1)
const pageSize = ref(15)
const total = ref(0)

// 获取推荐列表
const fetchRecommendations = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const response = await axios.get('http://localhost:8080/recommendations/more', {
      params: {
        userId: userStore.userId,
        page: currentPage.value,
        pageSize: pageSize.value
      }
    })

    if (response.data.code === 200) {
      recommendations.value = response.data.data.list
      total.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取推荐列表失败:', error)
    ElMessage.error('获取推荐列表失败')
  } finally {
    loading.value = false
  }
}

// 处理页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchRecommendations()
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
</script>

<style scoped>
.recommendations-container {
  background-color: #2d2d2d;
  border-radius: 8px;
  padding: 16px;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 16px;
}

.page-header h2 {
  color: #fff;
  font-size: 20px;
  margin: 0;
}

.recommendations-grid {
  display: grid;
  grid-template-columns: repeat(5, 140px);
  gap: 12px;
  justify-content: center;
  min-height: 200px;
}

.anime-card {
  width: 140px;
  cursor: pointer;
  transition: transform 0.2s;
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
  margin-bottom: 6px;
}

.cover-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.anime-info {
  width: 140px;
  padding: 0 4px;
}

.title {
  color: #fff;
  font-size: 14px;
  margin-bottom: 2px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tags {
  color: #909399;
  font-size: 12px;
  margin-bottom: 2px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.recommendation-score {
  color: #67C23A;
  font-size: 12px;
  text-align: right;
}

.pagination-container {
  margin-top: 16px;
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

:deep(.el-pagination .btn-prev),
:deep(.el-pagination .btn-next) {
  background: #363636;
}
</style>