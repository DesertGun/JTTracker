<template>
  <b-container class="projectMain" fluid>
    <b-row class="formRow justify-content-center">
      <b-col class="projectCol" cols="5">
        <h3>Project-creation</h3>
        <div class="projectForm">
          <b-form class="forms">
            <b-form-group
              id="projectName"
              description="Name of a new project is required"
              label="Project name:"
              label-for="projectNameInput"
            >
              <b-form-input
                id="projectNameInput"
                v-model="projectName"
                class="projectNameInput"
                placeholder="Enter project Name"
                required
                type="text"
              />
            </b-form-group>

            <b-form-group
              id="projectDesc"
              description="Description is optional"
              label="Project description:"
              label-for="inputProjectDesc"
            >
              <b-form-input
                id="inputProjectDesc"
                v-model="projectDesc"
                placeholder="Enter project-description"
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
              <b-form-checkbox
                v-model="status"
              >
                This project is active
              </b-form-checkbox>
            </b-form-group>
            <b-button
              variant="primary"
              @click="createProject()"
            >
              Submit
            </b-button>
          </b-form>
        </div>
      </b-col>
    </b-row>
    <b-row class="tableRow justify-content-center">
      <b-col class="tableCol" cols="8">
        <h3>Created-Projects:</h3>
        <b-table-simple
          bordered
          caption-top
          class="projectTable"
          hover
          outlined
          small
          striped
        >
          <b-thead head-variant="dark">
            <b-tr>
              <b-th>Index</b-th>
              <b-th>Name</b-th>
              <b-th>Priority</b-th>
              <b-th>Time</b-th>
              <b-th>Options</b-th>
            </b-tr>
          </b-thead>
          <b-tbody v-for="(item, index) in items" :key="item.projectID">
            <b-td>{{ index + 1 }}</b-td>
            <b-td>{{ item.projectName }}</b-td>
            <b-td>{{ item.priority }}</b-td>
            <b-td>
              <div v-if="item.projectTime === 0">
                None
              </div>
              <div v-else>
                {{ formatTime(item.projectTime).hours() }}h:
                {{ formatTime(item.projectTime).minutes() }}m:
                {{ formatTime(item.projectTime).seconds() }}s
              </div>
            </b-td>
            <b-td>
              <b-button
                variant="danger"
                @click="deleteProject(index)"
              >
                X
              </b-button>
              <b-button
                variant="secondary"
                @click="editRecord(item.projectID)"
              >
                Edit
              </b-button>
            </b-td>
          </b-tbody>
        </b-table-simple>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import moment from 'moment'
import { mapGetters, mapActions } from 'vuex'
import { v4 as uuidv4 } from 'uuid'

export default {
  middleware: 'authenticated',
  computed: {
    validationPriority () {
      return this.priority != null
    }
  },
  asyncData () {
    return {
      prioritySelect: [
        { text: 'Select priority', value: null },
        'Low',
        'Normal',
        'High',
        'Urgent!'
      ],
      items: [],
      priority: null,
      projectName: null,
      projectDesc: null,
      projectID: null,
      status: false
    }
  },
  mounted () {
    this.items = this.getProjects()
  },
  methods: {
    ...mapGetters({ getProjects: 'project/projects' }),
    ...mapActions({ updateProjects: 'project/setProjectsAction' }),
    editRecord (projectID) {
      this.$router.push(`projectEdit?projectID=${projectID}`)
    },
    formatTime (time) {
      return moment.duration(time)
    },
    async createProject () {
      this.projectID = uuidv4()
      if (this.projectName != null && this.priority != null) {
        const project = {
          projectID: this.projectID,
          projectName: this.projectName,
          priority: this.priority,
          projectDesc: this.projectDesc,
          status: this.status
        }
        await this.$axios.post('api/project', project)
        this.$store.commit('project/addProject', project)
      } else {
        alert('Invalid-Data')
      }
      this.projectID = null
      this.projectName = null
      this.projectDesc = null
      this.status = false
      this.priority = null
    },
    async deleteProject (index) {
      await this.$axios.delete('/api/project/' + this.$store.state.project.projects[index].projectID)
      this.$store.dispatch('project/deleteProjectAction', index)
    }
  }
}
</script>
