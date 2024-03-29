<template>
  <b-container class="projectMain" fluid>
    <b-row class="formRow justify-content-center">
      <b-col />
      <b-col class="projectCol">
        <div style="text-align: center">
          <h3>Project</h3>
        </div>
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
              <b-form-checkbox v-model="status">
                This project is active
              </b-form-checkbox>
            </b-form-group>
            <b-button variant="primary" @click="createProject()">
              Create
            </b-button>
          </b-form>
        </div>
      </b-col>
      <b-col />
    </b-row>
    <b-row class="projectOverview">
      <b-col />
      <b-col cols="9">
        <h3>Existing projects:</h3>
        <div v-if="items.length === 0">
          <h4>No records available!</h4>
        </div>
        <div v-else>
          <b-table
            id="project-table"
            :fields="fields"
            :items="items"
            :per-page="perPage"
            :current-page="currentPage"
            hover
            responsive
            striped
          >
            <col v-for="field in fields" :key="field.key" />
            <template #cell(projectName)="data">
              {{ data.item.projectName }}
            </template>
            <template #cell(projectDesc)="data">
              <div v-if="data.item.projectDesc === null">
                No description available
              </div>
              <div v-else>
                {{ data.item.projectDesc }}
              </div>
            </template>
            <template #cell(priority)="data">
              {{ data.item.priority }}
            </template>
            <template #cell(projectTime)="data">
              <div v-if="data.item.projectTime === 0">None</div>
              <div v-else>
                {{ formatTime(data.item.projectTime).hours() }}h:
                {{ formatTime(data.item.projectTime).minutes() }}m:
                {{ formatTime(data.item.projectTime).seconds() }}s
              </div>
            </template>
            <template #cell(showEdit)="data">
              <b-button
                variant="outline-secondary"
                @click="editRecord(data.item.projectID)"
              >
                <b-icon icon="gear-fill" />
              </b-button>
              <b-button
                variant="outline-danger"
                @click="deleteProject(data.item.projectID)"
              >
                <b-icon icon="trash-fill" />
              </b-button>
            </template>
          </b-table>
          <b-row>
            <b-col />
            <b-col style="max-width: fit-content">
              <b-pagination
                v-model="currentPage"
                :total-rows="rows"
                :per-page="perPage"
                aria-controls="project-table"
              ></b-pagination>
            </b-col>
            <b-col />
          </b-row>
        </div>
      </b-col>
      <b-col />
    </b-row>
  </b-container>
</template>

<script>
import moment from 'moment'
import { mapGetters, mapActions } from 'vuex'
import { v4 as uuidv4 } from 'uuid'

export default {
  name: 'ProjectPage',
  middleware: 'authenticated',
  asyncData() {
    return {
      fields: [
        { key: 'projectName', label: 'Name' },
        { key: 'projectDesc', label: 'Description' },
        { key: 'priority', label: 'Priority' },
        { key: 'projectTime', label: 'Duration' },
        { key: 'showEdit', label: 'Options' },
      ],
      prioritySelect: [
        { text: 'Select priority', value: null },
        'Low',
        'Normal',
        'High',
        'Urgent!',
      ],
      items: [],
      priority: null,
      projectName: null,
      projectDesc: null,
      projectID: null,
      status: false,
      perPage: 5,
      currentPage: 1,
    }
  },
  computed: {
    validationPriority() {
      return this.priority != null
    },
    rows() {
      return this.items.length
    },
    ...mapGetters({ getProjects: 'project/projects' }),
  },
  mounted() {
    this.items = this.getProjects
  },
  methods: {
    editRecord(projectID) {
      this.$router.push(`projectEdit?projectID=${projectID}`)
    },
    ...mapActions({
      updateProjects: 'project/setProjectsAction',
      deleteProjectAction: 'project/deleteProjectAction',
    }),
    formatTime(time) {
      return moment.duration(time)
    },
    async createProject() {
      this.projectID = uuidv4()
      if (this.projectName != null && this.priority != null) {
        const project = {
          projectID: this.projectID,
          projectName: this.projectName,
          priority: this.priority,
          projectDesc: this.projectDesc,
          status: this.status,
        }
        await this.$store.dispatch('project/addProjectAction', project)
        this.items = this.getProjects
      } else {
        alert('Invalid-Data')
      }
      this.projectID = null
      this.projectName = null
      this.projectDesc = null
      this.status = false
      this.priority = null
    },
    async deleteProject(projectID) {
      await this.$axios.delete('/project/' + projectID)
      const storeProjects = this.$store.state.project.projects

      const indexInStore = storeProjects.findIndex(
        (storeProject) => storeProject.projectID === projectID
      )
      this.deleteProjectAction(indexInStore)

      this.items = this.getProjects
    },
  },
}
</script>

<style>
.projectMain {
  padding-top: 5vh;
}

.projectOverview {
  padding-top: 5vh;
}
</style>
