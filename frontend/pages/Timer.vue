<template>
  <div class="timerContainer">
    <b-row style="text-align: center">
      <b-col />
      <b-col><h3>Timer</h3></b-col>
      <b-col />
    </b-row>
    <b-container fluid>
      <b-row>
        <b-col cols="2">
          <h4>Tracking:</h4>
        </b-col>
        <b-col cols="8">
          <h4 id="clock" />
          <b-button v-if="isTracked === false" variant="success" @click="rec()">
            <b-icon icon="play-fill" />
            Record Time
          </b-button>
          <b-button v-if="isTracked === true" variant="danger" @click="stopRec()">
            <b-icon icon="stop-fill" />
            Save Time
          </b-button>
        </b-col>
        <b-col />
      </b-row>
    </b-container>

    <b-container class="timeTableContainer" fluid>
      <b-row>
        <b-col cols="2">
          <h4>Saved recording/s:</h4>
        </b-col>
        <b-col cols="10">
          <div v-if="items.length===0">
            <h4>There are no records currently avalible!</h4>
          </div>
          <div v-else>
            <b-table :fields="fields" :items="items" hover responsive="sm" striped>
              <col v-for="field in fields" :key="field.key">
              <template v-slot:cell(index)="data">
                {{ data.index + 1 }}
              </template>
              <template v-slot:cell(timeDesc)="data">
                {{ data.item.timeDesc }}
              </template>
              <template v-slot:cell(startTime)="data">
                {{ formatTime(data.item.startTime) }}
              </template>
              <template v-slot:cell(endTime)="data">
                {{ formatTime(data.item.endTime) }}
              </template>
              <template v-slot:cell(duration)="data">
                <div v-if="data.item.duration.asHours()<24">
                  {{ data.item.duration.hours() }}h :
                  {{ data.item.duration.minutes() }}m :
                  {{ data.item.duration.seconds() }}s
                </div>
                <div v-else-if="data.item.duration.asDays()<30">
                  {{ data.item.duration.days() }}D :
                  {{ data.item.duration.hours() }}h :
                  {{ data.item.duration.minutes() }}m
                </div>
                <div v-else-if="data.item.duration.asMonths()<12">
                  {{ data.item.duration.months() }}M :
                  {{ data.item.duration.days() }}D :
                  {{ data.item.duration.hours() }}h
                </div>
                <div v-else>
                  Not a realistic track record!
                </div>
              </template>
              <template v-slot:cell(showEdit)="data">
                <b-button variant="danger" @click="deleteRecord(data.index)">
                  <b-icon icon="trash-fill" />
                </b-button>
                <b-button variant="secondary" @click="editRecord(data.item.timeID)">
                  <b-icon icon="gear-fill" />
                  Edit
                </b-button>
              </template>
            </b-table>
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
  asyncData () {
    return {
      fields: [
        'index',
        { key: 'timeDesc', label: 'Description' },
        { key: 'startTime', label: 'Start' },
        { key: 'endTime', label: 'End' },
        { key: 'duration', label: 'Duration' },
        { key: 'showEdit', label: 'Options' }
      ],

      items: [],
      startTime: null,
      endTime: null,
      currentDate: null,
      timeDesc: null,
      timeID: null,
      duration: null,
      isTracked: false
    }
  },

  mounted () {
    this.currentDate = moment()
    const update = function () {
      document.getElementById('clock').innerHTML = moment().format(
        'LTS'
      )
    }
    setInterval(update, 1000)
    this.items = this.getTimers()
  },
  methods: {
    ...mapGetters({ getTimers: 'timer/timers' }),
    ...mapActions({ updateTimers: 'timer/setTimersAction', updateProjects: 'project/setProjectsAction' }),
    editRecord (timeID) {
      this.$router.push(`timeEdit?timeID=${timeID}`)
    },
    rec () {
      this.isTracked = true
      this.startTime = moment()
      this.endTime = null
    },
    formatTime (time) {
      return time.format('LTS')
    },
    countDuration (startTime, endTime) {
      const diffTime = endTime.diff(startTime)
      return moment.duration(diffTime)
    },
    async stopRec () {
      this.endTime = moment()
      this.duration = this.countDuration(this.startTime, this.endTime)

      const timeid = require('uuid/v4')
      this.timeID = timeid()
      if (this.endTime != null && this.startTime != null) {
        const timer = {
          startTime: this.startTime,
          endTime: this.endTime,
          timeID: this.timeID,
          timeDesc: this.timeDesc,
          duration: this.duration
        }
        await this.$axios.post('api/timer', timer)
        this.$store.commit('timer/addTimer', timer)
      } else {
        alert('Invalid time-records?!')
      }
      this.startTime = null
      this.endTime = null
      this.timeID = null
      this.isTracked = false
      this.duration = null
    },
    async deleteRecord (index) {
      await this.$axios.delete('/api/timer/' + this.$store.state.timer.timers[index].timeID)
      this.$store.dispatch('timer/deleteTimerAction', index)
      this.updateProjects()
    }
  }
}
</script>

<style>
  .timerContainer {
    padding-top: 30px;
  }

  .timeTableContainer {
    padding-top: 30px;
  }
</style>
