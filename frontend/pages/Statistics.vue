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
          <b-col class="pt-3" style="min-width: fit-content">
            <b-card style="min-height: 100%">
              <b-card-body>
                <div class="timer-data">
                  <h4 class="title-statistics">Timer statistics:</h4>
                  <br />
                  <br />
                  <div v-if="!getNumberOfTimers">
                    <h5>No timer/s available</h5>
                  </div>
                  <div v-else>
                    <h5>{{ getNumberOfTimers }} timer/s logged</h5>
                  </div>
                  <br />
                  <h5>Minimal-time logged:</h5>
                  <h6>
                    {{ formatTotalTime(getMinDuration).hours() }}h:
                    {{ formatTotalTime(getMinDuration).minutes() }}m
                  </h6>
                  <br />
                  <h5>Average-time logged:</h5>
                  <h6>
                    {{ formatTotalTime(getAvgTimeTracked).hours() }}h:
                    {{ formatTotalTime(getAvgTimeTracked).minutes() }}m
                  </h6>
                  <br />
                  <h5>Maximal-time logged:</h5>
                  <h6>
                    {{ formatTotalTime(getMaxDuration).hours() }}h:
                    {{ formatTotalTime(getMaxDuration).minutes() }}m
                  </h6>
                  <br />
                </div>
              </b-card-body>
            </b-card>
          </b-col>

          <b-col class="pt-3" style="min-width: fit-content">
            <b-card style="min-height: 100%">
              <b-card-body>
                <div class="rating">
                  <h4 class="title-statistics">General overview:</h4>
                  <br />
                  <br />
                  <h5>Your productivity rating:</h5>
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
                    {{ formatTotalTime(getTotalTimeTracked).minutes() }}m
                  </h6>
                </div>
              </b-card-body>
            </b-card>
          </b-col>
          <b-col class="pt-3" style="min-width: fit-content">
            <b-card style="min-height: 100%">
              <b-card-body>
                <div class="project-data">
                  <h4 class="title-statistics">Project statistics:</h4>
                  <br />
                  <br />
                  <div v-if="!getNumberOfProjects">
                    <h5>No project/s available</h5>
                  </div>
                  <div v-else>
                    <h5>{{ getNumberOfProjects }} project/s created</h5>
                    <br />
                    <h5>
                      {{ getTimersNotInProjectsTotal }} timer/s without a
                      project
                    </h5>
                    <br />
                    <h5>
                      {{ getTimersInProjectsTotal }} timer/s with a project
                    </h5>
                  </div>
                  <br />
                  <h5>Total time logged in project/s:</h5>
                  <h6>
                    {{
                      formatTotalTime(getTotalTimeTrackedInProjects).hours()
                    }}h:
                    {{
                      formatTotalTime(getTotalTimeTrackedInProjects).minutes()
                    }}m
                  </h6>
                </div>
              </b-card-body>
            </b-card>
          </b-col>
        </b-row>
      </b-container>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
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
  mounted() {
    this.updateStatistics()
  },
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
    ...mapActions({ updateStatistics: 'statistics/setStatisticsData' }),
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
  background-color: #ffffff;
}

.title-statistics {
  font-size: 1.7rem;
}
</style>
