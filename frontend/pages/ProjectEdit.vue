<template>
  <b-container class="projectEditContainer" fluid>
    <b-form>
      <b-row class="projectDataRow">
        <b-col cols="12">
          <b-row>
            <b-col />
            <b-col cols="5">
              <h3>Project-Edition</h3>
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
                Submit
              </b-button>
            </b-col>
            <b-col />
          </b-row>
        </b-col>
      </b-row>
    </b-form>
    <b-row class="projectTimeRow">
      <b-col>
        <b-row>
          <b-col />
          <b-col cols="10">
            <h3>Your recorded Times:</h3>
            <div v-if="timesUser.length === 0">
              It seems you have no time-records...
            </div>
            <div v-else>
              <b-card-group id="userTimes" class="cardsUserTimes" deck>
                <b-container>
                  <b-row cols="3">
                    <b-col
                      v-for="(timeUser, index) in timesUser"
                      :key="timeUser.timeID"
                      class="timeCardCol"
                      md="4"
                    >
                      <b-card class="cards">
                        <h4 v-if="timeUser.timeDesc === null">
                          No description available
                        </h4>
                        <h4 v-else>
                          {{ timeUser.timeDesc }}
                        </h4>
                        <b-card-body>
                          <b-row> Time-record Nr.: {{ index + 1 }}</b-row>
                          <b-row>
                            From: {{ formatTime(timeUser.startTime) }}
                          </b-row>
                          <b-row>
                            Until: {{ formatTime(timeUser.endTime) }}</b-row
                          >
                          <b-row>
                            Duration:
                            {{ timeUser.duration.hours() }}h :
                            {{ timeUser.duration.minutes() }}m :
                            {{ timeUser.duration.seconds() }}s
                          </b-row>
                          <b-row>
                            Recorded on: {{ formatDate(timeUser.startTime) }}
                          </b-row>
                        </b-card-body>
                        <b-button
                          v-if="timeUser.assigned === true"
                          class="mr-2"
                          variant="danger"
                          @click="removeTimeFromProject(timeUser)"
                        >
                          <b-icon icon="clipboard-minus" />
                          Remove
                        </b-button>
                        <b-button
                          v-if="timeUser.assigned === false"
                          class="mr-2"
                          variant="primary"
                          @click="addTimeToProject(timeUser)"
                        >
                          <b-icon icon="clipboard-plus" />
                          Add
                        </b-button>
                        <b-button
                          class="mr-2"
                          variant="secondary"
                          @click="addTimeToProject(timeUser)"
                        >
                          <b-icon icon="clock-history" />
                          Edit Time
                        </b-button>
                      </b-card>
                    </b-col>
                  </b-row>
                </b-container>
              </b-card-group>
            </div>
          </b-col>
          <b-col />
        </b-row>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import moment from 'moment'
import { mapGetters, mapActions } from 'vuex'

export default {
  middleware: 'authenticated',
  computed: {
    validationPriority() {
      return this.priority != null
    },
    ...mapGetters({ getProjects: 'project/projects' }),
  },
  asyncData() {
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
    }
  },
  async mounted() {
    try {
      const projectID = this.$route.query.projectID
      const response = await this.$axios.get('api/project/' + projectID)
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

      const userTimeresponse = await this.$axios.get('api/timer/')

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
    ...mapActions({ updateProjects: 'project/setProjectsAction' }),
    formatTime(time) {
      return time.format('LTS')
    },
    formatDate(time) {
      return time.format('ll')
    },
    async editProject() {
      await this.$axios.patch('/api/project/' + this.projectID, {
        projectID: this.projectID,
        projectDesc: this.projectDesc,
        projectName: this.projectName,
        status: this.status,
        priority: this.priority,
      })
      await this.updateProjects()
      this.$router.push('/project')
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

        await this.$axios.patch('/api/project/time/' + this.projectID, {
          projectID: this.projectID,
          trackedTimeList: this.timesProject,
          projectTime: this.projectTime.toISOString(),
        })
        this.$store.dispatch('project/setProjectsAction')
        time.assigned = true
      }
    },
    async removeTimeFromProject(time) {
      this.projectTime.subtract(time.duration)
      const index = this.timesProject.findIndex(
        (item) => item.timeID === time.timeID
      )
      this.timesProject.splice(index, 1)
      await this.$axios.patch('/api/project/time/' + this.projectID, {
        projectID: this.projectID,
        trackedTimeList: this.timesProject,
        projectTime: this.projectTime.toISOString(),
      })
      this.$store.dispatch('project/setProjectsAction')
      time.assigned = false
    },
  },
}
</script>

<style>
.cards {
  margin-top: 10vh;
}

.timeCardCol {
  flex: content;
  max-width: fit-content;
}
</style>
