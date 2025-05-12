<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { PhotoPoem } from "~/utils/types";
import apiClient from "~/service/api";
import {FilterMatchMode} from "@primevue/core";

const photopoems = ref<PhotoPoem[]>([] as PhotoPoem[]);

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

onMounted(async ()=> {
  try {
    const response = await apiClient.get<PhotoPoem[]>("/photopoems");
    photopoems.value = response.data;
  } catch (error) {
    console.log(error);
  }
});
</script>

<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold outfit-headline">Fotogedichte</h1>
    </template>
    <template #content>
      <DataTable
          v-model:filters="filters"
          filter-display="row"
          :global-filter-fields="['title', 'volume', 'issue', 'page_number', 'author.full_name', 'photographer.full_name']"
          :value="photopoems"
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
        <Column field="title" header="Titel" sortable>
          <template #body="slotProps">
            <NuxtLink
                :to="`/photopoems/${slotProps.data.id}`"
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
        <Column field="volume" header="Jahrgang" class="roboto-plain"/>
        <Column field="issue" header="Ausgabe" class="roboto-plain"/>
        <Column field="page_number" header="Seite" class="roboto-plain"/>
        <Column field="publication_date" header="Publikationsdatum" class="roboto-plain"/>
        <Column field="publication_medium.title" header="Publikationsmedium" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.publication_medium != null">
              <div class="roboto-plain">{{ slotProps.data.publication_medium.title }}</div>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column header ="Autor" field="author.full_name" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.author != null">
              <NuxtLink :to="`/persons/${slotProps.data.author.id}`"
                        class="roboto-plain">
                {{ slotProps.data.author.full_name }}
              </NuxtLink>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column header="Fotograf" field="photographer.full_name" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.photographer != null">
              <NuxtLink :to="`/persons/${slotProps.data.photographer.id}`"
                        class="roboto-plain">
                {{ slotProps.data.photographer.full_name }}
              </NuxtLink>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500 italic">Unbekannt</span>
            </div>
          </template>
        </Column>
      </DataTable>
    </template>
  </Card>
</template>

<style scoped>

</style>
