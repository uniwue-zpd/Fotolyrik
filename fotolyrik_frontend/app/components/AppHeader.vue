<script setup lang="ts">
import 'primeicons/primeicons.css';
import { ref } from 'vue';
import GeneralSearch from "~/components/UI/GeneralSearch.vue";
const op = ref();
const mobileMenu = ref();

const toggleUser = (event: any) => {
  op.value.toggle(event);
};

const toggleMobileMenu = (event: any) => {
  mobileMenu.value.toggle(event);
};
</script>

<template>
  <header class="bg-primary">
    <div class="flex flex-row justify-between items-center max-w-[1140px] mx-auto p-3">
      <NuxtLink to="/">
        <img class="h-[40px] w-auto" src="/fl_wortmarke-blaugrau.svg" alt="logo">
      </NuxtLink>
      <div class="hidden md:flex flex-row items-center space-x-2">
        <GeneralSearch />
        <NuxtLink to="/project" class="outfit-headline text-white">Projekt</NuxtLink>
        <NuxtLink to="/photopoems" class="outfit-headline text-white">Sammlung</NuxtLink>
        <NuxtLink to="/contact" class="outfit-headline text-white">Kontakt</NuxtLink>
        <div class="card flex justify-center align-middle text-white">
          <Button type="button" icon="pi pi-user" rounded-sm aria-label="User" variant="link" class="text-white" @click="toggleUser"/>
          <Popover ref="op">
            <div class="card flex justify-center">
              <div>Hier entsteht der Anmeldebereich</div>
            </div>
          </Popover>
        </div>
        <UIButtonsDarkThemeToggle/>
      </div>

      <div class="flex md:hidden items-center">
        <Button
            type="button"
            icon="pi pi-bars"
            aria-label="Menu"
            variant="link"
            class="text-white"
            @click="toggleMobileMenu"
        />

        <Popover ref="mobileMenu">
          <div class="flex flex-col gap-4 p-2 min-w-[180px]">
            <NuxtLink to="/project" class="outfit-headline"> Projekt </NuxtLink>
            <NuxtLink to="/photopoems" class="outfit-headline"> Sammlung </NuxtLink>
            <NuxtLink to="/contact" class="outfit-headline"> Kontakt </NuxtLink>
            <GeneralSearch in-popup />
            <UIButtonsDarkThemeToggle in-popup />

          </div>
        </Popover>
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
