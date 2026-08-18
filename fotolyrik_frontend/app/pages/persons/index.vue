<script setup lang="ts">
import { ref } from "vue";
import { FilterMatchMode } from "@primevue/core";
import { usePersonStore } from "~/stores/PersonStore";

const person_api = usePerson();
const { data: cachedPersons } = await useAsyncData( 'person-list', () => person_api.fetchPersons());
const persons = computed(() => {
  return cachedPersons.value?.map(person => ({
    ...person,
    pseudonyms: person.pseudonyms.sort().join(', ')
  }));
})

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  fullName: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});
</script>

<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold text-primary outfit-headline">Personen</h1>
    </template>
    <template #content>
      <DataTable
          v-model:filters="filters"
          filter-display="row"
          :global-filter-fields="['fullName', 'sex', 'birthYear', 'deathYear', 'pseudonyms']"
          :value="persons"
          stripedRows paginator :rows="10"
      >
        <template #header>
          <div class="flex flex-row justify-between items-center">
            <div class="p-2 border border-solid rounded-md hover:shadow-md">
              <NuxtLink to="/persons/create" class="flex items-center">
                <i class="pi pi-pen-to-square mr-2"/>
                <div class="text-primary roboto-plain">Neu anlegen</div>
              </NuxtLink>
            </div>
            <IconField>
              <InputIcon>
                <i class="pi pi-search"/>
              </InputIcon>
              <InputText
                  v-model="filters['global'].value"
                  type="text"
                  placeholder="Tabelle durchsuchen"
              />
            </IconField>
          </div>
        </template>
        <Column field="fullName" header="Name" sortable>
          <template #body="slotProps">
            <NuxtLink
                :to="`/persons/${slotProps.data.id}`"
                class="roboto-plain font-semibold"
            >
              {{ slotProps.data.fullName || `Person mit ID: ${slotProps.data.id}` }}
            </NuxtLink>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
                v-model="filterModel.value"
                type="text" @input="filterCallback()"
                placeholder="Nach Namen suchen"
            />
          </template>
        </Column>
        <Column field="firstName" header="Vorname" class="roboto-plain" :sortable="true"/>
        <Column field="lastName" header="Nachname" class="roboto-plain" :sortable="true"/>
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
