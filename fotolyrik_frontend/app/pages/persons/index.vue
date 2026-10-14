<script setup lang="ts">
import { ref } from "vue";
import { FilterMatchMode } from "@primevue/core";
import ContentCard from "~/components/UI/cards/ContentCard.vue";
import PageSelection from "~/components/UI/buttons/PageSelection.vue";

const { width } = useWindowSize();

const searchFocused = ref(false);
const searchQuery = ref("");

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
  data: persons_data,
  pending: isLoading,
  refresh
} = useAsyncData<Page<PersonPreviewDTO>>(
  "persons-paginated",
  () => person_api.searchPaginated(searchQuery.value, { ...pageParameter })
);

const { data: personList } = await person_api.getAll();

const persons = computed(() => {
  return personList.value?.map(person => ({
    ...person,
    pseudonyms: person.pseudonyms.sort().join(', ')
  }));
})

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  fullName: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

watch(pageParameter, () => refresh(), { deep: true });
watch(searchQuery, () => {
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

    <div class="flex flex-row justify-between items-stretch gap-2 mb-2">
      <div v-if="width > 600 || !searchFocused" class="p-2 border border-solid rounded-md hover:shadow-md">
        <NuxtLink to="/persons/create" class="flex items-center">
          <i class="pi pi-pen-to-square mr-2" />
          <div class="text-primary roboto-plain">Neu {{ width > 600 ? 'anlegen' : '' }}</div>
        </NuxtLink>
      </div>
      <Select v-if="width > 600 || !searchFocused" v-model="pageParameter.size" :options="pageSizeOptions"
        :optionLabel="width > 600 ? 'label' : 'value'" optionValue="value" />
      <IconField class="search-field min-w-0 flex-1" :class="{ 'is-expanded': width <= 600 && searchFocused }">
        <InputIcon>
          <i class="pi pi-search" />
        </InputIcon>
        <InputText v-model="searchQuery" type="text" placeholder="In allen Angaben suchen" class="search-input"
          aria-label="In allen Angaben suchen" @focus="searchFocused = true" @blur="searchFocused = false" />
      </IconField>
    </div>
    <span v-if="!persons_data || persons_data.totalPages === 0"
      class="flex w-full justify-center text-primary text-2xl py-5">
      Keine Personen gefunden!</span>
    <div v-else-if="persons_data" v-for="person in persons_data.content" :key="person.id" class="flex flex-col gap-2">
      <ContentCard flex-dir="row" :headerLink="`/persons/${person.id}`">
        <template #header>{{ person.fullName?.trim() || "(Kein Name)" }}</template>
        <template #subContent>
          <div class="flex flex-row gap-2 w-full *:text-sm *:text-primary *:outfit-headline *:font-medium">
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
    <PageSelection :page-parameter="pageParameter" :total-pages="persons_data?.totalPages" />
  </div>



  <Card>
    <template #title>
      <h1 class="text-3xl font-bold text-primary outfit-headline">Personen</h1>
    </template>
    <template #content>
      <DataTable v-model:filters="filters" filter-display="row"
        :global-filter-fields="['fullName', 'sex', 'birthYear', 'deathYear', 'pseudonyms']" :value="persons" stripedRows
        paginator :rows="10">
        <template #header>
          <div class="flex flex-row justify-between items-center">
            <div class="p-2 border border-solid rounded-md hover:shadow-md">
              <NuxtLink to="/persons/create" class="flex items-center">
                <i class="pi pi-pen-to-square mr-2" />
                <div class="text-primary roboto-plain">Neu anlegen</div>
              </NuxtLink>
            </div>
            <IconField>
              <InputIcon>
                <i class="pi pi-search" />
              </InputIcon>
              <InputText v-model="filters['global'].value" type="text" placeholder="Tabelle durchsuchen" />
            </IconField>
          </div>
        </template>
        <Column field="fullName" header="Name" sortable>
          <template #body="slotProps">
            <NuxtLink :to="`/persons/${slotProps.data.id}`" class="roboto-plain font-semibold">
              {{ slotProps.data.fullName || `Person mit ID: ${slotProps.data.id}` }}
            </NuxtLink>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText v-model="filterModel.value" type="text" @input="filterCallback()"
              placeholder="Nach Namen suchen" />
          </template>
        </Column>
        <Column field="firstName" header="Vorname" class="roboto-plain" :sortable="true" />
        <Column field="lastName" header="Nachname" class="roboto-plain" :sortable="true" />
        <Column field="pseudonyms" header="Pseudonyme" class="roboto-plain" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.pseudonyms">
              <div class="roboto-plain">{{ slotProps.data.pseudonyms }}</div>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column field="birthYear" header="Geburtsjahr" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.birthYear != null">
              <div class="roboto-plain">{{ slotProps.data.birthYear }}</div>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column field="deathYear" header="Sterbejahr" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.deathYear != null">
              <div class="roboto-plain">{{ slotProps.data.deathYear }}</div>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column field="sex" header="Geschlecht" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.sex != null">
              <div class="roboto-plain">{{ slotProps.data.sex === 'männlich' ? "männlich" : "weiblich" }}</div>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
      </DataTable>
    </template>
  </Card>
</template>
