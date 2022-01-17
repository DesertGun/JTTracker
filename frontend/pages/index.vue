<template>
  <div class="container">
    <div>
      <logo />
      <div v-if="!isLoggedIn">
        <h2 class="subtitle">
          Welcome and thank you for your interest in my App!
        </h2>
        <h3 class="subsubtitle">
          JTTracker is an open source time tracking web application that helps
          you increase your productivity by reflecting on your time
        </h3>
        <div>
          <b-button nuxt-link to="/register" variant="primary">
            Register
          </b-button>
          <b-button nuxt-link to="/login" variant="secondary"> Login </b-button>
        </div>
      </div>
      <div v-else>
        <h4 class="subtitle">Thank you for signing up!</h4>
        <p class="subsubtitle">
          To get a better overview, navigate to the Dashboard.
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from '~/components/Logo.vue'

export default {
  name: 'MainPage',
  components: {
    Logo,
  },
  asyncData() {},
  computed: {
    ...mapGetters({ isLoggedIn: 'auth/isLoggedIn' }),
  },
  mounted() {},
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
