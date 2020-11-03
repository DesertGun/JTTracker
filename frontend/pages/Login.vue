<template>
  <b-container class="loginContainer" fluid>
    <b-form>
      <b-row>
        <b-col />
        <b-col>
          <h4>Login</h4>
          <b-form-group
            description="Please enter your username to login"
            label="Username:"
            label-for="usernameInput"
          >
            <b-form-input
              id="usernameInput"
              v-model="username"
              placeholder="Username"
              required
              type="email"
            />
          </b-form-group>
          <b-form-group
            description="Please enter your password"
            label="Password:"
            label-for="passwordInput"
          >
            <b-form-input
              id="passwordInput"
              v-model="password"
              placeholder="Password"
              type="password"
            />
          </b-form-group>
          <b-row>
            <b-col>
              <b-button variant="primary" @click="submit()">
                Login
              </b-button>
            </b-col>
            <b-col>
              <b-button variant="danger" @click="submitRecovery()">
                I forgot my Password!
              </b-button>
            </b-col>
          </b-row>
        </b-col>
        <b-col />
      </b-row>
    </b-form>
  </b-container>
</template>

<script>
import { mapActions } from 'vuex'
export default {
  asyncData () {
    return {
      password: null,
      username: null
    }
  },
  methods: {
    async submit () {
      try {
        const response = await this.$axios.post('/login', { username: this.username, password: this.password })
        if (response.data.jwtToken) {
          const auth = { jwtToken: response.data.jwtToken }
          this.$store.dispatch('auth/setAuthAction', auth)
          this.setUserTimers()
          this.setUserProjects()
          this.$router.replace('/')
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    ...mapActions({ setUserTimers: 'timer/setTimersAction', setUserProjects: 'project/setProjectsAction' }),
    submitRecovery () {
      this.$router.push('/recovery')
    }
  }
}
</script>
