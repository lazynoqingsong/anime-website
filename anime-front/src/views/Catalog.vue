<template>
  <div class="page-container">
    <div class="scroll-container">
      <div class="content-wrapper">
        <!-- 筛选区域 -->
        <div class="filter-section">
          <!-- 地区筛选 -->
          <div class="filter-group">
            <div class="filter-label">地区:</div>
            <div class="filter-options">
              <span 
                class="filter-item"
                :class="{ active: selectedArea === '' }"
                @click="selectedArea = ''"
              >全部</span>
              <span 
                v-for="area in areas" 
                :key="area.value"
                :class="['filter-item', { active: selectedArea === area.value }]"
                @click="selectedArea = area.value"
              >
                {{ area.label }}
              </span>
            </div>
          </div>

          <!-- 版本筛选 -->
          <div class="filter-group">
            <div class="filter-label">版本:</div>
            <div class="filter-options">
              <span 
                class="filter-item"
                :class="{ active: selectedVersion === '' }"
                @click="selectedVersion = ''"
              >全部</span>
              <span 
                v-for="version in versions" 
                :key="version.value"
                :class="['filter-item', { active: selectedVersion === version.value }]"
                @click="selectedVersion = version.value"
              >
                {{ version.label }}
              </span>
            </div>
          </div>

          <!-- 首字母筛选 -->
          <div class="filter-group">
            <div class="filter-label">首字母:</div>
            <div class="filter-options">
              <span 
                class="filter-item"
                :class="{ active: selectedLetter === '' }"
                @click="selectedLetter = ''"
              >全部</span>
              <span 
                v-for="letter in letters" 
                :key="letter"
                :class="['filter-item', { active: selectedLetter === letter }]"
                @click="selectedLetter = letter"
              >
                {{ letter }}
              </span>
            </div>
          </div>

          <!-- 年份筛选 -->
          <div class="filter-group">
            <div class="filter-label">年份:</div>
            <div class="filter-options">
              <span 
                class="filter-item"
                :class="{ active: selectedYear === '' }"
                @click="selectedYear = ''"
              >全部</span>
              <span 
                v-for="year in years" 
                :key="year.value"
                :class="['filter-item', { active: selectedYear === year.value }]"
                @click="selectedYear = year.value"
              >
                {{ year.label }}
              </span>
            </div>
          </div>

          <!-- 季度筛选 -->
          <div class="filter-group">
            <div class="filter-label">季度:</div>
            <div class="filter-options">
              <span 
                class="filter-item"
                :class="{ active: selectedSeason === '' }"
                @click="selectedSeason = ''"
              >全部</span>
              <span 
                v-for="season in seasons" 
                :key="season.value"
                :class="['filter-item', { active: selectedSeason === season.value }]"
                @click="selectedSeason = season.value"
              >
                {{ season.label }}
              </span>
            </div>
          </div>

          <!-- 状态筛选 -->
          <div class="filter-group">
            <div class="filter-label">状态:</div>
            <div class="filter-options">
              <span 
                class="filter-item"
                :class="{ active: selectedStatus === '' }"
                @click="selectedStatus = ''"
              >全部</span>
              <span 
                v-for="status in statuses" 
                :key="status.value"
                :class="['filter-item', { active: selectedStatus === status.value }]"
                @click="selectedStatus = status.value"
              >
                {{ status.label }}
              </span>
            </div>
          </div>

          <!-- 类型筛选 -->
          <div class="filter-group">
            <div class="filter-label">类型:</div>
            <div class="filter-options">
              <span 
                class="filter-item"
                :class="{ active: selectedType === '' }"
                @click="selectedType = ''"
              >全部</span>
              <span 
                v-for="type in types" 
                :key="type.value"
                :class="['filter-item', { active: selectedType === type.value }]"
                @click="selectedType = type.value"
              >
                {{ type.label }}
              </span>
            </div>
          </div>

          <!-- 添加重置按钮 -->
          <div class="filter-actions">
            <el-button type="primary" @click="resetFilters">重置筛选</el-button>
          </div>
        </div>

        <!-- 动漫列表展示区域 -->
        <div class="anime-list" v-loading="loading">
          <el-empty v-if="animeList.length === 0" description="暂无数据" />
          <div v-else v-for="anime in animeList" :key="anime.id" class="anime-item">
            <div class="anime-cover">
              <img :src="getCoverImage(anime.cover_image) || 'http://localhost:8080/images/avatar.jpg'" 
                   :alt="anime.title"
                   @error="handleImageError">
            </div>
            <div class="anime-info">
              <h3 class="title">{{ anime.title }}</h3>
              <div class="meta-info">
                <p><span class="label">动画种类:</span> {{ anime.format }}</p>
                <p><span class="label">首播时间:</span> {{ anime.release_date }}</p>
                <p><span class="label">播放状态:</span> {{ anime.status }}</p>
                <p><span class="label">剧情类型:</span>  {{ anime.genres || '暂无' }}</p>
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
                  @click="goToPlay(anime.id)"
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
                  @click="handleCurrentChange(currentPage - 1)">
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
                  @click="handleCurrentChange(currentPage + 1)">
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
      </div>

      <!-- 返回顶部按钮 -->
      <BackToTop />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed, onUnmounted } from 'vue'
import axios from 'axios'
import { ArrowUpBold } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import BackToTop from '@/components/BackToTop.vue'

// 筛选选项数据
const categories = ref({
  region: [],
  format: [],
  letter: [],
  season: [],
  genre: []
})

// 获取分类数据
const fetchCategories = async (type) => {
  try {
    const response = await axios.get(`http://localhost:8080/anime/categories/${type}`)
    if (response.data.code === 200) {
      // 对于season类型，使用id作为value
      if (type === 'season') {
        categories.value[type] = response.data.data.map(item => ({
          label: item.name,
          value: item.id  // 使用category表中的id
        }))
      } else {
        // 其他类型保持不变
        categories.value[type] = response.data.data.map(item => ({
          label: item.name,
          value: item.name
        }))
      }
    }
  } catch (error) {
    console.error(`获取${type}分类失败:`, error)
  }
}
const getCoverImage = (path) => {
  if (!path) {
    return 'http://localhost:8080/images/avatar.jpg'
  }
  // 直接返回完整的URL
  return `http://localhost:8080/images/${path}`
}

onMounted(async () => {
  await Promise.all([
    fetchCategories('region'),
    fetchCategories('format'),
    fetchCategories('letter'),
    fetchCategories('season'),
    fetchCategories('genre')
  ])

  // 挂载时添加滚动监听
  const scrollContainer = document.querySelector('.scroll-container')
  if (scrollContainer) {
    scrollContainer.addEventListener('scroll', handleScroll)
  }

  // 在组件挂载时获取季度数据
  fetchSeasons()
})

// 筛选选项数据
const areas = [
  { label: '日本', value: '日本' },
  { label: '中国', value: '中国' },
  { label: '欧美', value: '欧美' }
]

const versions = [
  { label: 'TV', value: 'TV' },
  { label: '剧场版', value: '剧场版' },
  { label: 'OVA', value: 'OVA' }
]

const letters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']

const years = [
  { label: '2025', value: '2025' },
  { label: '2024', value: '2024' },
  { label: '2023', value: '2023' },
  { label: '2022', value: '2022' },
  { label: '2021', value: '2021' }
]

// 修改seasons数组的定义
const seasons = ref([])

// 在组件挂载时获取季度数据
const fetchSeasons = async () => {
  try {
    const response = await axios.get('http://localhost:8080/anime/categories/season')
    if (response.data.code === 200) {
      seasons.value = response.data.data.map(item => ({
        label: item.name,
        value: item.name // 使用name作为value
      }))
    }
  } catch (error) {
    console.error('获取季度数据失败:', error)
  }
}

const statuses = [
  { label: '连载', value: '连载' },
  { label: '完结', value: '完结' },
  { label: '未播放', value: '未播放' }
]

// 添加类型选项
const types = [
  { label: '搞笑', value: '搞笑' },
  { label: '运动', value: '运动' },
  { label: '励志', value: '励志' },
  { label: '热血', value: '热血' },
  { label: '战斗', value: '战斗' },
  { label: '竞技', value: '竞技' },
  { label: '校园', value: '校园' },
  { label: '青春', value: '青春' },
  { label: '恋爱', value: '恋爱' },
  { label: '后宫', value: '后宫' },
  { label: '治愈', value: '治愈' },
  { label: '推理', value: '推理' },
  { label: '科幻', value: '科幻' },
]

// 选中的筛选值
const selectedArea = ref('')
const selectedVersion = ref('')
const selectedLetter = ref('')
const selectedYear = ref('')
const selectedSeason = ref('')
const selectedStatus = ref('')
const selectedType = ref('')

// 动漫列表数据
const animeList = ref([])
const loading = ref(false)

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const jumpPage = ref('')

// 计算总页数
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

// 计算显示的页码范围
const displayPageCount = 5 // 显示的页码数量
const startPage = computed(() => {
  let start = currentPage.value - Math.floor(displayPageCount / 2)
  return Math.max(1, Math.min(start, totalPages.value - displayPageCount + 1))
})

const endPage = computed(() => {
  return Math.min(startPage.value + displayPageCount - 1, totalPages.value)
})

const displayPages = computed(() => {
  const pages = []
  for (let i = startPage.value; i <= endPage.value; i++) {
    pages.push(i)
  }
  return pages
})

// 页码跳转处理
const handleJumpPage = () => {
  if (jumpPage.value && jumpPage.value > 0 && jumpPage.value <= totalPages.value) {
    handleCurrentChange(jumpPage.value)
  }
  jumpPage.value = ''
}

// 页码变化处理
const handleCurrentChange = (page) => {
  if (page < 1 || page > totalPages.value || page === currentPage.value) return
  currentPage.value = page
  fetchFilteredAnimes()
}

/**
 * 监听筛选条件变化
 * 当任何筛选条件发生变化时,重置页码并重新获取数据
 */
watch([
  selectedArea,
  selectedVersion,
  selectedLetter,
  selectedYear,
  selectedSeason,
  selectedStatus,
  selectedType
], () => {
  console.log('筛选条件变化,重新获取数据...')
  console.log('当前筛选条件:', {
    area: selectedArea.value,
    version: selectedVersion.value,
    letter: selectedLetter.value,
    year: selectedYear.value,
    season: selectedSeason.value,
    status: selectedStatus.value,
    type: selectedType.value
  })
  
  // 重置页码
  currentPage.value = 1
  // 重新获取数据
  fetchFilteredAnimes()
})

/**
 * 获取筛选后的动漫列表
 * 根据所有筛选条件和分页信息获取动漫数据
 */
const fetchFilteredAnimes = async () => {
  loading.value = true
  try {
    const params = {
      region_id: selectedArea.value || '',
      format_id: selectedVersion.value || '',
      letter_id: selectedLetter.value || '',
      year: selectedYear.value || '',
      season_id: selectedSeason.value || '',
      status: selectedStatus.value || '',
      genre: selectedType.value || '',
      currentPage: currentPage.value,
      pageSize: pageSize.value
    }
    
    console.log('发送的筛选参数:', params)
    
    const response = await axios.get('http://localhost:8080/anime/filter', { params })
    console.log('筛选响应数据:', response.data)
    
    if (response.data.code === 200 && response.data.data) {
      animeList.value = response.data.data.list || []
      total.value = response.data.data.total || 0
      console.log('处理后的动漫列表:', animeList.value)
      console.log('总条数:', total.value)
    } else {
      console.error('获取动漫列表失败:', response.data.msg)
      animeList.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取动漫列表失败:', error)
    console.error('详细错误信息:', {
      message: error.message,
      config: error.config,
      status: error.response?.status,
      statusText: error.response?.statusText,
      responseData: error.response?.data
    })
    animeList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

/**
 * 重置所有筛选条件
 */
const resetFilters = () => {
  selectedArea.value = ''
  selectedVersion.value = ''
  selectedLetter.value = ''
  selectedYear.value = ''
  selectedSeason.value = ''
  selectedStatus.value = ''
  selectedType.value = ''
  currentPage.value = 1
}

// 页面加载时获取初始数据
fetchFilteredAnimes()

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.src = 'http://localhost:8080/images/avatar.jpg'
}

// 返回顶部相关
const showBackTop = ref(false)

// 监听滚动事件
const handleScroll = () => {
  const scrollContainer = document.querySelector('.scroll-container')
  if (scrollContainer) {
    showBackTop.value = scrollContainer.scrollTop > 300
  }
}

// 返回顶部方法
const scrollToTop = () => {
  const scrollContainer = document.querySelector('.scroll-container')
  if (scrollContainer) {
    scrollContainer.scrollTo({
      top: 0,
      behavior: 'smooth'
    })
  }
}

// 卸载时移除监听
onUnmounted(() => {
  const scrollContainer = document.querySelector('.scroll-container')
  if (scrollContainer) {
    scrollContainer.removeEventListener('scroll', handleScroll)
  }
})

// 在 script setup 中添加
const router = useRouter()

// 添加跳转方法
const goToDetail = (animeId) => {
  router.push(`/anime/${animeId}`)
}

// 添加在线播放方法
const goToPlay = (animeId) => {
  router.push(`/anime/${animeId}/play/1`) // 默认播放第一集
}
</script>

<style scoped>
.filter-section {
  background-color: #2d2d2d;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 20px;
}

.filter-group {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #3d3d3d;
}

.filter-group:last-child {
  border-bottom: none;
}

.filter-label {
  color: #999;
  width: 60px;
  flex-shrink: 0;
}

.filter-options {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.filter-item {
  color: #fff;
  cursor: pointer;
  padding: 2px 8px;
  border-radius: 2px;
  transition: all 0.3s;
}

.filter-item:hover {
  color: #409EFF;
}

.filter-item.active {
  background-color: #409EFF;
  color: #fff;
}

/* 动漫列表样式 */
.anime-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 10px;
}

.anime-item {
  display: flex;
  gap: 20px;
  background-color: #2d2d2d;
  padding: 15px;
  border-radius: 4px;
  transition: all 0.3s;
}

.anime-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.anime-cover {
  flex-shrink: 0;
  width: 200px;
  height: 280px;
  overflow: hidden;
  border-radius: 4px;
}

.anime-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.anime-cover img:hover {
  transform: scale(1.05);
}

.anime-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 20px;
  color: #fff;
  margin: 0 0 15px 0;
}

.meta-info {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin-bottom: 15px;
}

.meta-info p {
  margin: 0;
  color: #999;
  font-size: 14px;
}

.label {
  color: #666;
  margin-right: 8px;
}

.description {
  color: #999;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 20px 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.action-buttons {
  margin-top: auto;
  display: flex;
  gap: 10px;
}

.detail-btn, .play-btn {
  min-width: 100px;
}

/* 适配移动端 */
@media screen and (max-width: 768px) {
  .anime-item {
    flex-direction: column;
  }
  
  .anime-cover {
    width: 100%;
    height: 200px;
  }
  
  .meta-info {
    grid-template-columns: 1fr;
  }
}

/* 分页样式 */
.pagination-container {
  margin-top: 20px;
  padding: 20px 0;
  display: flex;
  justify-content: center;
}

.pagination-wrapper {
  display: flex;
  align-items: center;
  gap: 15px;
  color: #fff;
}

.page-total {
  font-size: 14px;
  color: #999;
}

.page-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 5px;
}

.page-item {
  min-width: 32px;
  height: 32px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background: #2d2d2d;
  transition: all 0.3s;
  user-select: none;
  padding: 0 10px;
}

.page-item:hover:not(.disabled):not(.active) {
  background: #3d3d3d;
  color: #00a1d6;
}

.page-item.active {
  background: #00a1d6;
  color: #fff;
}

.page-item.disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.page-ellipsis {
  cursor: default;
}

.page-jump {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  color: #999;
}

.page-jump input {
  width: 50px;
  height: 32px;
  border: 1px solid #3d3d3d;
  border-radius: 4px;
  background: #2d2d2d;
  color: #fff;
  text-align: center;
  padding: 0 5px;
}

.page-jump input:focus {
  border-color: #00a1d6;
  outline: none;
}

/* 优化内容宽度 */
.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 返回顶部按钮样式 */
.back-to-top {
  position: fixed;
  right: 40px;
  bottom: 40px;
  width: 40px;
  height: 40px;
  background-color: #00a1d6;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 99;
}

.back-to-top:hover {
  background-color: #00b5e5;
  transform: translateY(-2px);
}

.back-to-top .el-icon {
  font-size: 20px;
}

/* 优化响应式布局 */
@media screen and (max-width: 1400px) {
  .content-wrapper {
    max-width: 1000px;
  }
}

@media screen and (max-width: 1200px) {
  .content-wrapper {
    max-width: 800px;
  }
}

/* 添加重置按钮样式 */
.filter-actions {
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
}
</style>