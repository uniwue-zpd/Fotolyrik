// https://nuxt.com/docs/api/configuration/nuxt-config
import Aura from '@primevue/themes/aura';
import tailwindcss from "@tailwindcss/vite";

export default defineNuxtConfig({
  compatibilityDate: '2024-11-01',
  devtools: { enabled: true },
  runtimeConfig: {
    apiBaseUrl: 'http://localhost:8080'
  },
  routeRules: {
    '/photopoems/**': {
      ssr: false
    }
  },
  modules: ['@pinia/nuxt', '@primevue/nuxt-module', '@formkit/nuxt', '@nuxt/icon'],
  primevue: {
    options: {
      theme: {
        preset: Aura
      }
    },
    autoImport: true
  },
  formkit: {
    configFile: './formkit.config.ts'
  },
  icon: {
    clientBundle: {
      scan: true,
    }
  },
  css: ['./assets/css/main.css'],
  vite: {
    plugins: [tailwindcss()],
  },
})
