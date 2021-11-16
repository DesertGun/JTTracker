export const state = () => {
  return {
    projects: [],
  }
}

export const mutations = {
  setProjects(state, projects) {
    state.projects = projects
  },
  deleteProject(state, index) {
    state.projects.splice(index, 1)
  },
  addProject(state, project) {
    state.projects.push(project)
  },
}

export const actions = {
  async setProjectsAction({ commit }) {
    try {
      const response = await this.$axios.get('/project')
      const projects = response.data.map((projectJSON) => {
        const projectName = projectJSON.projectName
        const projectDesc = projectJSON.projectDesc
        const status = projectJSON.status
        const priority = projectJSON.priority
        const projectTime = projectJSON.projectTime

        return {
          ...projectJSON,
          projectName,
          projectDesc,
          status,
          priority,
          projectTime,
        }
      })
      commit('setProjects', projects)
    } catch (e) {
      alert(e.toString())
    }
  },
  deleteProjectAction({ commit, index }) {
    try {
      commit('deleteProject', index)
    } catch (e) {
      alert(e.toString())
    }
  },
}
export const getters = {
  projects: (state) => state.projects,
}
