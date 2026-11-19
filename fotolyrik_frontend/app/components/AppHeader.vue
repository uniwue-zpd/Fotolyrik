<script setup lang="ts">
import 'primeicons/primeicons.css';
import GeneralSearch from "~/components/UI/GeneralSearch.vue";
import type { NavigationMenuItem } from '@nuxt/ui';

const op = ref();
const toggle = (event: any) => op.value.toggle(event)

const collectionChildren: NavigationMenuItem[] = [
  { label: 'Fotogedichte', to: '/photopoems', icon: 'i-lucide-images' },
  { label: 'Publikationsmedien', to: '/publication_media', icon: 'i-lucide-newspaper' },
  { label: 'Menschen', to: '/persons', icon: 'i-lucide-users' },
  { label: 'Schlagwörter', to: '/keywords', icon: 'i-lucide-tags' },
  { label: 'Orte', to: '/places', icon: 'i-lucide-map-pin' },
];

const collectionMenu: NavigationMenuItem[] = [{
  label: 'Sammlung',
  to: '/photopoems',
  children: collectionChildren,
}];

const mobileMenu: NavigationMenuItem[] = [
  { label: 'Projekt', to: '/project', icon: 'i-lucide-info' },
  { label: 'Sammlung', icon: 'i-lucide-library', defaultOpen: true, children: collectionChildren },
  { label: 'Kontakt', to: '/contact', icon: 'i-lucide-mail' },
];

const mobileMenuOpen = ref(false);

const route = useRoute();
watch(() => route.fullPath, () => { mobileMenuOpen.value = false; });
</script>

<template>
  <header class="bg-primary relative z-40">
    <div class="flex flex-row justify-between items-center max-w-[1140px] mx-auto p-3">
      <NuxtLink to="/" class="shrink-0">
        <img class="h-[32px] sm:h-[40px] w-auto" src="/fl_wortmarke-blaugrau.svg" alt="logo">
      </NuxtLink>
      <div class="hidden md:flex flex-row items-center space-x-2">
        <GeneralSearch />

        <NuxtLink to="/project" class="hidden md:inline outfit-headline text-white">Projekt</NuxtLink>
        <!-- viewportWrapper needs z-50: the centering translate creates a stacking context below page content -->
        <UNavigationMenu
          :items="collectionMenu"
          content-orientation="vertical"
          :delay-duration="0"
          class="hidden md:flex"
          :ui="{
            link: 'outfit-headline text-base font-normal text-white hover:text-white data-[state=open]:text-white px-1 py-0 before:bg-transparent hover:before:bg-transparent data-[state=open]:before:bg-transparent',
            linkTrailingIcon: 'size-4 text-white',
            viewportWrapper: 'w-60 start-1/2 -translate-x-1/2 z-50',
            content: 'p-1',
            childLink: 'px-3 py-2'
          }"
        />
        <NuxtLink to="/contact" class="hidden md:inline outfit-headline text-white">Kontakt</NuxtLink>

        <div class="card flex justify-center align-middle text-white">
          <Button type="button" icon="pi pi-user" rounded-sm aria-label="User" variant="link" class="text-white" @click="toggle"/>
          <Popover ref="op">
            <div class="card flex justify-center">
              <div>Hier entsteht der Anmeldebereich</div>
            </div>
          </Popover>
        </div>
        <UIButtonsDarkThemeToggle></UIButtonsDarkThemeToggle>

        <USlideover v-model:open="mobileMenuOpen" side="right" title="Menü" class="md:hidden">
          <UButton
            icon="i-lucide-menu"
            variant="link"
            aria-label="Menü öffnen"
            class="md:hidden text-white hover:text-white"
          />
          <template #body>
            <UNavigationMenu
              :items="mobileMenu"
              orientation="vertical"
              :ui="{ link: 'outfit-headline text-base py-2', childLink: 'py-2' }"
            />
          </template>
        </USlideover>
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
