<template>
  <div class="about">
    <b-container fluid>
      <b-row>
        <b-col cols="3" />
        <b-col cols="6">
          <h3>About JTTracker</h3>
          <h5 class="desc">
            JTTracker is a Time tracking-Web-Application which is designed to
            help developers track their spend time, in order to to measure their
            efficiency and to order their work-time for more productivity
          </h5>
          <b-button @click="clickButton()"> Go back to homepage </b-button>
        </b-col>
        <b-col cols="3" />
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
.about {
  text-align: center;
  margin: 0 auto;
  display: flex;
}
</style>
