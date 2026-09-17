<script setup lang="ts">
import { ref, watch, onBeforeUnmount } from "vue";
import { FilterMatchMode } from "@primevue/core";
import {useFiles} from "~/composables/useFiles";
import PhotopoemFilter from "~/components/UI/filters/PhotopoemFilter.vue";
import type {Page, PhotopoemPageable} from "~/utils/types";

const initialPageParameter: PhotopoemPageable = {
  page: 0,
  size: 10,
  sort: 'title,asc'
};

const initialFilters: PhotopoemPageable = {
  title: undefined,
  subtitle: undefined,
  'alt-title': undefined,
  series: undefined,
  volume: undefined,
  issue: undefined,
  'publication-date': undefined,
  'pub-medium-id': undefined,
  'pub-place-id': undefined,
  'location-id': undefined,
  'author-id': undefined,
  'photographer-id': undefined,
  'depicted-person-id': undefined,
  'contributor-id': undefined,
  'theme-id': undefined,
  'image-motif-id': undefined,
  'copyright-image-id': undefined,
  'copyright-text-id': undefined,
  'language-id': undefined
};

const pageParameter = reactive<PhotopoemPageable>({ ...initialPageParameter });
const filters = reactive<PhotopoemPageable>({ ...initialFilters });

const resetFilter = () => {
  Object.assign(pageParameter, initialPageParameter);
  Object.assign(filters, initialFilters);
};

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

const photopoemApi = usePhotopoem();

const pageOptions = computed(() =>
    Array.from({ length: photopoems.value?.totalPages ?? 0 }, (_, index) => ({
      label: `${ index + 1 }`,
      value: index
    }))
);

const {
  data: photopoems,
  pending: isLoading,
  error: hasError,
  refresh
} = useAsyncData<Page<PhotoPoemDTO>>(
    "photopoems-paginated",
    () => photopoemApi.fetchPaginated({ ...pageParameter, ...filters })
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

useHead(() => ({
  title: 'Fotogedichte - Sammlung'
}));
</script>

<template>
  <div class="flex flex-col gap-4">
    <h1 class="text-3xl font-bold outfit-headline text-primary">Fotogedichte</h1>
    <div class="flex flex-row justify-end gap-2">
      <Select v-model="pageParameter.sort" :options="sortOptions" optionLabel="label" optionValue="value" class="h-9 items-center" />
      <Select v-model="pageParameter.size" :options="pageSizeOptions" optionLabel="label" optionValue="value" class="h-9 items-center" />
    </div>
    <div class="flex flex-col gap-5 lg:flex-row justify-between">
      <div class="lg:w-1/4">
        <PhotopoemFilter :filters="filters" @reset-filters="resetFilter" />
      </div>
      <div class="lg:w-3/4">
        <div class="flex flex-col gap-2 h-full">
          <div v-if="isLoading" class="flex flex-col gap-2 items-center">
            <ProgressSpinner/>
            <div class="roboto-plain text-primary font-semibold text-lg">Inhalte werden geladen</div>
          </div>
          <div v-else-if="photopoems" class="flex flex-col gap-2 justify-between min-h-full">
            <div class="flex flex-col gap-2">
              <div v-for="photopoem in photopoems.content" :key="photopoem.id" class="border-2 border-primary rounded-md p-2 shadow-md">
                <div class="flex flex-col gap-1">
                  <NuxtLink :to="`/photopoems/${photopoem.id}`" class="text-lg group relative w-fit outfit-headline font-semibold text-primary">
                    {{ photopoem.title || photopoem.altTitle || "Unbenanntes Fotogedicht" }}
                    <span class="absolute bottom-0 left-0 h-px w-0 bg-current transition-all duration-300 group-hover:w-full"/>
                  </NuxtLink>
                  <div v-if="photopoem.publicationDate" class="flex flex-row gap-2">
                    <span class="text-sm roboto-plain">Erschienen:</span>
                    <div class="text-sm text-primary outfit-headline font-medium">{{ photopoem.publicationDate }}</div>
                  </div>
                  <div v-if="photopoem.publicationMedium" class="flex flex-row gap-2">
                    <span class="text-sm roboto-plain">In:</span>
                    <NuxtLink :to="`/publication_media/${photopoem.publicationMedium.id}`" class="text-sm text-primary outfit-headline font-medium">
                      {{ photopoem.publicationMedium.title }}
                    </NuxtLink>
                  </div>
                </div>
              </div>
            </div>
            <div class="flex items-center justify-center align-bottom gap-3">
              <button
                  type="button"
                  class="px-2 h-9 rounded-md border border-primary text-primary hover:bg-primary hover:text-white transition disabled:opacity-40 disabled:cursor-not-allowed"
                  :disabled="pageParameter.page === 0"
                  aria-label="Vorherige Seite"
                  @click="pageParameter.page!--"
              >
                <i class="pi pi-chevron-left"/>
              </button>
              <Select
                  v-model="pageParameter.page"
                  :options="pageOptions"
                  optionLabel="label"
                  optionValue="value"
                  :disabled="photopoems?.totalPages <= 1"
                  class="h-9 items-center"
              />
              <button
                  type="button"
                  class="px-2 h-9 rounded-md border border-primary text-primary hover:bg-primary hover:text-white transition disabled:opacity-40 disabled:cursor-not-allowed"
                  :disabled="pageParameter.page! >= (photopoems?.totalPages ?? 1) - 1"
                  aria-label="Nächste Seite"
                  @click="pageParameter.page!++"
              >
                <i class="pi pi-chevron-right"/>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>
