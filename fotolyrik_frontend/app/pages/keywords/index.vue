<script setup lang="ts">
import { ref, watch } from "vue";
import type { Page } from "~/utils/types";
import PageSelection from "~/components/UI/buttons/PageSelection.vue";

function getKeywordColor(id: number): string {
  const hue = (id * 137.508) % 360;
  return `hsla(${hue}, 80%, 70%, 0.4)`;
}

const initialPageParameter = {
  page: 0,
  size: 10,
  sort: "value,asc"
};
const pageParameter = reactive<Pageable>({ ...initialPageParameter });
const searchQuery = ref("");

const keyword_api = useKeyword();

const {
  data: keywords,
  pending: isLoading,
  refresh
} = useAsyncData<Page<KeywordDTO>>(
  "keywords-paginated",
  () => keyword_api.searchPaginated(searchQuery.value, { ...pageParameter })
);

const pageSizeOptions = ref([
  { label: "5 pro Seite", value: 5 },
  { label: "10 pro Seite", value: 10 },
  { label: "15 pro Seite", value: 15 },
  { label: "30 pro Seite", value: 30 },
  { label: "50 pro Seite", value: 50 }
]);

watch([pageParameter, searchQuery], () => refresh(), { deep: true });
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-2xl outfit-headline text-primary font-bold">Schlagwörter</h1>
    <p class="roboto-plain">
      Eine Übersicht aller Schlagwörter in der Datenbank. Klicken Sie auf ein Schlagwort, um Details anzuzeigen oder zu
      bearbeiten.
    </p>
    <Tabs value="0">
      <TabList>
        <Tab value="0">
          <p class="roboto-plain font-bold">Tabellarische Übersicht</p>
        </Tab>
        <Tab value="1">
          <p class="roboto-plain font-bold">Tag-Ansicht</p>
        </Tab>
      </TabList>
      <div class="flex flex-row justify-between items-center mx-4 mt-4">
        <div class="p-2 border border-solid rounded-md hover:shadow-md">
          <NuxtLink to="/keywords/create" class="flex items-center">
            <i class="pi pi-pen-to-square mr-2" />
            <div class="text-primary roboto-plain">Neu anlegen</div>
          </NuxtLink>
        </div>
        <Select v-model="pageParameter.size" :options="pageSizeOptions" optionLabel="label" optionValue="value"
          class="h-9 items-center" />
        <IconField>
          <InputIcon>
            <i class="pi pi-search" />
          </InputIcon>
          <InputText v-model="searchQuery" type="text" placeholder="Alles durchsuchen" />
        </IconField>
      </div>
      <span v-if="!keywords || keywords?.totalElements === 0"
        class="flex w-full justify-center text-primary text-2xl py-5">Keine Schlagwörter gefunden!</span>
      <TabPanels v-else-if="keywords" class="min-h-30">
        <TabPanel value="0" class="flex flex-col">
          <div class="flex flex-col gap-2 justify-between">
            <div class="flex flex-col gap-2">
              <div v-if="isLoading" v-for="_ in pageParameter.size"
                class="border-2 border-primary rounded-md p-2 shadow-md animate-pulse">
                <span
                  class="absolute bottom-0 left-0 h-px w-0 bg-current transition-all duration-300 group-hover:w-full" />
                <div class="flex flex-col gap-1 text-transparent">
                  Placeholder
                  <div class="flex flex-row gap-2">
                    <div class="text-sm outfit-headline font-medium text-transparent">Placeholder</div>
                  </div>
                </div>
              </div>
              <div v-else-if="keywords" v-for="keyword in keywords.content" :key="keyword.id"
                class="border-2 border-primary rounded-md p-2 shadow-md">
                <div class="flex flex-col gap-1">
                  <NuxtLink :to="`/keywords/${keyword.id}`"
                    class="text-lg group relative w-fit outfit-headline font-semibold text-primary">
                    {{ keyword.value }}
                    <span
                      class="absolute bottom-0 left-0 h-px w-0 bg-current transition-all duration-300 group-hover:w-full" />
                  </NuxtLink>

                  <div v-if="keyword" class="flex flex-row gap-2">
                    <span class="text-sm roboto-plain">GND-ID:</span>
                    <NuxtLink :to="`https://explore.gnd.network/gnd/${keyword.gndId}`" title="Zum GND-Explorer wechseln"
                      target="_blank">
                      <div class="text-sm text-primary outfit-headline font-medium hover:underline">
                        {{ keyword.gndId }}
                      </div>
                    </NuxtLink>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </TabPanel>
        <TabPanel value="1">
          <div class="flex flex-wrap gap-3">
            <NuxtLink v-if="keywords" v-for="keyword in keywords.content" :to="`/keywords/${keyword.id}`"
              class="font-medium roboto-plain p-1 rounded-lg transition-transform hover:scale-105 shadow-md hover:shadow-lg"
              :style="[{ background: getKeywordColor(keyword.id) }]">
              {{ keyword.value }}
            </NuxtLink>
          </div>
        </TabPanel>
      </TabPanels>
      <PageSelection :pageParameter :totalPages="keywords?.totalPages" />
    </Tabs>
  </div>
</template>

<style scoped></style>