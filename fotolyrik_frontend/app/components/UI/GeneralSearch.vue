<script setup lang="ts">
const visible = ref(false);

// Handle search input
const query = ref('');
const results = ref<GeneralSearchResult[]>([]);
const loading = ref(false);
const lastSearched = ref('');
const type_mapping: { [key: string]: string } = {
  photopoems: 'Fotogedicht',
  persons: 'Person',
  places: 'Ort',
  publication_media: 'Publikationsmedium',
  locations: "Fundort",
  keywords: 'Schlagwort'
}

const icon_map: Record<string, string> = {
  photopoems: 'i-lucide-images',
  persons: 'i-lucide-users',
  places: 'i-lucide-map-pin',
  publication_media: 'i-lucide-newspaper',
  locations: 'i-lucide-archive',
  keywords: 'i-lucide-tags'
}

const fetchResults = async (search: string) => {
  if (!search.trim()) {
    results.value = []
    return
  }

  loading.value = true
  try {
    const response = await $fetch<GeneralSearchResult[]>('/api/search',
        { params: { query: search }}
    );
    // Ignore responses for an outdated search term
    if (search !== query.value) return
    results.value = response
  } catch (error) {
    console.error('Search error:', error)
    results.value = []
  } finally {
    if (search === query.value) {
      lastSearched.value = search
      loading.value = false
    }
  }
}

// Only show "no results" once the request for the current term has finished
const showNoResults = computed(() =>
  query.value.length >= 3 && !loading.value && lastSearched.value === query.value && results.value.length === 0
)

const debouncedFetch = debounce(fetchResults, 300);

const onInput = () => {
  if (query.value.length >= 3) {
    debouncedFetch(query.value);
  } else {
    results.value = []
  }
}

const clearResults = () => {
  results.value = []
  visible.value = false
  query.value = '';
  lastSearched.value = '';
  loading.value = false;
}

// Reset the search whenever the panel closes (outside click, Esc or result click)
watch(visible, (open) => {
  if (!open) clearResults()
})

watch(query, (val) => {
  if (!val || val.length < 3) {
    results.value = []
  }
})
</script>

<template>
  <UPopover
    v-model:open="visible"
    :content="{ side: 'bottom', align: 'end', sideOffset: 8, collisionPadding: 16 }"
    :ui="{ content: 'w-[calc(100vw-2rem)] sm:w-[28rem] max-w-[calc(100vw-2rem)]' }"
  >
    <UButton
      icon="i-lucide-search"
      variant="link"
      aria-label="Suche öffnen"
      class="text-white hover:text-white cursor-pointer"
    />
    <template #content>
      <div class="flex flex-col gap-3 p-3">
        <UInput
          v-model="query"
          icon="i-lucide-search"
          placeholder="Mind. 3 Zeichen"
          size="lg"
          autofocus
          class="w-full"
          @update:model-value="onInput"
        >
          <template v-if="query" #trailing>
            <UButton
              color="neutral"
              variant="link"
              size="sm"
              icon="i-lucide-x"
              aria-label="Suche leeren"
              class="cursor-pointer"
              @click="query = ''"
            />
          </template>
        </UInput>
        <div v-if="results.length" class="flex flex-col gap-1 max-h-[50vh] sm:max-h-[60vh] overflow-y-auto">
          <NuxtLink
            v-for="result in results"
            :key="`${result.type}-${result.id}`"
            :to="`/${result.type}/${result.id}`"
            class="flex flex-row items-center gap-3 rounded-md p-2 text-muted hover:text-default hover:bg-elevated transition-colors"
            @click="clearResults"
          >
            <UIcon :name="icon_map[result.type] || 'i-lucide-circle-help'" class="size-6 shrink-0" />
            <div class="flex flex-col min-w-0">
              <span class="roboto-plain text-sm sm:text-base font-semibold text-default truncate">{{ result.title }}</span>
              <span class="text-sm roboto-plain text-muted">{{ type_mapping[result.type] }}</span>
            </div>
          </NuxtLink>
        </div>
        <div v-else-if="showNoResults" class="flex flex-col items-center gap-1 py-4 text-center" aria-live="polite">
          <UIcon name="i-lucide-search-x" class="size-6 text-muted" />
          <p class="roboto-plain text-sm text-muted">
            Keine Ergebnisse für „{{ query }}“ gefunden.
          </p>
        </div>
      </div>
    </template>
  </UPopover>
</template>
