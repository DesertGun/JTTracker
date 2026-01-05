// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  modules: [
    '@nuxt/eslint',
    '@nuxt/hints',
    '@nuxt/test-utils/module',
    '@pinia/nuxt',
    '@bootstrap-vue-next/nuxt',
    'nuxt-auth-utils',
  ],
  ssr: false,
  devtools: { enabled: true },
  app: {
    head: {
      title: process.env.npm_package_name || '',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        {
          name: 'description',
          content: process.env.npm_package_description || '',
        },
      ],
      link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
    },
  },
  css: ['bootstrap/dist/css/bootstrap.min.css'],
  runtimeConfig: {
    oauth: {
      keycloak: {
        clientSecret: process.env.NUXT_KEYCLOAK_CLIENT_SECRET || '',
        clientId: process.env.NUXT_OAUTH_KEYCLOAK_CLIENT_ID || 'jttracker-frontend',
        serverUrl:process.env.NUXT_OAUTH_KEYCLOAK_SERVER_URL || 'http://localhost:9090',
        redirectURL: process.env.NUXT_OAUTH_KEYCLOAK_REDIRECT_URL || 'http://localhost:3000/api/auth/keycloak',
        realm: process.env.NUXT_OAUTH_KEYCLOAK_REALM || 'ttracker-realm'
        },
      },
     public: {
      keycloakUrl: process.env.NUXT_OAUTH_KEYCLOAK_SERVER_URL || 'http://localhost:9090',
      keycloakRealm: process.env.NUXT_OAUTH_KEYCLOAK_REALM || 'jttracker-realm',
      keycloakClientId: process.env.NUXT_OAUTH_KEYCLOAK_CLIENT_ID || 'jttracker-frontend',
      apiBaseUrl: process.env.API_BASE_URL || 'http://localhost:8080',
      baseUrl: process.env.BASE_URL || 'http://localhost:3000',
    }
  },
  compatibilityDate: '2025-07-15',
  nitro: {
    prerender: {
      crawlLinks: true,
      failOnError: false,
    },
  },
  eslint: {
    config: {
      stylistic: false,
    },
  },
  pinia: {
    storesDirs: ['./stores/**'],
  },
   imports: {
    autoImport: true
  },
  typescript: {
    strict: true
  }
})
