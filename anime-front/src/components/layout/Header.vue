<template>
  <header class="header">
    <div class="header-container">
      <!-- Logo -->
      <div class="logo">
        <div @click="showAnnouncementAndNavigate">
          <h1>AGE动漫</h1>
        </div>
      </div>

      <!-- 导航菜单 -->
      <nav class="nav-menu">
        <router-link 
          v-for="item in menuItems" 
          :key="item.path"
          :to="item.path"
          class="nav-item"
        >
          {{ item.name }}
        </router-link>
      </nav>
     
      <!-- 搜索框 -->
      <div class="search-box">
        <el-autocomplete
          v-model="searchText"
          :fetch-suggestions="querySearchAsync"
          placeholder="请输入番名搜索"
          class="search-input"
          @select="handleSelect"
          @keyup.enter="handleSearch"
          clearable
        >
          <template #default="{ item }">
            <div class="suggestion-item">
              <span>{{ item.value }}</span>
              <el-icon class="delete-icon" @click.stop="removeHistory(item)">
                <Close />
              </el-icon>
            </div>
          </template>
          <template #prefix>
            <el-icon class="search-icon" @click="handleSearch">
              <Search />
            </el-icon>
          </template>
        </el-autocomplete>
      </div>

      <!-- 右侧功能区 -->
      <div class="right-section">
        <!-- 历史记录 -->
        <el-popover
          placement="bottom"
          :width="300"
          trigger="click"
          popper-class="history-popover"
          @show="fetchWatchHistory"
        >
          <template #reference>
            <div class="history-btn-wrapper">
              <el-button 
                class="history-icon" 
                :icon="Clock" 
                text
              >
                <span class="history-text">观看历史</span>
              </el-button>
            </div>
          </template>
          
          <div class="history-content">
            <div class="history-header">
              <span>观看历史</span>
              <el-button 
                type="danger" 
                link 
                @click="clearHistory"
                v-if="watchHistory.length > 0"
              >
                清空记录
              </el-button>
            </div>
            
            <div class="history-list" v-if="watchHistory.length > 0">
              <div 
                v-for="item in watchHistory" 
                :key="item.id" 
                class="history-item"
                @click="goToEpisode(item)"
              >
            
                <div class="history-info">
                  <div class="anime-title" :title="item.anime_title">{{ item.anime_title }}</div>
                  <div class="episode-info">第{{ item.episode_number }}集</div>
                  <div class="watch-time">{{ formatDate(item.watch_time) }}</div>
                </div>
              </div>
            </div>
            
            <el-empty 
              v-else 
              description="暂无观看记录" 
              :image-size="60"
            />
          </div>
        </el-popover>

        <!-- 用户信息/登录按钮 -->
        <div class="user-info">
          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <div class="avatar">
                  <img :src="userStore.userAvatar" alt="用户头像">
                </div>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button class="login-btn" @click="showLoginDialog = true">登录</el-button>
            <el-button class="register-btn" @click="handleShowRegister">注册</el-button>
          </template>
        </div>
      </div>
    </div>
  </header>

  <!-- 添加登录/注册对话框 -->
  <el-dialog
    v-model="showLoginDialog"
    :title="activeTab === 'login' ? '登录' : '注册'"
    width="400px"
    destroy-on-close
    center
  >
    <el-form
      v-if="activeTab === 'login'"
      ref="loginFormRef"
      :model="loginForm"
      :rules="loginRules"
      label-width="0"
    >
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          placeholder="16个字符内的字母、数字"
          :prefix-icon="User"
          clearable
          @focus="handleInputFocus"
          @blur="handleInputBlur"
        />
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          placeholder="6-32位字母、数字或符号"
          :prefix-icon="Lock"
          show-password
          @focus="handleInputFocus"
          @blur="handleInputBlur"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" class="submit-btn" @click="handleLogin">
          登录
        </el-button>
        <el-button link type="primary" @click="switchToRegister">
          没有账号？去注册
        </el-button>
      </el-form-item>
    </el-form>

    <el-form
      v-else
      ref="registerFormRef"
      :model="registerForm"
      :rules="registerRules"
      label-width="0"
    >
      <el-form-item prop="username">
        <el-input
          v-model="registerForm.username"
          placeholder="16个字符内的字母、数字"
          :prefix-icon="User"
        />
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="registerForm.password"
          type="password"
          placeholder="6-32位字母、数字或符号"
          :prefix-icon="Lock"
          show-password
        />
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          placeholder="确认密码"
          :prefix-icon="Lock"
          show-password
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" class="submit-btn" @click="handleRegister">
          注册
        </el-button>
        <el-button link type="primary" @click="switchToLogin">
          已有账号？去登录
        </el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 公告弹窗 -->
  <el-dialog
    v-model="announcementVisible"
    title="网站公告"
    width="50%"
    :modal="true"
    :close-on-click-modal="false"
    :z-index="2000"
    :before-close="handleClose"
    class="announcement-dialog"
    destroy-on-close
    center
  >
    <div class="announcement-content">
      <h2>欢迎来到AGE动漫!</h2>
      
      <div class="announcement-section">
        <h3>🌟 网站介绍</h3>
        <p>AGE动漫是一个专注于提供高质量动漫资源的在线平台，致力于为动漫爱好者打造一个舒适、便捷的观影体验。</p>
      </div>
      
      <div class="announcement-section">
        <h3>✨ 主要功能</h3>
        <ul>
          <li>🎬 <strong>在线观看</strong> - 高清流畅的观看体验，支持多种分辨率</li>
          <li>📅 <strong>新番时间表</strong> - 及时了解每周最新动漫更新情况</li>
          <li>🏆 <strong>动漫排行榜</strong> - 查看最热门、最受欢迎的作品</li>
          <li>🔍 <strong>智能搜索</strong> - 快速找到您想看的动漫</li>
          <li>👍 <strong>个性推荐</strong> - 基于您的观看历史，推荐您可能喜欢的作品</li>
          <li>💬 <strong>交流论坛</strong> - 与其他动漫爱好者分享观点和心得</li>
        </ul>
      </div>
      
      <div class="announcement-section">
        <h3>📢 最新通知</h3>
        <p>我们已全面升级服务器，观影体验更加流畅！新增多部经典动漫，欢迎大家观看和评论。</p>
        <p>如有任何问题或建议，欢迎在论坛中反馈，我们会认真听取每一位用户的意见。</p>
      </div>
      
      <div class="announcement-section">
        <h3>🙏 特别提示</h3>
        <p>本站所有资源均来自互联网，仅供学习交流使用，请勿用于商业用途。喜欢的作品请支持正版！</p>
      </div>
    </div>
    
    <template #footer>
      <div class="dialog-footer">
        <el-checkbox v-model="doNotShowAgain">24小时内不再提示</el-checkbox>
        <el-button type="primary" @click="closeAnnouncement">
          我知道了
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, nextTick, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Search, Clock, User, Lock, Close } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { useUserStore } from '@/stores/user'
import { formatDate } from '@/utils/format'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const searchText = ref('')
const suggestions = ref([])
const watchHistory = ref([])

// 登录相关
const showLoginDialog = ref(false)
const activeTab = ref('login')
const loginFormRef = ref()
const registerFormRef = ref()

const loginForm = ref({
  username: '',
  password: ''
})

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { max: 16, message: '长度不能超过16个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 32, message: '长度在6到32个字符', trigger: 'blur' }
  ]
}

const registerRules = {
  ...loginRules,
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 获取搜索建议
const querySearchAsync = (queryString, cb) => {
  const history = getSearchHistory()
  suggestions.value = history.map(item => ({
    value: item
  }))
  cb(suggestions.value)
}

// 获取搜索历史
const getSearchHistory = () => {
  const history = localStorage.getItem('searchHistory')
  return history ? JSON.parse(history) : []
}

// 保存搜索历史
const saveSearchHistory = (keyword) => {
  if (!keyword) return
  let history = getSearchHistory()
  history = history.filter(item => item !== keyword)
  history.unshift(keyword)
  history = history.slice(0, 5)
  localStorage.setItem('searchHistory', JSON.stringify(history))
  updateSuggestions()
}

// 删除搜索历史
const removeHistory = (item) => {
  let history = getSearchHistory()
  history = history.filter(h => h !== item.value)
  localStorage.setItem('searchHistory', JSON.stringify(history))
  updateSuggestions()
}

// 更新建议列表
const updateSuggestions = () => {
  const history = getSearchHistory()
  suggestions.value = history.map(item => ({
    value: item
  }))
}

// 处理选择历史记录
const handleSelect = (item) => {
  searchText.value = item.value
}

// 处理搜索
const handleSearch = () => {
  if (!searchText.value) return
  saveSearchHistory(searchText.value)
  router.push(`/search?keyword=${encodeURIComponent(searchText.value)}`)
}

// 监听路由变化清空搜索框
watch(
  () => route.path,
  () => {
    searchText.value = ''
    updateSuggestions() // 路由变化时更新建议列表
  }
)

// 获取观看历史
const fetchWatchHistory = async () => {
  if (!userStore.isLoggedIn) return
  try {
    const response = await axios.get(`http://localhost:8080/watch-history?userId=${userStore.userId}`)
    if (response.data.code === 200) {
      watchHistory.value = response.data.data
    }
  } catch (error) {
    console.error('获取观看历史失败:', error)
  }
}

// 清空历史记录
const clearHistory = async () => {
  try {
    const response = await request.delete(`/watch-history?userId=${userStore.userId}`)
    if (response.data.code === 200) {
      watchHistory.value = []
      ElMessage.success('清空观看历史成功')
    }
  } catch (error) {
    ElMessage.error('清空观看历史失败')
  }
}

// 跳转到对应集数
const goToEpisode = (item) => {
  router.push(`/anime/${item.anime_id}/play/${item.episode_number}`)
}

// 处理输入框聚焦
const handleInputFocus = (e) => {
  e.target.select() // 聚焦时选中全部文本
}

// 处理输入框失焦
const handleInputBlur = () => {
  // 可以在这里添加额外的验证逻辑
}

// 修改登录处理方法
const handleLogin = async () => {
  try {
    const valid = await loginFormRef.value?.validate()
    if (!valid) return

    const response = await axios.get('http://localhost:8080/user/selectAll')
    const users = response.data.data
    
    const user = users.find(u => 
      u.username === loginForm.value.username.trim() && 
      u.password === loginForm.value.password
    )
    
    if (user) {
      userStore.login({
        id: user.id,
        username: user.username,
        avatar: user.avatar
      })
      showLoginDialog.value = false
      ElMessage.success('登录成功')
      
      // 清空表单
      loginForm.value = {
        username: '',
        password: ''
      }
    } else {
      ElMessage.error('用户名或密码错误')
    }
  } catch (error) {
    console.error('登录错误:', error)
    ElMessage.error('登录失败，请稍后重试')
  }
}

// 修改注册处理方法
const handleRegister = async () => {
  try {
    const valid = await registerFormRef.value?.validate()
    if (!valid) return
    
    const result = await userStore.register({
      username: registerForm.value.username.trim(),
      password: registerForm.value.password
    })

    if (result.success) {
      ElMessage.success(result.message)
      registerForm.value = {
        username: '',
        password: '',
        confirmPassword: ''
      }
      showLoginDialog.value = false
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    console.error('注册错误:', error)
    ElMessage.error('注册失败，请稍后重试')
  }
}

// 处理下拉菜单命令
const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 切换到注册页面
const handleShowRegister = () => {
  activeTab.value = 'register'
  showLoginDialog.value = true
}

// 切换到注册
const switchToRegister = () => {
  activeTab.value = 'register'
}

// 切换到登录
const switchToLogin = () => {
  activeTab.value = 'login'
}

// 处理登出
const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  if (route.path === '/profile') {
    router.push('/')
  }
}

// 导航菜单项
const menuItems = [
  { name: '首页', path: '/' },
  { name: '目录', path: '/catalog' },
  { name: '一周更新', path: '/weekly' },
   { name: '排行榜', path: '/ranking' }
]

// 公告弹窗相关
const announcementVisible = ref(false)
const doNotShowAgain = ref(false)

// 修改公告弹窗相关方法
const showAnnouncementAndNavigate = (event) => {
  // 阻止默认跳转
  event.preventDefault()
  
  // 检查是否在24小时内设置了不再显示
  const expireTimeStr = localStorage.getItem('announcementExpireTime')
  const currentTime = new Date().getTime()
  
  if (expireTimeStr && currentTime < parseInt(expireTimeStr)) {
    console.log('公告在24小时禁用期内，不显示')
    // 如果当前不在首页，导航到首页
    if (route.path !== '/') {
      router.push('/')
    }
    return
  }
  
  // 已过期或未设置，显示公告
  announcementVisible.value = true
  
  // 如果当前不在首页，则导航到首页
  if (route.path !== '/') {
    router.push('/')
  }
}

// 原有方法保留但修改
const showAnnouncement = () => {
  // 检查是否在24小时内设置了不再显示
  const expireTimeStr = localStorage.getItem('announcementExpireTime')
  const currentTime = new Date().getTime()
  
  if (expireTimeStr && currentTime < parseInt(expireTimeStr)) {
    console.log('公告在24小时禁用期内，不显示')
    return
  }
  
  announcementVisible.value = true
}

// 关闭公告弹窗
const closeAnnouncement = () => {
  announcementVisible.value = false
  if (doNotShowAgain.value) {
    // 计算24小时后的时间戳
    const expireTime = new Date().getTime() + 24 * 60 * 60 * 1000
    // 存储到期时间而不是简单的布尔值
    localStorage.setItem('announcementExpireTime', expireTime.toString())
    console.log('设置24小时内不再显示公告，到期时间：', new Date(expireTime).toLocaleString())
  } else {
    // 清除过期时间设置
    localStorage.removeItem('announcementExpireTime')
  }
}

// 处理弹窗关闭
const handleClose = () => {
  closeAnnouncement()
}

// 在组件加载时检查是否需要自动显示公告
onMounted(() => {
  fetchWatchHistory()
  // 监听观看历史更新事件
  window.addEventListener('watchHistoryUpdated', fetchWatchHistory)
  // 添加打开登录对话框的事件监听
  window.addEventListener('openLoginDialog', () => {
    showLoginDialog.value = true
    activeTab.value = 'login'
  })
  // 检查是否在24小时内设置了不再显示
  const expireTimeStr = localStorage.getItem('announcementExpireTime')
  const currentTime = new Date().getTime()
  const visited = sessionStorage.getItem('visited')
  
  console.log('公告显示检查 - 过期时间:', expireTimeStr ? new Date(parseInt(expireTimeStr)).toLocaleString() : '未设置', '已访问:', visited)
  
  // 如果设置了过期时间且未过期，则不显示
  if (expireTimeStr && currentTime < parseInt(expireTimeStr)) {
    console.log('公告在24小时禁用期内，不显示')
    // 标记本次会话已访问，避免刷新页面后再次检查
    sessionStorage.setItem('visited', 'true')
  } else if (!visited) {
    // 首次访问且未设置不再显示或已过期，则显示公告
    console.log('显示首次访问公告')
    announcementVisible.value = true
    // 标记本次会话已访问
    sessionStorage.setItem('visited', 'true')
  }
  
  // 勾选框默认状态根据是否存在未过期的设置决定
  doNotShowAgain.value = expireTimeStr && currentTime < parseInt(expireTimeStr)
})

// 组件卸载时移除监听器
onBeforeUnmount(() => {
  window.removeEventListener('watchHistoryUpdated', fetchWatchHistory)
  // 移除打开登录对话框的事件监听
  window.removeEventListener('openLoginDialog', () => {
    showLoginDialog.value = true
    activeTab.value = 'login'
  })
})

// 监听登录状态变化，重新获取历史记录
watch(() => userStore.isLoggedIn, (newVal) => {
  if (newVal) {
    fetchWatchHistory()
  } else {
    watchHistory.value = []
  }
})
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background-color: #2c2c2c;
  color: #fff;
  z-index: 1000;
}

.header-container {
  max-width: 1200px;
  height: 100%;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo a {
  text-decoration: none;
  color: #fff;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
}

.nav-menu {
  display: flex;
  gap: 20px;
}

.nav-item {
  color: #fff;
  text-decoration: none;
  font-size: 16px;
  padding: 5px 10px;
}

.nav-item:hover {
  color: #ff6b6b;
}

.nav-item.router-link-active {
  color: #ff6b6b;
}

.el-button.history-icon {
  
 background-color: rgb(220, 53, 69);
 color: white;
}
.el-button.history-icon:hover {
  background-color: rgb(187, 45, 59);
  
}



.user-info .avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
}

.user-info .avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}


.login-btn,
.register-btn {
  color: #fff;
  text-decoration: none;
  padding: 6px 15px;
  border-radius: 4px;
  transition: all 0.3s;
}



.register-btn {
  background:rgb(220, 53, 69);
  margin-left: 10px;
  text-align: center;
}

.login-btn {
  background:rgb(220, 53, 69);
  text-align: center;
}

.register-btn:hover {
  background: #ff5252;
}




.right-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

/* 添加新样式 */
:deep(.el-dialog) {
  border-radius: 8px;
}

:deep(.el-tabs__item.is-active) {
  color: rgb(220, 53, 69);
}

:deep(.el-tabs__active-bar) {
  background-color: rgb(220, 53, 69);
}

.submit-btn {
  width: 100%;
  background-color: rgb(220, 53, 69);
  border-color: rgb(220, 53, 69);
}

.submit-btn:hover {
  background-color: rgb(187, 45, 59);
  border-color: rgb(187, 45, 59);
}

.search-box {
  flex: 1;
  max-width: 500px;
  margin: 0 20px;
}

.search-input {
  width: 100%;
}

:deep(.el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.1);
  box-shadow: none !important;
}

:deep(.el-input__inner) {
  color: #fff;
  box-shadow: none;
}


/* :deep(.el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.5) !important;
  opacity: 1 !important;
} */


:deep(.el-input__prefix-inner) {
  color: rgba(255, 255, 255, 0.5);
}

:deep(.el-input__suffix-inner) {
  color: rgba(255, 255, 255, 0.5);
}

.suggestion-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px;
}

.delete-icon {
  color: #909399;
  cursor: pointer;
}

.delete-icon:hover {
  color: #f56c6c;
}

.search-icon {
  cursor: pointer;
  color: #fff;
}

.history-popover {
  background: #1f1f1f;
  border: 1px solid #333;
}

.history-content {
  padding: 12px;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
  margin-bottom: 12px;
}

.history-header span {
  font-size: 16px;
  font-weight: 500;
}

.history-list {
  max-height: 400px;
  overflow-y: auto;
}

.history-item {
  display: flex;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 8px;
}

.history-item:hover {
  background-color: #f5f7fa;
}

.history-cover {
  width: 100px;
  height: 60px;
  margin-right: 12px;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
}

.history-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.history-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.anime-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
  white-space: normal;
  word-break: break-all;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-clamp: 2;
  overflow: hidden;
  line-height: 1.3;
}

.episode-info {
  font-size: 13px;
  color: #409eff;
  margin-bottom: 4px;
}

.watch-time {
  font-size: 12px;
  color: #909399;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.history-icon {
  font-size: 20px;
  color: #909399;
  padding: 8px;
  border-radius: 50%;
}

.history-icon:hover {
  background-color: rgba(144, 147, 153, 0.1);
}

/* 添加新的样式 */
.history-btn-wrapper {
  display: flex;
  align-items: center;
}

.history-icon {
  display: flex;
  align-items: center;
  gap: 4px;
}

.history-text {
  font-size: 14px;
  margin-left: 4px;
}

/* 修改表单样式 */
:deep(.el-input__inner) {
  background-color: #fff !important;
  color: #333 !important;
}

:deep(.el-input__inner:focus) {
  border-color: #409EFF !important;
}

/* 公告弹窗样式 */
.announcement-dialog {
  z-index: 2000 !important;
}

.announcement-dialog :deep(.el-dialog__header) {
  background-color: #409EFF;
  padding: 15px 20px;
}

.announcement-dialog :deep(.el-dialog__title) {
  color: #fff;
  font-weight: bold;
  font-size: 18px;
}

.announcement-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #fff;
}

.announcement-content {
  padding: 10px;
  max-height: 60vh;
  overflow-y: auto;
}

.announcement-content h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #409EFF;
}

.announcement-section {
  margin-bottom: 20px;
}

.announcement-section h3 {
  margin-bottom: 10px;
  color: #409EFF;
  border-bottom: 1px solid #eee;
  padding-bottom: 5px;
}

.announcement-section ul {
  padding-left: 20px;
}

.announcement-section li {
  margin-bottom: 8px;
  list-style-type: none;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo div {
  cursor: pointer;
}
</style> 