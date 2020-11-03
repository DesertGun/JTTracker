<template>
  <div>
    <b-container class="headerCont" fluid>
      <div id="nav">
        <b-navbar class="navBar fixed-top" type="light">
          <b-navbar-nav>
            <b-nav-item nuxt-link to="/">
              Home
            </b-nav-item>
            <div v-if="$store.state.auth.jwtToken">
              <b-nav-item-dropdown right text="Modules">
                <b-dropdown-item
                  nuxt-link
                  to="/dashboard"
                >
                  Main-Dashboard
                </b-dropdown-item>
                <b-dropdown-item nuxt-link to="/timer">
                  Timer
                </b-dropdown-item>
                <b-dropdown-item
                  nuxt-link
                  to="/project"
                >
                  Project
                </b-dropdown-item>
                <b-dropdown-item
                  nuxt-link
                  to="/statistics"
                >
                  Statistics
                </b-dropdown-item>
              </b-nav-item-dropdown>
            </div>
            <b-nav-item nuxt-link to="/about">
              About
            </b-nav-item>
            <b-nav-item nuxt-link to="/help">
              Help
            </b-nav-item>
          </b-navbar-nav>

          <!-- Right aligned nav items -->

          <b-navbar-nav class="ml-auto">
            <div v-if="$store.state.auth.jwtToken">
              <b-row>
                <b-col>
                  <b-avatar button @click="toProfile()">
                    <img :src="'https://gravatar.com/avatar/${hash}?d=identicon'">
                  </b-avatar>
                </b-col>
                <b-col>
                  <b-nav-item-dropdown right>
                    <!-- Using 'button-content' slot -->
                    <template v-slot:button-content>
                      <em>User</em>
                    </template>
                    <b-dropdown-item
                      nuxt-link
                      to="/profile"
                    >
                      Profile
                    </b-dropdown-item>
                    <b-dropdown-item @click="logout">
                      Sign Out
                    </b-dropdown-item>
                  </b-nav-item-dropdown>
                </b-col>
              </b-row>
            </div>
          </b-navbar-nav>
        </b-navbar>
      </div>
    </b-container>
  </div>
</template>

<script>
export default {
  name: 'Header',
  methods: {
    logout () {
      this.$store.dispatch('auth/logout')
    },
    toProfile () {
      this.$router.push('/profile')
    }
  }
}
</script>

<style scoped>
  .navBar {
    background: #8fd19e;
  }

  #nav {
    color: #000000;
  }

  #nav a.router-link-exact-active {
    color: #21392e;
  }

  .headerCont {
    margin-bottom: 5vh;
  }

</style>
