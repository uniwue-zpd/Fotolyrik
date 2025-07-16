<script setup lang="ts">
import { ref } from "vue";
import { FilterMatchMode } from "@primevue/core";
import { usePhotopoemStore } from "~/stores/PhotopoemStore";

const store = usePhotopoemStore();

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});
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
          :global-filter-fields="['title', 'volume', 'issue', 'page_number', 'publication_date', 'publication_medium.title']"
          :value="store.photopoems"
      >
        <template #header>
          <div class="flex flex-row justify-between items-center">
            <div class="p-2 border-[1px] border-solid rounded-md hover:shadow-md">
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
                  placeholder="Schlagwortsuche"
              />
            </IconField>
          </div>
        </template>
        <Column field="title" header="Titel" sortable>
          <template #body="{ data }">
            <div class="flex flex-row space-x-5 items-center">
              <NuxtLink
                  :to="`/photopoems/${data.id}`"
                  class="roboto-plain font-semibold"
              >
                {{ data.title }}
              </NuxtLink>
              <AvatarGroup>
                <div v-if="data.images.length > 0">
                  <Avatar
                      v-for="image in data.images"
                      :key="image.id"
                      :image="`api/uploads/${image.filename}`"
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
        <Column header ="Autor:innen" field="authors">
          <template #body="slotProps">
            <div v-if="slotProps.data.authors != null && slotProps.data.authors.length > 0">
              <span v-for="(author, index) in slotProps.data.authors" :key="author.id">
                <NuxtLink :to="`/persons/${author.id}`" class="roboto-plain">
                  {{ author.full_name }}
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
                  {{ photographer.full_name }}
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
