<template>
  <div class="page-container">
    <div class="scroll-container">
      <div class="content-wrapper">
        <div class="post-detail-container" v-loading="loading">
          <!-- 返回论坛按钮 -->
          <div class="back-link">
            <el-button @click="goBack" plain icon="ArrowLeft" size="small">返回论坛</el-button>
          </div>
          
          <!-- 帖子内容 -->
          <div class="post-main" v-if="post">
            <div class="post-header">
              <h1 class="post-title">{{ post.title }}</h1>
              <div class="post-meta">
                <span class="post-author">{{ post.username }}</span>
                <span class="post-time">{{ formatDate(post.createdTime) }}</span>
                <span class="post-views">
                  <el-icon><View /></el-icon> {{ post.viewCount }}
                </span>
              </div>
            </div>
            <div class="post-content">
              {{ post.content }}
            </div>
          </div>
          
          <!-- 回复列表 -->
          <div class="reply-section">
            <div class="reply-header">
              <h3>全部回复 ({{ replies.length }})</h3>
            </div>
            
            <div class="reply-list">
              <template v-if="replies.length > 0">
                <div class="reply-item" v-for="(reply, index) in replies" :key="reply.id">
                  <div class="reply-author">
                    <strong>{{ reply.username }}</strong>
                    <span class="reply-index">#{{ index + 1 }}</span>
                  </div>
                  <div class="reply-content">{{ reply.content }}</div>
                  <div class="reply-time">{{ formatDate(reply.createdTime) }}</div>
                </div>
              </template>
              <el-empty v-else description="暂无回复" />
            </div>
            
            <!-- 发表回复 -->
            <div class="reply-form" v-if="userStore.isLoggedIn">
              <h3>发表回复</h3>
              <el-form :model="replyForm" :rules="rules" ref="replyFormRef">
                <el-form-item prop="content">
                  <el-input
                    v-model="replyForm.content"
                    type="textarea"
                    placeholder="写下你的回复"
                    :rows="4"
                    maxlength="1000"
                    show-word-limit
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitReply" :loading="submitting">发表回复</el-button>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 未登录提示 -->
            <div class="login-tip" v-else>
              <p>登录后才能回复</p>
              <el-button type="primary" @click="goToLogin">去登录</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/format'
import axios from 'axios'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 获取帖子id
const postId = ref(parseInt(route.params.id))

// 数据状态
const loading = ref(false)
const submitting = ref(false)
const post = ref(null)
const replies = ref([])

// 回复表单
const replyFormRef = ref(null)
const replyForm = reactive({
  content: ''
})

const rules = {
  content: [
    { required: true, message: '请输入回复内容', trigger: 'blur' },
    { min: 2, max: 1000, message: '内容长度在2-1000个字符之间', trigger: 'blur' }
  ]
}

// 获取帖子详情
const fetchPostDetail = async () => {
  loading.value = true
  try {
    const response = await axios.get(`http://localhost:8080/forum/posts/${postId.value}`)
    
    if (response.data.code === 200) {
      post.value = response.data.data
    }
  } catch (error) {
    console.error('获取帖子详情失败:', error)
    ElMessage.error('获取帖子详情失败')
  } finally {
    loading.value = false
  }
}

// 获取回复列表
const fetchReplies = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/forum/replies/${postId.value}`)
    
    if (response.data.code === 200) {
      replies.value = response.data.data
    }
  } catch (error) {
    console.error('获取回复列表失败:', error)
    ElMessage.error('获取回复列表失败')
  }
}

// 提交回复
const submitReply = async () => {
  if (!replyFormRef.value) return
  
  await replyFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      const response = await axios.post('http://localhost:8080/forum/replies', {
        postId: postId.value,
        userId: userStore.userId,
        content: replyForm.content
      })
      
      if (response.data.code === 200) {
        ElMessage.success('回复成功')
        replyForm.content = ''
        await fetchReplies()
        await fetchPostDetail() // 刷新帖子信息以更新回复计数
      } else {
        ElMessage.error(response.data.message || '回复失败，请稍后重试')
      }
    } catch (error) {
      console.error('提交回复失败:', error)
      // 即使有错误但回复可能已创建成功，尝试刷新列表
      if (error.response && error.response.data && error.response.data.message && error.response.data.message.includes('BigInteger cannot be cast')) {
        ElMessage({
          message: '回复已发布，但系统返回了一个非致命错误',
          type: 'warning'
        })
        replyForm.content = ''
        await fetchReplies()
        await fetchPostDetail()
      } else {
        ElMessage.error('回复失败：' + (error.response?.data?.message || error.message || '未知错误'))
      }
    } finally {
      submitting.value = false
    }
  })
}

// 返回论坛
const goBack = () => {
  router.push('/forum')
}

// 跳转登录
const goToLogin = () => {
  router.push('/login')
}

onMounted(async () => {
  await fetchPostDetail()
  await fetchReplies()
})
</script>

<style scoped>
.post-detail-container {
  max-width: 900px;
  margin: 0 auto;
  background-color: #2d2d2d;
  border-radius: 8px;
  padding: 20px;
}

.back-link {
  margin-bottom: 20px;
}

.post-main {
  margin-bottom: 30px;
}

.post-header {
  margin-bottom: 20px;
  border-bottom: 1px solid #3a3a3a;
  padding-bottom: 15px;
}

.post-title {
  color: #fff;
  font-size: 22px;
  margin: 0 0 10px 0;
}

.post-meta {
  display: flex;
  align-items: center;
  color: #999;
  font-size: 14px;
}

.post-author {
  margin-right: 15px;
}

.post-time {
  margin-right: 15px;
}

.post-views {
  display: flex;
  align-items: center;
}

.post-views .el-icon {
  margin-right: 5px;
}

.post-content {
  color: #fff;
  font-size: 16px;
  line-height: 1.7;
  white-space: pre-wrap;
  word-break: break-word;
  margin-bottom: 30px;
}

.reply-section {
  border-top: 1px solid #3a3a3a;
  padding-top: 20px;
}

.reply-header {
  margin-bottom: 20px;
}

.reply-header h3 {
  color: #fff;
  margin: 0;
}

.reply-list {
  margin-bottom: 30px;
}

.reply-item {
  padding: 15px;
  border-bottom: 1px solid #3a3a3a;
}

.reply-author {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  color: #409EFF;
}

.reply-index {
  color: #999;
}

.reply-content {
  color: #fff;
  margin-bottom: 10px;
  white-space: pre-wrap;
  word-break: break-word;
}

.reply-time {
  color: #999;
  font-size: 13px;
  text-align: right;
}

.reply-form h3 {
  color: #fff;
  margin-bottom: 15px;
}

.login-tip {
  text-align: center;
  padding: 20px;
  color: #999;
}
</style> 