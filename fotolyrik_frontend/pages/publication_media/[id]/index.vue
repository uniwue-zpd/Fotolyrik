<script setup lang="ts">

import PageToolbar from "~/components/pagetools/PageToolbar.vue";

const router = useRoute();
const store = usePubMediumStore();

const pub_medium_id = Number(router.params.id);
const pub_medium_item = ref<PubMedium | null>(null);
const previous_pub_medium = ref<PubMedium | null>(null);
const next_pub_medium = ref<PubMedium | null>(null);

onMounted(async () => {
  await store.fetchPubMediumById(pub_medium_id);
  pub_medium_item.value = store.current_pub_medium;
  previous_pub_medium.value = store.previousPubMedium();
  next_pub_medium.value = store.nextPubMedium();
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
          <tr v-if="pub_medium_item.publication_places.length > 0">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Publikationsorte</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">
              <span v-for="(place, index) in pub_medium_item.publication_places" :key="place.id">
                <NuxtLink :to="`/places/${place.id}`" class="roboto-plain">
                  {{ place.name }}
                </NuxtLink>
                <span v-if="index < pub_medium_item.publication_places.length -1">, </span>
              </span>
            </td>
          </tr>
          <tr v-if="pub_medium_item.publisher">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Herausgeber</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ pub_medium_item.publisher }}</td>
          </tr>
          <tr v-if="pub_medium_item.pub_rhytm">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Publikationsrhythmus</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ pub_medium_item.pub_rhytm }}</td>
          </tr>
          <tr v-if="pub_medium_item.start_year && pub_medium_item.end_year">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Publikationsjahre</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">
              {{ pub_medium_item.start_year }} - {{ pub_medium_item.end_year }}
            </td>
          </tr>
          <tr v-if="pub_medium_item.amount_volumes">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Jahrgänge</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ pub_medium_item.amount_volumes }}</td>
          </tr>
          <tr v-if="pub_medium_item.amount_issues">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Ausgaben</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ pub_medium_item.amount_issues }}</td>
          </tr>
          </tbody>
        </table>
      </template>
    </Card>
    <div class="flex flex-row justify-between">
      <div class="previus">
        <div v-if="previous_pub_medium" class="p-2 border-[1px] border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/publication_media/${previous_pub_medium.id}`"
              class="flex flex-row items-center space-x-2"
          >
            <i class="pi pi-arrow-left"/>
            <div class="text-[#063D79] roboto-plain">Vorheriger Eintrag</div>
          </NuxtLink>
        </div>
      </div>
      <div class="next">
        <div v-if="next_pub_medium" class="p-2 border-[1px] border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/publication_media/${next_pub_medium.id}`"
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
