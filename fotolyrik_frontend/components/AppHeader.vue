<script setup lang="ts">
import 'primeicons/primeicons.css';
import { ref } from 'vue';
import apiClient from "~/service/api";
const op = ref();
const toggle = (event: any) => op.value.toggle(event)

const visible = ref(false);

// Handle search input
const query = ref('');
const results = ref<GeneralSearchResult[]>([]);
const type_mapping = {
  photopoems: 'Fotogedicht',
  persons: 'Person',
  places: 'Ort',
  publication_media: 'Publikationsmedium'
}

const fetchResults = async (search: string) => {
  if (!search.trim()) {
    results.value = []
    return
  }

  try {
    const { data } = await apiClient.get('/search', {
      params: { query: search }
    });
    results.value = data;
  } catch (error) {
    console.error('Search error:', error)
    results.value = []
  }
}

const debouncedFetch = debounce(fetchResults, 300)

const onInput = () => {
  debouncedFetch(query.value);
}

const clearResults = () => {
  results.value = []
  visible.value = false
}
</script>

<template>
  <header class="bg-[#063D79]">
    <div class="flex flex-row justify-between items-center max-w-[1140px] mx-auto p-3">
      <NuxtLink to="/">
        <img class="h-[40px] w-auto" src="../public/fl_wortmarke-blaugrau.svg" alt="logo">
      </NuxtLink>
      <div class="flex flex-row items-center space-x-2">
        <button @click="visible = true">
          <i class="pi pi-search text-white"/>
        </button>
        <Dialog v-model:visible="visible" modal position="top" class="min-w-[30%]">
          <template #header>
            <h2 class="text-xl font-bold outfit-headline text-[#063D79]">Suchen</h2>
          </template>
          <div class="flex flex-col gap-4">
            <input
                type="search"
                class="px-3 py-3 border rounded-md shadow focus:outline-none hover:shadow-md"
                v-model="query"
                @input="onInput"
            />
            <div class="flex flex-col gap-2">
              <div v-for="result in results">
                <div class="flex flex-col gap-2 rounded-md p-3 shadow-sm hover:shadow-md hover:scale-105 transition-transform duration-300">
                  <NuxtLink
                      :to="`/${result.type}/${result.id}`"
                      @click="clearResults"
                      class="roboto-plain font-semibold"
                  >
                    {{ result.title }}
                  </NuxtLink>
                  <p class="text-sm roboto-plain text-gray-500">{{ type_mapping[result.type] }}</p>
                </div>
              </div>
            </div>
          </div>
        </Dialog>
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
