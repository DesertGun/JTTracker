<template>
  <div>
    {{ currentTime }}
  </div>
</template>

<script setup lang="ts">
import moment from 'moment'

const currentTime = ref<string>('')

const updateTime = () => {
  currentTime.value = moment().format('HH:mm:ss')
}

onMounted(() => {
  updateTime()
  const interval = setInterval(updateTime, 1000)
  
  onUnmounted(() => {
    clearInterval(interval)
  })
})
</script>

<style></style>