export default {
  /*
   ** Headers of the page
   */
  head: {
    title: process.env.npm_package_name || '',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      {
        hid: 'description',
        name: 'description',
        content: process.env.npm_package_description || '',
      },
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
  },
  /*
   ** Customize the progress-bar color
   */
  loading: { color: '#fff' },
  /*
   ** Global CSS
   */
  css: [],
  target: 'static',
  ssr: false,
  generate: {
    fallback: true
  },
  /*
   ** Plugins to load before mounting the App
   */
  plugins: [],
  /*
   ** Nuxt.js dev-modules
   */
  buildModules: [
    // Doc: https://github.com/nuxt-community/eslint-module
    '@nuxtjs/eslint-module',
  ],
  /*
   ** Nuxt.js modules
   */
  modules: [
    // Doc: https://bootstrap-vue.js.org
    [
      'bootstrap-vue/nuxt',
      {
        icons: true,
      },
    ],
    '@nuxtjs/axios',
    '@nuxtjs/auth-next'
  ],
  /*
   ** Build configuration
   */
  build: {
    /*
     ** You can extend webpack config here
     */
    extend(config, ctx) {
      config.output.publicPath = '/_nuxt/'
    },
  },
  axios: {
    baseURL: 'http://localhost:8080',
  },
  publicRuntimeConfig: {
    keycloakUrl: process.env.KEYCLOAK_URL || 'http://localhost:9090',
    keycloakRealm: process.env.KEYCLOAK_REALM || 'jttracker-realm',
    keycloakClientId: process.env.KEYCLOAK_CLIENT_ID || 'jttracker-frontend',
  },
  
  auth: {
    redirect: {
      login: '/Callback',
      logout: '/',
      callback: '/Callback',
      home: '/'
    },
    strategies: {
      keycloak: {
        scheme: 'oauth2',
        endpoints: {
          authorization: `${process.env.KEYCLOAK_URL || 'http://localhost:9090'}/realms/${process.env.KEYCLOAK_REALM || 'jttracker-realm'}/protocol/openid-connect/auth`,
          token: `${process.env.KEYCLOAK_URL || 'http://localhost:9090'}/realms/${process.env.KEYCLOAK_REALM || 'jttracker-realm'}/protocol/openid-connect/token`,
          userInfo: `${process.env.KEYCLOAK_URL || 'http://localhost:9090'}/realms/${process.env.KEYCLOAK_REALM || 'jttracker-realm'}/protocol/openid-connect/userinfo`,
          logout: `${process.env.KEYCLOAK_URL || 'http://localhost:9090'}/realms/${process.env.KEYCLOAK_REALM || 'jttracker-realm'}/protocol/openid-connect/logout?redirect_uri=${encodeURIComponent(process.env.BASE_URL || 'http://localhost:3000')}`
        },
        token: {
          property: 'access_token',
          type: 'Bearer',
          maxAge: 300
        },
        refreshToken: {
          property: 'refresh_token',
          maxAge: 1800
        },
        responseType: 'code',
        grantType: 'authorization_code',
        clientId: process.env.KEYCLOAK_CLIENT_ID || 'jttracker-frontend',
        scope: ['openid', 'profile', 'email', 'jttracker-frontend-dedicated'],
        codeChallengeMethod: 'S256'
      }
    },
    watchLoggedIn: true,
    rewriteRedirects: true
  }
}
