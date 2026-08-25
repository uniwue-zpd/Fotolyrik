<script setup lang="ts">
import { ref, watch, onBeforeUnmount } from "vue";
import { FilterMatchMode } from "@primevue/core";
import {useFiles} from "~/composables/useFiles";

const photopoemApi = usePhotopoem();
const fileApi = useFiles();
const {data: photopoemList} = photopoemApi.usePhotopoemList();

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  title: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
});

// Preload image object URLs
const previewURLs = ref<Record<number, string>>({});
watch(
  () => photopoemList.value,
  async (photopoems) => {
    if (!photopoems || photopoems.length === 0) return;
    const tasks: Promise<void>[] = [];
    photopoems.forEach((p) => {
      if (p.images && p.images.length > 0 && p.imagesVisible === AccessLevel.PUBLIC) {
        p.images.forEach((img: any) => {
          if (!previewURLs.value[img.id]) {
            tasks.push((async () => {
              try {
                const url = await fileApi.getImageContent(img.id);
                if (url) previewURLs.value[img.id] = url;
              } catch (err) {
                console.error('Failed to preload image', img.id, err);
              }
            })());
          }
        });
      }
    });
    try {
      await Promise.all(tasks);
    } catch (e) {
      console.error('Error preloading images', e);
    }
  },
  { immediate: true, deep: true }
);

// Revoke blob URLs on unmount
onBeforeUnmount(() => {
  Object.values(previewURLs.value).forEach((url) => {
    try { URL.revokeObjectURL(url); } catch (e) { /* ignore */ }
  });
  previewURLs.value = {};
});

useHead(() => ({
  title: 'Sammlung'
}));
</script>

<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold outfit-headline text-primary">Fotogedichte</h1>
    </template>
    <template #content>
      <DataTable
          v-model:filters="filters"
          filter-display="row"
          :global-filter-fields="['title', 'altTitle', 'volume', 'issue', 'pageNumber', 'publicationDate', 'publicationMedium.title', 'contributions.contributor.fullName']"
          :value="photopoemList"
          stripedRows paginator :rows="10"
      >
        <template #header>
          <div class="flex flex-row justify-between items-center">
            <div class="p-2 border border-solid rounded-md hover:shadow-md">
              <NuxtLink to="/photopoems/create" class="flex items-center">
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
                  placeholder="Felder durchsuchen"
              />
            </IconField>
          </div>
        </template>
        <Column field="title" header="Titel" :sortable="true">
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
                <div v-if="data.images.length > 0 && data.imagesVisible === AccessLevel.PUBLIC">
                  <Avatar
                      v-for="image in data.images"
                      :key="image.id"
                      :image="previewURLs[image.id] || ''"
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
        <Column field="pageCount" header="Umfang" class="roboto-plain" :sortable="true"/>
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
        <Column header ="Autor:innen" field="contributions">
          <template #body="slotProps: {data:PhotoPoemDTO}">
            <div v-if="slotProps.data.contributions && slotProps.data.contributions.filter(x => x.role === ContributionRole.AUTHOR).length > 0">
              <ul class="list-inside">
                <li v-for="(c, index) in slotProps.data.contributions.filter(x => x.role === ContributionRole.AUTHOR)" :key="c.contributor?.id ?? index">
                  <NuxtLink :to="`/persons/${c.contributor?.id}`" class="roboto-plain">
                    {{ c.contributor.fullName }}
                  </NuxtLink>
                </li>
              </ul>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column header ="Fotograf:innen" field="contributions">
          <template #body="slotProps: {data:PhotoPoemDTO}">
            <div v-if="slotProps.data.contributions && slotProps.data.contributions.filter(x => x.role === ContributionRole.PHOTOGRAPHER).length > 0">
              <ul class="list-inside">
                <li v-for="(c, index) in slotProps.data.contributions.filter(x => x.role === ContributionRole.PHOTOGRAPHER)" :key="c.contributor?.id ?? index">
                  <NuxtLink :to="`/persons/${c.contributor?.id}`" class="roboto-plain">
                    {{ c.contributor.fullName }}
                  </NuxtLink>
                </li>
              </ul>
            </div>
            <div v-else>
              <span class="roboto-italic text-gray-500">Unbekannt</span>
            </div>
          </template>
        </Column>
        <Column header ="Sonstige Mitwirkende" field="contributions">
          <template #body="slotProps: {data:PhotoPoemDTO}">
            <div v-if="slotProps.data.contributions && slotProps.data.contributions.filter(x => x.role === ContributionRole.OTHER).length > 0">
              <ul class="list-inside">
                <li v-for="(c, index) in slotProps.data.contributions.filter(x => x.role === ContributionRole.OTHER)" :key="c.contributor?.id ?? index">
                  <NuxtLink :to="`/persons/${c.contributor?.id}`" class="roboto-plain">
                    {{ c.contributor.fullName }}
                  </NuxtLink>
                </li>
              </ul>
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
