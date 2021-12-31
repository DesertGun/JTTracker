<template>
  <div class="about">
    <b-container fluid>
      <b-row>
        <b-col cols="3" />
        <b-col cols="6">
          <div style="text-align: center">
            <h3>About JTTracker</h3>
          </div>
          <h5 class="desc">
            JTTracker is an open source time tracking web application that helps you increase your
            productivity by reflecting on your time.
            <br />
            Whether it's breaks or work, hourly, daily or monthly, JTTracker helps you keep track of your time.
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
  name: 'AboutPage',
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
