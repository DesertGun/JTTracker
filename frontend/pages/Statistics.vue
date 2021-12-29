<template>
  <div>
    <b-container fluid>
      <b-row>
        <b-col />
        <b-col>
          <div style="text-align: center">
            <h3>Statistics</h3>
          </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <div>
      <b-container fluid>
        <b-row>
          <b-col>
            <br />
            <div class="timer-data">
              <h4>Timer-related statistics:</h4>
              <br />
              <h5>{{ getNumberOfTimers }} timer/s logged</h5>
              <br />
              <h5>Minimal-time logged:</h5>
              <h6>
                {{ formatTotalTime(getMinDuration).hours() }}h:
                {{ formatTotalTime(getMinDuration).minutes() }}m:
                {{ formatTotalTime(getMinDuration).seconds() }}s
              </h6>
              <br />
              <h5>Average-time logged:</h5>
              <h6>
                {{ formatTotalTime(getAvgTimeTracked).hours() }}h:
                {{ formatTotalTime(getAvgTimeTracked).minutes() }}m:
                {{ formatTotalTime(getAvgTimeTracked).seconds() }}s
              </h6>
              <br />
              <h5>Maximal-time logged:</h5>
              <h6>
                {{ formatTotalTime(getMaxDuration).hours() }}h:
                {{ formatTotalTime(getMaxDuration).minutes() }}m:
                {{ formatTotalTime(getMaxDuration).seconds() }}s
              </h6>
              <br />
            </div>
          </b-col>
          <b-col>
            <br />
            <div class="rating">
              <h4>General overview:</h4>
              <br />
              <h5>Your productivity rating:</h5>
              <br />
              <b-form-rating
                v-model="getProductivityLevel"
                no-border
                readonly
                class="rating-level"
              ></b-form-rating>
              <br />
              <h5>Most productive year:</h5>
              <h6>{{ getMostProductiveYear }}</h6>
              <br />
              <h5>Most productive month:</h5>
              <h6>{{ getMostProductiveMonth }}</h6>
              <br />
              <h5>Total time logged:</h5>
              <h6>
                {{ formatTotalTime(getTotalTimeTracked).hours() }}h:
                {{ formatTotalTime(getTotalTimeTracked).minutes() }}m:
                {{ formatTotalTime(getTotalTimeTracked).seconds() }}s
              </h6>
            </div>
          </b-col>
          <b-col>
            <br />
            <div class="project-data">
              <h4>Project-related statistics:</h4>
              <br />
              <h5>{{ getNumberOfProjects }} project/s created</h5>
              <br />
              <h5>
                {{ getTimersNotInProjectsTotal }} timer/s without a project
              </h5>
              <br />
              <h5>{{ getTimersInProjectsTotal }} timer/s with a project</h5>
              <br />
              <h5>Total time logged in project/s:</h5>
              <h6>
                {{ formatTotalTime(getTotalTimeTrackedInProjects).hours() }}h:
                {{ formatTotalTime(getTotalTimeTrackedInProjects).minutes() }}m:
                {{ formatTotalTime(getTotalTimeTrackedInProjects).seconds() }}s
              </h6>
            </div>
          </b-col>
        </b-row>
      </b-container>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import moment from 'moment'

export default {
  name: 'StatisticsPage',
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
      getProductivityLevel: 'statistics/getProductivityLevel',
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

<style>
.rating {
  text-align: center;
  padding-top: 3%;
}
.timer-data {
  text-align: center;
  padding-top: 3%;
}
.project-data {
  text-align: center;
  padding-top: 3%;
}
.rating-level {
  background-color: #f8f8ff;
}
</style>
