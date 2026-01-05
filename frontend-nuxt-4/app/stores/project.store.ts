import { defineStore } from 'pinia'

export const useProjectStore = defineStore('project', {
  state: () => ({
    projects: [] as any[],
  }),
  
  getters: {
    getProjects: (state) => state.projects,
  },
  
  actions: {
    async setProjects() {
      const api = useApi()
      try {
        const response:any = await api.get('/projects')
        this.projects = response.map((projectJSON: any) => {
          const projectName = projectJSON.projectName
          const projectDesc = projectJSON.projectDesc
          const status = projectJSON.status
          const priority = projectJSON.priority
          const projectTime = projectJSON.projectTime
          const trackedTimeList = projectJSON.trackedTimeList

          return {
            ...projectJSON,
            projectName,
            projectDesc,
            status,
            priority,
            projectTime,
            trackedTimeList,
          }
        })
      } catch (e: any) {
        alert(e.toString())
      }
    },
    
    deleteProject(index: number) {
      try {
        this.projects.splice(index, 1)
      } catch (e: any) {
        alert(e.toString())
      }
    },
    
    async addProject(project: any) {
      const api = useApi()
      await api.post('/project', project)
      this.projects.push(project)
    },
  },
})