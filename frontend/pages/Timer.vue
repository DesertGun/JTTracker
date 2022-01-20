<template>
  <div class="timerContainer">
    <b-container fluid class="mt-3">
      <b-row>
        <b-col />
        <b-col style="max-width: fit-content">
          <b-card>
            <b-card-body>
              <b-row style="text-align: center">
                <b-col><h3>Current Timer</h3></b-col>
              </b-row>
              <b-row>
                <b-col />
                <b-col>
                  <div class="display-4">
                    <Clock style="font-size: 32px" />
                  </div>
                </b-col>
                <b-col />
              </b-row>
              <b-row class="mt-4">
                <b-col />
                <b-col style="min-width: fit-content">
                  <b-button
                    v-if="isTracked === false"
                    variant="success"
                    @click="rec()"
                  >
                    <b-icon icon="play-fill" />
                    Record
                  </b-button>
                  <b-button
                    v-if="isTracked === true"
                    variant="danger"
                    @click="stopRec()"
                  >
                    <b-icon icon="stop-fill" />
                    Save
                  </b-button>
                </b-col>
                <b-col />
              </b-row>
            </b-card-body>
          </b-card>
        </b-col>
        <b-col />
      </b-row>
    </b-container>

    <b-container class="timeTableContainer" fluid>
      <b-row>
        <b-col />
        <b-col cols="9">
          <h4>Timers:</h4>
          <div v-if="items.length === 0">
            <h4>There are no records currently avalible!</h4>
          </div>
          <div v-else>
            <b-table
              id="timer-table"
              :fields="fields"
              :items="items"
              :per-page="perPage"
              :current-page="currentPage"
              hover
              responsive="sm"
              striped
            >
              <col v-for="field in fields" :key="field.key" />
              <template #cell(timeDesc)="data">
                <div v-if="data.item.timeDesc === null">
                  No description available
                </div>
                <div v-else>
                  {{ data.item.timeDesc }}
                </div>
              </template>
              <template #cell(date)="data">
                {{ formatDate(data.item.startTime) }}
              </template>
              <template #cell(startTime)="data">
                {{ formatTime(data.item.startTime) }}
              </template>
              <template #cell(endTime)="data">
                <div v-if="data.item.duration.asHours() < 24">
                  {{ formatTime(data.item.endTime) }}
                </div>
                <div v-else>{{ formatTime(data.item.endTime) }} +1</div>
              </template>
              <template #cell(duration)="data">
                <div v-if="data.item.duration.asHours() < 24">
                  {{ data.item.duration.hours() }}h :
                  {{ data.item.duration.minutes() }}m :
                  {{ data.item.duration.seconds() }}s
                </div>
                <div v-else-if="data.item.duration.asDays() < 30">
                  {{ data.item.duration.days() }}D :
                  {{ data.item.duration.hours() }}h :
                  {{ data.item.duration.minutes() }}m
                </div>
                <div v-else-if="data.item.duration.asMonths() < 12">
                  {{ data.item.duration.months() }}M :
                  {{ data.item.duration.days() }}D :
                  {{ data.item.duration.hours() }}h
                </div>
                <div v-else>Not a realistic track record!</div>
              </template>
              <template #cell(showEdit)="data">
                <b-button
                  variant="outline-secondary"
                  @click="editRecord(data.item.timeID)"
                >
                  <b-icon icon="gear-fill" />
                </b-button>
                <b-button
                  variant="outline-danger"
                  @click="deleteRecord(data.index)"
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
                  aria-controls="timer-table"
                ></b-pagination>
              </b-col>
              <b-col />
            </b-row>
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
import { v4 as uuidv4 } from 'uuid'
import Clock from '@/components/Clock.vue'

export default {
  name: 'TimerPage',
  components: { Clock },
  middleware: 'authenticated',
  asyncData() {
    return {
      fields: [
        { key: 'timeDesc', label: 'Description' },
        { key: 'date', label: 'Date' },
        { key: 'startTime', label: 'Start' },
        { key: 'endTime', label: 'End' },
        { key: 'duration', label: 'Duration' },
        { key: 'showEdit', label: 'Options' },
      ],

      items: [],
      startTime: null,
      endTime: null,
      currentDate: null,
      timeDesc: null,
      timeID: null,
      duration: null,
      isTracked: false,
      perPage: 8,
      currentPage: 1,
    }
  },
  computed: {
    ...mapGetters({ getTimers: 'timer/timers' }),
    rows() {
      return this.items.length
    },
  },
  mounted() {
    this.currentDate = moment()
    this.items = this.getTimers
  },
  methods: {
    editRecord(timeID) {
      this.$router.push(`timeEdit?timeID=${timeID}`)
    },
    ...mapActions({
      updateTimers: 'timer/setTimersAction',
      updateProjects: 'project/setProjectsAction',
      addTimer: 'timer/addTimerAction',
      setStatistics: 'statistics/setStatisticsData',
      deleteTimer: 'timer/deleteTimerAction',
    }),
    rec() {
      this.isTracked = true
      this.startTime = moment()
      this.endTime = null
    },
    formatTime(time) {
      return time.format('HH:mm:ss')
    },
    formatDate(time) {
      return time.format('ll')
    },
    countDuration(startTime, endTime) {
      const diffTime = endTime.diff(startTime)
      return moment.duration(diffTime)
    },
    async stopRec() {
      this.endTime = moment()
      this.duration = this.countDuration(this.startTime, this.endTime)
      this.timeID = uuidv4()
      if (this.endTime != null && this.startTime != null) {
        const timer = {
          startTime: this.startTime,
          endTime: this.endTime,
          timeID: this.timeID,
          timeDesc: this.timeDesc,
          duration: this.duration,
        }
        await this.addTimer(timer)
      } else {
        alert('Invalid time-records?!')
      }
      this.startTime = null
      this.endTime = null
      this.timeID = null
      this.isTracked = false
      this.duration = null
      this.setStatistics()
    },
    async deleteRecord(index) {

      await this.$axios.delete(
        '/timer/' + this.$store.state.timer.timers[index].timeID
      )
      await this.updateTimers()
      this.items = this.getTimers
      this.updateProjects()
      this.setStatistics()
    },
  },
}
</script>

<style>
.timerContainer {
  padding-top: 5vh;
}

.timeTableContainer {
  padding-top: 5vh;
}
</style>
