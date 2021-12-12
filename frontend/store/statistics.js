export const state = () => ({
  numberOfTimers: null,
  numberOfProjects: null,
  totalTimeTracked: null,
  totalTimeTrackedInProjects: null,
  timersNotInProjectsTotal: null,
  timersInProjectsTotal: null,
  maxDuration: null,
  minDuration: null,
  avgTimeTracked: null,
})

export const mutations = {
  setStatisticsDataMutation(state, statisticsData) {
    state.numberOfTimers = statisticsData.numberOfTimers
    state.numberOfProjects = statisticsData.numberOfProjects
    state.totalTimeTracked = statisticsData.totalTimeTracked
    state.avgTimeTracked = statisticsData.avgTimeTracked
    state.maxDuration = statisticsData.maxDuration
    state.minDuration = statisticsData.minDuration
    state.totalTimeTrackedInProjects = statisticsData.totalTimeTrackedInProjects
    state.timersInProjectsTotal = statisticsData.timersInProjectsTotal
    state.timersNotInProjectsTotal = statisticsData.timersNotInProjectsTotal
  },
}

export const actions = {
  async setStatisticsData({ commit }) {
    try {
      const response = await this.$axios.get('/statistics')

      const numberOfTimers = response.data.numberOfTimers
      const numberOfProjects = response.data.numberOfProjects
      const totalTimeTracked = response.data.totalTimeTracked
      const maxDuration = response.data.maxDuration
      const minDuration = response.data.minDuration
      const avgTimeTracked = response.data.avgTimeTracked
      const totalTimeTrackedInProjects =
        response.data.totalTimeTrackedInProjects
      const timersNotInProjectsTotal = response.data.timersNotInProjectsTotal
      const timersInProjectsTotal = response.data.timersInProjectsTotal

      const statisticsData = {
        numberOfTimers,
        numberOfProjects,
        totalTimeTracked,
        maxDuration,
        minDuration,
        avgTimeTracked,
        totalTimeTrackedInProjects,
        timersInProjectsTotal,
        timersNotInProjectsTotal,
      }

      await commit('setStatisticsDataMutation', statisticsData)
    } catch (e) {
      alert(e.toString())
    }
  },
}

export const getters = {
  getNumberOfTimers: (state) => state.numberOfTimers,
  getNumberOfProjects: (state) => state.numberOfProjects,
  getTotalTimeTracked: (state) => state.totalTimeTracked,
  getTotalTimeTrackedInProjects: (state) => state.totalTimeTrackedInProjects,
  getTimersInProjectsTotal: (state) => state.timersInProjectsTotal,
  getMaxDuration: (state) => state.maxDuration,
  getMinDuration: (state) => state.minDuration,
  getAvgTimeTracked: (state) => state.avgTimeTracked,
  getTimersNotInProjectsTotal: (state) => state.timersNotInProjectsTotal,
}

export const setters = {}
