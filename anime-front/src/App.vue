<script setup>
import { RouterLink, RouterView } from 'vue-router'
import Header from '@/components/layout/Header.vue'
import ForumEntrance from '@/components/ForumEntrance.vue'
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

onMounted(() => {
  userStore.initializeStore()
})
</script>

<template>
  <Header />
  <RouterView />
  <ForumEntrance />
</template>

<style>
body {
  margin: 0;
  padding: 0;
}
</style>
