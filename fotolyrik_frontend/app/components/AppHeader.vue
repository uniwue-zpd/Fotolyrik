<script setup lang="ts">
import 'primeicons/primeicons.css';
import { ref } from 'vue';
import GeneralSearch from "~/components/UI/GeneralSearch.vue";
const op = ref();
const toggle = (event: any) => op.value.toggle(event)
const {loggedIn, user} = useUserSession();
</script>

<template>
  <header class="bg-primary">
    <div class="flex flex-row justify-between items-center max-w-[1140px] mx-auto p-3">
      <NuxtLink to="/">
        <img class="h-[40px] w-auto" src="/fl_wortmarke-blaugrau.svg" alt="logo">
      </NuxtLink>
      <div class="flex flex-row items-center space-x-2">
        <GeneralSearch />
        <NuxtLink to="/project" class="outfit-headline text-white">Projekt</NuxtLink>
        <NuxtLink to="/photopoems" class="outfit-headline text-white">Sammlung</NuxtLink>
        <NuxtLink to="/contact" class="outfit-headline text-white">Kontakt</NuxtLink>
        <div class="card flex justify-center align-middle text-white">
          <Button type="button" icon="pi pi-user" rounded-sm aria-label="User" variant="link" class="text-white" @click="toggle"/>
          <Popover ref="op">
            <div class="card flex justify-center">
              <div v-if="user" >Hallo {{user.name}}
                <NuxtLink to="/auth/logout" external><Button>Logout </Button></NuxtLink>
              </div>
              <NuxtLink v-if="!loggedIn" to="/auth/login" external><Button>Login </Button></NuxtLink>
            </div>
          </Popover>
        </div>
        <UIButtonsDarkThemeToggle></UIButtonsDarkThemeToggle>
      </div>
    </div>
  </header>
</template>

<style scoped>
.p-button-link {
  color: white;
}

.p-button-link:not(:disabled):hover {
  color: white;
}
</style>
