export const state = () => ({
  numberOfTimers: null,
  numberOfProjects: null,
  totalTimeTracked: false,
  totalTimeTrackedInProjects: null,
})

export const mutations = {
  setStatisticsDataMutation(state, statisticsData) {
    state.numberOfTimers = statisticsData.numberOfTimers
    state.numberOfProjects = statisticsData.numberOfProjects
    state.totalTimeTracked = statisticsData.totalTimeTracked
    state.totalTimeTrackedInProjects = statisticsData.totalTimeTrackedInProjects
  },
}

export const actions = {
  async setStatisticsData({ commit }) {
    try {
      const response = await this.$axios.get('/statistics')

      const numberOfTimers = response.data.numberOfTimers
      const numberOfProjects = response.data.numberOfProjects
      const totalTimeTracked = response.data.totalTimeTracked
      const totalTimeTrackedInProjects =
        response.data.totalTimeTrackedInProjects

      const statisticsData = {
        numberOfTimers,
        numberOfProjects,
        totalTimeTracked,
        totalTimeTrackedInProjects,
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
}

export const setters = {}
