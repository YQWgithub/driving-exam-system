<template>
  <div id="app">
    <router-view />

    <div v-if="showHomeButton" :class="['home-fab', homeFabClass]">
      <el-tooltip content="返回首页" placement="left">
        <el-button type="primary" :icon="House" circle @click="goHome" />
      </el-tooltip>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { House } from '@element-plus/icons-vue'

export default {
  name: 'App',
  setup() {
    const route = useRoute()
    const router = useRouter()

    const showHomeButton = computed(() => {
      return route.meta.requiresAuth && route.path !== '/home'
    })

    const homeFabClass = computed(() => {
      return route.meta.requiresAdmin ? 'is-admin' : 'is-learner'
    })

    const goHome = () => {
      router.push('/home')
    }

    return {
      House,
      showHomeButton,
      homeFabClass,
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

.home-fab {
  position: fixed;
  z-index: 2000;
}

.home-fab :deep(.el-button) {
  width: 40px;
  height: 40px;
  box-shadow: 0 6px 18px rgba(31, 45, 61, 0.18);
}

.home-fab.is-learner {
  left: 20px;
  bottom: 20px;
}

.home-fab.is-admin {
  right: 20px;
  top: 76px;
}
</style>
