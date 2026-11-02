<script setup lang="ts">
import { ref, watch } from "vue";
import PageSelection from "~/components/UI/buttons/PageSelection.vue";
import ContentCard from "~/components/UI/cards/ContentCard.vue";
import type { Page, Pageable } from "~/utils/types";

const { width } = useWindowSize();

const initialPageParameter: Pageable = {
  page: 0,
  size: 10,
  sort: "title,asc"
};

const pageParameter = reactive<Pageable>({ ...initialPageParameter });

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
  () => pubMediumApi.searchPaginated("", { ...pageParameter })
);

watch(pageParameter, () => refresh(), { deep: true });

useHead(() => ({
  title: "Publikationsmedien - Sammlung"
}));
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-2xl outfit-headline text-primary font-bold">Publikationsmedien</h1>
    <p class="roboto-plain">
      Eine Übersicht aller Publikationsmedien in der Datenbank. Klicken Sie auf ein Publikationsmedium, um Details
      anzuzeigen oder zu bearbeiten.
    </p>
    <Divider />
    <div class="flex flex-row justify-between gap-2">
      <div class="p-2 border border-solid rounded-md hover:shadow-md">
        <NuxtLink to="/publication_media/create" class="flex items-center">
          <i class="pi pi-pen-to-square mr-2" />
          <div class="text-primary roboto-plain">Neu {{ width > 600 ? 'anlegen' : '' }}</div>
        </NuxtLink>
      </div>
      <div class="flex flex-row gap-2">
        <Select v-model="pageParameter.sort" :options="sortOptions" optionLabel="label" optionValue="value"
          class="h-9 items-center" />
        <Select v-model="pageParameter.size" :options="pageSizeOptions" optionLabel="label" optionValue="value"
          class="h-9 items-center" />
      </div>
    </div>
    <div v-if="false" class="flex flex-col gap-2 items-center">
      <ProgressSpinner />
      <div class="roboto-plain text-primary font-semibold text-lg">Inhalte werden geladen</div>
    </div>
    <div v-else-if="hasError" class="roboto-plain text-red-600">
      Die Publikationsmedien konnten nicht geladen werden.
    </div>
    <div v-else-if="publicationMedia" class="flex flex-col gap-2 justify-between min-h-full">
      <div class="flex flex-col gap-2">
        <ContentCard v-for="{ id, title, startYear, endYear } in publicationMedia.content" :key="id"
          :showPlaceholder="isLoading" :headerLink="`/publication_media/${id}`">
          <template #header>
            {{ title || "Unbenanntes Publikationsmedium" }}
          </template>
          <template #subContent>
            <div class="flex flex-row gap-2">
              <div class="text-sm roboto-plain">Erschienen:</div>
              <div class="text-sm text-primary outfit-headline font-medium">
                {{ startYear ?? '????' }} - {{ endYear ?? '????' }}</div>
            </div>
          </template>
        </ContentCard>
      </div>
      <PageSelection :pageParameter :totalPages="publicationMedia?.totalPages" />
    </div>
  </div>
</template>

<style scoped></style>
