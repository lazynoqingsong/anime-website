import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'  // 直接导入而不是使用动态导入
import Catalog from '@/views/Catalog.vue'
import Weekly from '@/views/Weekly.vue'
import Ranking from '@/views/Ranking.vue'
import Profile from '@/views/Profile.vue'
import AnimeDetail from '@/views/AnimeDetail.vue'
import AnimePlayer from '@/views/AnimePlayer.vue'
import SearchResult from '@/views/SearchResult.vue'
import Recommendations from '@/views/Recommendations.vue'
import Forum from '@/views/Forum.vue'
import ForumPost from '@/views/ForumPost.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home  // 使用直接导入的组件
    },
    {
      path: '/catalog',
      name: 'catalog',
      component: Catalog
    },
    {
      path: '/weekly',
      name: 'weekly',
      component: Weekly
    },
    {
      path: '/ranking',
      name: 'ranking',
      component: Ranking
    },
    {
      path: '/profile',
      name: 'profile',
      component: Profile,
      meta: {
        requiresAuth: true // 标记需要登录才能访问
      }
    },
    {
      path: '/anime/:id',
      name: 'AnimeDetail',
      component: AnimeDetail
    },
    {
      path: '/anime/:id/play/:episode',
      name: 'AnimePlayer',
      component: AnimePlayer
    },
    {
      path: '/search',
      name: 'search',
      component: SearchResult
    },
    {
      path: '/recommendations',
      name: 'recommendations',
      component: Recommendations,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/forum',
      name: 'forum',
      component: Forum
    },
    {
      path: '/forum/post/:id',
      name: 'forumPost',
      component: ForumPost
    }
  ]
})

export default router
