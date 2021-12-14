<template>
  <div>
    <b-container fluid>
      <b-row>
        <b-col />
        <b-col>
          <h3>Statistics</h3>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container fluid>
      <div>
        <b-row>
          <b-col />
          <b-col>
            <h5>Statistics of app-activity:</h5>
            <br />
            Timers logged:
            {{ getNumberOfTimers }}
            <br />
            Number of projects created:
            {{ getNumberOfProjects }}
            <br />
            Number of timers without a project:
            {{ getTimersNotInProjectsTotal }}
            <br />
            Number of timers with a project:
            {{ getTimersInProjectsTotal }}
            <br />
            Time logged in total:
            {{ formatTotalTime(getTotalTimeTracked).hours() }}h:
            {{ formatTotalTime(getTotalTimeTracked).minutes() }}m:
            {{ formatTotalTime(getTotalTimeTracked).seconds() }}s
            <br />
            Minimal-time logged:
            {{ formatTotalTime(getMinDuration).hours() }}h:
            {{ formatTotalTime(getMinDuration).minutes() }}m:
            {{ formatTotalTime(getMinDuration).seconds() }}s
            <br />
            Average-time logged:
            {{ formatTotalTime(getAvgTimeTracked).hours() }}h:
            {{ formatTotalTime(getAvgTimeTracked).minutes() }}m:
            {{ formatTotalTime(getAvgTimeTracked).seconds() }}s
            <br />
            Maximal-time logged:
            {{ formatTotalTime(getMaxDuration).hours() }}h:
            {{ formatTotalTime(getMaxDuration).minutes() }}m:
            {{ formatTotalTime(getMaxDuration).seconds() }}s
            <br />
            Time logged in projects total:
            {{ formatTotalTime(getTotalTimeTrackedInProjects).hours() }}h:
            {{ formatTotalTime(getTotalTimeTrackedInProjects).minutes() }}m:
            {{ formatTotalTime(getTotalTimeTrackedInProjects).seconds() }}s
            <br />
            Most productive year:
            {{ getMostProductiveYear }}
            <br />
            Most productive month:
            {{ getMostProductiveMonth }}
          </b-col>
          <b-col />
        </b-row>
      </div>
    </b-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import moment from 'moment'

export default {
  middleware: 'authenticated',
  asyncData() {
    return {}
  },
  computed: {
    ...mapGetters({
      getNumberOfTimers: 'statistics/getNumberOfTimers',
      getNumberOfProjects: 'statistics/getNumberOfProjects',
      getTotalTimeTracked: 'statistics/getTotalTimeTracked',
      getTotalTimeTrackedInProjects: 'statistics/getTotalTimeTrackedInProjects',
      getTimersInProjectsTotal: 'statistics/getTimersInProjectsTotal',
      getTimersNotInProjectsTotal: 'statistics/getTimersNotInProjectsTotal',
      getAvgTimeTracked: 'statistics/getAvgTimeTracked',
      getMaxDuration: 'statistics/getMaxDuration',
      getMinDuration: 'statistics/getMinDuration',
      getMostProductiveMonth: 'statistics/getMostProductiveMonth',
      getMostProductiveYear: 'statistics/getMostProductiveYear',
      getUsername: 'user/getUsername',
    }),
  },
  mounted() {},
  methods: {
    formatTime(time) {
      return time.format('LTS')
    },
    formatDate(time) {
      return time.format('ll')
    },
    formatTotalTime(time) {
      return moment.duration(time)
    },
  },
}
</script>
