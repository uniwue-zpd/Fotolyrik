// https://nuxt.com/docs/api/configuration/nuxt-config
import Aura from '@primevue/themes/aura';
import tailwindcss from "@tailwindcss/vite";
import { definePreset } from "@primevue/themes";

const UniwuePreset = definePreset(Aura, {
    semantic: {
        primary: {
            50: '{blue.50}',
            100: '{blue.100}',
            200: '{blue.200}',
            300: '{blue.300}',
            400: '{blue.400}',
            500: '{blue.500}',
            600: '{blue.600}',
            700: '{blue.700}',
            800: '{blue.800}',
            900: '{blue.900}',
            950: '{blue.950}',
        }
    }
})

export default defineNuxtConfig({
  compatibilityDate: '2024-11-01',
  devtools: { enabled: true },
  nitro: {
    devProxy: {
      '/api': {
        target: process.env.API_BASE_URL || 'http://localhost:8080',
        changeOrigin: true,
        autoRewrite: true
      }
    }
  },
  routeRules: {
    '/photopoems/**': {
      ssr: false
    }
  },
  modules: [
    '@pinia/nuxt',
    '@primevue/nuxt-module',
    '@formkit/nuxt'
  ],
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
  css: ['./assets/css/main.css'],
  vite: {
    plugins: [tailwindcss()],
  },
})
