<script setup lang="ts">
import {ref, onMounted} from "vue";
import { FilterMatchMode } from "@primevue/core";
import { usePersonStore } from "~/stores/PersonStore";

const store = usePersonStore();

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  full_name: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

onMounted(async () => {
  await store.fetchAllPersons();
})
</script>

<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold text-[#063D79] outfit-headline">Personen</h1>
    </template>
    <template #content>
      <DataTable
          v-model:filters="filters"
          filter-display="row"
          :global-filter-fields="['full_name', 'sex', 'birth_year', 'death_year']"
          :value="store.persons"
      >
        <template #header>
          <div class="flex justify-end">
            <IconField>
              <InputIcon>
                <i class="pi pi-search"/>
              </InputIcon>
              <InputText
                  v-model="filters['global'].value"
                  type="text"
                  placeholder="Schlagwortsuche"
              />
            </IconField>
          </div>
        </template>
        <Column field="full_name" header="Name" sortable>
          <template #body="slotProps">
            <NuxtLink
                :to="`/persons/${slotProps.data.id}`"
                class="roboto-plain font-semibold"
            >
              {{ slotProps.data.full_name }}
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
        <Column field="first_name" header="Vorname" class="roboto-plain"/>
        <Column field="last_name" header="Nachname" class="roboto-plain"/>
        <Column field="pseudonym" header="Pseudonym" class="roboto-plain">
          <template #body="slotProps">
            <div v-if="slotProps.data.pseudonym != null">
              <div class="roboto-plain">{{ slotProps.data.pseudonym }}</div>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column field="death_year" header="Geburtsjahr" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.birth_year != null">
              <div class="roboto-plain">{{ slotProps.data.birth_year }}</div>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column field="death_year" header="Sterbejahr" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.death_year != null">
              <div class="roboto-plain">{{ slotProps.data.death_year }}</div>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column field="sex" header="Geschlecht" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.sex != null">
              <div class="roboto-plain">{{ slotProps.data.sex === 'MALE' ? "männlich" : "weiblich" }}</div>
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
