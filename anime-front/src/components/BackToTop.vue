<template>
  <div class="back-to-top" 
       v-show="show" 
       @click="scrollToTop">
    <el-icon><ArrowUpBold /></el-icon>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ArrowUpBold } from '@element-plus/icons-vue'

const show = ref(false)

// 处理滚动事件
const handleScroll = () => {
  const scrollContainer = document.querySelector('.scroll-container')
  if (scrollContainer) {
    show.value = scrollContainer.scrollTop > 300
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

// 挂载时添加监听
onMounted(() => {
  const scrollContainer = document.querySelector('.scroll-container')
  if (scrollContainer) {
    scrollContainer.addEventListener('scroll', handleScroll)
  }
})

// 卸载时移除监听
onUnmounted(() => {
  const scrollContainer = document.querySelector('.scroll-container')
  if (scrollContainer) {
    scrollContainer.removeEventListener('scroll', handleScroll)
  }
})
</script>

<style scoped>
.back-to-top {
  position: fixed;
  right: 40px;
  bottom: 40px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #409EFF;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  z-index: 999;
}

.back-to-top:hover {
  background-color: #66b1ff;
  transform: translateY(-3px);
}

.back-to-top .el-icon {
  font-size: 20px;
}
</style>