<script setup lang="ts">
import { ref } from "vue";
import { FilterMatchMode } from "@primevue/core";

const store = usePubMediumStore();
const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  pubRhytm: { value: null, matchMode: FilterMatchMode.IN },
});

const publication_rhytms = ref(['wöchentlich', 'halbmonatlich', 'monatlich', 'halbjährlich', 'jährlich']);
</script>

<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold outfit-headline text-[#063D79]">Publikationsmedien</h1>
    </template>
    <template #content>
      <DataTable
          v-model:filters="filters"
          filter-display="row"
          :global-filter-fields="['title', 'subtitle', 'publisher', 'pubRhytm', 'startYear', 'endYear', 'amountVolumes', 'amountIssues']"
          :value="store.pub_media"
      >
        <template #header>
          <div class="flex flex-row justify-between items-center">
            <div class="p-2 border-[1px] border-solid rounded-md hover:shadow-md">
              <NuxtLink to="/publication_media/create" class="flex items-center">
                <i class="pi pi-pen-to-square mr-2"/>
                <div class="text-[#063D79] roboto-plain">Neu anlegen</div>
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
              <span v-for="(place, index) in slotProps.data.publicationPlaces" :key="place.id">
                <NuxtLink :to="`/places/${ place.id }`" class="roboto-plain">
                  {{ place.name }}
                </NuxtLink>
                <span v-if="index < slotProps.data.publicationPlaces.length -1">, </span>
              </span>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column field="publisher" header="Verlag" class="roboto-plain" :sortable="true"/>
        <Column
            field="pubRhytm" filterField="pubRhytm"
            header="Publikationsrhythmus"
            class="roboto-plain"
            :showFilterMenu="false" :sortable="true"
        >
          <template #body="slotProps">
            {{ slotProps.data.pubRhytm }}
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <MultiSelect
                v-model="filterModel.value"
                @change="filterCallback()"
                :options="publication_rhytms"
                placeholder="Beliebige"
            >
              <template #option="slotProps">
                <div>{{ slotProps.option }}</div>
              </template>
            </MultiSelect>
          </template>
        </Column>
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
