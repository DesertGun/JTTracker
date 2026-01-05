<template>
  <div class="container">
    <div v-if="loggedIn">
      <Logo class="pb-3" />
      <h2 class="subtitle pb-3">
        Welcome and thank you for your interest in my App!
      </h2>
      <h3 class="subsubtitle pb-3">
        JTTracker is an open source time tracking web application that helps you
        increase your productivity by reflecting on your time
      </h3>
      <p class="subsubtitle">
        To get a better overview, navigate to the Dashboard.
      </p>
    </div>
    <div v-else class="container">
      <div class="text-center">
        <p>Redirecting to login...</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Logo from '~/components/logo-component.vue'
import { useAuthStore } from '~/stores/auth.store'
import { useProjectStore } from '~/stores/project.store'
import { useStatisticsStore } from '~/stores/statistics.store'
import { useTimerStore } from '~/stores/timer.store'
import { useUserStore } from '~/stores/user.store'

const { loggedIn, session } = useUserSession()

const authStore = useAuthStore()
const timerStore = useTimerStore()
const projectStore = useProjectStore()
const userStore = useUserStore()
const statisticsStore = useStatisticsStore()

onMounted(async () => {
  if (!loggedIn.value) {
    navigateTo("/api/auth/keycloak", { external: true });
  } else {
    authStore.setUser(session.value?.user);

    await Promise.all([
      timerStore.setTimers(),
      projectStore.setProjects(),
      userStore.setProfileData(),
      userStore.setProfilePicture(),
      userStore.setProfileHash(),
      statisticsStore.setStatisticsData(),
    ]);
  }
});
</script>

<style scoped>
.container {
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  margin-top: 10vh;
}

.subtitle {
  font-weight: 300;
  font-size: 42px;
  color: #526488;
  word-spacing: 5px;
}

.subsubtitle {
  font-weight: 300;
  font-size: 32px;
  color: #526488;
  word-spacing: 5px;
}
</style>
