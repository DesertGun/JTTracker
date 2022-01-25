<template>
  <div id="nav">
    <b-container class="footerCont" fluid>
      <b-row style="justify-content: center">
        <b-col style="max-width: fit-content">
          <b-nav vertical>
            <h5>Pages</h5>
            <div v-if="$store.state.auth.jwtToken">
              <b-nav-item nuxt-link to="/dashboard"> Dashboard </b-nav-item>
            </div>
            <b-nav-item nuxt-link to="/about"> About </b-nav-item>
            <b-nav-item nuxt-link to="/help"> Help </b-nav-item>
          </b-nav>
        </b-col>
        <div v-if="$store.state.auth.jwtToken">
          <b-col />
          <b-col>
            <b-nav vertical>
              <h5>Modules</h5>
              <b-nav-item nuxt-link to="/timer"> Timer </b-nav-item>
              <b-nav-item nuxt-link to="/project"> Project </b-nav-item>
              <b-nav-item nuxt-link to="/statistics"> Statistics </b-nav-item>
            </b-nav>
          </b-col>
          <b-col />
        </div>
        <div v-else>
          <b-col />
        </div>
        <b-col style="max-width: fit-content">
          <b-nav vertical>
            <h5>Terms</h5>
            <b-nav-item nuxt-link to="/terms"> Terms of Service </b-nav-item>
            <b-nav-item nuxt-link to="/privacy"> Privacy-Terms </b-nav-item>
          </b-nav>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  name: 'FooterComponent',
}
</script>

<style>
.footerCont {
  background-color: #71c837;
  padding: 5vh;
}

.nav {
  margin-left: 10vh;
  margin-right: 10vh;
}

.nav-link {
  color: #39651c;
}
</style>
