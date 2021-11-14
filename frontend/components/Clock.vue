<template>
  <div>
    {{ currentTime }}
  </div>
</template>

<script>
import moment from 'moment'

export default {
  name: 'Clock',
  data() {
    return {
      currentTime: null,
    }
  },
  mounted() {
    window.setInterval(() => {
      this.time()
    }, 1000)
  },
  methods: {
    time() {
      this.currentTime = moment().format('LTS')
    },
  },
}
</script>

<style></style>
