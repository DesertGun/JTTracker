<template>
  <div>
    <b-container class="footerCont" fluid>
      <b-row>
        <b-col />
        <b-col>
          <h5>Pages</h5>
          <b-nav type="light" vertical>
            <div v-if="$store.state.auth.jwtToken">
              <b-nav-item nuxt-link to="/dashboard">
                Dashboard
              </b-nav-item>
            </div>
            <b-nav-item nuxt-link to="/about">
              About
            </b-nav-item>
            <b-nav-item nuxt-link to="/help">
              Help
            </b-nav-item>
          </b-nav>
        </b-col>
        <b-col />
        <div v-if="$store.state.auth.jwtToken">
          <b-col>
            <h5>Modules</h5>
            <b-nav type="light" vertical>
              <b-nav-item nuxt-link to="/timer">
                Timer
              </b-nav-item>
              <b-nav-item nuxt-link to="/project">
                Project
              </b-nav-item>
              <b-nav-item nuxt-link to="/statistics">
                Statistics
              </b-nav-item>
            </b-nav>
          </b-col>
        </div>
        <b-col />
        <b-col>
          <h5>Terms</h5>
          <b-nav type="light" vertical>
            <b-nav-item nuxt-link to="/terms">
              Terms of Service
            </b-nav-item>
            <b-nav-item nuxt-link to="/privacy">
              Privacy-Terms
            </b-nav-item>
          </b-nav>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
  .footerCont {
    background-color: #8fd19e;
    padding: 5vh;
  }
</style>
