<template>
  <b-container class="loginContainer" fluid>
    <b-form>
      <b-row>
        <b-col />
        <b-col cols="3" style="min-width: max-content">
          <div style="text-align: center">
            <h3>Login</h3>
          </div>
          <b-form-group
            description="Please enter your username to login"
            label="Username:"
            label-for="usernameInput"
          >
            <b-form-input
              id="usernameInput"
              v-model="username"
              data-cy="usernameInput"
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
              data-cy="passwordInput"
              placeholder="Password"
              type="password"
            />
          </b-form-group>
          <b-row cols="8">
            <b-col>
              <b-button  data-cy="loginBtn" variant="primary" @click="submit()"> Login </b-button>
            </b-col>
            <b-col style="min-width: fit-content">
              <b-button variant="outline-danger" @click="submitRecovery()">
                I forgot my Password!
              </b-button>
            </b-col>
          </b-row>
          <b-row>
            <b-col />
            <b-col cols="10">
              <div v-if="error" class="pt-2" style="text-align: center">
                <b-alert data-cy="loginError" dismissible show variant="danger">
                  Wrong password or username !
                </b-alert>
              </div>
            </b-col>
            <b-col />
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
  name: 'LoginPage',
  asyncData() {
    return {
      password: null,
      username: null,
      error: null,
    }
  },
  methods: {
    async submit() {
      try {
        const response = await this.$axios.post('/login', {
          username: this.username,
          password: this.password,
        })
        if (response.data.jwtToken) {
          const auth = { jwtToken: response.data.jwtToken }
          this.$store.dispatch('auth/setAuthAction', auth)
          this.$store.dispatch('user/setProfileData')
          this.$store.dispatch('user/setProfilePicture')
          this.$store.dispatch('user/setProfileHash')
          this.$store.dispatch('statistics/setStatisticsData')
          this.setUserTimers()
          this.setUserProjects()
          this.$router.replace('/')
        } else {
          this.error = response.data.error
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    ...mapActions({
      setUserTimers: 'timer/setTimersAction',
      setUserProjects: 'project/setProjectsAction',
    }),
    submitRecovery() {
      this.$router.push('/recovery')
    },
  },
}
</script>
