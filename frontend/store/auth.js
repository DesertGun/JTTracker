export const state = () => {
  return {
    jwtToken: null,
  }
}

export const mutations = {
  setAuth(state, token) {
    localStorage.setItem('user-jwt', token)
    const jwtToken = localStorage.getItem('user-jwt')
    state.jwtToken = jwtToken
  },
  logout(state) {
    state.jwtToken = ''
  },
}

export const actions = {
  setAuthAction({ commit }, auth) {
    const token = auth.jwtToken
    this.$axios.defaults.headers.common.Authorization = `Bearer ${token}`
    commit('setAuth', token)
  },
  logout({ commit }) {
    commit('logout')
    delete this.$axios.defaults.headers.common.Authorization
    this.$router.push('/')
  },
}
export const getters = {
  isLoggedIn: (state) => !!state.jwtToken,
  token: (state) => state.jwtToken,
}
