import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    hash: null as string | null,
    profilePicture: null as string | null,
    hasProfilePicture: false,
    username: null as string | null,
    accountname: null as string | null,
  }),

  getters: {
    getHash: state => state.hash,
    getUsername: state => state.username,
    getAccountname: state => state.accountname,
    getProfilePicture: state => state.profilePicture,
    getUserHasProfilePicture: state => state.hasProfilePicture,
  },

  actions: {
    setProfileData() {
      try {
        const { session } = useUserSession()

        this.username = session.value?.user?.preferred_username || null
        this.accountname = session.value?.user?.accountName || null
      }
      catch (e) {
        console.error('setProfileData failed', e)
      }
    },

    async setProfilePicture() {
      const api = useApi()
      try {
        const userResponse:any = await api.get('/user')
        const pictureId = userResponse.profilePictureID

        if (!pictureId) return

        const pictureResponse:any = await api.get('/user/picture/', {
          responseType: 'arraybuffer',
        })

        const base64 = btoa(
          new Uint8Array(pictureResponse).reduce(
            (data: string, byte: number) => data + String.fromCharCode(byte),
            ''
          )
        )

        this.profilePicture = `data:image/jpeg;base64,${base64}`
        this.hasProfilePicture = true
      } catch (e) {
        console.error('setProfilePicture failed', e)
      }
    },
    
    async setProfileHash() {
      const api = useApi()
      try {
        const response:any = await api.get('/user')
        const hash = response.hash

        if (hash) {
          if (process.client) {
            localStorage.setItem('user-profile-hash', hash)
            const profileHashLocal = localStorage.getItem('user-profile-hash')
            this.hash = profileHashLocal
          } else {
            this.hash = hash
          }
        }
      } catch (e) {
        console.error('setProfileHash failed', e)
      }
    },
    
    deleteProfilePicture() {
      this.hasProfilePicture = false
      this.profilePicture = null
    },
  },
})