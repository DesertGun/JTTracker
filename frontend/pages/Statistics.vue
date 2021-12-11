<template>
  <div>
    <b-container fluid>
      <b-row>
        <b-col />
        <b-col>
          <h3>These are your statistics!</h3>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container fluid>
      <div>
        <b-row>
          <b-col />
          <b-col>
            Hello user {{ username }} ! Here are your statistics:
            {{ numberOfTimers }}
            {{ numberOfProjects }}
            {{ totalTimeTracked }}
            {{ totalTimeTrackedInProjects }}
          </b-col>
          <b-col />
        </b-row>
      </div>
    </b-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  middleware: 'authenticated',
  asyncData() {
    return {
      numberOfTimers: null,
      numberOfProjects: null,
      totalTimeTracked: null,
      totalTimeTrackedInProjects: null,
      username: null,
    }
  },
  computed: {
    ...mapGetters({
      getNumberOfTimers: 'statistics/getNumberOfTimers',
      getNumberOfProjects: 'statistics/getNumberOfProjects',
      getTotalTimeTracked: 'statistics/getTotalTimeTracked',
      getTotalTimeTrackedInProjects: 'statistics/getTotalTimeTrackedInProjects',
      getUsername: 'user/getUsername',
    }),
  },
  mounted() {
    try {
      this.numberOfTimers = this.getNumberOfTimers
      this.numberOfProjects = this.getNumberOfProjects
      this.totalTimeTracked = this.getTotalTimeTracked
      this.totalTimeTrackedInProjects = this.getTotalTimeTrackedInProjects
      this.username = this.getUsername
    } catch (e) {
      alert(e.toString())
    }
  },
}
</script>
