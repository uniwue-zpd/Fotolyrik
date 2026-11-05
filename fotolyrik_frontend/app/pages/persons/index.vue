<script setup lang="ts">
import ContentCard from "~/components/UI/cards/ContentCard.vue";
import PageSelection from "~/components/UI/buttons/PageSelection.vue";
import PersonFilter from "~/components/UI/filters/PersonFilter.vue";
import type { Page, PersonPageable } from "~/utils/types";

const initialPageParameter: PersonPageable = {
  page: 0,
  size: 15,
  sort: "lastName,asc"
};

const initialFilters: PersonPageable = {
  name: undefined,
  'birth-year': undefined,
  'death-year': undefined,
  sex: null
};

const pageParameter = reactive<PersonPageable>({ ...initialPageParameter });
const filters = reactive<PersonPageable>({ ...initialFilters });

const resetFilter = () => {
  Object.assign(pageParameter, initialPageParameter);
  Object.assign(filters, initialFilters);
};

const sortOptions = ref([
  { label: "Name aufsteigend", value: "lastName,asc" },
  { label: "Name absteigend", value: "lastName,desc" }
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

const personApi = usePerson();

const {
  data: persons,
  pending: isLoading,
  refresh
} = useAsyncData<Page<PersonPreviewDTO>>(
  "persons-paginated",
  () => personApi.fetchPaginated({ ...pageParameter, ...filters })
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
  title: 'Personen - Übersicht'
}));
</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex flex-col gap-2">
      <h1 class="text-3xl font-bold outfit-headline text-primary">Personen</h1>
      <p class="roboto-plain">
        Eine Übersicht aller Personen in der Datenbank. Klicken Sie auf eine Person, um Details anzuzeigen oder zu
        bearbeiten.
      </p>
    </div>
    <div class="grid grid-cols-[1fr_auto] gap-2 sm:flex sm:flex-wrap sm:justify-end">
      <UButton to="/persons/create" icon="i-lucide-square-pen" label="Neu anlegen" color="neutral" variant="outline"
        size="lg" class="col-span-2 justify-center text-primary hover:shadow-md" />
      <USelect v-model="sort" :items="sortOptions" size="lg" class="w-full sm:w-48" />
      <USelect v-model="pageParameter.size" :items="pageSizeOptions" size="lg" class="sm:w-36" />
    </div>
    <div class="flex flex-col gap-5 lg:flex-row justify-between">
      <div class="lg:w-1/4">
        <PersonFilter :filters="filters" @reset-filters="resetFilter" />
      </div>
      <div class="flex flex-col gap-2 justify-between lg:w-3/4">
        <div v-if="isLoading" class="flex flex-col gap-2 items-center py-5">
          <ProgressSpinner />
          <div class="roboto-plain text-primary font-semibold text-lg">Inhalte werden geladen</div>
        </div>
        <div v-else-if="!persons || persons.content.length === 0"
          class="flex w-full justify-center text-primary text-2xl py-5">
          Keine Personen gefunden!
        </div>
        <div v-else class="flex flex-col gap-2">
            <ContentCard v-for="{ id, fullName, studioName, birthYear, deathYear, pseudonyms, gndId } in persons.content" :key="id"
              :headerLink="`/persons/${id}`">
              <template #header>
                <template v-if="fullName?.trim() || studioName?.trim()">
                  {{ fullName?.trim() || studioName?.trim() }}
                </template>
                <span v-else class="text-muted font-medium">Name unbekannt</span>
              </template>
              <template #subContent>
                <div v-if="birthYear || deathYear || pseudonyms.length > 0 || gndId"
                  class="flex flex-row flex-wrap gap-x-2 text-sm text-primary outfit-headline font-medium">
                  <span v-if="birthYear || deathYear">({{ birthYear ?? '?' }} - {{ deathYear ?? '?' }})</span>
                  <span v-if="pseudonyms.length > 0" class="whitespace-normal">
                    aka. {{ pseudonyms.map(p => `"${p}"`).join(', ') }}
                  </span>
                  <NuxtLink v-if="gndId" :to="`https://explore.gnd.network/gnd/${gndId}`" target="_blank" rel="noopener"
                    title="Im GND-Explorer öffnen"
                    class="group/gnd inline-flex items-center gap-1 text-sm text-primary outfit-headline font-medium">
                    <img src="/gnd-logo.png" alt="GND" class="h-4 w-auto shrink-0" />
                    <span class="group-hover/gnd:underline">{{ gndId }}</span>
                    <UIcon name="i-lucide-external-link" class="size-3 shrink-0 opacity-70" />
                  </NuxtLink>
                </div>
              </template>
            </ContentCard>
        </div>
        <PageSelection :page-parameter="pageParameter" :total-pages="persons?.totalPages" />
      </div>
    </div>
  </div>
</template>
