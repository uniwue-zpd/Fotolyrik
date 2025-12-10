<script setup lang="ts">

import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";
import PhotopoemPreview from "~/components/UI/PhotopoemPreview.vue";

const router = useRoute();
const pubmedium_store = usePubMediumStore();
const photopoem_store = usePhotopoemStore();

const pub_medium_id = Number(router.params.id);
const pub_medium_item = ref<PubMediumDTO | null>(null);
const previous_pub_medium = ref<PubMediumDTO | null>(null);
const next_pub_medium = ref<PubMediumDTO | null>(null);
const pub_medium_photopoems = ref<PhotoPoemDTO[] | []>([]);

onMounted(async () => {
  await pubmedium_store.fetchPubMediumById(pub_medium_id);
  pub_medium_item.value = pubmedium_store.current_pub_medium;
  previous_pub_medium.value = pubmedium_store.previousPubMedium();
  next_pub_medium.value = pubmedium_store.nextPubMedium();
  pub_medium_photopoems.value = await photopoem_store.filterPhotopoems({'pubmedium-id': pub_medium_id});
  console.log(pub_medium_photopoems.value);
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <Card>
      <template #title>
        <div class="flex flex-row justify-between">
          <h1 class="text-3xl font-bold text-[#063D79] outfit-headline">{{ pub_medium_item?.title }}</h1>
          <PageToolbar
              v-if="pub_medium_item"
              :id="pub_medium_item.id"
              entity_type="pub_medium"
              :page_url="`${router.fullPath}`"
          />
        </div>
      </template>
      <template #content>
        <table class="min-w-full divide-y divide-gray-200 roboto-plain">
          <tbody v-if="pub_medium_item" class="bg-white divide-y divide-gray-200">
          <tr v-if="pub_medium_item.subtitle">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Titel-Zusatz</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ pub_medium_item.subtitle }}</td>
          </tr>
          <tr v-if="pub_medium_item.publicationPlaces.length > 0">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Publikationsorte</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">
              <span v-for="(place, index) in pub_medium_item.publicationPlaces" :key="place.id">
                <NuxtLink :to="`/places/${ place.id }`" class="roboto-plain">
                  {{ place.name }}
                </NuxtLink>
                <span v-if="index < pub_medium_item.publicationPlaces.length -1">, </span>
              </span>
            </td>
          </tr>
          <tr v-if="pub_medium_item.publisher">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Herausgeber</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ pub_medium_item.publisher.name }}</td>
          </tr>
          <tr v-if="pub_medium_item.pubRhythms.length > 0">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Publikationsrhythmen</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">
              <ul class="list-disc list-inside">
                <li v-for="rhythm in pub_medium_item.pubRhythms">
                  {{ rhythm.value }}
                </li>
              </ul>
            </td>
          </tr>
          <tr v-if="pub_medium_item.startYear && pub_medium_item.endYear">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Publikationsjahre</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">
              {{ pub_medium_item.startYear }} - {{ pub_medium_item.endYear }}
            </td>
          </tr>
          <tr v-if="pub_medium_item.amountVolumes">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Jahrgänge</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ pub_medium_item.amountVolumes }}</td>
          </tr>
          <tr v-if="pub_medium_item.amountIssues">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Ausgaben</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ pub_medium_item.amountIssues }}</td>
          </tr>
          </tbody>
        </table>
      </template>
      <template #footer>
        <div v-if="pub_medium_photopoems.length > 0" class="max-h-[30vh] flex flex-col gap-2">
          <h2 class="text-xl font-bold text-[#063D79] outfit-headline">Fotogedichte in "{{ pub_medium_item?.title }}"</h2>
          <div class="overflow-y-auto pb-2">
            <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
              <div v-for="photopoem in pub_medium_photopoems" :key="photopoem.id">
                <PhotopoemPreview :photopoem="photopoem"/>
              </div>
            </div>
          </div>
        </div>
      </template>
    </Card>
    <div class="flex flex-row justify-between">
      <div class="previus">
        <div v-if="previous_pub_medium" class="p-2 border border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/publication_media/${ previous_pub_medium.id }`"
              class="flex flex-row items-center space-x-2"
          >
            <i class="pi pi-arrow-left"/>
            <div class="text-[#063D79] roboto-plain">Vorheriger Eintrag</div>
          </NuxtLink>
        </div>
      </div>
      <div class="next">
        <div v-if="next_pub_medium" class="p-2 border border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/publication_media/${ next_pub_medium.id }`"
              class="flex flex-row items-center space-x-2"
          >
            <div class="text-[#063D79] roboto-plain">Nächster Eintrag</div>
            <i class="pi pi-arrow-right"/>
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
