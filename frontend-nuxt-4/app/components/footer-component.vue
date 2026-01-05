<template>
  <div id="nav">
    <BContainer class="footerCont" fluid>
      <BRow style="justify-content: center">
        <BCol style="max-width: fit-content">
          <BNav vertical>
            <h5>Pages</h5>
            <BNavItem to="/dashboard">Dashboard</BNavItem>
            <BNavItem to="/about">About</BNavItem>
            <BNavItem to="/help">Help</BNavItem>
          </BNav>
        </BCol>
        <BCol />
        <BCol>
          <BNav vertical>
            <h5>Modules</h5>
            <BNavItem to="/timer">Timer</BNavItem>
            <BNavItem to="/project">Project</BNavItem>
            <BNavItem to="/statistics">Statistics</BNavItem>
          </BNav>
        </BCol>
        <BCol />
        <BCol style="max-width: fit-content">
          <BNav vertical>
            <h5>Terms</h5>
            <BNavItem to="/terms">Terms of Service</BNavItem>
            <BNavItem to="/privacy">Privacy-Terms</BNavItem>
          </BNav>
        </BCol>
      </BRow>
    </BContainer>
  </div>
</template>

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