<template>
  <div>
    <b-container class="dashboardContainer justify-content-center" fluid>
      <b-row class="infoRow">
        <b-col />
        <b-col cols="10">
          <h4>Modules:</h4>
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
    <b-container class="dataDisplayContainer justify-content-center" fluid>
      <b-row class="displayRow">
        <b-col />
        <b-col cols="10">
          <h3>Your latest Times and Projects!</h3>
          <b-row>
            <b-col>
              <h4>Last-Times:</h4>
              <div v-if="timesUser.length !== 0">
                <b-card-group deck>
                  <b-row cols="3">
                    <b-card v-for="time in timesUser" :key="time.timeID" md="4">
                      <b-card-text>
                        <b-row>
                          <b-col>
                            <div v-if="time.timeDesc === null">
                              <h4>
                                Sadly, there is no description for the current
                                record.
                              </h4>
                            </div>
                            <div v-else>
                              <h4>{{ time.timeDesc }}</h4>
                            </div>
                            Recorded on: {{ formatDate(time.startTime) }} at
                            {{ formatTime(time.startTime) }}, recording ended on
                            {{ formatDate(time.endTime) }} at
                            {{ formatTime(time.endTime) }}. With total duration
                            of {{ time.duration.hours() }}h :
                            {{ time.duration.minutes() }}m :
                            {{ time.duration.seconds() }}s
                          </b-col>
                        </b-row>
                      </b-card-text>
                    </b-card>
                  </b-row>
                </b-card-group>
              </div>
              <div v-else>
                <h5>No Projects avalible !</h5>
              </div>
            </b-col>
          </b-row>
          <b-row>
            <b-col>
              <h4>Last-Projects:</h4>
              <div v-if="projectsUser.length !== 0">
                <b-card-group deck>
                  <b-row cols="3">
                    <b-card
                      v-for="project in projectsUser"
                      :key="project.projectID"
                      md="4"
                    >
                      <b-card-text>
                        <b-row>
                          <b-col>
                            <h4>{{ project.projectName }}</h4>
                            <div v-if="project.projectDesc === null">
                              Sadly, there is no description to the current
                              project.
                            </div>
                            <div v-else>
                              {{ project.projectDesc }}
                            </div>
                            Following project was marked as
                            {{ project.priority }}. It has
                            {{ project.trackedTimeList.length }} time-records
                            allocated. Time allocated in total:
                            {{ formatTotalTime(project.projectTime).hours() }}h:
                            {{
                              formatTotalTime(project.projectTime).minutes()
                            }}m:
                            {{
                              formatTotalTime(project.projectTime).seconds()
                            }}s
                          </b-col>
                        </b-row>
                      </b-card-text>
                    </b-card>
                  </b-row>
                </b-card-group>
              </div>
              <div v-else>
                <h5>No Projects avalible !</h5>
              </div>
            </b-col>
          </b-row>
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
  middleware: 'authenticated',
  computed: {
    ...mapGetters({
      getUserTimers: 'timer/timers',
      getUserProjects: 'project/projects',
    }),
  },
  asyncData() {
    return {
      timesUser: [],
      projectsUser: [],
    }
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
        .splice(this.projectsUser.length - 3, 3)

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
        .splice(this.timesUser.length - 3, 3)
    } catch (e) {
      alert(e.toString())
    }
  },
  methods: {
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
      return time.format('LTS')
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

.dataDisplayContainer {
  padding-top: 30px;
}
</style>
