<script setup lang="ts">
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";
import PhotopoemPreview from "~/components/UI/PhotopoemPreview.vue";
import PubMediumMetrics from "~/components/visualizations/PubMediumMetrics.vue";
import PhotopoemDatePlot from "~/components/visualizations/PhotopoemDatePlot.vue";

const router = useRoute();
const pubmedium_store = usePubMediumStore();
const photopoem_store = usePhotopoemStore();

const pub_medium_id = Number(router.params.id);
const pub_medium_item = ref<PubMediumDTO | null>(null);
const previous_pub_medium = ref<PubMediumDTO | null>(null);
const next_pub_medium = ref<PubMediumDTO | null>(null);
const pub_medium_photopoems = ref<PhotoPoemDTO[] | []>([]);
const pub_medium_metrics = ref<PubMediumMetricsDTO | null>(null);
const photopoemsHavePubDates = computed(() => {
  return pub_medium_photopoems.value.some(poem => poem.publicationDate);
});

onMounted(async () => {
  await pubmedium_store.fetchPubMediumById(pub_medium_id);
  pub_medium_item.value = pubmedium_store.current_pub_medium;
  previous_pub_medium.value = pubmedium_store.previousPubMedium();
  next_pub_medium.value = pubmedium_store.nextPubMedium();
  pub_medium_photopoems.value = await photopoem_store.filterPhotopoems({'pubmedium-id': pub_medium_id});
  pub_medium_metrics.value = await pubmedium_store.fetchPubMediumMetrics(pub_medium_id);
});

</script>

<template>
  <div class="flex flex-col gap-2">
    <Card>
      <template #title>
        <div class="flex flex-row justify-between">
          <h1 class="text-3xl font-bold text-primary outfit-headline">{{ pub_medium_item?.title }}</h1>
          <PageToolbar
              v-if="pub_medium_item"
              :id="pub_medium_item.id"
              entity_type="pub_medium"
              :page_url="`${router.fullPath}`"
          />
        </div>
      </template>
      <template #content>
        <div class="flex flex-col gap-4">
          <div class="flex flex-col gap-2 md:flex-row">
            <table class="min-w-[55%] divide-y divide-gray-200 roboto-plain">
              <tbody v-if="pub_medium_item" class=" divide-y divide-gray-200">
              <tr v-if="pub_medium_item.subtitle">
                <td class="px-6 py-4 whitespace-nowrap font-semibold">Titel-Zusatz</td>
                <td class="px-6 py-4 whitespace-nowrap ">{{ pub_medium_item.subtitle }}</td>
              </tr>
              <tr v-if="pub_medium_item.publicationPlaces.length > 0">
                <td class="px-6 py-4 whitespace-nowrap font-semibold">Publikationsorte</td>
                <td class="px-6 py-4 whitespace-nowrap ">
                  <div class="flex flex-wrap gap-3.5">
                  <span v-for="place in pub_medium_item.publicationPlaces" :key="place.id">
                    <NuxtLink
                        :to="`/places/${place.id}`"
                        class="p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md font-semibold"
                    >
                      {{ place.name }}
                    </NuxtLink>
                  </span>
                  </div>
                </td>
              </tr>
              <tr v-if="pub_medium_item.publisher">
                <td class="px-6 py-4 whitespace-nowrap font-semibold">Herausgeber</td>
                <td class="px-6 py-4 whitespace-nowrap ">{{ pub_medium_item.publisher.name }}</td>
              </tr>
              <tr v-if="pub_medium_item.pubRhythms.length > 0">
                <td class="px-6 py-4 whitespace-nowrap font-semibold">Publikationsrhythmen</td>
                <td class="px-6 py-4 whitespace-nowrap ">
                  <div class="flex flex-wrap gap-3.5">
                    <div v-for="rhythm in pub_medium_item.pubRhythms" :key="rhythm.id">
                      <div class="p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md font-semibold">
                        {{ rhythm.value }}
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
              <tr v-if="pub_medium_item.startYear && pub_medium_item.endYear">
                <td class="px-6 py-4 whitespace-nowrap font-semibold">Publikationsjahre</td>
                <td class="px-6 py-4 whitespace-nowrap ">
                  {{ pub_medium_item.startYear }} - {{ pub_medium_item.endYear }}
                </td>
              </tr>
              <tr v-if="pub_medium_item.amountVolumes">
                <td class="px-6 py-4 whitespace-nowrap font-semibold">Jahrgänge</td>
                <td class="px-6 py-4 whitespace-nowrap ">{{ pub_medium_item.amountVolumes }}</td>
              </tr>
              <tr v-if="pub_medium_item.amountIssues">
                <td class="px-6 py-4 whitespace-nowrap font-semibold">Ausgaben</td>
                <td class="px-6 py-4 whitespace-nowrap ">{{ pub_medium_item.amountIssues }}</td>
              </tr>
              </tbody>
            </table>
            <PubMediumMetrics v-if="pub_medium_metrics" :data="pub_medium_metrics"/>
          </div>
          <div v-if="pub_medium_photopoems.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Fotogedichte in "{{ pub_medium_item?.title }}"</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in pub_medium_photopoems" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="photopoemsHavePubDates" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Veröffentlichungen nach Datum</h2>
            <PhotopoemDatePlot :data="pub_medium_photopoems ?? []"/>
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
            <div class="roboto-plain">Vorheriger Eintrag</div>
          </NuxtLink>
        </div>
      </div>
      <div class="next">
        <div v-if="next_pub_medium" class="p-2 border border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/publication_media/${ next_pub_medium.id }`"
              class="flex flex-row items-center space-x-2"
          >
            <div class=" roboto-plain">Nächster Eintrag</div>
            <i class="pi pi-arrow-right"/>
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
