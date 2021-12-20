<template>
  <b-container class="timeEditContainer" fluid>
    <b-row>
      <b-col />
      <b-col cols="6">
        <div style="text-align: center">
          <h3>Time-Record-Edit</h3>
        </div>
        <div class="editDesc">
          <b-row>
            <b-col sm="2">
              <label for="timeDesc">Time-description:</label>
            </b-col>
            <b-col>
              <b-input-group class="mb-2">
                <b-form-textarea
                  id="timeDesc"
                  v-model="timeDesc"
                  max-rows="4"
                  rows="1"
                />
              </b-input-group>
            </b-col>
          </b-row>
        </div>

        <div class="editStart">
          <b-row>
            <b-col sm="2">
              <label for="startTimeEdit">Start-Time:</label>
            </b-col>
            <b-col>
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
            </b-col>
          </b-row>
        </div>

        <div class="editEnd">
          <b-row>
            <b-col sm="2">
              <label for="endTimeEdit">End-Time:</label>
            </b-col>
            <b-col>
              <b-input-group id="endTimeEdit" class="mb-2">
                <b-form-datepicker
                  v-model="endTimeDate"
                  class="mr-2"
                  size="sm"
                />
                <b-form-timepicker
                  v-model="endTimeTimer"
                  show-seconds
                  size="sm"
                />
              </b-input-group>
            </b-col>
          </b-row>
        </div>

        <b-button type="submit" variant="primary" @click="onSubmit">
          Submit
        </b-button>
      </b-col>
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
      this.updateStatistics()
      this.$router.push(this.from.fullPath)
    },
    ...mapActions({
      setUserTimers: 'timer/setTimersAction',
      updateProjects: 'project/setProjectsAction',
      updateStatistics: 'statistics/setStatisticsData',
    }),
    countDuration(startTime, endTime) {
      const diffTime = endTime.diff(startTime)
      return moment.duration(diffTime)
    },
  },
}
</script>
