import moment from 'moment'

export const state = () => {
  return {
    timers: [],
  }
}

export const mutations = {
  setTimers(state, timers) {
    state.timers = timers
  },
  deleteTimer(state, index) {
    state.timers.splice(index, 1)
  },
  addTimer(state, timer) {
    state.timers.push(timer)
  },
}

export const actions = {
  async setTimersAction({ commit }) {
    try {
      const response = await this.$axios.get('/timer')
      const timers = response.data.map((timerJson) => {
        const startTime = moment(timerJson.startTime)
        const endTime = moment(timerJson.endTime)
        const duration = moment.duration(timerJson.duration)
        return {
          ...timerJson,
          startTime,
          endTime,
          duration,
        }
      })
      commit('setTimers', timers)
    } catch (e) {
      alert(e.toString())
    }
  },
  deleteTimerAction({ commit, index }) {
    try {
      commit('deleteTimer', index)
    } catch (e) {
      alert(e.toString())
    }
  },
}
export const getters = {
  timers: (state) => state.timers,
}
