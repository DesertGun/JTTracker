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
      const userdata = {
        username: response.data.username,
        accountname: response.data.accountName,
        securityenabled: response.data.securityEnabled,
      }
      commit('setProfileData', userdata)
    } catch (e) {
      console.error('setProfileData failed', e)
    }
  },
  async setProfilePicture({ commit }) {
    try {
      const userResponse = await this.$axios.get('/user')
      const pictureId = userResponse.data.profilePictureID

      if (!pictureId) return

      const pictureResponse = await this.$axios.get('/user/picture/', {
        responseType: 'arraybuffer',
      })

      const base64 = btoa(
        new Uint8Array(pictureResponse.data).reduce(
          (data, byte) => data + String.fromCharCode(byte),
          ''
        )
      )

      const profilePicture = `data:image/jpeg;base64,${base64}`

      commit('setProfilePicture', profilePicture)
    } catch (e) {
      console.error('setProfilePicture failed', e)
    }
  },
  async setProfileHash({ commit }) {
    try {
      const response = await this.$axios.get('/user')
      const hash = response.data.hash

      if (hash) {
        commit('setProfileHash', hash)
      }
    } catch (e) {
      console.error('setProfileHash failed', e)
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
