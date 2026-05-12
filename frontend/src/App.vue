<template>
  <div id="app">
    <router-view />

    <div v-if="showGlobalNav" class="global-nav" aria-label="页面导航">
      <div class="nav-title">{{ routeTitle }}</div>
      <div class="nav-actions">
        <el-tooltip content="返回上一页" placement="bottom">
          <el-button :icon="Back" circle @click="goBack" />
        </el-tooltip>
        <el-tooltip content="返回首页" placement="bottom">
          <el-button type="primary" :icon="House" circle @click="goHome" />
        </el-tooltip>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Back, House } from '@element-plus/icons-vue'

export default {
  name: 'App',
  setup() {
    const route = useRoute()
    const router = useRouter()

    const showGlobalNav = computed(() => {
      return route.meta.requiresAuth && route.path !== '/home'
    })

    const routeTitle = computed(() => route.meta.title || '页面导航')

    const goBack = () => {
      if (window.history.state?.back) {
        router.back()
        return
      }
      router.push('/home')
    }

    const goHome = () => {
      router.push('/home')
    }

    return {
      Back,
      House,
      showGlobalNav,
      routeTitle,
      goBack,
      goHome
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  min-height: 100vh;
}

.global-nav {
  position: fixed;
  left: 20px;
  top: 76px;
  z-index: 2000;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 10px 8px 12px;
  background: rgba(255, 255, 255, 0.96);
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  box-shadow: 0 6px 18px rgba(31, 45, 61, 0.12);
  backdrop-filter: blur(8px);
}

.nav-title {
  max-width: 128px;
  overflow: hidden;
  color: #303133;
  font-size: 14px;
  font-weight: 600;
  line-height: 1;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.nav-actions {
  display: flex;
  gap: 6px;
}

@media (max-width: 640px) {
  .global-nav {
    left: 12px;
    top: 68px;
  }
}
</style>
