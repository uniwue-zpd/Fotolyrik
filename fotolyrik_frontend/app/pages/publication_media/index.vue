<script setup lang="ts">
import CreateButton from "~/components/UI/buttons/CreateButton.vue";
import { ref, watch } from "vue";
import PageSelection from "~/components/UI/buttons/PageSelection.vue";
import ContentCard from "~/components/UI/cards/ContentCard.vue";
import PublicationMediaFilter from "~/components/UI/filters/PubMediaFilter.vue";
import type { Page, PubMediumPageable } from "~/utils/types";

const { width } = useWindowSize();

const initialPageParameter: PubMediumPageable = {
  page: 0,
  size: 10,
  sort: "title,asc"
};

const initialFilters: PubMediumPageable = {
  title: undefined,
  subtitle: undefined,
  'publisher.name': undefined,
  editorialOffice: undefined,
  startYear: undefined,
  endYear: undefined,
  amountVolumes: undefined,
  amountIssues: undefined
};

const pageParameter = reactive<PubMediumPageable>({ ...initialPageParameter });
const filters = reactive<PubMediumPageable>({ ...initialFilters });

const sortOptions = ref([
  { label: "Aufsteigend (A-Z)", value: "title,asc" },
  { label: "Absteigend (Z-A)", value: "title,desc" }
]);

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
} = useAsyncData<Page<PubMediumPreviewDTO>>(
  "publication-media-paginated",
  () => pubMediumApi.filterPaginated({ ...pageParameter, ...filters })
);

const resetFilter = () => {
  Object.assign(pageParameter, initialPageParameter);
  Object.assign(filters, initialFilters);
};

const debouncedRefresh = debounce(() => {
  if (pageParameter.page === 0) {
    refresh();
  } else {
    pageParameter.page = 0;
  }
}, 300);

watch(filters, () => debouncedRefresh(), { deep: true });
watch(pageParameter, () => refresh(), { deep: true });

useHead(() => ({
  title: "Publikationsmedien - Sammlung"
}));
</script>

<template>
  <div class="flex flex-col gap-4">
    <h1 class="text-3xl outfit-headline text-primary font-bold">Publikationsmedien</h1>
    <p class="roboto-plain">
      Eine Übersicht aller Publikationsmedien in der Datenbank. Klicken Sie auf ein Publikationsmedium, um Details
      anzuzeigen oder zu bearbeiten.
    </p>
    <div class="flex flex-row justify-between gap-2">
      <CreateButton to="/publication_media/create" />
      <div class="flex flex-row gap-2">
        <Select v-model="pageParameter.sort" :options="sortOptions" optionLabel="label" optionValue="value"
          class="h-9 items-center" />
        <Select v-model="pageParameter.size" :options="pageSizeOptions" optionLabel="label" optionValue="value"
          class="h-9 items-center" />
      </div>
    </div>
    <div class="flex flex-col gap-5 lg:flex-row justify-between">
      <div class="lg:w-1/4">
        <PublicationMediaFilter :filters="filters" @reset-filters="resetFilter" />
      </div>
      <div class="lg:w-3/4 lg:max-w-3/4 min-w-0">
        <div v-if="hasError" class="roboto-plain text-red-600">
          Die Publikationsmedien konnten nicht geladen werden.
        </div>
        <span v-else-if="!publicationMedia || publicationMedia?.totalElements === 0"
          class="flex w-full justify-center text-primary text-2xl py-5">Keine Publikationsmedien gefunden!</span>
        <div v-else-if="publicationMedia" class="flex flex-col gap-2 justify-between min-h-full min-w-0">
          <div class="flex flex-col gap-2 min-w-0">
            <ContentCard v-for="{ id, title, startYear, endYear } in publicationMedia.content" :key="id"
              :headerLink="`/publication_media/${id}`" :showPlaceholder="isLoading">
              <template #header>
                {{ title || "Unbenanntes Publikationsmedium" }}
              </template>
              <template #subContent>
                <div class="flex flex-row gap-2">
                  <div class="text-sm roboto-plain">Erschienen:</div>
                  <div class="text-sm text-primary outfit-headline font-medium">
                    {{ startYear ?? '????' }} - {{ endYear ?? '????' }}
                  </div>
                </div>
              </template>
            </ContentCard>
          </div>
          <PageSelection :pageParameter :totalPages="publicationMedia.totalPages" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
