<template>
  <div>
    <b-container class="dashboardContainer justify-content-center" fluid>
      <b-row class="infoRow">
        <b-col />
        <b-col cols="10">
          <div style="text-align: center">
            <h3>Modules:</h3>
          </div>
          <br />
          <b-row class="moduleCardRow">
            <b-card-group deck>
              <b-card>
                <b-card-text>
                  <b-row>
                    <b-col>
                      <h3>Timer-Module:</h3>
                      Start tracking your time for better organization!
                    </b-col>
                  </b-row>
                </b-card-text>
                <b-button variant="primary" @click="timer()">
                  Go To Timer
                </b-button>
              </b-card>
              <b-card>
                <b-card-text>
                  <b-row>
                    <b-col>
                      <h3>Project-Module:</h3>
                      Create a project which will help you in keeping better
                      track on your recordings!
                    </b-col>
                  </b-row>
                </b-card-text>
                <b-button variant="primary" @click="project()">
                  Go To Project
                </b-button>
              </b-card>
              <b-card>
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
          </b-row>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container fluid>
      <div style="text-align: center">
        <h3>Your productivity level:</h3>
      </div>
      <b-row>
        <b-col />
        <b-col>
          <b-form-rating
            v-model="getProductivityLevel"
            no-border
            readonly
            class="rating-level"
          ></b-form-rating>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container fluid>
      <div style="text-align: center">
        <h3>Your latest times and projects:</h3>
      </div>
      <h4>Latest times logged:</h4>
      <b-row>
        <div v-if="timesUser.length !== 0" class="flexElementFix">
          <b-card-group deck>
            <b-col v-for="time in timesUser" :key="time.timeID">
              <b-card class="cards">
                <b-card-text>
                  <div v-if="time.timeDesc === null">
                    <h4>
                      Sadly, there is no description for the current record.
                    </h4>
                  </div>
                  <div v-else>
                    <h4>{{ time.timeDesc }}</h4>
                  </div>
                  Recorded on: {{ formatDate(time.startTime) }}
                  <br />
                  at {{ formatTime(time.startTime) }}
                  <br />
                  recording ended on: {{ formatDate(time.endTime) }}
                  <br />
                  at {{ formatTime(time.endTime) }}
                  <br />
                  With total duration of
                  <br />
                  {{ time.duration.hours() }}h : {{ time.duration.minutes() }}m
                  : {{ time.duration.seconds() }}s
                </b-card-text>
                <b-button variant="secondary" @click="editTimer(time.timeID)">
                  <b-icon icon="gear-fill" />
                  Edit
                </b-button>
              </b-card>
            </b-col>
          </b-card-group>
        </div>
        <div v-else>
          <h5>No timers avalible !</h5>
        </div>
      </b-row>
    </b-container>
    <b-container fluid>
      <br />
      <h4>Latest projects created:</h4>
      <b-row>
        <div v-if="projectsUser.length !== 0" class="flexElementFix">
          <b-card-group deck>
            <b-col v-for="project in projectsUser" :key="project.projectID">
              <div v-if="project.status === true">
                <b-card class="cards">
                  <b-card-text>
                    <h4>{{ project.projectName }}</h4>
                    <h5>
                      <div v-if="project.projectDesc === null">
                        Sadly, there is no description for the current project.
                      </div>
                      <div v-else>
                        {{ project.projectDesc }}
                      </div>
                    </h5>
                    Following project was marked with priority:
                    <br />
                    {{ project.priority }}
                    <br />
                    It has
                    {{ project.trackedTimeList.length }}
                    timers logged
                    <br />
                    Time allocated in total:
                    <br />
                    {{ formatTotalTime(project.projectTime).hours() }}h:
                    {{ formatTotalTime(project.projectTime).minutes() }}m:
                    {{ formatTotalTime(project.projectTime).seconds() }}s
                  </b-card-text>
                  <b-button
                    variant="secondary"
                    @click="editProject(project.projectID)"
                  >
                    <b-icon icon="gear-fill" />
                    Edit
                  </b-button>
                </b-card>
              </div>
              <div v-else>
                <h5>No Projects avalible !</h5>
              </div>
            </b-col>
          </b-card-group>
        </div>
        <div v-else>
          <h5>No Projects avalible !</h5>
        </div>
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
    timer() {
      this.$router.push('/timer')
    },
    project() {
      this.$router.push('/project')
    },
    countDuration(startTime, endTime) {
      const diffTime = endTime.diff(startTime)
      return moment.duration(diffTime)
    },
    formatTime(time) {
      return time.format('HH:mm:ss')
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

.rating-level {
  background-color: #f8f8ff;
}
</style>
