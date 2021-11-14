<template>
  <div class="help">
    <b-container fluid>
      <b-row>
        <b-col />
        <b-col cols="6">
          <h3>Help and FAQ</h3>
          <h5 class="desc">
            Q: What does JTTracker stand for? <br />
            A: JavaTimeTracker
          </h5>
          <b-button @click="clickButton()"> Return to homepage </b-button>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  methods: {
    clickButton() {
      this.$router.push('/')
    },
  },
}
</script>

<style>
.help {
  text-align: center;
}
</style>
