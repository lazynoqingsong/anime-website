import { defineStore } from 'pinia'
import { ref } from 'vue'
import avatarImg from '@/assets/img/avatar.jpg'
import axios from 'axios'

export const useUserStore = defineStore('user', () => {
  // 状态
  const isLoggedIn = ref(false)
  const username = ref('')
  const userId = ref(null)
  const userAvatar = ref(avatarImg)

  // 初始化时从 localStorage 恢复状态
  const initializeStore = () => {
    const storedUser = JSON.parse(localStorage.getItem('user'))
    if (storedUser) {
      isLoggedIn.value = true
      username.value = storedUser.username
      userId.value = storedUser.id
      userAvatar.value = storedUser.avatar || avatarImg
    }
  }

  // 登录
  const login = (userData) => {
    isLoggedIn.value = true
    username.value = userData.username
    userId.value = userData.id
    userAvatar.value = userData.avatar || avatarImg
    
    // 存储完整的用户信息到 localStorage
    localStorage.setItem('user', JSON.stringify({
      id: userData.id,
      username: userData.username,
      avatar: userData.avatar || avatarImg
    }))
  }

  // 登出
  const logout = () => {
    isLoggedIn.value = false
    username.value = ''
    userId.value = null
    userAvatar.value = avatarImg
    // 清除 localStorage
    localStorage.removeItem('user')
  }

  // 检查登录状态
  const checkLoginStatus = () => {
    const storedUser = JSON.parse(localStorage.getItem('user'))
    if (storedUser) {
      isLoggedIn.value = true
      username.value = storedUser.username
      userId.value = storedUser.id
      userAvatar.value = storedUser.avatar || avatarImg
    }
  }

  // 注册
  const register = async (userData) => {
    try {
      const response = await axios.post('http://localhost:8080/user/register', {
        username: userData.username,
        password: userData.password
      })
      
      if (response.data.code === 200) {
        return {
          success: true,
          message: response.data.message || '注册成功'
        }
      } else {
        return {
          success: false,
          message: response.data.message || '注册失败'
        }
      }
    } catch (error) {
      console.error('注册错误:', error)
      return {
        success: false,
        message: error.response?.data?.message || '注册失败，请稍后重试'
      }
    }
  }

  return {
    isLoggedIn,
    username,
    userId,
    userAvatar,
    login,
    logout,
    checkLoginStatus,
    register,
    initializeStore
  }
}) 