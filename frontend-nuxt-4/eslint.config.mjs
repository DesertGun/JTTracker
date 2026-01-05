import js from "@eslint/js";
import vue from "eslint-plugin-vue";
import importPlugin from "eslint-plugin-import";
import globals from "globals";

const nuxtGlobals = {
  defineNuxtConfig: "readonly",
  defineNuxtPlugin: "readonly",
  useRuntimeConfig: "readonly",
  navigateTo: "readonly",
  useRoute: "readonly",
  useRouter: "readonly",
  useState: "readonly",
  useAsyncData: "readonly",
  useFetch: "readonly",
  $fetch: "readonly",
};

export default [
  js.configs.recommended,

  // Vue files
  {
    files: ["**/*.vue"],
    languageOptions: {
      globals: {
        ...globals.browser,
        ...globals.node,
      },
    },
    plugins: {
      vue,
      import: importPlugin,
    },
    rules: {
      // Base rules
      "no-console": "warn",
      indent: "off",
      "space-before-function-paren": "off",
      "space-in-parens": "off",
      "no-lonely-if": "off",
      "func-call-spacing": "off",

      // Import rules
      "import/no-unresolved": "off",
      "import/no-default-export": "off",
      "import/default": "off",
      "import/no-named-as-default-member": "off",

      // Vue rules
      "vue/html-indent": "off",
      "vue/html-self-closing": "off",
      "vue/html-closing-bracket-newline": "off",
      "vue/first-attribute-linebreak": "off",
      "vue/multi-word-component-names": "off",
      "vue/no-v-html": "off",
    },
  },

  // JS / TS files
  {
    files: ["**/*.{js,ts,mjs,cjs}"],
    languageOptions: {
      globals: {
        ...globals.node,
      },
    },
    plugins: {
      import: importPlugin,
    },
    rules: {
      "no-console": "warn",
      indent: "off",
      "space-before-function-paren": "off",
      "space-in-parens": "off",
      "no-lonely-if": "off",
      "func-call-spacing": "off",

      "import/no-unresolved": "off",
      "import/no-default-export": "off",
      "import/default": "off",
      "import/no-named-as-default-member": "off",
    },
  },
  {
    files: ["nuxt.config.ts"],
    languageOptions: {
      globals: nuxtGlobals,
    },
  },
  {
    files: ["server/**/*.{ts,js}"],
    languageOptions: {
      globals: {
        ...nuxtGlobals,
        defineEventHandler: "readonly",
        sendRedirect: "readonly",
        setUserSession: "readonly",
        defineOAuthKeycloakEventHandler: "readonly",
      },
    },
  },
  {
    files: ["app/**/*.{ts,vue}"],
    languageOptions: {
      globals: nuxtGlobals,
    },
  },
];
