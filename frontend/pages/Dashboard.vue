<template>
  <div>
    <b-container class="dashboardContainer" fluid>
      <b-row>
        <b-col />
        <b-col>
          <div style="text-align: center">
            <h3>Modules:</h3>
          </div>
        </b-col>
        <b-col />
      </b-row>
      <b-row class="infoRow">
        <b-col />
        <b-col style="min-width: fit-content">
          <b-card-group deck>
            <b-card class="cards">
              <b-card-text>
                <b-row>
                  <b-col>
                    <h3>Timers:</h3>
                    Start tracking your time for better organization!
                  </b-col>
                </b-row>
              </b-card-text>
              <b-button variant="primary" @click="goToTimer()">
                Go To Timer
              </b-button>
            </b-card>
            <b-card class="cards">
              <b-card-text>
                <b-row>
                  <b-col>
                    <h3>Projects:</h3>
                    Create a project which will help you in keeping better track
                    on your timers!
                  </b-col>
                </b-row>
              </b-card-text>
              <b-button variant="primary" @click="goToProject()">
                Go To Project
              </b-button>
            </b-card>
            <b-card class="cards">
              <b-card-text>
                <b-row>
                  <b-col>
                    <h3>Statistics:</h3>
                    Want to know how productive you are? View your statistics!
                  </b-col>
                </b-row>
              </b-card-text>
              <b-button variant="primary"> Go To Stats </b-button>
            </b-card>
          </b-card-group>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container fluid class="mt-5">
      <div style="text-align: center">
        <h3>Your productivity level:</h3>
      </div>
      <b-row class="mt-3">
        <b-col />
        <b-col>
          <b-form-rating
            v-model="getProductivityLevel"
            no-border
            readonly
            class="rating-level-dashboard"
          ></b-form-rating>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container fluid class="timeContainer mt-5">
      <div style="text-align: center">
        <h3>Your latest times and projects:</h3>
      </div>
      <h4 class="mt-5">Latest timers logged:</h4>
      <b-row>
        <b-col />
        <b-col style="min-width: fit-content;">
          <div v-if="timesUser.length !== 0" class="flexElementFix">
            <b-card-group deck style="justify-content: center;">
              <b-card
                v-for="time in timesUser"
                :key="time.timeID"
                class="cards"
              >
                <b-card-text>
                  <div v-if="time.timeDesc === null">
                    <h4>No description available</h4>
                  </div>
                  <div v-else>
                    <h4>{{ time.timeDesc }}</h4>
                  </div>
                  Recording date:
                  {{ formatDate(time.startTime) }}
                  <br />
                  <div v-if="time.duration.asHours() < 24">
                    Recording time:
                    {{ formatTime(time.startTime) }} -
                    {{ formatTime(time.endTime) }}
                    <br />
                  </div>
                  <div v-else>
                    Recording time:
                    {{ formatTime(time.startTime) }} -
                    {{ formatTime(time.endTime) }} +1
                    <br />
                  </div>
                  <div v-if="time.duration.asHours() < 24">
                    Total duration:
                    {{ time.duration.hours() }}h :
                    {{ time.duration.minutes() }}m :
                    {{ time.duration.seconds() }}s
                  </div>
                  <div v-else>
                    Total duration:
                    {{ time.duration.days() }}D : {{ time.duration.hours() }}h :
                    {{ time.duration.minutes() }}m
                  </div>
                </b-card-text>
                <b-button
                  variant="outline-secondary"
                  @click="editTimer(time.timeID)"
                >
                  <b-icon icon="gear-fill" />
                </b-button>
              </b-card>
            </b-card-group>
          </div>
          <div v-else>
            <h5>No timers avalible !</h5>
          </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container fluid class="projectContainer">
      <br />
      <h4>Latest projects created:</h4>
      <b-row>
        <b-col />
        <b-col style="min-width: fit-content">
          <div v-if="projectsUser.length !== 0" class="flexElementFix">
            <b-card-group deck style="justify-content: center;">
              <b-card
                v-for="project in projectsUser"
                :key="project.projectID"
                class="cards"
              >
                <div v-if="project.status === true">
                  <b-card-text>
                    <h4>{{ project.projectName }}</h4>
                    <h5>
                      <div v-if="project.projectDesc === null">
                        No description available
                      </div>
                      <div v-else>
                        {{ project.projectDesc }}
                      </div>
                    </h5>
                    Priority for this project is:
                    {{ project.priority }}
                    <br />
                    {{ project.trackedTimeList.length }} timers logged
                    <br />
                    Project time:
                    {{ formatTotalTime(project.projectTime).hours() }}h:
                    {{ formatTotalTime(project.projectTime).minutes() }}m
                  </b-card-text>
                  <b-button
                    variant="outline-secondary"
                    @click="editProject(project.projectID)"
                  >
                    <b-icon icon="gear-fill" />
                  </b-button>
                </div>
              </b-card>
            </b-card-group>
          </div>
          <div v-else>
            <h5>No Projects avalible !</h5>
          </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import moment from 'moment'

export default {
  name: 'DashboardPage',
  middleware: 'authenticated',
  asyncData() {
    return {
      timesUser: [],
      projectsUser: [],
    }
  },
  computed: {
    ...mapGetters({
      getUserTimers: 'timer/timers',
      getUserProjects: 'project/projects',
      getProductivityLevel: 'statistics/getProductivityLevel',
    }),
  },
  mounted() {
    try {
      this.projectsUser = this.getUserProjects
        .map((projectJSON) => {
          const projectTime = projectJSON.projectTime
          return {
            ...projectJSON,
            projectTime,
          }
        })
        .splice(this.projectsUser.length - 4, 4)

      this.timesUser = this.getUserTimers
        .map((timerJson) => {
          const startTime = moment(timerJson.startTime)
          const endTime = moment(timerJson.endTime)
          const duration = this.countDuration(startTime, endTime)

          return {
            ...timerJson,
            startTime,
            endTime,
            duration,
          }
        })
        .splice(this.timesUser.length - 4, 4)
      this.updateStatistics()
    } catch (e) {
      alert(e.toString())
    }
  },
  methods: {
    editProject(projectID) {
      this.$router.push(`projectEdit?projectID=${projectID}`)
    },
    editTimer(timeID) {
      this.$router.push(`timeEdit?timeID=${timeID}`)
    },
    goToTimer() {
      this.$router.push('/timer')
    },
    goToProject() {
      this.$router.push('/project')
    },
    countDuration(startTime, endTime) {
      const diffTime = endTime.diff(startTime)
      return moment.duration(diffTime)
    },
    formatTime(time) {
      return time.format('HH:mm')
    },
    formatDate(time) {
      return time.format('ll')
    },
    formatTotalTime(time) {
      return moment.duration(time)
    },
    ...mapActions({
      updateProjects: 'project/setProjectsAction',
      updateTimers: 'timer/setTimersAction',
      updateStatistics: 'statistics/setStatisticsData',
    }),
  },
}
</script>

<style>
.dashboardContainer {
  padding: 30px;
}

.cards {
  margin-top: 10vh;
  min-width: 300px;
  max-width: 300px;
}

.flexElementFix {
  max-width: 100%;
}

.rating-level-dashboard {
  background-color: #f8f8ff;
}

.timeContainer {
  padding-left: 5vh;
  padding-right: 5vh;
}

.projectContainer {
  padding-left: 5vh;
  padding-right: 5vh;
}
</style>
