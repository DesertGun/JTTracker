<!-- pages/auth/callback.vue -->
<template>
  <div>
    <b-container>
      <b-row>
        <b-col />
        <b-col>
          <p>Loading Your Details</p>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  async mounted() {
    try {
      await this.$auth.fetchUser();
      this.$auth.redirect('home');
    } catch (err) {
      console.error('Auth-Error:', err)
      // TODO: Rewrite Fallback in error case
      this.$auth.loginWith('keycloak')
    }
  },
}
</script>
