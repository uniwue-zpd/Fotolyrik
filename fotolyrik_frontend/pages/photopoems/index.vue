<script setup lang="ts">
import { ref } from "vue";
import { FilterMatchMode } from "@primevue/core";
import { usePhotopoemStore } from "~/stores/PhotopoemStore";

const store = usePhotopoemStore();

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

useHead(() => ({
  title: 'Sammlung'
}));
</script>

<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold outfit-headline text-[#063D79]">Fotogedichte</h1>
    </template>
    <template #content>
      <DataTable
          v-model:filters="filters"
          filter-display="row"
          :global-filter-fields="['title', 'altTitle', 'volume', 'issue', 'pageNumber', 'publicationDate', 'publicationMedium.title']"
          :value="store.photopoems"
          stripedRows paginator :rows="10"
      >
        <template #header>
          <div class="flex flex-row justify-between items-center">
            <div class="p-2 border border-solid rounded-md hover:shadow-md">
              <NuxtLink to="/photopoems/create" class="flex items-center">
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
                  placeholder="Felder durchsuchen"
              />
            </IconField>
          </div>
        </template>
        <Column field="title" header="Titel" sortable>
          <template #body="{ data }">
            <div class="flex flex-row space-x-5 items-center">
              <NuxtLink
                  v-if="data.title"
                  :to="`/photopoems/${data.id}`"
                  class="roboto-plain font-semibold"
              >
                {{ data.title }}
              </NuxtLink>
              <div v-else class="roboto-italic text-gray-500">
                Unbenannt
              </div>
              <AvatarGroup>
                <div v-if="data.images.length > 0">
                  <Avatar
                      v-for="image in data.images"
                      :key="image.id"
                      :image="`/api/uploads/${image.filename}`"
                      shape="circle"
                      oncontextmenu="return false;"
                  />
                </div>
              </AvatarGroup>
            </div>
          </template>
          <template #filter="{ filterModel, filterCallback }">
            <InputText
                v-model="filterModel.value"
                type="text" @input="filterCallback()"
                placeholder="Nach Titel suchen"
            />
          </template>
        </Column>
        <Column field="altTitle" header="Alternativtitel" class="roboto-plain">
          <template #body="{ data }">
            <NuxtLink
                v-if="!data.title"
                :to="`/photopoems/${data.id}`"
                class="roboto-plain font-semibold"
            >
              {{ data.altTitle }}
            </NuxtLink>
            <div v-else class="roboto-plain">
              {{ data.altTitle }}
            </div>
          </template>
        </Column>
        <Column field="volume" header="Jahrgang" class="roboto-plain"/>
        <Column field="issue" header="Ausgabe" class="roboto-plain"/>
        <Column field="pageNumber" header="Seite(n)" class="roboto-plain"/>
        <Column field="publicationDate" header="Publikationsdatum" class="roboto-plain"/>
        <Column field="publicationMedium.title" header="Publikationsmedium" :sortable="true">
          <template #body="slotProps">
            <div v-if="slotProps.data.publicationMedium != null">
              <NuxtLink :to="`/publication_media/${slotProps.data.publicationMedium.id}`" class="roboto-plain">
                {{ slotProps.data.publicationMedium.title }}
              </NuxtLink>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column header ="Autor:innen" field="authors">
          <template #body="slotProps">
            <div v-if="slotProps.data.authors != null && slotProps.data.authors.length > 0">
              <span v-for="(author, index) in slotProps.data.authors" :key="author.id">
                <NuxtLink :to="`/persons/${author.id}`" class="roboto-plain">
                  {{ author.fullName }}
                </NuxtLink>
                <span v-if="index < slotProps.data.authors.length -1">, </span>
              </span>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column header ="Fotograf:innen" field="photographers">
          <template #body="slotProps">
            <div v-if="slotProps.data.photographers != null && slotProps.data.photographers.length > 0">
              <span v-for="(photographer, index) in slotProps.data.photographers" :key="photographer.id">
                <NuxtLink :to="`/persons/${photographer.id}`" class="roboto-plain">
                  {{ photographer.fullName }}
                </NuxtLink>
                <span v-if="index < slotProps.data.photographers.length -1">, </span>
              </span>
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

<style scoped>

</style>
