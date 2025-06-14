<template>
  <div class="page-container">
    <div class="scroll-container">
      <div class="content-wrapper">
    <!-- 用户信息区域 -->
    <div class="user-info">
      <h2>{{ greeting }} {{ userStore.username }}</h2>
    </div>

    <!-- 功能列表区域 -->
    <div class="menu-list">
      <div class="menu-item" @click="handleMenuClick('favorites')">
        <div class="menu-item-left">
          <el-icon><Star /></el-icon>
          <span>我的收藏</span>
          <span class="count">{{ favoriteCount }}</span>
        </div>
        <el-icon><ArrowRight /></el-icon>
      </div>
      
      <div class="menu-item" @click="handleMenuClick('comments')">
        <div class="menu-item-left">
          <el-icon><ChatLineSquare /></el-icon>
          <span>我的评论</span>
          <span class="count">{{ commentCount }}</span>
        </div>
        <el-icon><ArrowRight /></el-icon>
      </div>
      
      <div class="menu-item" @click="handleMenuClick('changePassword')">
        <div class="menu-item-left">
          <el-icon><Lock /></el-icon>
          <span>修改密码</span>
        </div>
        <el-icon><ArrowRight /></el-icon>
      </div>
      
      <div class="menu-item" @click="handleMenuClick('logout')">
        <div class="menu-item-left">
          <el-icon><SwitchButton /></el-icon>
          <span>注销登录</span>
        </div>
        <el-icon><ArrowRight /></el-icon>
      </div>
    </div>
        
    <!-- 添加收藏列表组件 -->
    <div v-if="showFavorites" class="favorites-wrapper">
      <MyFavorites ref="favoritesRef" />
    </div>

    <!-- 添加评论列表组件 -->
    <div v-if="showComments" class="comments-wrapper">
      <MyComments ref="commentsRef" />
      
    </div>
 
    <!-- 修改密码表单 -->
    <div v-if="showChangePassword" class="change-password-wrapper">
      <div class="form-container">
        <h2>修改密码</h2>
        <el-form
          ref="passwordFormRef"
          :model="passwordForm"
          :rules="passwordRules"
          label-width="100px"
          class="password-form"
        >
          <el-form-item label="原密码" prop="oldPassword">
            <el-input
              v-model="passwordForm.oldPassword"
              type="password"
              placeholder="请输入原密码"
            />
          </el-form-item>
          
          <el-form-item label="新密码" prop="newPassword">
            <el-input
              v-model="passwordForm.newPassword"
              type="password"
              placeholder="请输入新密码"
            />
          </el-form-item>
          
          <el-form-item label="重复新密码" prop="confirmPassword">
            <el-input
              v-model="passwordForm.confirmPassword"
              type="password"
              placeholder="请再次输入新密码"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleChangePassword">提交</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
 
 
      </div>
       <!-- 返回顶部按钮 -->
       <BackToTop />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { Star, ChatLineSquare, Lock, SwitchButton, ArrowRight } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import MyComments from '@/components/MyComments.vue'
import BackToTop from '@/components/BackToTop.vue'
import axios from 'axios'
import MyFavorites from '@/components/MyFavorites.vue'

const userStore = useUserStore()
const router = useRouter()

// 问候语
const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 12) return '早上好!'
  if (hour < 18) return '下午好!'
  return '晚上好!'
})

// 添加响应式变量
const showComments = ref(false)
const showChangePassword = ref(false)
const showFavorites = ref(false)
const commentsRef = ref(null)
const favoritesRef = ref(null)
const commentCount = ref(0)
const favoriteCount = ref(0)

// 密码表单数据
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单校验规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 修改密码表单ref
const passwordFormRef = ref(null)

// 获取评论总数
const fetchCommentCount = async () => {
  try {
    const response = await axios.get('http://localhost:8080/comments/user', {
      params: {
        userId: userStore.userId,
        page: 1,
        pageSize: 1
      }
    })
    if (response.data.code === 200) {
      commentCount.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取评论总数失败:', error)
  }
}

// 获取收藏总数
const fetchFavoriteCount = async () => {
  try {
    const response = await axios.get('http://localhost:8080/favorite', {
      params: {
        userId: userStore.userId,
        page: 1,
        pageSize: 1
      }
    })
    if (response.data.code === 200) {
      favoriteCount.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取收藏总数失败:', error)
  }
}

// 处理修改密码提交
const handleChangePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await axios.post('http://localhost:8080/user/change-password', {
          userId: userStore.userId,
          oldPassword: passwordForm.value.oldPassword,
          newPassword: passwordForm.value.newPassword
        })
        
        if (response.data.code === 200) {
          ElMessage.success('密码修改成功')
          showChangePassword.value = false
          // 清空表单
          passwordForm.value = {
            oldPassword: '',
            newPassword: '',
            confirmPassword: ''
          }
        } else {
          ElMessage.error(response.data.message || '密码修改失败')
        }
      } catch (error) {
        console.error('修改密码失败:', error)
        ElMessage.error('修改密码失败，请重试')
      }
    }
  })
}

// 修改菜单点击处理方法
const handleMenuClick = async (action) => {
  switch (action) {
    case 'favorites':
      showFavorites.value = !showFavorites.value
      showChangePassword.value = false
      showComments.value = false
      break
    case 'comments':
      showComments.value = !showComments.value
      showChangePassword.value = false
      showFavorites.value = false
      break
    case 'changePassword':
      showChangePassword.value = !showChangePassword.value
      showComments.value = false
      showFavorites.value = false
      break
    case 'logout':
      ElMessageBox.confirm(
        '确定要注销登录吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        userStore.logout()
        router.push('/')
      }).catch(() => {})
      break
    // 其他功能待实现
  }
}

onMounted(() => {
  fetchCommentCount()
  fetchFavoriteCount()
})
</script>

<style scoped>
/* 引入全局样式 */
@import '@/assets/styles/common.css';

/* 确保文字颜色为白色 */
.content-wrapper {
  color: #fff;
}

.user-info {
  background: #2c2c2c;
  padding: 15px 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.user-info h2 {
  margin: 0;
  font-size: 16px;
  font-weight: normal;
}

.menu-list {
  background: #2c2c2c;
  border-radius: 4px;
  overflow: hidden;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  border-bottom: 1px solid #3a3a3a;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  background-color: #3a3a3a;
}

.menu-item-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.menu-item-left .el-icon {
  font-size: 18px;
}

.count {
  background: #f56c6c;
  color: #fff;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  margin-left: 5px;
}

.menu-item .el-icon:last-child {
  color: #909399;
  font-size: 16px;
}

.empty-data {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 40px;
  color: #909399;
}

.empty-data img {
  width: 120px;
  margin-bottom: 15px;
  opacity: 0.8;
}

.empty-data p {
  font-size: 14px;
  margin: 0;
}

.change-password-wrapper {
  margin-top: 20px;
}

.form-container {
  background: #2c2c2c;
  border-radius: 8px;
  padding: 20px;
}

.form-container h2 {
  font-size: 18px;
  margin: 0 0 20px;
  color: #fff;
}

.password-form {
  max-width: 500px;
}

:deep(.el-form-item__label) {
  color: #fff !important;
}

:deep(.el-input__inner) {
  background-color: #363636 !important;
  border-color: #4c4d4f !important;
  color: #fff !important;
}

:deep(.el-input__inner:focus) {
  border-color: #409EFF !important;
}

:deep(.el-form-item__error) {
  color: #f56c6c !important;
}
</style>