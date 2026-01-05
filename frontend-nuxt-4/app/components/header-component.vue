<template>
  <div>
    <BContainer class="headerCont" fluid>
      <div id="nav">
        <BNavbar class="navBar fixed-top navbar-expand" type="light">
          <BNavbarNav class="left-navbar-element">
            <BNavbarBrand to="/">JTTracker</BNavbarBrand>
            <BNavItem to="/dashboard">Dashboard</BNavItem>
            <BNavItemDropdown text="Modules">
              <BDropdownItem to="/timer">Timer</BDropdownItem>
              <BDropdownItem to="/project">Project</BDropdownItem>
              <BDropdownItem to="/statistics">Statistics</BDropdownItem>
            </BNavItemDropdown>
            <BNavItem to="/about">About</BNavItem>
            <BNavItem to="/help">Help</BNavItem>
          </BNavbarNav>
          <BNavbarNav class="ms-auto right-navbar-element">
            <BAvatar button @click="toProfile">
              <div v-if="hasProfilePicture">
                <BImg
                  :src="profilePicture"
                  height="150"
                  width="150"
                  alt="Profile"
                />
              </div>
              <div v-else>
                <BImg
                  :src="`https://gravatar.com/avatar/${hash}?d=identicon`"
                  alt="Avatar"
                />
              </div>
            </BAvatar>
            <BNavItemDropdown>
              <template #button-content>
                <em>{{ username }}</em>
              </template>
              <BDropdownItem to="/profile">Profile</BDropdownItem>
              <BDropdownItem @click="logout">Sign Out</BDropdownItem>
            </BNavItemDropdown>
          </BNavbarNav>
        </BNavbar>
      </div>
    </BContainer>
  </div>
</template>

<script setup lang="ts">
const userStore = useUserStore()
const { clear } = useUserSession()
const router = useRouter()

const profilePicture:any = computed(() => userStore.getProfilePicture)
const hash = computed(() => userStore.getHash)
const hasProfilePicture = computed(() => userStore.getUserHasProfilePicture)
const username = computed(() => userStore.getUsername)

const logout = async () => {
  await clear()
  navigateTo('/auth/logout', { external: true })
}

const toProfile = () => {
  router.push('/profile')
}
</script>

<style scoped>
.navBar {
  background: #71c837;
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

.left-navbar-element {
  margin-left: 10vh;
}

.right-navbar-element {
  margin-right: 10vh;
}
</style>