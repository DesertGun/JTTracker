<template>
  <div class="help">
    <b-container fluid>
      <b-row>
        <b-col />
        <b-col cols="6">
          <div style="text-align: center">
            <h3>Help and FAQ</h3>
          </div>
          <h5>
            <p>Q: What does JTTracker stand for?</p>
            <p>A: JavaTimeTracker</p>
            <p>Q: How can JTTracker help me?</p>
            <p>
              A: JTTracker helps you to keep a closer eye on time during work or
              other relevant activities. <br />
              Every productive day consists of several steps that you have to
              complete personally. Time plays a special role in this. <br />
              In order to plan the next days more efficiently, it is very
              helpful to know how much time you spend each day.
            </p>
            <p>
              Q: Could you give some practical examples of how JTTracker could
              be used?
            </p>
            <p>
              A: In order to create billing, you need an exact working time.<br />
              If you have to indicate your progress in a team, you also need to
              know how long it took you to complete each task.<br />
              For self-organisation, it is useful to know how long the breaks
              and the working time lasted.<br />
              These are just a few examples where JTTracker covers them all.
            </p>
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
  name: 'HelpPage',
  methods: {
    clickButton() {
      this.$router.push('/')
    },
  },
}
</script>
