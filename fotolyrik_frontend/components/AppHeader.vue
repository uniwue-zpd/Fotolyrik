<script setup lang="ts">
import Button from 'primevue/button'
import Popover from 'primevue/popover';
import 'primeicons/primeicons.css';
import { ref } from 'vue';

const op = ref();
const toggle = (event: any) => op.value.toggle(event)
</script>

<template>
  <header class="bg-[#063D79]">
    <div class="flex flex-row justify-between items-center max-w-[1140px] mx-auto p-4">
      <NuxtLink to="/">
        <img class="h-[40px] w-auto" src="../public/fl_wortmarke-blaugrau.svg" alt="logo">
      </NuxtLink>
      <div class="flex flex-row items-center space-x-2">
        <NuxtLink to="/project" class="outfit-headline text-white">Projekt</NuxtLink>
        <NuxtLink to="/collection" class="outfit-headline text-white">Sammlung</NuxtLink>
        <NuxtLink to="/contact" class="outfit-headline text-white">Kontakt</NuxtLink>
        <div class="card flex justify-center align-middle text-white">
          <Button type="button" icon="pi pi-user" rounded aria-label="User" variant="link" class="text-white" @click="toggle"/>
          <Popover ref="op">
            <div class="card flex justify-center">
              <div>Hier entsteht der Anmeldebereich</div>
            </div>
          </Popover>
        </div>
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
