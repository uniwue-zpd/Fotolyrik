// https://nuxt.com/docs/api/configuration/nuxt-config
import Aura from '@primevue/themes/aura';
import tailwindcss from "@tailwindcss/vite";
import { definePreset } from "@primevue/themes";

const UniwuePreset = definePreset(Aura, {
    semantic: {
        primary: {
            50: '#7dbbff',
            100: '#479fff',
            200: '#1182fe',
            300: '#0067d9',
            400: '#0054b0',
            500: '#004188',
            600: '#003773',
            700: '#002d5f',
            800: '#00234a',
            900: '#001936',
            950: '#001328',
        }
    }
})

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
        preset: UniwuePreset
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
