export const state = () => ({
  hash: null,
  profilePicture: null,
  hasProfilePicture: false,
  username: null,
  accountname: null,
  securityenabled: null,
})

export const mutations = {
  setProfilePicture(state, profilePicture) {
    state.profilePicture = profilePicture
    state.hasProfilePicture = true
  },
  setProfileHash(state, hash) {
    localStorage.setItem('user-profile-hash', hash)
    const profileHashLocal = localStorage.getItem('user-profile-hash')
    state.hash = profileHashLocal
  },
  setProfileData(state, userdata) {
    state.username = userdata.username
    state.accountname = userdata.accountname
    state.securityenabled = userdata.securityenabled
  },
  deleteProfilePicture(state) {
    state.hasProfilePicture = false
    state.profilePicture = null
  },
}

export const actions = {
  async setProfileData({ commit }) {
    try {
      const response = await this.$axios.get('/user')
      const username = response.data.username
      const accountname = response.data.accountName
      const securityenabled = response.data.securityEnabled

      const userdata = { username, accountname, securityenabled }
      await commit('setProfileData', userdata)
    } catch (e) {
      alert(e.toString())
    }
  },
  async setProfilePicture({ commit }) {
    try {
      const response = await this.$axios.get('/user')
      if (response.data.profilePictureID) {
        const profilePictureResponse = await this.$axios
          .get('user/picture/', {
            responseType: 'arraybuffer',
          })
          .then((response) => Buffer.from(response.data, 'base64'))

        const profilePicture =
          'data:image/jpeg;base64,' + profilePictureResponse
        await commit('setProfilePicture', profilePicture)
      }
    } catch (e) {
      alert(e.toString())
    }
  },
  async setProfileHash({ commit }) {
    try {
      const response = await this.$axios.get('/user')
      const hash = response.data.hash
      await commit('setProfileHash', hash)
    } catch (e) {
      alert(e.toString())
    }
  },
  deleteProfilePicture({ commit }) {
    commit('deleteProfilePicture')
  },
}

export const getters = {
  getHash: (state) => state.hash,
  getUsername: (state) => state.username,
  getAccountname: (state) => state.accountname,
  getProfilePicture: (state) => state.profilePicture,
  hasProfilePicture: (state) => state.hasProfilePicture,
  hasSecurityEnabled: (state) => state.securityenabled,
}

export const setters = {}
