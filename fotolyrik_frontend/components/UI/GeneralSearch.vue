<script setup lang="ts">
import { ref } from "vue";
import apiClient from "~/service/api";

const visible = ref(false);

// Handle search input
const query = ref('');
const results = ref<GeneralSearchResult[]>([]);
const type_mapping: { [key: string]: string } = {
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
  if (query.value.length >= 2) {
    debouncedFetch(query.value);
  } else {
    results.value = []
  }
}

const clearResults = () => {
  results.value = []
  visible.value = false
}
</script>

<template>
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
          <div class="flex flex-col gap-2 rounded-md p-2 shadow-sm hover:shadow-md hover:scale-105 transition-transform duration-300">
            <NuxtLink
                :to="`/${result.type}/${result.id}`"
                @click="clearResults"
                class="roboto-plain text-base font-semibold text-black"
            >
              {{ result.title }}
            </NuxtLink>
            <p class="text-sm roboto-plain text-gray-500">{{ type_mapping[result.type] }}</p>
          </div>
        </div>
      </div>
    </div>
  </Dialog>
</template>

<style scoped>

</style>
