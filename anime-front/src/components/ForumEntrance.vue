<template>
  <div class="forum-entrance" @click="goToForum" :title="'前往论坛'">
    <el-icon class="forum-icon"><ChatLineRound /></el-icon>
    <span class="forum-text">论坛</span>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ChatLineRound } from '@element-plus/icons-vue'

const router = useRouter()

const goToForum = () => {
  router.push('/forum')
}
</script>

<style scoped>
.forum-entrance {
  position: fixed;
  right: 40px;
  bottom: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  z-index: 999;
  background-color: #409EFF;
  border-radius: 50%;
  width: 60px;
  height: 60px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  padding: 10px;
}

.forum-entrance:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
}

.forum-icon {
  font-size: 24px;
  color: #fff;
  margin-bottom: 4px;
}

.forum-text {
  font-size: 12px;
  color: #fff;
  font-weight: bold;
}
</style> 