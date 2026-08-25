<script setup lang="ts">
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";
import PhotopoemPreview from "~/components/UI/PhotopoemPreview.vue";

const locationApi = useLocation();
const photopoem_api = usePhotopoem();

const router = useRoute();
const location_id = Number(router.params.id);

const { data: location_item} =locationApi.useLocationId(location_id)
const {data: is_location} = photopoem_api.useFilteredPhotopoems({'location-id': location_id});
useHead({
  title: () => location_item.value
      ? `${location_item.value.name} - Fundortsverzeichnis`
      : 'Nicht gefunden - Fundortsverzeichnis',
});
</script>

<template>
  <Card v-if="location_item">
    <template #title>
      <div class="flex flex-col">
        <div class="flex flex-row justify-between">
          <h1 class="text-3xl font-bold text-primary outfit-headline">{{ location_item?.name }}</h1>
          <PageToolbar
              v-if="location_item"
              :id="location_item.id"
              entity_type="location"
              :page_url="`${router.fullPath}`"
          />
        </div>
        <h2 class="text-xl font-normal roboto-italic text-[#9bb1c9]">Fundort</h2>
      </div>
    </template>
    <template #content>
      <div v-if="location_item.description" class="flex flex-col gap-2 py-5">
        <div class="font-semibold roboto-plain">Beschreibung:</div>
        <div class="roboto-plain text-justify">
          {{ location_item.description }}
        </div>
      </div>
      <Divider/>
      <div class="flex flex-col gap-2">
        <div v-if="is_location && is_location.length > 0" class="max-h-[30vh] flex flex-col gap-2">
          <h2 class="text-xl font-bold text-primary outfit-headline">Fotogedichte an diesem Fundort</h2>
          <div class="overflow-y-auto pb-2">
            <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
              <div v-for="photopoem in is_location" :key="photopoem.id">
                <PhotopoemPreview :photopoem="photopoem"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
    <template #footer>
      <Divider/>
      <div class="flex flex-col text-base">
        <div v-if="location_item?.createdDate" class="flex flex-row space-x-2 roboto-plain">
          <p class="font-semibold">Erstellt am:</p>
          <p>{{ new Date(location_item.createdDate).toLocaleDateString() }}</p>
        </div>
        <div v-if="location_item?.lastModifiedDate" class="flex flex-row space-x-2 roboto-plain">
          <p class="font-semibold">Zuletzt geändert am:</p>
          <p>{{ new Date(location_item.lastModifiedDate).toLocaleDateString() }}</p>
        </div>
      </div>
    </template>
  </Card>
  <div v-else class="p-4 bg-red-100 rounded-md flex justify-center gap-2">
    <p class="text-red-700 font-bold roboto-plain text-3xl">Der Fundort konnte nicht gefunden werden</p>
  </div>
</template>

<style scoped>
</style>
