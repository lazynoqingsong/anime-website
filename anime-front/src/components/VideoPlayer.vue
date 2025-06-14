<template>
  <div class="video-player-container" ref="playerContainer">
    <video
      ref="videoRef"
      class="video-element"
      @timeupdate="handleTimeUpdate"
      @loadedmetadata="handleMetadata"
      @play="isPlaying = true"
      @pause="isPlaying = false"
    >
      <source :src="proxyVideoUrl" type="video/mp4">
      您的浏览器不支持 HTML5 视频播放
    </video>

    <!-- 上次观看进度提示 -->
    <div 
      v-if="showLastProgress" 
      class="last-progress-tip"
      @click="jumpToLastProgress"
    >
      上次观看到：{{ formatTime(lastProgressTime) }}
      <span class="jump-text">点击跳转</span>
    </div>

    <!-- 控制栏 -->
    <div class="controls-overlay" v-show="showControls">
      <!-- 进度条 -->
      <div class="progress-bar" @click="handleProgressClick">
        <div class="progress-background"></div>
        <div class="progress-current" :style="{ width: progress + '%' }"></div>
        <div class="progress-handle" :style="{ left: progress + '%' }"></div>
      </div>

      <!-- 控制按钮组 -->
      <div class="controls-bar">
        <div class="left-controls">
          <!-- 播放/暂停按钮 -->
          <button class="control-btn" @click="togglePlay">
            <el-icon><VideoPlay v-if="!isPlaying" /><VideoPause v-else /></el-icon>
          </button>

          <!-- 音量控制 -->
          <div class="volume-control">
            <button class="control-btn" @click="toggleMute">
              <el-icon>
                <Mute v-if="isMuted" />
                <VideoCamera v-else />
              </el-icon>
            </button>
            <div class="volume-slider" @click.stop>
              <el-slider 
                v-model="volume" 
                :min="0" 
                :max="100"
                vertical 
                height="60px"
              />
            </div>
          </div>

          <!-- 时间显示 -->
          <div class="time-display">
            {{ formatTime(currentTime) }} / {{ formatTime(duration) }}
          </div>
        </div>

        <div class="right-controls">
          <!-- 播放速度设置 -->
          <el-dropdown trigger="click" @command="handleSpeedChange">
            <button class="control-btn">
              <el-icon><Setting /></el-icon>
            </button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item 
                  v-for="speed in playbackSpeeds" 
                  :key="speed"
                  :command="speed"
                  :class="{ active: currentSpeed === speed }"
                >
                  {{ speed }}x
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <!-- 全屏按钮 -->
          <button class="control-btn" @click="toggleFullscreen">
            <el-icon><FullScreen v-if="!isFullscreen" /><Aim v-else /></el-icon>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, onBeforeUnmount, computed } from 'vue'
import { 
  VideoPlay, 
  VideoPause, 
  VideoCamera, 
  Mute,
  Setting,
  FullScreen,
  Aim
} from '@element-plus/icons-vue'

const props = defineProps({
  videoUrl: {
    type: String,
    required: true
  },
  animeId: {
    type: Number,
    required: true
  },
  episodeNumber: {
    type: Number,
    required: true
  }
})

// 修改代理URL处理
const proxyVideoUrl = computed(() => {
  if (!props.videoUrl) return ''
  
  // 如果是本地视频文件（假设以 local: 开头）
  if (props.videoUrl.startsWith('local:')) {
    const fileName = props.videoUrl.replace('local:', '')
    return `http://localhost:8080/api/proxy/local-video/${fileName}`
  }
  
  // 如果是 akamaized.net 的链接，使用代理
  if (props.videoUrl.includes('akamaized.net')) {
    const timestamp = new Date().getTime()
    return `http://localhost:8080/api/proxy/video?url=${encodeURIComponent(props.videoUrl)}&t=${timestamp}`
  }
  
  return props.videoUrl
})

// 视频相关引用
const videoRef = ref(null)
const playerContainer = ref(null)

// 播放器状态
const isPlaying = ref(false)
const isMuted = ref(false)
const volume = ref(100)
const currentTime = ref(0)
const duration = ref(0)
const progress = ref(0)
const showControls = ref(true)
const isFullscreen = ref(false)
const currentSpeed = ref(1)
const controlsTimer = ref(null)
const progressSaveTimer = ref(null)

// 播放速度选项
const playbackSpeeds = [0.5, 0.75, 1, 1.25, 1.5, 2]

// 添加新的响应式变量
const showLastProgress = ref(false)
const lastProgressTime = ref(0)

// 控制播放暂停
const togglePlay = async () => {
  const video = videoRef.value
  try {
    if (video.paused) {
      await video.play()
      isPlaying.value = true
    } else {
      await video.pause()
      isPlaying.value = false
    }
  } catch (error) {
    console.error('播放控制出错:', error)
  }
}

// 控制音量
const toggleMute = () => {
  const video = videoRef.value
  video.muted = !video.muted
  isMuted.value = video.muted
}

// 监听音量变化
watch(volume, (newVolume) => {
  const video = videoRef.value
  video.volume = newVolume / 100
  isMuted.value = newVolume === 0
})

// 处理进度条点击
const handleProgressClick = (e) => {
  const video = videoRef.value
  const progressBar = e.currentTarget
  const rect = progressBar.getBoundingClientRect()
  const percent = (e.clientX - rect.left) / rect.width
  if (video && !isNaN(video.duration)) {
    video.currentTime = percent * video.duration
    progress.value = percent * 100
  }
}

// 处理播放速度变化
const handleSpeedChange = (speed) => {
  const video = videoRef.value
  video.playbackRate = speed
  currentSpeed.value = speed
}

// 处理全屏
const toggleFullscreen = async () => {
  if (!document.fullscreenElement) {
    await playerContainer.value.requestFullscreen()
    isFullscreen.value = true
  } else {
    await document.exitFullscreen()
    isFullscreen.value = false
  }
}

// 格式化时间
const formatTime = (seconds) => {
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = Math.floor(seconds % 60)
  return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`
}

// 更新进度
const handleTimeUpdate = () => {
  const video = videoRef.value
  if (video && !isNaN(video.duration)) {
    currentTime.value = video.currentTime
    progress.value = (video.currentTime / video.duration) * 100
  }
}

// 获取上次播放进度
const getLastProgress = () => {
  const key = `anime_progress_${props.animeId}_${props.episodeNumber}`
  const savedTime = localStorage.getItem(key)
  return savedTime ? parseFloat(savedTime) : 0
}

// 添加上次进度提示组件的点击处理
const jumpToLastProgress = () => {
  const video = videoRef.value
  if (video && lastProgressTime.value > 0) {
    video.currentTime = lastProgressTime.value
    currentTime.value = lastProgressTime.value
    progress.value = (lastProgressTime.value / video.duration) * 100
    showLastProgress.value = false
    
    // 自动开始播放
    video.play().catch(error => {
      console.error('自动播放失败:', error)
    })
  }
}

// 修改视频URL监听，确保正确清理和重置状态
watch(() => props.videoUrl, (newUrl, oldUrl) => {
  if (newUrl === oldUrl) return // 如果URL没有变化，直接返回
  
  const video = videoRef.value
  if (video) {
    // 清理原有状态
    if (controlsTimer.value) {
      clearTimeout(controlsTimer.value)
    }
    if (progressSaveTimer.value) {
      clearInterval(progressSaveTimer.value)
    }
    
    // 重置播放器状态
    isPlaying.value = false
    video.pause()
    video.currentTime = 0
    currentTime.value = 0
    progress.value = 0
    
    // 重要：等待视频元数据加载完成后再设置时间
    video.addEventListener('loadedmetadata', () => {
      // 获取新视频的播放进度
      const lastTime = getLastProgress()
      if (lastTime > 0) {
        video.currentTime = lastTime
        currentTime.value = lastTime
        progress.value = (lastTime / video.duration) * 100
        
        // 显示进度提示
        lastProgressTime.value = lastTime
        if (lastTime < video.duration - 30) {
          showLastProgress.value = true
          setTimeout(() => {
            showLastProgress.value = false
          }, 3000)
        }
      }
    }, { once: true }) // 使用 once 确保事件监听器只触发一次
    
    // 重新加载视频
    video.load()
  }
}, { immediate: true })

// 修改视频元数据加载处理函数
const handleMetadata = () => {
  const video = videoRef.value
  if (!video) return
  
  duration.value = video.duration
  
  // 如果还没有设置过时间，则获取上次播放进度
  if (currentTime.value === 0) {
    const lastTime = getLastProgress()
    if (lastTime > 0) {
      video.currentTime = lastTime
      currentTime.value = lastTime
      progress.value = (lastTime / video.duration) * 100
      
      lastProgressTime.value = lastTime
      if (lastTime < video.duration - 30) {
        showLastProgress.value = true
        setTimeout(() => {
          showLastProgress.value = false
        }, 3000)
      }
    }
  }
  
  // 确保视频暂停
  video.pause()
  isPlaying.value = false
}

// 修改保存进度的方法，增加保存条件
const savePlayProgress = () => {
  const video = videoRef.value
  if (video && video.currentTime > 0 && !video.paused) {  // 只在播放时且有进度时保存
    const key = `anime_progress_${props.animeId}_${props.episodeNumber}`
    localStorage.setItem(key, video.currentTime.toString())
  }
}

// 自动隐藏控制栏
const startControlsTimer = () => {
  showControls.value = true
  if (controlsTimer.value) {
    clearTimeout(controlsTimer.value)
  }
  controlsTimer.value = setTimeout(() => {
    if (!videoRef.value?.paused) {
      showControls.value = false
    }
  }, 3000)
}

// 监听鼠标移动
onMounted(() => {
  // 添加事件监听
  if (playerContainer.value) {
    playerContainer.value.addEventListener('mousemove', startControlsTimer)
    playerContainer.value.addEventListener('mouseleave', () => {
      showControls.value = false
    })
  }
  
  // 设置定时保存进度
  progressSaveTimer.value = setInterval(() => {
    savePlayProgress()
  }, 5000)
})

onBeforeUnmount(() => {
  // 清理定时器
  if (controlsTimer.value) {
    clearTimeout(controlsTimer.value)
  }
  if (progressSaveTimer.value) {
    clearInterval(progressSaveTimer.value)
  }

  // 安全地移除事件监听
  if (playerContainer.value) {
    playerContainer.value.removeEventListener('mousemove', startControlsTimer)
    playerContainer.value.removeEventListener('mouseleave', () => {
      showControls.value = false
    })
  }

  // 保存最后的播放进度
  savePlayProgress()
})
</script>

<style scoped>
.video-player-container {
  position: relative;
  width: 100%;
  background: #000;
  aspect-ratio: 16/9;
}

.video-element {
  width: 100%;
  height: 100%;
}

.controls-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  padding: 10px;
  transition: opacity 0.3s;
}

.progress-bar {
  position: relative;
  height: 4px;
  cursor: pointer;
  margin-bottom: 10px;
}

.progress-background {
  position: absolute;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.2);
}

.progress-current {
  position: absolute;
  height: 100%;
  background: #409EFF;
}

.progress-handle {
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 12px;
  height: 12px;
  background: #409EFF;
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s;
}

.progress-bar:hover .progress-handle {
  opacity: 1;
}

.controls-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.left-controls,
.right-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.control-btn {
  background: none;
  border: none;
  color: #fff;
  cursor: pointer;
  padding: 5px;
  font-size: 20px;
}

.volume-control {
  position: relative;
  display: flex;
  align-items: center;
}

.volume-slider {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  padding: 10px;
  border-radius: 4px;
  display: none;
}

.volume-control:hover .volume-slider {
  display: block;
}

.time-display {
  color: #fff;
  font-size: 14px;
}

:deep(.el-dropdown-menu__item.active) {
  color: #409EFF;
}

/* 添加上次观看进度提示样式 */
.last-progress-tip {
  position: absolute;
  bottom: 10%;
  left: 10%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  z-index: 2;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
}

.last-progress-tip:hover {
  background: rgba(0, 0, 0, 0.9);
}

.jump-text {
  color: #409EFF;
  font-size: 12px;
}
</style> 