import { defineStore } from 'pinia'

export const useStatisticsStore = defineStore('statistics', {
  state: () => ({
    numberOfTimers: null as number | null,
    numberOfProjects: null as number | null,
    totalTimeTracked: null as number | null,
    totalTimeTrackedInProjects: null as number | null,
    timersNotInProjectsTotal: null as number | null,
    timersInProjectsTotal: null as number | null,
    maxDuration: null as number | null,
    minDuration: null as number | null,
    avgTimeTracked: null as number | null,
    mostProductiveYear: null as number | null,
    mostProductiveMonth: null as string | null,
    productivityLevel: null as string | null,
  }),
  
  getters: {
    getNumberOfTimers: (state) => state.numberOfTimers,
    getNumberOfProjects: (state) => state.numberOfProjects,
    getTotalTimeTracked: (state) => state.totalTimeTracked,
    getTotalTimeTrackedInProjects: (state) => state.totalTimeTrackedInProjects,
    getTimersInProjectsTotal: (state) => state.timersInProjectsTotal,
    getMaxDuration: (state) => state.maxDuration,
    getMinDuration: (state) => state.minDuration,
    getAvgTimeTracked: (state) => state.avgTimeTracked,
    getTimersNotInProjectsTotal: (state) => state.timersNotInProjectsTotal,
    getMostProductiveMonth: (state) => state.mostProductiveMonth,
    getMostProductiveYear: (state) => state.mostProductiveYear,
    getProductivityLevel: (state) => state.productivityLevel,
  },
  
  actions: {
    async setStatisticsData() {
      const api = useApi()
      try {
        const response:any = await api.get('/statistics')

        this.numberOfTimers = response.numberOfTimers
        this.numberOfProjects = response.numberOfProjects
        this.totalTimeTracked = response.totalTimeTracked
        this.maxDuration = response.maxDuration
        this.minDuration = response.minDuration
        this.avgTimeTracked = response.avgTimeTracked
        this.totalTimeTrackedInProjects = response.totalTimeTrackedInProjects
        this.timersNotInProjectsTotal = response.timersNotInProjectsTotal
        this.timersInProjectsTotal = response.timersInProjectsTotal
        this.mostProductiveYear = response.mostProductiveYear
        this.mostProductiveMonth = response.mostProductiveMonth
        this.productivityLevel = response.productivityLevel
      } catch (e: any) {
        alert(e.toString())
      }
    },
  },
})