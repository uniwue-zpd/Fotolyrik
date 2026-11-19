<script setup lang="ts">
import PageSelection from "~/components/UI/buttons/PageSelection.vue";
import ContentCard from "~/components/UI/cards/ContentCard.vue";
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
  'pubmedium-id': undefined,
  'pubplace-id': undefined,
  'location-id': undefined,
  'author-id': undefined,
  'photographer-id': undefined,
  'depicted-person-id': undefined,
  'other-contributor-id': undefined,
  'theme-id': undefined,
  'image-motif-id': undefined,
  'copyright-status-image-id': undefined,
  'copyright-status-text-id': undefined,
  'language-id': undefined
};

const pageParameter = reactive<PhotopoemPageable>({ ...initialPageParameter });
const filters = reactive<PhotopoemPageable>({ ...initialFilters });

const resetFilter = () => {
  Object.assign(pageParameter, initialPageParameter);
  Object.assign(filters, initialFilters);
};

const sortOptions = ref([
  { label: "Aufsteigend", value: "title,asc" },
  { label: "Absteigend", value: "title,desc" }
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

const photopoemApi = usePhotopoem();

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
    <div class="flex flex-col gap-2">
      <h1 class="text-3xl font-bold outfit-headline text-primary">Fotogedichte</h1>
      <p class="roboto-plain">
        Eine Übersicht aller Fotogedichte in der Datenbank. Klicken Sie auf ein Fotogedicht, um Details
        anzuzeigen oder zu bearbeiten.
      </p>
    </div>
    <div class="flex flex-col gap-5 lg:flex-row justify-between">
      <div class="lg:w-1/4">
        <PhotopoemFilter :filters="filters" @reset-filters="resetFilter" />
      </div>
      <div class="flex flex-col gap-4 lg:w-3/4 min-w-0">
        <div class="grid grid-cols-[1fr_auto] gap-2 sm:flex sm:flex-wrap sm:justify-end">
          <UButton to="/photopoems/create" icon="i-lucide-square-pen" label="Neu anlegen" color="neutral"
            variant="outline" size="lg" class="col-span-2 justify-center text-primary hover:shadow-md" />
          <USelect v-model="sort" :items="sortOptions" size="lg" class="w-full sm:w-48" />
          <USelect v-model="pageParameter.size" :items="pageSizeOptions" size="lg" class="sm:w-36" />
        </div>
        <div class="flex flex-col gap-2 justify-between">
          <div v-if="hasError" class="roboto-plain text-red-600">
            Die Fotogedichte konnten nicht geladen werden.
          </div>
          <div v-else-if="isLoading" class="flex flex-col gap-2 items-center py-5">
            <ProgressSpinner />
            <div class="roboto-plain text-primary font-semibold text-lg">Inhalte werden geladen</div>
          </div>
          <div v-else-if="!photopoems || photopoems.content.length === 0"
            class="flex w-full justify-center text-primary text-2xl py-5">
            Keine Fotogedichte gefunden!
          </div>
          <div v-else class="flex flex-col gap-2 min-w-0">
            <div class="flex flex-col gap-2">
              <ContentCard v-for="photopoem in photopoems.content" :key="photopoem.id"
                :headerLink="`/photopoems/${photopoem.id}`">
                <template #header>
                  {{ photopoem.title || photopoem.altTitle || "Unbenanntes Fotogedicht" }}
                </template>
                <template #subContent>
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
                </template>
              </ContentCard>
            </div>
          </div>
          <PageSelection :page-parameter="pageParameter" :total-pages="photopoems?.totalPages" />
        </div>
      </div>
    </div>
  </div>
</template>
