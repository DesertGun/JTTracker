
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
  target: 'server',
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
    '@nuxtjs/pwa',
  ],
  /*
   ** Build configuration
   */
  build: {
    /*
     ** You can extend webpack config here
     */
    extend(config, ctx) {},
  },
  axios: {
    baseURL: 'http://localhost:8080',
  },
  pwa: {
    manifest: {
      name: 'JTTracker 1.2.0',
      lang: 'en',
      useWebmanifestExtension: false,
    },
    meta: {
      title: 'JTTracker',
      author: 'DesertGun',
    },
    icon: {
      fileName: 'icon.png',
    },
  },
}
