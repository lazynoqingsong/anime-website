<template>
  <div class="page-container">
    <div class="scroll-container">
      <div class="content-wrapper">
        <!-- 搜索结果头部 -->
        <div class="search-header">
          <h2 class="result-title">
            关键词 <span class="highlight">{{ keyword }}</span> 搜索结果如下, 共 <span class="highlight">{{ total }}</span> 条记录
          </h2>
        </div>

        <!-- 搜索结果列表 -->
        <div class="anime-list" v-loading="loading">
          <el-empty v-if="animeList.length === 0" description="暂无数据" />
          <div v-else v-for="anime in animeList" :key="anime.id" class="anime-item">
            <div class="anime-cover">
              <img :src="getCoverImage(anime.cover_image)" 
                   :alt="anime.title"
                   @error="handleImageError">
            </div>
            <div class="anime-info">
              <h3 class="title">{{ anime.title }}</h3>
              <div class="meta-info">
                <p><span class="label">动画种类:</span> {{ anime.format }}</p>
                <p><span class="label">首播时间:</span> {{ formatDate(anime.release_date) }}</p>
                <p><span class="label">播放状态:</span> {{ anime.status }}</p>
                <p><span class="label">剧情类型:</span> {{ anime.genres || '暂无' }}</p>
                <p><span class="label">制作公司:</span> {{ anime.studio || '暂无' }}</p>
              </div>
              <p class="description">{{ anime.description || '暂无简介' }}</p>
              <div class="action-buttons">
                <el-button 
                  type="info" 
                  size="small" 
                  class="detail-btn"
                  @click="goToDetail(anime.id)"
                >资源详情</el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  class="play-btn"
                  @click="goToPlayer(anime.id)"
                >在线播放</el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页组件 -->
        <div class="pagination-container">
          <div class="pagination-wrapper">
            <div class="page-total">共 {{ total }} 条</div>
            <ul class="page-list">
              <li class="page-item" 
                  :class="{ disabled: currentPage === 1 }" 
                  @click="currentPage > 1 && handleCurrentChange(currentPage - 1)">
                <span>上一页</span>
              </li>
              
              <li v-if="startPage > 1" class="page-item" @click="handleCurrentChange(1)">
                <span>1</span>
              </li>
              
              <li v-if="startPage > 2" class="page-item page-ellipsis">
                <span>...</span>
              </li>
              
              <li v-for="page in displayPages" 
                  :key="page" 
                  class="page-item"
                  :class="{ active: currentPage === page }"
                  @click="handleCurrentChange(page)">
                <span>{{ page }}</span>
              </li>
              
              <li v-if="endPage < totalPages - 1" class="page-item page-ellipsis">
                <span>...</span>
              </li>
              
              <li v-if="endPage < totalPages" 
                  class="page-item" 
                  @click="handleCurrentChange(totalPages)">
                <span>{{ totalPages }}</span>
              </li>
              
              <li class="page-item" 
                  :class="{ disabled: currentPage === totalPages }"
                  @click="currentPage < totalPages && handleCurrentChange(currentPage + 1)">
                <span>下一页</span>
              </li>
            </ul>
            
            <div class="page-jump">
              <span>前往</span>
              <input type="number" 
                     v-model.number="jumpPage" 
                     @keyup.enter="handleJumpPage"
                     @input="validateJumpPage"
                     min="1" 
                     :max="totalPages">
              <span>页</span>
            </div>
          </div>
        </div>
      </div>
      <BackToTop />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import BackToTop from '@/components/BackToTop.vue'

const route = useRoute()
const router = useRouter()
const keyword = ref('')
const animeList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)

// 搜索动漫 - 将函数定义移到 watch 之前
const searchAnimes = async () => {
  if (!keyword.value) return
  
  loading.value = true
  try {
    const response = await axios.get('http://localhost:8080/anime/search', {
      params: {
        keyword: keyword.value,
        currentPage: currentPage.value,
        pageSize: pageSize.value
      }
    })
    
    if (response.data.code === 200) {
      animeList.value = response.data.data.list
      total.value = response.data.data.total
    }
  } catch (error) {
    console.error('搜索失败:', error)
  } finally {
    loading.value = false
  }
}

// 监听路由参数变化
watch(
  () => route.query.keyword,
  (newKeyword) => {
    if (newKeyword) {
      keyword.value = newKeyword
      currentPage.value = 1
      searchAnimes()
    }
  },
  { immediate: true }
)

// 处理分页变化
const handleCurrentChange = (page) => {
  // 添加边界检查
  if (page < 1 || page > totalPages.value || page === currentPage.value) {
    return
  }
  currentPage.value = page
  searchAnimes()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  searchAnimes()
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/anime/${id}`)
}

// 跳转到播放页
const goToPlayer = (animeId) => {
  router.push(`/anime/${animeId}/play/1`)
}

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.src = '/default-cover.jpg'
}

const getCoverImage = (path) => {
  if (!path) {
    return 'http://localhost:8080/images/avatar.jpg'
  }
  // 直接返回完整的URL
  return `http://localhost:8080/images/${path}`
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '未知'
  return date.split('T')[0]
}

// 添加分页相关的计算属性
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))
const jumpPage = ref(1)

// 显示的页码范围
const displayPages = computed(() => {
  const pages = []
  const maxDisplayPages = 5
  let start = Math.max(1, currentPage.value - Math.floor(maxDisplayPages / 2))
  let end = Math.min(totalPages.value, start + maxDisplayPages - 1)
  
  if (end - start + 1 < maxDisplayPages) {
    start = Math.max(1, end - maxDisplayPages + 1)
  }
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

const startPage = computed(() => displayPages.value[0])
const endPage = computed(() => displayPages.value[displayPages.value.length - 1])

// 跳转页面处理
const handleJumpPage = () => {
  if (!jumpPage.value || jumpPage.value < 1) {
    jumpPage.value = 1
  } else if (jumpPage.value > totalPages.value) {
    jumpPage.value = totalPages.value
  }
  
  if (jumpPage.value === currentPage.value) {
    return
  }
  
  handleCurrentChange(jumpPage.value)
}

// 添加输入验证
const validateJumpPage = (event) => {
  const value = event.target.value
  if (value && (value < 1 || value > totalPages.value)) {
    event.target.value = value < 1 ? 1 : totalPages.value
    jumpPage.value = parseInt(event.target.value)
  }
}
</script>

<style scoped>
.anime-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 10px;
}

.anime-item {
  display: flex;
  gap: 20px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  transition: all 0.3s;
}

.anime-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.anime-cover {
  width: 200px;
  height: 280px;
  border-radius: 4px;
  overflow: hidden;
}

.anime-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.anime-cover:hover img {
  transform: scale(1.1);
}

.anime-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 20px;
  color: #fff;
  margin-bottom: 15px;
}

.meta-info {
  margin-bottom: 15px;
}

.meta-info p {
  color: #909399;
  margin: 8px 0;
  font-size: 14px;
}

.label {
  color: #fff;
  margin-right: 10px;
}

.description {
  color: #909399;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 20px;
  flex: 1;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

/* 分页样式 */
.pagination-container {
  margin-top: 30px;
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

.page-list {
  display: flex;
  gap: 8px;
  list-style: none;
}

.page-item {
  min-width: 32px;
  height: 32px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  cursor: pointer;
  transition: all 0.3s;
}

.page-item:hover:not(.disabled) {
  background: #409EFF;
}

.page-item.active {
  background: #409EFF;
}

.page-item.disabled {
  cursor: not-allowed;
  opacity: 0.5;
  pointer-events: none;
}

.page-jump {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #909399;
}

.page-jump input {
  width: 50px;
  height: 32px;
  border: 1px solid #4c4d4f;
  border-radius: 4px;
  background: transparent;
  color: #fff;
  text-align: center;
}

.search-header {
  padding: 20px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.result-title {
  margin-left: 15px;
  font-size: 18px;
  color: #fff;
  font-weight: normal;
}

.highlight {
  color: #f56c6c;  /* Element Plus 的红色主题色 */
  font-weight: bold;
  margin: 0 3px;
}

.page-jump input::-webkit-outer-spin-button,
.page-jump input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* .page-jump input[type=number] {
  -moz-appearance: textfield;
} */
</style> 