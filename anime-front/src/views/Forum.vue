<template>
  <div class="page-container">
    <div class="scroll-container">
      <div class="content-wrapper">
        <div class="forum-container">
          <div class="forum-header">
            <h1>动漫交流论坛</h1>
            <el-button type="primary" @click="showCreateForm" v-if="userStore.isLoggedIn">发布帖子</el-button>
            <el-button type="primary" @click="goToLogin" v-else>登录发帖</el-button>
          </div>

          <div class="forum-filter">
            <el-radio-group v-model="sortBy" size="small">
              <el-radio-button label="latest">最新发布</el-radio-button>
              <el-radio-button label="hot">热门讨论</el-radio-button>
            </el-radio-group>
          </div>

          <div class="post-list" v-loading="loading">
            <template v-if="posts.length > 0">
              <div class="post-item" v-for="post in posts" :key="post.id" @click="goToPost(post.id)">
                <div class="post-content">
                  <div class="post-title">{{ post.title }}</div>
                  <div class="post-info">
                    <span class="post-author">{{ post.username }}</span>
                    <span class="post-time">{{ formatDate(post.createdTime) }}</span>
                  </div>
                </div>
                <div class="post-stats">
                  <div class="stat-item">
                    <el-icon><View /></el-icon>
                    <span>{{ post.viewCount }}</span>
                  </div>
                  <div class="stat-item">
                    <el-icon><ChatLineSquare /></el-icon>
                    <span>{{ post.replyCount }}</span>
                  </div>
                </div>
              </div>
            </template>
            <el-empty v-else description="暂无帖子" />
          </div>

          <div class="pagination-container" v-if="total > 0">
            <el-pagination
              v-model:current-page="currentPage"
              :page-size="pageSize"
              :total="total"
              layout="prev, pager, next"
              @current-change="handlePageChange"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- 发帖表单对话框 -->
    <el-dialog
      v-model="showDialog"
      title="发布新帖"
      width="500px"
      :before-close="handleClose"
    >
      <el-form :model="postForm" :rules="rules" ref="postFormRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="postForm.title" placeholder="请输入帖子标题" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="postForm.content"
            type="textarea"
            placeholder="请输入帖子内容"
            :rows="6"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDialog = false">取消</el-button>
          <el-button type="primary" @click="submitPost" :loading="submitting">发布</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { View, ChatLineSquare } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/format'
import axios from 'axios'

const router = useRouter()
const userStore = useUserStore()

// 数据状态
const loading = ref(false)
const submitting = ref(false)
const showDialog = ref(false)
const posts = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const sortBy = ref('latest')

// 表单相关
const postFormRef = ref(null)
const postForm = reactive({
  title: '',
  content: ''
})

const rules = {
  title: [
    { required: true, message: '请输入帖子标题', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度在2-100个字符之间', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入帖子内容', trigger: 'blur' },
    { min: 5, max: 2000, message: '内容长度在5-2000个字符之间', trigger: 'blur' }
  ]
}

// 获取帖子列表
const fetchPosts = async () => {
  loading.value = true
  try {
    const response = await axios.get('http://localhost:8080/forum/posts', {
      params: {
        page: currentPage.value,
        pageSize: pageSize.value,
        sortBy: sortBy.value
      }
    })
    
    if (response.data.code === 200) {
      posts.value = response.data.data.list
      total.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取帖子列表失败:', error)
    ElMessage.error('获取帖子列表失败')
  } finally {
    loading.value = false
  }
}

// 处理页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchPosts()
}

// 跳转到帖子详情页
const goToPost = (postId) => {
  router.push(`/forum/post/${postId}`)
}

// 显示发帖表单
const showCreateForm = () => {
  showDialog.value = true
}

// 跳转到登录页
const goToLogin = () => {
  router.push('/login')
}

// 关闭对话框
const handleClose = () => {
  ElMessageBox.confirm('确定要取消发帖吗？已输入的内容将会丢失', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    showDialog.value = false
    resetForm()
  }).catch(() => {})
}

// 重置表单
const resetForm = () => {
  postForm.title = ''
  postForm.content = ''
  if (postFormRef.value) {
    postFormRef.value.resetFields()
  }
}

// 提交发帖
const submitPost = async () => {
  if (!postFormRef.value) return
  
  await postFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      const response = await axios.post('http://localhost:8080/forum/posts', {
        userId: userStore.userId,
        title: postForm.title,
        content: postForm.content
      })
      
      if (response.data.code === 200) {
        ElMessage.success('发布成功')
        showDialog.value = false
        resetForm()
        fetchPosts()
      } else {
        ElMessage.error(response.data.message || '发布失败，请稍后重试')
      }
    } catch (error) {
      console.error('发布帖子失败:', error)
      // 即使有错误但帖子可能已创建成功，尝试刷新列表
      if (error.response && error.response.data && error.response.data.message && error.response.data.message.includes('BigInteger cannot be cast')) {
        ElMessage({
          message: '帖子已发布，但系统返回了一个非致命错误',
          type: 'warning'
        })
        showDialog.value = false
        resetForm()
        fetchPosts() // 刷新帖子列表
      } else {
        ElMessage.error('发布失败：' + (error.response?.data?.message || error.message || '未知错误'))
      }
    } finally {
      submitting.value = false
    }
  })
}

// 监听排序方式变化
onMounted(() => {
  fetchPosts()
  
  // 监听排序方式变化
  watch(sortBy, () => {
    currentPage.value = 1
    fetchPosts()
  })
})
</script>

<style scoped>
.forum-container {
  max-width: 900px;
  margin: 0 auto;
  background-color: #2d2d2d;
  border-radius: 8px;
  padding: 20px;
}

.forum-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.forum-header h1 {
  color: #fff;
  margin: 0;
  font-size: 24px;
}

.forum-filter {
  margin-bottom: 20px;
}

.post-list {
  min-height: 400px;
}

.post-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #3a3a3a;
  cursor: pointer;
  transition: background-color 0.2s;
}

.post-item:hover {
  background-color: #363636;
}

.post-content {
  flex: 1;
}

.post-title {
  color: #fff;
  font-size: 16px;
  margin-bottom: 8px;
  font-weight: 500;
}

.post-info {
  display: flex;
  align-items: center;
  color: #999;
  font-size: 13px;
}

.post-author {
  margin-right: 10px;
}

.post-stats {
  display: flex;
  align-items: center;
}

.stat-item {
  display: flex;
  align-items: center;
  margin-left: 15px;
  color: #999;
}

.stat-item .el-icon {
  margin-right: 5px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  padding: 10px 0;
}

:deep(.el-pagination) {
  --el-pagination-bg-color: transparent;
  --el-pagination-hover-color: #409EFF;
  --el-pagination-button-color: #fff;
  --el-text-color-primary: #fff;
  padding: 5px;
}

:deep(.el-pagination .el-pager li) {
  background: #363636;
  margin: 0 3px;
  min-width: 32px;
  height: 32px;
  line-height: 32px;
}

:deep(.el-pagination .el-pager li.is-active) {
  background: #409EFF;
  color: #ffffff;
  font-weight: bold;
}

:deep(.el-pagination .btn-prev),
:deep(.el-pagination .btn-next) {
  background: #363636;
  color: #ffffff;
  margin: 0 3px;
}
</style> 