<script setup lang="ts">
import { ref } from "vue";
import { FilterMatchMode } from "@primevue/core";

const pubMediumApi = usePubMedium();
const {data: pubMediumList} = pubMediumApi.usePubMediumList();
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  'publisher.name': { value: null, matchMode: FilterMatchMode.CONTAINS }
});
</script>

<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold outfit-headline text-primary">Publikationsmedien</h1>
    </template>
    <template #content>
      <DataTable
          v-model:filters="filters"
          filter-display="row"
          :global-filter-fields="['title', 'subtitle', 'publisher.name', 'editorialOffice','startYear', 'endYear', 'amountVolumes', 'amountIssues']"
          :value="pubMediumList"
          stripedRows paginator :rows="10"
      >
        <template #header>
          <div class="flex flex-row justify-between items-center">
            <div class="p-2 border border-solid rounded-md hover:shadow-md">
              <NuxtLink to="/publication_media/create" class="flex items-center">
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
                  placeholder="Schlagwortsuche"
              />
            </IconField>
          </div>
        </template>
        <Column field="title" header="Titel" class="roboto-plain" sortable>
          <template #body="slotProps">
            <NuxtLink
                :to="`/publication_media/${ slotProps.data.id }`"
                class="roboto-plain font-semibold"
            >
              {{ slotProps.data.title }}
            </NuxtLink>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
                v-model="filterModel.value"
                type="text" @input="filterCallback()"
                placeholder="Nach Titel suchen"
            />
          </template>
        </Column>
        <Column field="subtitle" header="Untertitel" class="roboto-plain"/>
        <Column header ="Publikationsorte" field="publicationPlaces" class="roboto-plain">
          <template #body="slotProps">
            <div v-if="slotProps.data.publicationPlaces != null && slotProps.data.publicationPlaces.length > 0">
              <ul class="list-disc list-inside">
                <li v-for="place in slotProps.data.publicationPlaces">
                  <NuxtLink :to="`/places/${ place.id }`" class="roboto-plain">
                    {{ place.name }}
                  </NuxtLink>
                </li>
              </ul>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column field="publisher.name" header="Verlag" class="roboto-plain" :sortable="true"/>
        <Column
            field="pubRhytms" filterField="pubRhytm"
            header="Publikationsrhythmen"
            class="roboto-plain"
        >
          <template #body="slotProps">
            <ul class="list-disc list-inside">
              <li v-for="rhythm in slotProps.data.pubRhythms">{{ rhythm.value }}</li>
            </ul>
          </template>
        </Column>
        <Column field="editorialOffice" header="Schriftleitung" class="roboto-plain" :sortable="true"/>
        <Column field="startYear" header="Startjahr" class="roboto-plain" :sortable="true"/>
        <Column field="endYear" header="Endjahr" class="roboto-plain" :sortable="true"/>
        <Column field="amountVolumes" header="Anzahl Bände" class="roboto-plain" :sortable="true"/>
        <Column field="amountIssues" header="Anzahl Ausgaben" class="roboto-plain" :sortable="true"/>
      </DataTable>
    </template>
  </Card>
</template>

<style scoped>

</style>
