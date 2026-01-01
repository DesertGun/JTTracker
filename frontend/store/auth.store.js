export const getters = {
  // TODO: For future roles distinction
  user: (state, getters, rootState) => {
    return rootState.auth.user
  },
  roles: (state, getters, rootState) => {
    return rootState.auth.user?.realm_access?.roles || []
  }
}