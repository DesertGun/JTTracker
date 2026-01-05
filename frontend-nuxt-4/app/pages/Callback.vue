<template>
  <div>
    <BContainer>
      <BRow>
        <BCol />
        <BCol>
          <div v-if="error" class="text-center mt-5">
            <h3 class="text-danger">Authentication Error</h3>
            <p>{{ errorMessage }}</p>
            <BButton variant="primary" @click="retryLogin"> Try Again </BButton>
          </div>
          <div v-else class="text-center mt-5">
            <p>Loading Your Details</p>
            <BSpinner variant="primary" />
          </div>
        </BCol>
        <BCol />
      </BRow>
    </BContainer>
  </div>
</template>

<script setup lang="ts">

const route = useRoute();
const { loggedIn, fetch } = useUserSession();

const error = ref(!!route.query.error);
const errorMessage = ref(
  (route.query.error as string) || "An error occurred during authentication"
);

const retryLogin = () => {
  navigateTo("/api/auth/keycloak", { external: true });
};

onMounted(async () => {
  if (!error.value) {
    try {
      await fetch();

      if (loggedIn.value) {
        navigateTo("/");
      } else {
        retryLogin();
      }
    } catch (err) {
      console.error("Auth-Error:", err);
      retryLogin();
    }
  }
});
</script>
