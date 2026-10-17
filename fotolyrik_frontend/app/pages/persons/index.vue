<script setup lang="ts">
import { nextTick, ref } from "vue";
import ContentCard from "~/components/UI/cards/ContentCard.vue";
import PageSelection from "~/components/UI/buttons/PageSelection.vue";
import type { PersonSearchField, PersonSearchMatchMode } from "~/utils/types";

const { width } = useWindowSize();
const smallScreen = computed(() => { return width.value < 850 })

const searchFocused = ref(false);
const searchQuery = ref("");
const searchContainer = ref<HTMLElement | null>(null);
const searchField = ref<PersonSearchField>("ALL");
const searchMatchMode = ref<PersonSearchMatchMode>("CONTAINS");

const expandSearch = async () => {
  searchFocused.value = true;
  await nextTick();
  searchContainer.value?.querySelector("input")?.focus();
};

const searchFieldOptions: { label: string; value: PersonSearchField }[] = [
  { label: "Alle Felder", value: "ALL" },
  { label: "Name", value: "FULL_NAME" },
  { label: "Geburtsjahr", value: "BIRTH_YEAR" },
  { label: "Sterbejahr", value: "DEATH_YEAR" },
  { label: "Pseudonyme", value: "PSEUDONYMS" }
];
const searchMatchModeOptions: { label: string; value: PersonSearchMatchMode }[] = [
  { label: "Beginnt mit", value: "STARTS_WITH" },
  { label: "Enthält", value: "CONTAINS" },
  { label: "Enthält nicht", value: "NOT_CONTAINS" },
  { label: "Endet mit", value: "ENDS_WITH" },
  { label: "Ist gleich", value: "EQUALS" },
  { label: "Ist ungleich", value: "NOT_EQUALS" }
];

const initialPageParameter = {
  page: 0,
  size: 15,
  sort: "lastName,asc"
};
const pageParameter = reactive<Pageable>({ ...initialPageParameter });

const pageSizeOptions = ref([
  { label: "5 pro Seite", value: 5 },
  { label: "10 pro Seite", value: 10 },
  { label: "15 pro Seite", value: 15 },
  { label: "30 pro Seite", value: 30 },
  { label: "50 pro Seite", value: 50 }
]);

const person_api = usePerson();

const {
  data: persons,
  pending: isLoading,
  refresh
} = useAsyncData<Page<PersonPreviewDTO>>(
  "persons-paginated",
  () => person_api.searchPaginated(
    searchQuery.value,
    { ...pageParameter },
    searchField.value,
    searchMatchMode.value
  )
);

watch(pageParameter, () => refresh(), { deep: true });
watch([searchQuery, searchField, searchMatchMode], () => {
  if (pageParameter.page !== 0) {
    pageParameter.page = 0;
    return;
  }
  refresh();
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-2xl outfit-headline text-primary font-bold">Personen</h1>
    <p class="roboto-plain">
      Eine Übersicht aller Personen in der Datenbank. Klicken Sie auf eine Person, um Details anzuzeigen oder zu
      bearbeiten.
    </p>
    <Divider />
    <div class="flex flex-row flex-wrap justify-between items-stretch gap-2 mb-2">
      <div v-if="!smallScreen || !searchFocused" class="flex flex-row gap-2">
        <div class="p-2 border border-solid rounded-md hover:shadow-md">
          <NuxtLink to="/persons/create" class="flex items-center">
            <i class="pi pi-pen-to-square mr-2" />
            <div class="text-primary roboto-plain">Neu {{ !smallScreen ? 'anlegen' : '' }}</div>
          </NuxtLink>
        </div>
        <Select v-model="pageParameter.size" :options="pageSizeOptions" :optionLabel="!smallScreen ? 'label' : 'value'"
          optionValue="value" />
      </div>
      <div class="flex min-w-0 gap-2 justify-between" :class="{ 'flex-col w-full': smallScreen && searchFocused }">
        <div v-if="!smallScreen || searchFocused" class="flex flex-row gap-2">
          <Select v-model="searchField" :options="searchFieldOptions" optionLabel="label" optionValue="value"
            aria-label="Suchfeld" class="flex-1" />
          <Select v-model="searchMatchMode" :options="searchMatchModeOptions" optionLabel="label" optionValue="value"
            aria-label="Suchoperator" class="flex-1" />
          <Button v-if="smallScreen && searchFocused" type="button" icon="pi pi-angle-up" aria-label="Suche einklappen"
            variant="outlined" class="ml-auto" style="border-color: var(--p-select-border-color)"
            @click="searchFocused = false" />
        </div>
        <div ref="searchContainer"
          class="min-w-0 shrink-0 transition-[width] duration-300 ease-in-out motion-reduce:transition-none"
          :class="{ 'w-10': smallScreen && !searchFocused, 'w-full': smallScreen && searchFocused, 'w-40': !smallScreen }">
          <Button v-if="smallScreen && !searchFocused" type="button" icon="pi pi-search" aria-label="Suchen"
            variant="outlined" class="h-10 w-10 p-0" @click="expandSearch"
            style="border-color: var(--p-select-border-color)" />
          <IconField v-else class=" w-full min-w-0" :class="{ '': smallScreen && searchFocused }">
            <InputIcon>
              <i class="pi pi-search" />
            </InputIcon>
              <InputText v-model="searchQuery" type="text" placeholder="Suchen" class=" w-full min-w-0"
              aria-label="Suchen" @focus="searchFocused = true" />
            <InputIcon v-if="searchQuery" class="cursor-pointer" @click="searchQuery = ''">
              <i class="pi pi-times" />
            </InputIcon>
          </IconField>
        </div>
      </div>
    </div>
    <span v-if="!persons || persons.totalPages === 0" class="flex w-full justify-center text-primary text-2xl py-5">
      Keine Personen gefunden!</span>
    <div v-else-if="isLoading && searchQuery.length < 2" v-for="_ in pageParameter.size"
      class="border-2 border-primary rounded-md p-2 shadow-md animate-pulse">
      <div class="flex flex-col gap-1">
        <div class="w-fit text-lg outfit-headline font-semibold text-transparent">Placeholder</div>
      </div>
    </div>
    <div v-else-if="persons" v-for="person in persons.content" :key="person.id" class="flex flex-col gap-2">
      <ContentCard flex-dir="row" :headerLink="`/persons/${person.id}`">
        <template #header>{{ person.fullName?.trim() || "(Kein Name)" }}</template>
        <template #subContent>
          <div class="flex flex-row gap-2 *:text-sm *:text-primary *:outfit-headline *:font-medium">
            <div v-if="person.birthYear && person.deathYear">
              ({{ person.birthYear }} - {{ person.deathYear }})
            </div>
            <div v-if="person.pseudonyms.length > 0" class="flex flex-row gap-1">
              aka.
              <div v-for="(pseudonym, index) in person.pseudonyms">
                "{{ pseudonym }}"{{ index < person.pseudonyms.length - 1 ? ', ' : '' }} </div>
              </div>
            </div>
        </template>
      </ContentCard>
    </div>
    <PageSelection :page-parameter="pageParameter" :total-pages="persons?.totalPages" />
  </div>
</template>

<style scoped>

</style>
