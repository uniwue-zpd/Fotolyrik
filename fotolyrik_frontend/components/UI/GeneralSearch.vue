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

const icon_map: Record<string, string> = {
  photopoems: 'i-material-symbols-notes-rounded',
  persons: 'i-material-symbols-person-3-outline-rounded',
  places: 'i-material-symbols-location-on-outline-rounded',
  publication_media: 'i-material-symbols-book-5-rounded'
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
  query.value = '';
}
</script>

<template>
  <button @click="visible = true">
    <i class="pi pi-search text-white"/>
  </button>
  <Dialog v-model:visible="visible" modal position="top" class="min-w-[30%]" @hide="clearResults">
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
            <NuxtLink
                :to="`/${result.type}/${result.id}`"
                @click="clearResults"
                class="text-gray-400 hover:text-black flex flex-row items-center space-x-3 rounded-md p-2 shadow-sm hover:shadow-md hover:scale-105 transition-transform duration-300"
            >
              <Icon :name="icon_map[result.type]" class="text-2xl"/>
              <div class="flex flex-col gap-2">
                <h2 class="roboto-plain text-base text-black font-semibold">{{ result.title }}</h2>
                <p class="text-sm roboto-plain text-gray-500">{{ type_mapping[result.type] }}</p>
              </div>
            </NuxtLink>
        </div>
      </div>
    </div>
  </Dialog>
</template>

<style scoped>

</style>
