<template>
  <div class="container">
    <div>
      <logo />
      <h1 class="title">
        JTTracker
      </h1>
      <h2 class="subtitle">
        Welcome and thank you for your interest in my App!
      </h2>
      <div class="links">
        <div v-if="!isLoggedIn">
          <b-button nuxt-link to="/register" variant="primary">
            Register
          </b-button>
          <b-button nuxt-link to="/login" variant="secondary">
            Login
          </b-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from '~/components/Logo.vue'

export default {
  components: {
    Logo
  },
  asyncData () {
    return {
      isLoggedIn: false
    }
  },
  mounted () {
    if (this.getAccountStatus()) {
      this.isLoggedIn = true
    }
  },
  methods: {
    ...mapGetters({ getAccountStatus: 'auth/isLoggedIn' })
  }
}
</script>

<style>
  .container {
    margin: 0 auto;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
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
</style>
