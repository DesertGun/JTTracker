import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as any,
  }),
  
  getters: {
    roles: (state) => {
      return state.user?.realm_access?.roles || []
    },
  },
  
  actions: {
    setUser(user: any) {
      this.user = user
    },
  },
})