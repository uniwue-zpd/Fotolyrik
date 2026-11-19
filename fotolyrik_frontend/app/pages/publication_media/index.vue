<script setup lang="ts">
import ContentCard from "~/components/UI/cards/ContentCard.vue";
import PageSelection from "~/components/UI/buttons/PageSelection.vue";
import PubMediaFilter from "~/components/UI/filters/PubMediaFilter.vue";
import type { Page, PubMediumDTO, PubMediumPageable } from "~/utils/types";

const initialPageParameter: PubMediumPageable = {
  page: 0,
  size: 10,
  sort: "title,asc"
};

const initialFilters: PubMediumPageable = {
  title: undefined,
  subtitle: undefined,
  publisher: undefined,
  'editorial-office': undefined,
  'start-year': undefined,
  'end-year': undefined,
  'amount-volumes': undefined,
  'amount-issues': undefined
};

const pageParameter = reactive<PubMediumPageable>({ ...initialPageParameter });
const filters = reactive<PubMediumPageable>({ ...initialFilters });

const resetFilter = () => {
  Object.assign(pageParameter, initialPageParameter);
  Object.assign(filters, initialFilters);
};

const sortOptions = ref([
  { label: "Titel aufsteigend", value: "title,asc" },
  { label: "Titel absteigend", value: "title,desc" }
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

const pubMediumApi = usePubMedium();

const {
  data: publicationMedia,
  pending: isLoading,
  error: hasError,
  refresh
} = useAsyncData<Page<PubMediumDTO>>(
  "publication-media-paginated",
  () => pubMediumApi.fetchPaginated({ ...pageParameter, ...filters })
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

const isFiltered = computed(() => Object.values(filters).some(v => v !== undefined && v !== null && v !== ''));
const totalCount = computed(() => publicationMedia.value?.totalElements ?? 0);
const totalCountLabel = computed(() =>
  `${totalCount.value.toLocaleString('de-DE')} ${totalCount.value === 1 ? 'Publikationsmedium' : 'Publikationsmedien'}`
  + (isFiltered.value ? ' gefunden' : ' insgesamt')
);

useHead(() => ({
  title: 'Publikationsmedien - Übersicht'
}));
</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex flex-col gap-2">
      <h1 class="text-3xl font-bold outfit-headline text-primary">Publikationsmedien</h1>
      <p class="roboto-plain">
        Eine Übersicht aller Publikationsmedien in der Datenbank. Klicken Sie auf ein Publikationsmedium, um Details
        anzuzeigen oder zu bearbeiten.
      </p>
    </div>
    <div class="flex flex-col gap-5 lg:flex-row justify-between">
      <div class="lg:w-1/4">
        <PubMediaFilter :filters="filters" @reset-filters="resetFilter" />
      </div>
      <div class="flex flex-col gap-4 lg:w-3/4 min-w-0">
        <div class="grid grid-cols-[1fr_auto] gap-2 sm:flex sm:flex-wrap sm:justify-end">
          <UButton to="/publication_media/create" icon="i-lucide-square-pen" label="Neu anlegen" color="neutral"
            variant="outline" size="lg" class="col-span-2 justify-center text-primary hover:shadow-md" />
          <USelect v-model="sort" :items="sortOptions" size="lg" class="w-full sm:w-48" />
          <USelect v-model="pageParameter.size" :items="pageSizeOptions" size="lg" class="sm:w-36" />
        </div>
        <div class="flex flex-col gap-2 justify-between">
          <p v-if="publicationMedia" class="text-sm text-muted roboto-plain" aria-live="polite">
            {{ totalCountLabel }}
          </p>
          <div v-if="hasError" class="roboto-plain text-red-600">
            Die Publikationsmedien konnten nicht geladen werden.
          </div>
          <div v-else-if="isLoading" class="flex flex-col gap-2 items-center py-5">
            <ProgressSpinner />
            <div class="roboto-plain text-primary font-semibold text-lg">Inhalte werden geladen</div>
          </div>
          <div v-else-if="!publicationMedia || publicationMedia.content.length === 0"
            class="flex w-full justify-center text-primary text-2xl py-5">
            Keine Publikationsmedien gefunden!
          </div>
          <div v-else class="flex flex-col gap-2 min-w-0">
            <ContentCard v-for="{ id, title, subtitle, startYear, endYear } in publicationMedia.content" :key="id"
              :headerLink="`/publication_media/${id}`">
              <template #header>
                <template v-if="title?.trim()">{{ title }}</template>
                <span v-else class="text-muted font-medium">Unbenanntes Publikationsmedium</span>
              </template>
              <template #subContent>
                <div v-if="subtitle || startYear || endYear"
                  class="flex flex-row flex-wrap gap-x-2 text-sm text-primary outfit-headline font-medium">
                  <span v-if="startYear || endYear">({{ startYear ?? '?' }} - {{ endYear ?? '?' }})</span>
                  <span v-if="subtitle" class="whitespace-normal">{{ subtitle }}</span>
                </div>
              </template>
            </ContentCard>
          </div>
          <PageSelection :page-parameter="pageParameter" :total-pages="publicationMedia?.totalPages" />
        </div>
      </div>
    </div>
  </div>
</template>
