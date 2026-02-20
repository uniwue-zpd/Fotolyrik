// https://nuxt.com/docs/api/configuration/nuxt-config
import Aura from '@primeuix/themes/aura';
import tailwindcss from "@tailwindcss/vite";
import { definePreset } from "@primeuix/themes";

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
        },
        colorScheme: {
            light: {
                primary: {
                    color: '#063D79',
                },
                surface: {
                    100: '#F1F2F2',
                    500: '#808080',
                    950: '#000000'
                }
            },
            dark: {
                primary: {
                    color: '#136ac6',
                },
                surface: {
                    100: '#302f35',
                    500: '#808080',
                    950: '#FFFFFF'
                }
            }
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
  modules: [
      '@pinia/nuxt',
      '@primevue/nuxt-module',
      '@formkit/nuxt',
      '@nuxt/icon',
      '@nuxtjs/color-mode',
      '@vueuse/nuxt',
  ],
  primevue: {
    options: {
      theme: {
        preset: UniwuePreset,
        options: {
            darkModeSelector: '.dark',
        }
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
  colorMode: {
    preference: 'system',
    fallback: 'light',
    storage: 'localStorage',
    storageKey: 'nuxt-color-mode'
  }
})
