export default defineEventHandler(async (event) => {
  const config = useRuntimeConfig()
  
  // Clear the user session
  await clearUserSession(event)

  // Redirect to Keycloak logout
  const keycloakLogoutUrl = `${config.public.keycloakUrl}/realms/${config.public.keycloakRealm}/protocol/openid-connect/logout?redirect_uri=${encodeURIComponent(config.public.baseUrl)}`
  
  return sendRedirect(event, keycloakLogoutUrl)
})