<script setup lang="ts">
import { ref, watch } from "vue";
import ContentCard from "~/components/UI/cards/ContentCard.vue";
import PageSelection from "~/components/UI/buttons/PageSelection.vue";
import KeywordFilter from "~/components/UI/filters/KeywordFilter.vue";
import type { KeywordPageable, KeywordPreviewDTO, Page } from "~/utils/types";

const initialPageParameter: KeywordPageable = {
  page: 0,
  size: 10,
  sort: "value,asc"
};

const initialFilters: KeywordPageable = {
  value: undefined,
  'gnd-id': undefined
};

const pageParameter = reactive<KeywordPageable>({ ...initialPageParameter });
const filters = reactive<KeywordPageable>({ ...initialFilters });

const resetFilter = () => {
  Object.assign(pageParameter, initialPageParameter);
  Object.assign(filters, initialFilters);
};

const sortOptions = ref([
  { label: "Schlagwort A–Z", value: "value,asc" },
  { label: "Schlagwort Z–A", value: "value,desc" }
]);

const sort = computed({
  get: () => String(pageParameter.sort ?? initialPageParameter.sort),
  set: (value: string) => { pageParameter.sort = value; }
});

const pageSizeOptions = ref([
  { label: "5 pro Seite", value: 5 },
  { label: "10 pro Seite", value: 10 },
  { label: "15 pro Seite", value: 15 },
  { label: "30 pro Seite", value: 30 },
  { label: "50 pro Seite", value: 50 }
]);

const keywordApi = useKeyword();

const {
  data: keywords,
  pending: isLoading,
  refresh
} = useAsyncData<Page<KeywordPreviewDTO>>(
  "keywords-paginated",
  () => keywordApi.fetchPaginated({ ...pageParameter, ...filters })
);

const debouncedRefresh = debounce(() => {
  if (pageParameter.page === 0) {
    refresh();
  } else {
    pageParameter.page = 0;
  }
}, 300);

watch(filters, () => debouncedRefresh(), { deep: true });
watch(pageParameter, () => refresh(), { deep: true });

const totalCount = computed(() => keywords.value?.totalElements ?? 0);
const totalCountLabel = computed(() =>
  `${totalCount.value.toLocaleString('de-DE')} ${totalCount.value === 1 ? 'Schlagwort' : 'Schlagwörter'}`
  + (filters.value || filters['gnd-id'] ? ' gefunden' : ' insgesamt')
);

// Keyword cloud
// function getKeywordColor(id: number): string {
//   const hue = (id * 137.508) % 360;
//   return `hsla(${hue}, 80%, 70%, 0.4)`;
// }
// const cloudSizes = ["text-sm", "text-base", "text-lg", "text-base"];
//
// const hash = (id: number) => Math.abs((id * 2654435761) | 0);
// const cloudSize = (id: number) => cloudSizes[(hash(id) >> 3) % cloudSizes.length];
// const skeletonWidths = ["w-16", "w-24", "w-20", "w-28", "w-14", "w-32"];

useHead(() => ({
  title: 'Schlagwörter - Übersicht'
}));
</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex flex-col gap-2">
      <h1 class="text-3xl font-bold outfit-headline text-primary">Schlagwörter</h1>
      <p class="roboto-plain">
        Eine Übersicht aller Schlagwörter in der Datenbank. Klicken Sie auf ein Schlagwort, um Details anzuzeigen oder zu
        bearbeiten.
      </p>
    </div>
    <div class="flex flex-col gap-5 lg:flex-row justify-between">
      <div class="lg:w-1/4">
        <KeywordFilter :filters="filters" @reset-filters="resetFilter" />
      </div>
      <div class="flex flex-col gap-4 lg:w-3/4">
        <div class="grid grid-cols-[1fr_auto] gap-2 sm:flex sm:flex-wrap sm:justify-end">
          <UButton to="/keywords/create" icon="i-lucide-square-pen" label="Neu anlegen" color="neutral" variant="outline"
            size="lg" class="col-span-2 justify-center text-primary hover:shadow-md" />
          <USelect v-model="sort" :items="sortOptions" size="lg" class="w-full sm:w-48" />
          <USelect v-model="pageParameter.size" :items="pageSizeOptions" size="lg" class="sm:w-36" />
        </div>
        <div class="flex flex-col gap-2 justify-between">
          <p v-if="keywords" class="text-sm text-muted roboto-plain" aria-live="polite">
            {{ totalCountLabel }}
          </p>
          <div v-if="isLoading" class="flex flex-col gap-2">
            <USkeleton v-for="n in pageParameter.size" :key="n" class="h-13 w-full rounded-md" />
          </div>
          <div v-else-if="!keywords || keywords.content.length === 0"
            class="flex w-full justify-center text-primary text-2xl py-5">
            Keine Schlagwörter gefunden!
          </div>
          <div v-else class="flex flex-col gap-2">
            <ContentCard v-for="keyword in keywords.content" :key="keyword.id" :headerLink="`/keywords/${keyword.id}`">
              <template #header>{{ keyword.value }}</template>
              <template #subContent>
                <NuxtLink v-if="keyword.gndId" :to="`https://explore.gnd.network/gnd/${keyword.gndId}`" target="_blank"
                  rel="noopener" title="Im GND-Explorer öffnen"
                  class="group/gnd inline-flex items-center gap-1 text-sm text-primary outfit-headline font-medium">
                  <img src="/gnd-logo.png" alt="GND" class="h-4 w-auto shrink-0" />
                  <span class="group-hover/gnd:underline">{{ keyword.gndId }}</span>
                  <UIcon name="i-lucide-external-link" class="size-3 shrink-0 opacity-70" />
                </NuxtLink>
              </template>
            </ContentCard>
          </div>
          <PageSelection :page-parameter="pageParameter" :total-pages="keywords?.totalPages" />
        </div>
      </div>
    </div>

    <!--
      Keyword cloud
        <div class="rounded-xl border border-default bg-elevated/50 p-6">
          <div v-if="isLoading" class="flex flex-wrap justify-center gap-2.5">
            <USkeleton v-for="n in pageParameter.size" :key="n"
              :class="['h-8 rounded-full', skeletonWidths[n % skeletonWidths.length]]" />
          </div>
          <div v-else-if="!keywords || keywords.content.length === 0"
            class="flex w-full justify-center text-primary text-2xl py-5">
            Keine Schlagwörter gefunden!
          </div>
          <div v-else>
            <div class="flex flex-wrap items-center justify-center gap-2.5">
              <NuxtLink v-for="keyword in keywords.content" :key="keyword.id" :to="`/keywords/${keyword.id}`"
                no-prefetch
                :title="keyword.gndId ? `GND: ${keyword.gndId}` : keyword.value"
                :class="[
                  'inline-flex items-center gap-1.5 rounded-full px-3.5 py-1.5 outfit-headline font-medium',
                  'transition duration-200 hover:-translate-y-0.5 hover:shadow-md',
                  'focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary',
                  cloudSize(keyword.id)
                ]"
                :style="{ background: getKeywordColor(keyword.id) }">
                {{ keyword.value }}
                <img v-if="keyword.gndId" src="/gnd-logo.png" alt="GND" class="h-3.5 w-auto opacity-80" />
              </NuxtLink>
            </div>
          </div>
        </div>
    -->
  </div>
</template>
