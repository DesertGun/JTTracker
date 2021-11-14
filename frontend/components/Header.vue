<template>
  <div>
    <b-container class="headerCont" fluid>
      <div id="nav">
        <b-navbar class="navBar fixed-top" type="light">
          <b-navbar-nav>
            <b-nav-item nuxt-link to="/"> Home </b-nav-item>
            <div v-if="isLoggedIn">
              <b-nav-item-dropdown right text="Modules">
                <b-dropdown-item nuxt-link to="/dashboard">
                  Main-Dashboard
                </b-dropdown-item>
                <b-dropdown-item nuxt-link to="/timer"> Timer </b-dropdown-item>
                <b-dropdown-item nuxt-link to="/project">
                  Project
                </b-dropdown-item>
                <b-dropdown-item nuxt-link to="/statistics">
                  Statistics
                </b-dropdown-item>
              </b-nav-item-dropdown>
            </div>
            <b-nav-item nuxt-link to="/about"> About </b-nav-item>
            <b-nav-item nuxt-link to="/help"> Help </b-nav-item>
          </b-navbar-nav>
          <b-navbar-nav class="ml-auto">
            <div v-if="isLoggedIn">
              <b-row>
                <b-col>
                  <b-avatar button @click="toProfile()">
                    <b-img-lazy
                      :src="
                        'https://gravatar.com/avatar/' +
                        getHash +
                        '?d=identicon'
                      "
                    />
                  </b-avatar>
                </b-col>
                <b-col>
                  <b-nav-item-dropdown right>
                    <template v-slot:button-content>
                      <em>{{ getUsername }}</em>
                    </template>
                    <b-dropdown-item nuxt-link to="/profile">
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
import { mapGetters } from 'vuex'

export default {
  name: 'Header',
  computed: {
    ...mapGetters({
      getProfilePicture: 'user/getProfilePicture',
      getHash: 'user/getHash',
      hasProfilePicture: 'user/hasProfilePicture',
      isLoggedIn: 'auth/isLoggedIn',
      getUsername: 'user/getUsername',
    }),
  },
  methods: {
    logout() {
      this.$store.dispatch('auth/logout')
    },
    toProfile() {
      this.$router.push('/profile')
    },
  },
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
