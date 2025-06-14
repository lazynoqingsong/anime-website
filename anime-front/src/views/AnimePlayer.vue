<template>
    <div class="page-container">
      <div class="scroll-container">
        <div class="content-wrapper">
  <div class="player-page">
    <!-- 视频播放器区域 -->
    <div class="player-container">
      <VideoPlayer 
        v-if="episodes.length && getCurrentEpisodeUrl()"
        :video-url="getCurrentEpisodeUrl()"
        :anime-id="animeId"
        :episode-number="currentEpisode"
      />
      <div v-else class="loading-placeholder">
        <el-icon><Loading /></el-icon>
        <span>加载中...</span>
      </div>
    </div>

    <!-- 动漫信息区域 -->
    <div class="anime-info">
      <div class="info-header">
        <h1>{{ animeInfo.title }}</h1>
        <div class="episode-title">第{{ currentEpisode }}集</div>
      </div>
      
      <div class="info-content">
        <div class="info-item">
          <span class="label">播放状态：</span>
          <span>{{ animeInfo.status || '未知' }}</span>
        </div>
        <div class="info-item">
          <span class="label">动画种类：</span>
          <span>{{ animeInfo.format || '未知' }}</span>
        </div>
        <div class="info-item">
          <span class="label">制作公司：</span>
          <span>{{ animeInfo.studio || '未知' }}</span>
        </div>
        <div class="info-item">
          <span class="label">首播时间：</span>
          <span>{{ animeInfo.release_date || '未知' }}</span>
        </div>
        <div class="info-item">
          <span class="label">剧情类型：</span>
          <span>{{ animeInfo.genres || '未知' }}</span>
        </div>
      </div>
    </div>

    <!-- 剧集列表 -->
    <div class="episode-list">
      <h2>剧集列表</h2>
      <div class="episode-grid">
        <div 
          v-for="episode in episodes" 
          :key="episode.id"
          class="episode-item"
          :class="{ active: currentEpisode === episode.episode_number }"
          @click="switchEpisode(episode.episode_number)"
        >
          第{{ episode.episode_number }}集
        </div>
      </div>
    </div>

    <!-- 评论区 -->
    <div class="comments-section">
      <h2>评论区 (共{{ commentTotal }}条评论)</h2>
      
      <!-- 评论输入框 -->
      <div class="comment-input" v-if="userStore.isLoggedIn">
        <el-input
          v-model="commentContent"
          type="textarea"
          :rows="3"
          placeholder="发表你的评论..."
          maxlength="500"
          show-word-limit
        />
        <el-button 
          type="primary" 
          @click="submitComment"
          :loading="isLoading"
          class="submit-btn"
        >
          发表评论
        </el-button>
      </div>
      <div v-else class="login-tip">
        <el-button type="primary" @click="goToLogin">登录后发表评论</el-button>
      </div>
      
      <!-- 评论列表 -->
      <div class="comments-list" v-loading="isLoading">
        <template v-if="comments.length > 0">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-header">
              <span class="username">{{ comment.username }}</span>
              <span class="time">{{ formatDate(comment.created_time) }}</span>
            </div>
            <div class="comment-content">{{ comment.content }}</div>
          </div>
          
          <!-- 分页 -->
          <div class="pagination-container" v-if="commentTotal > 0">
            <div class="pagination-wrapper">
              <div class="page-total">共 {{ commentTotal }} 条</div>
              <ul class="page-list">
                <li class="page-item" 
                    :class="{ disabled: currentPage === 1 }" 
                    @click="handleJumpPage(currentPage - 1)">
                  <span>上一页</span>
                </li>
                
                <li v-if="startPage > 1" class="page-item" @click="handleJumpPage(1)">
                  <span>1</span>
                </li>
                
                <li v-if="startPage > 2" class="page-item page-ellipsis">
                  <span>...</span>
                </li>
                
                <li v-for="page in displayPages" 
                    :key="page" 
                    class="page-item"
                    :class="{ active: currentPage === page }"
                    @click="handleJumpPage(page)">
                  <span>{{ page }}</span>
                </li>
                
                <li v-if="endPage < totalPages - 1" class="page-item page-ellipsis">
                  <span>...</span>
                </li>
                
                <li v-if="endPage < totalPages" 
                    class="page-item" 
                    @click="handleJumpPage(totalPages)">
                  <span>{{ totalPages }}</span>
                </li>
                
                <li class="page-item" 
                    :class="{ disabled: currentPage === totalPages }"
                    @click="handleJumpPage(currentPage + 1)">
                  <span>下一页</span>
                </li>
              </ul>
              
              <div class="page-jump">
                <span>前往</span>
                <input type="number" 
                       v-model.number="jumpPage" 
                       @keyup.enter="handleJumpPage"
                       min="1" 
                       :max="totalPages">
                <span>页</span>
              </div>
            </div>
          </div>
        </template>
        <el-empty v-else description="暂无评论" />
      </div>
    </div>
  </div>
  </div>
</div>
</div>
</template>

<script setup>
import { ref, onMounted, watch, onBeforeUnmount, computed } from 'vue'
import { useRoute, useRouter, onBeforeRouteLeave } from 'vue-router'
import { VideoPlay, Loading } from '@element-plus/icons-vue'
import axios from 'axios'
import VideoPlayer from '@/components/VideoPlayer.vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { formatDate } from '@/utils/format'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 基础状态
const animeId = ref(null)
const currentEpisode = ref(1)
const animeInfo = ref({})
const episodes = ref([])

// 评论相关状态
const commentContent = ref('')
const comments = ref([])
const commentTotal = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const isLoading = ref(false)
const jumpPage = ref(1)

// 计算属性
const totalPages = computed(() => Math.ceil(commentTotal.value / pageSize.value))

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

// 获取动漫信息
const fetchAnimeInfo = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/anime/${animeId.value}`)
    if (response.data.code === 200) {
      animeInfo.value = response.data.data
    }
  } catch (error) {
    console.error('获取动漫信息失败:', error)
  }
}

// 获取剧集列表
const fetchEpisodes = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/anime/${animeId.value}/episodes`)
    if (response.data.code === 200) {
      episodes.value = response.data.data
    }
  } catch (error) {
    console.error('获取剧集列表失败:', error)
  }
}

// 重置评论状态
const resetCommentState = () => {
  currentPage.value = 1
  commentTotal.value = 0
  comments.value = []
  commentContent.value = ''
}

// 获取评论列表
const fetchComments = async () => {
  if (!animeId.value) return
  
  isLoading.value = true
  try {
    const response = await axios.get('http://localhost:8080/comments', {
      params: {
        animeId: parseInt(animeId.value),
        page: currentPage.value,
        pageSize: pageSize.value
      }
    })
    
    if (response.data.code === 200) {
      comments.value = response.data.data.list
      commentTotal.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取评论失败:', error)
    ElMessage.error('获取评论失败')
  } finally {
    isLoading.value = false
  }
}

// 提交评论
const submitComment = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再发表评论')
    return
  }
  
  if (!commentContent.value.trim()) {
    ElMessage.warning('评论内容不能为空')
    return
  }
  
  isLoading.value = true
  try {
    const response = await axios.post('http://localhost:8080/comments', {
      animeId: parseInt(animeId.value),
      userId: parseInt(userStore.userId),
      content: commentContent.value.trim()
    })
    
    if (response.data.code === 200) {
      ElMessage.success('评论发表成功')
      commentContent.value = ''
      currentPage.value = 1
      await fetchComments()
    }
  } catch (error) {
    console.error('发表评论失败:', error)
    ElMessage.error('发表评论失败')
  } finally {
    isLoading.value = false
  }
}

// 修改分页处理方法
const handleJumpPage = async (page) => {
  // 验证页码范围
  if (typeof page === 'number') {
    if (page < 1) {
      currentPage.value = 1
    } else if (page > totalPages.value) {
      currentPage.value = totalPages.value
    } else {
      currentPage.value = page
    }
  } else {
    // 处理输入框的情况
    if (jumpPage.value < 1) {
      currentPage.value = 1
    } else if (jumpPage.value > totalPages.value) {
      currentPage.value = totalPages.value
    } else {
      currentPage.value = jumpPage.value
    }
  }
  
  // 重新获取评论
  await fetchComments()
  
  // 重置跳转页码输入框
  jumpPage.value = ''
}

// 添加监听当前页变化
watch(currentPage, async () => {
  await fetchComments()
})

// 修改 goToLogin 方法
const goToLogin = () => {
  // 触发全局事件，通知 Header 组件打开登录对话框
  window.dispatchEvent(new CustomEvent('openLoginDialog'))
}

// 切换剧集
const switchEpisode = (episodeNumber) => {
  currentEpisode.value = episodeNumber
  router.push(`/anime/${animeId.value}/play/${episodeNumber}`)
}

// 获取当前集数视频URL
const getCurrentEpisodeUrl = () => {
  if (!episodes.value || episodes.value.length === 0) return ''
  const currentEp = episodes.value.find(ep => ep.episode_number === currentEpisode.value)
  return currentEp ? currentEp.video_url : ''
}

// 添加观看历史
const addToWatchHistory = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    // 确保 episodes 数组已加载
    if (!episodes.value || episodes.value.length === 0) {
      console.error('剧集列表未加载')
      return
    }

    // 获取当前集数的完整信息
    const currentEpisodeInfo = episodes.value.find(ep => Number(ep.episode_number) === Number(currentEpisode.value))
    
    // 打印调试信息
    console.log('当前集数信息:', {
      currentEpisode: currentEpisode.value,
      episodes: episodes.value,
      currentEpisodeInfo: currentEpisodeInfo
    })

    if (!currentEpisodeInfo) {
      console.error('未找到当前集数信息')
      return
    }

    const params = new URLSearchParams()
    params.append('userId', userStore.userId)
    params.append('animeId', route.params.id)
    params.append('episodeId', currentEpisodeInfo.id)
    params.append('episodeNumber', currentEpisode.value)

    console.log('添加观看历史，参数:', Object.fromEntries(params))

    const response = await axios.post('http://localhost:8080/watch-history', params)
    
    if (response.data.code === 200) {
      console.log('观看历史添加成功:', response.data)
      // 触发观看历史更新事件
      window.dispatchEvent(new CustomEvent('watchHistoryUpdated'))
    } else {
      console.error('添加观看历史失败:', response.data)
      ElMessage.error(response.data.message || '添加观看历史失败')
    }
  } catch (error) {
    console.error('添加观看历史失败:', error.response?.data || error)
    ElMessage.error('添加观看历史失败，请稍后重试')
  }
}

// 监听路由参数变化
watch(
  () => route.params,
  async (newParams) => {
    if (newParams.id && newParams.episode) {
      animeId.value = parseInt(newParams.id)
      currentEpisode.value = parseInt(newParams.episode)
      await fetchAnimeInfo()
      await fetchEpisodes()
      resetCommentState()
      fetchComments()
      // 在获取到数据后添加观看历史
      await addToWatchHistory()
    }
  },
  { immediate: true }
)

// 组件卸载前清理
onBeforeRouteLeave((to, from, next) => {
  episodes.value = []
  animeInfo.value = {}
  currentEpisode.value = 1
  resetCommentState()
  next()
})

// 组件挂载
onMounted(async () => {
  if (animeId.value) {
    await fetchAnimeInfo()
    await fetchEpisodes()
    fetchComments()
    // 在获取到数据后添加观看历史
    await addToWatchHistory()
  }
})
</script>

<style scoped>
.player-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  color: #fff;
}

.player-container {
  width: 100%;
  background: #000;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
}

.video-player {
  width: 100%;
  aspect-ratio: 16/9;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #1a1a1a;
}

.player-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #666;
}

.player-placeholder .el-icon {
  font-size: 48px;
  margin-bottom: 10px;
}

.anime-info {
  background: #2d2d2d;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.info-header h1 {
  font-size: 24px;
  margin: 0;
}

.episode-title {
  font-size: 18px;
  color: #409EFF;
}

.info-content {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 10px;
}

.info-item {
  line-height: 1.8;
}

.info-item .label {
  color: #909399;
  margin-right: 8px;
}

.episode-list {
  background: #2d2d2d;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.episode-list h2 {
  margin-bottom: 20px;
}

.episode-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 10px;
}

.episode-item {
  padding: 8px;
  text-align: center;
  background: #363636;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.episode-item:hover {
  background: #409EFF;
}

.episode-item.active {
  background: #409EFF;
}

.comments-section {
  background: #2d2d2d;
  border-radius: 8px;
  padding: 20px;
  margin-top: 20px;
}

.comments-section h2 {
  margin: 0 0 20px 0;
  font-size: 20px;
  color: #fff;
}

.comment-input {
  margin-bottom: 20px;
}

.submit-btn {
  margin-top: 10px;
}

.login-tip {
  text-align: center;
  padding: 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
  margin-bottom: 20px;
}

.comments-list {
  min-height: 200px;
}

.comment-item {
  padding: 15px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.username {
  color: #409EFF;
  font-weight: bold;
}

.time {
  color: #666;
  font-size: 12px;
}

.comment-content {
  color: #fff;
  line-height: 1.5;
  word-break: break-all;
}

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
  margin: 0;
  padding: 0;
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
  padding: 0 8px;
}

.page-item:hover:not(.disabled):not(.active) {
  background: rgba(64, 158, 255, 0.5);
}

.page-item.active {
  background: #409EFF;
}

.page-item.disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.page-item.page-ellipsis {
  background: transparent;
  cursor: default;
}

.page-item.page-ellipsis:hover {
  background: transparent;
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

.page-jump input::-webkit-inner-spin-button,
.page-jump input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.loading-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #000;
  color: #fff;
  aspect-ratio: 16/9;
}

.loading-placeholder .el-icon {
  font-size: 48px;
  margin-bottom: 16px;
}
</style> 