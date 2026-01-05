export default defineOAuthKeycloakEventHandler({
  config: {
    scope: ['openid', 'profile', 'email', 'jttracker-frontend-dedicated'],
  },
  async onSuccess(event, { user, tokens }) {
    await setUserSession(event, {
      user: {
        id: user.sub,
        email: user.email,
        name: user.accountName || user.preferred_username,
        preferred_username: user.preferred_username,
        ...user,
      },
      accessToken: tokens.access_token,
      refreshToken: tokens.refresh_token,
      loggedInAt: Date.now(),
    })

    // Redirect to Callback page (matching old workflow)
    return sendRedirect(event, '/Callback')
  },
  onError(event, error) {
    console.error('Keycloak OAuth error:', error)
    return sendRedirect(event, '/Callback?error=oauth_failed')
  },
})