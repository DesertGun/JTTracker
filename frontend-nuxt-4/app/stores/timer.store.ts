import moment from 'moment'
import { defineStore } from 'pinia'

export const useTimerStore = defineStore('timer', {
  state: () => ({
    timers: [] as any[],
  }),
  
  getters: {
    getTimers: (state) => state.timers,
  },
  
  actions: {
    async addTimer(timer: any) {
      const api = useApi()
      await api.post('/timer', timer)
      this.timers.push(timer)
    },
    
    async setTimers() {
      const api = useApi()
      try {
        const response:any = await api.get('/timers')
        this.timers = response.map((timerJson: any) => {
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
      } catch (e: any) {
        alert(e.toString())
      }
    },
    
    deleteTimer(index: number) {
      try {
        this.timers.splice(index, 1)
      } catch (e: any) {
        alert(e.toString())
      }
    },
  },
})