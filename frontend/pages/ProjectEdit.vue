<template>
  <div>
    <b-container class="projectEditContainer" fluid>
      <b-form>
            <b-row>
              <b-col />
              <b-col style="min-width: fit-content">
                <div style="text-align: center">
                  <h3>Project-Settings</h3>
                </div>
                <b-form-group
                  id="inputProjectName"
                  description="Change the name if needed."
                  label="Project-name:"
                  label-for="projectName"
                >
                  <b-form-input
                    id="projectName"
                    v-model="projectName"
                    placeholder="Project-name"
                    required
                    type="text"
                  />
                </b-form-group>
                <b-form-group
                  id="inputProjectDesc"
                  description="Modify or add a description to your project"
                  label="Project-desc:"
                  label-for="projectDesc"
                >
                  <b-form-textarea
                    id="projectDesc"
                    v-model="projectDesc"
                    max-rows="6"
                    placeholder="Project-desc"
                    rows="3"
                    type="text"
                  />
                </b-form-group>
                <b-form-group
                  id="projectPriority"
                  label="Priority"
                  label-for="inputProjectPriority"
                >
                  <b-form-select
                    id="inputProjectPriority"
                    v-model="priority"
                    :options="prioritySelect"
                    required
                  />
                  <b-form-invalid-feedback :state="validationPriority">
                    You need to select one priority!
                  </b-form-invalid-feedback>
                </b-form-group>
                <b-form-group
                  id="projectStatus"
                  description="If a project is not active, it will not be followed in statistics and in the dashboard"
                >
                  <b-form-checkbox v-model="status">
                    This project is active
                  </b-form-checkbox>
                </b-form-group>
                <b-button variant="primary" @click="editProject">
                  Update
                </b-button>
              </b-col>
              <b-col />
            </b-row>

      </b-form>
    </b-container>
    <b-container fluid class="card-container">
      <h3>Your recorded times:</h3>
      <b-row>
        <b-col />
        <b-col style="min-width: fit-content">
        <div v-if="timesUser.length === 0">
          It seems you have no time-records...
        </div>
        <div v-else class="flexElementFix">

            <b-card-group deck>

                <b-card
v-for="(timeUser, index) in timesUser"
                :key="timeUser.timeID"
                 class="cards" >
                  <b-card-title>
                    <h4 v-if="timeUser.timeDesc === null">
                      No description available
                    </h4>
                    <h4 v-else>
                      {{ timeUser.timeDesc }}
                    </h4>
                  </b-card-title>
                  <b-card-body>
                    Time-record Nr.{{ index + 1 }}
                    <br />
                    From: {{ formatTime(timeUser.startTime) }}
                    <br />
                    Until: {{ formatTime(timeUser.endTime) }}
                    <br />
                    Duration:
                    <br />
                    {{ timeUser.duration.hours() }}h :
                    {{ timeUser.duration.minutes() }}m :
                    {{ timeUser.duration.seconds() }}s
                    <br />
                    Recorded on:
                    <br />
                    {{ formatDate(timeUser.startTime) }}
                  </b-card-body>
                  <b-row>
                    <b-col cols="5">
                      <b-button
                        v-if="timeUser.assigned === true"
                        variant="danger"
                        @click="removeTimeFromProject(timeUser)"
                      >
                        <b-icon icon="clipboard-minus" />
                        Del
                      </b-button>
                      <b-button
                        v-if="timeUser.assigned === false"
                        variant="primary"
                        @click="addTimeToProject(timeUser)"
                      >
                        <b-icon icon="clipboard-plus" />
                        Add
                      </b-button>
                    </b-col>
                    <b-col />
                    <b-col cols="5">
                      <b-button variant="secondary" @click="editTime(timeUser)">
                        <b-icon icon="clock-history" />
                        Edit
                      </b-button>
                    </b-col>
                  </b-row>
                </b-card>
            </b-card-group>

        </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
import moment from 'moment'
import { mapGetters, mapActions } from 'vuex'

export default {
  middleware: 'authenticated',
  asyncData({ from }) {
    return {
      prioritySelect: [
        { text: 'Select priority', value: null },
        'Low',
        'Normal',
        'High',
        'Urgent!',
      ],
      timesProject: [],
      timesUser: [],
      priority: null,
      projectName: null,
      projectDesc: null,
      projectID: null,
      status: false,
      assigned: false,
      projectTime: null,
      from,
    }
  },
  computed: {
    validationPriority() {
      return this.priority != null
    },
    ...mapGetters({ getProjects: 'project/projects' }),
  },
  async mounted() {
    try {
      const projectID = this.$route.query.projectID
      const response = await this.$axios.get('/project/' + projectID)
      this.projectName = response.data.projectName
      this.projectDesc = response.data.projectDesc
      this.status = response.data.status
      this.priority = response.data.priority
      this.projectID = projectID
      this.projectTime = moment.duration(response.data.projectTime)

      this.timesProject = response.data.trackedTimeList.map((projectJSON) => {
        const startTime = moment(projectJSON.startTime)
        const endTime = moment(projectJSON.endTime)
        return {
          ...projectJSON,
          startTime,
          endTime,
        }
      })

      const userTimeresponse = await this.$axios.get('/timer/')

      this.timesUser = userTimeresponse.data.map((timerJson) => {
        const startTime = moment(timerJson.startTime)
        const endTime = moment(timerJson.endTime)
        const duration = moment.duration(timerJson.duration)

        const assigned = this.timesProject.some(
          (item) => item.timeID === timerJson.timeID
        )

        return {
          ...timerJson,
          startTime,
          endTime,
          duration,
          assigned,
        }
      })
    } catch (e) {
      alert(e.toString())
    }
  },
  methods: {
    countDuration(startTime, endTime) {
      const diffTime = endTime.diff(startTime)
      return moment.duration(diffTime)
    },
    ...mapActions({
      updateProjects: 'project/setProjectsAction',
      updateStatistics: 'statistics/setStatisticsData',
    }),
    formatTime(time) {
      return time.format('HH:mm:ss')
    },
    formatDate(time) {
      return time.format('ll')
    },
    editTime(time) {
      this.$router.push(`timeEdit?timeID=${time.timeID}`)
    },
    async editProject() {
      await this.$axios.patch('/project/' + this.projectID, {
        projectID: this.projectID,
        projectDesc: this.projectDesc,
        projectName: this.projectName,
        status: this.status,
        priority: this.priority,
      })
      await this.updateProjects()
      await this.updateStatistics()
      await this.updateStatistics()
      this.$router.push(this.from.fullPath)
    },
    async addTimeToProject(time) {
      const found = this.timesProject.some(
        (item) => item.timeID === time.timeID
      )

      if (found) {
        alert('Element is already added')
      } else {
        this.timesProject.push({
          startTime: time.startTime,
          endTime: time.endTime,
          timeID: time.timeID,
          timeDesc: time.timeDesc,
        })
        this.projectTime.add(time.duration)

        await this.$axios.patch('/project/time/' + this.projectID, {
          projectID: this.projectID,
          trackedTimeList: this.timesProject,
          trackedTimeID: time.timeID,
          projectTime: this.projectTime.toISOString(),
        })
        await this.updateProjects()
        await this.updateStatistics()

        time.assigned = true
      }
    },
    async removeTimeFromProject(time) {
      this.projectTime.subtract(time.duration)
      const index = this.timesProject.findIndex(
        (item) => item.timeID === time.timeID
      )
      this.timesProject.splice(index, 1)
      await this.$axios.patch('/project/time/' + this.projectID, {
        projectID: this.projectID,
        trackedTimeList: this.timesProject,
        trackedTimeID: time.timeID,
        projectTime: this.projectTime.toISOString(),
      })
      await this.updateProjects()
      await this.updateStatistics()

      time.assigned = false
    },
  },
}
</script>

<style>
.cards {
  margin-top: 10vh;
  min-width: 300px;
  max-width: 300px;
}

.flexElementFix {
  max-width: 100%;
}

.projectEditContainer {
  padding-top: 5vh;
}
</style>
