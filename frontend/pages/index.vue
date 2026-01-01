<template>
  <div class="container">
    <div>
      <logo class="pb-3" />
      <h2 class="subtitle pb-3">
        Welcome and thank you for your interest in my App!
      </h2>
      <h3 class="subsubtitle pb-3">
        JTTracker is an open source time tracking web application that helps you
        increase your productivity by reflecting on your time
      </h3>
      <!--h4 class="subtitle pb-3">Thank you for signing up!</h4-->
      <p class="subsubtitle">
        To get a better overview, navigate to the Dashboard.
      </p>
    </div>
  </div>
</template>

<script>
import Logo from '~/components/Logo.vue';

export default {
  name: 'MainPage',
  components: {
    Logo,
  },

  mounted() {
    // TODO: Workaround for force Login on Entry -> either I will refactor to use Login/Register Templates or leave it that way
    if (!this.$auth.loggedIn) {
      this.$auth.loginWith('keycloak')
    } else {
      this.$store.dispatch('timer.store/setTimersAction')
      this.$store.dispatch('project.store/setProjectsAction')
      this.$store.dispatch('user.store/setProfileData')
      this.$store.dispatch('user.store/setProfilePicture')
      this.$store.dispatch('user.store/setProfileHash')
      this.$store.dispatch('statistics.store/setStatisticsData')
    }
  },
  methods: {
    logout() {
      this.$auth.logout()
    },
  },
}
</script>

<style>
.container {
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  margin-top: 10vh;
}

.title {
  font-family: 'Quicksand', 'Source Sans Pro', -apple-system, BlinkMacSystemFont,
    'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  display: block;
  font-weight: 300;
  font-size: 100px;
  color: #35495e;
  letter-spacing: 1px;
}

.subtitle {
  font-weight: 300;
  font-size: 42px;
  color: #526488;
  word-spacing: 5px;
}

.subsubtitle {
  font-weight: 300;
  font-size: 32px;
  color: #526488;
  word-spacing: 5px;
}
</style>
