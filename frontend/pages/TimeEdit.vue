<template>
  <b-container class="timeEditContainer mt-3" fluid>
    <b-row>
      <b-col />
      <b-card>
        <b-col>
          <div style="text-align: center">
            <h3>Timer-Settings</h3>
          </div>

          <div class="editDesc">
            <label for="timeDesc">Time-description:</label>
            <b-input-group class="mb-2">
              <b-form-input id="timeDesc" v-model="timeDesc" />
            </b-input-group>
          </div>

          <div class="editStart">
            <label for="startTimeEdit">Start-Time:</label>
            <b-input-group id="startTimeEdit" class="mb-2">
              <b-form-datepicker
                v-model="startTimeDate"
                class="mr-2"
                size="sm"
              />
              <b-form-timepicker
                v-model="startTimeTimer"
                show-seconds
                size="sm"
              />
            </b-input-group>
          </div>

          <div class="editEnd">
            <label for="endTimeEdit">End-Time:</label>
            <b-input-group id="endTimeEdit" class="mb-2">
              <b-form-datepicker v-model="endTimeDate" class="mr-2" size="sm" />
              <b-form-timepicker
                v-model="endTimeTimer"
                show-seconds
                size="sm"
              />
            </b-input-group>
          </div>

          <b-button type="submit" variant="primary" @click="onSubmit">
            Update
          </b-button>
        </b-col>
      </b-card>
      <b-col />
    </b-row>
  </b-container>
</template>

<script>
import moment from 'moment'
import { mapActions } from 'vuex'

export default {
  middleware: 'authenticated',
  asyncData({ from }) {
    return {
      timeDesc: null,
      startTime: null,
      endTime: null,
      startTimeDate: null,
      endTimeDate: null,
      startTimeTimer: null,
      endTimeTimer: null,
      timeID: null,
      duration: null,
      from,
    }
  },
  async mounted() {
    try {
      const timeID = this.$route.query.timeID
      const response = await this.$axios.get('/timer/' + timeID)
      this.timeDesc = response.data.timeDesc
      this.duration = moment.duration(response.data.duration)
      this.startTime = moment(response.data.startTime)
      this.endTime = moment(response.data.endTime)
      this.endTimeDate = this.endTime.format('YYYY-MM-DD')
      this.endTimeTimer = this.endTime.format('HH:mm:ss')
      this.startTimeDate = this.startTime.format('YYYY-MM-DD')
      this.startTimeTimer = this.startTime.format('HH:mm:ss')
      this.timeID = timeID
    } catch (e) {
      alert(e.toString())
    }
  },
  methods: {
    async onSubmit() {
      const timeStart = this.startTimeDate + ' ' + this.startTimeTimer
      const timeEnd = this.endTimeDate + ' ' + this.endTimeTimer
      const startTime = moment(timeStart, 'YYYY-MM-DD HH:mm:ss')
      const endTime = moment(timeEnd, 'YYYY-MM-DD HH:mm:ss')
      const duration = this.countDuration(startTime, endTime)
      await this.$axios.put('/timer/' + this.timeID, {
        startTime,
        endTime,
        timeID: this.timeID,
        timeDesc: this.timeDesc,
        duration,
      })
      await this.updateProjects()
      await this.setUserTimers()
      this.$router.push(this.from.fullPath)
    },
    ...mapActions({
      setUserTimers: 'timer/setTimersAction',
      updateProjects: 'project/setProjectsAction',
    }),
    countDuration(startTime, endTime) {
      const diffTime = endTime.diff(startTime)
      return moment.duration(diffTime)
    },
  },
}
</script>
