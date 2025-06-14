<template>
  <div class="my-comments">
    <div class="comments-header">
      <h2>我的评论 ({{ total }}条)</h2>
    </div>
    
    <div class="comments-list" v-loading="loading">
      <template v-if="comments.length > 0">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="anime-info" @click="goToAnime(comment.anime_id)">
            <img 
              :src="getCoverImage(comment.cover_image)" 
              :alt="comment.anime_title"
              @error="handleImageError"
            >
            <div class="anime-title">{{ comment.anime_title }}</div>
          </div>
          <div class="comment-content">
            <div class="content">{{ comment.content }}</div>
            <div class="time">{{ formatDate(comment.created_time) }}</div>
          </div>
        </div>
        
        <div class="pagination-container" v-if="total > 0">
          <div class="pagination-wrapper">
            <div class="page-total">共 {{ total }} 条</div>
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
      </template>
      <el-empty v-else description="暂无评论" />
    </div>
    
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { formatDate } from '@/utils/format'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const comments = ref([])
const total = ref(0)
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(5)

// 获取评论列表
const fetchComments = async () => {
  if (!userStore.userId) return
  
  loading.value = true
  try {
    const response = await axios.get('http://localhost:8080/comments/user', {
      params: {
        userId: userStore.userId,
        page: currentPage.value,
        pageSize: pageSize.value
      }
    })
    
    if (response.data.code === 200) {
      comments.value = response.data.data.list
      total.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取评论失败:', error)
  } finally {
    loading.value = false
  }
}

// 处理页码跳转
const handlePageChange = (page) => {
  currentPage.value = page
  fetchComments() // 重新获取当前页的评论数据
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

// 跳转到动漫详情页
const goToAnime = (animeId) => {
  router.push(`/anime/${animeId}`)
}

onMounted(() => {
  fetchComments()
})

// 暴露给父组件的属性和方法
defineExpose({
  total,
  fetchComments
})
</script>

<style scoped>
.my-comments {
  background: #2c2c2c;
  border-radius: 8px;
  padding: 20px;
}

.comments-header {
  margin-bottom: 20px;
}

.comments-header h2 {
  font-size: 18px;
  margin: 0;
  color: #fff;
}

.comments-list {
  min-height: 200px;
}

.comment-item {
  display: flex;
  gap: 20px;
  padding: 15px;
  background: #363636;
  border-radius: 8px;
  margin-bottom: 15px;
}

.anime-info {
  flex-shrink: 0;
  width: 120px;
  cursor: pointer;
}

.anime-info img {
  width: 120px;
  height: 160px;
  border-radius: 4px;
  object-fit: cover;
  margin-bottom: 8px;
}

.anime-title {
  font-size: 14px;
  color: #fff;
  padding: 0 5px;
  line-height: 1.4;
  overflow: visible;
  white-space: normal;
  text-align: left;
}

.comment-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.content {
  color: #fff;
  line-height: 1.6;
  margin-bottom: 10px;
}

.time {
  color: #909399;
  font-size: 12px;
}

/* 分页样式 */
.pagination-container {
  margin-top: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.page-total {
  color: #909399;
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

:deep(.el-pagination .el-input__inner) {
  background-color: #363636 !important;
  border-color: #4c4d4f !important;
  color: #fff !important;
  text-align: center;
}

:deep(.el-pagination .el-input__inner:focus) {
  border-color: #409EFF !important;
}
</style> 