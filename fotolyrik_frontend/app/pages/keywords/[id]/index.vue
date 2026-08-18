<script setup lang="ts">
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";
import PhotopoemPreview from "~/components/UI/PhotopoemPreview.vue";

const keyword_api = useKeyword();
const photopoem_store = usePhotopoemStore();

const router = useRoute();
const keyword_id = Number(router.params.id);


const is_theme = ref<PhotoPoemDTO[] | []>([]);
const is_image_motif = ref<PhotoPoemDTO[] | []>([]);

const { data: keyword_item, status } = await useAsyncData(
    `keyword-${keyword_id}`,
    () => keyword_api.fetchKeywordById(keyword_id)
);
onMounted(async () => {
  is_theme.value = await photopoem_store.filterPhotopoems({ 'theme-id': keyword_id });
  is_image_motif.value = await photopoem_store.filterPhotopoems({ 'image-motif-id': keyword_id });
  useHead({
    title: keyword_item.value ? `${keyword_item.value.value} - Schlagwortverzeichnis` : 'Nicht gefunden - Schlagwortverzeichnis',
  });
});
</script>

<template>
  <Card v-if="status !== 'pending' && keyword_item">
    <template #title>
      <div class="flex flex-col">
        <div class="flex flex-row justify-between">
          <h1 class="text-3xl font-bold text-primary outfit-headline">{{ keyword_item?.value }}</h1>
          <PageToolbar
              v-if="keyword_item"
              :id="keyword_item.id"
              entity_type="keyword"
              :page_url="`${router.fullPath}`"
          />
        </div>
        <h2 class="text-xl font-normal roboto-italic text-[#9bb1c9]">Schlagwort</h2>
      </div>
    </template>
    <template #content>
      <div v-if="keyword_item.gndId" class="flex flex-row space-x-5 py-5">
        <div class="font-semibold roboto-plain">GND-ID:</div>
        <div class="flex flex-row space-x-2 items-center">
          <p class="font-mono">{{ keyword_item.gndId }}</p>
          <NuxtLink
              :to="`https://explore.gnd.network/gnd/${keyword_item?.gndId}`"
              title="Zum GND-Explorer wechseln"
              target="_blank"
          >
            <i class="pi pi-external-link text-[#0073C9]"/>
          </NuxtLink>
        </div>
      </div>
      <Divider/>
      <div class="flex flex-col gap-2">
        <div v-if="is_theme.length > 0" class="max-h-[30vh] flex flex-col gap-2">
          <h2 class="text-xl font-bold text-primary outfit-headline">Thematik von</h2>
          <div class="overflow-y-auto pb-2">
            <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
              <div v-for="photopoem in is_theme" :key="photopoem.id">
                <PhotopoemPreview :photopoem="photopoem"/>
              </div>
            </div>
          </div>
        </div>
        <Divider/>
        <div v-if="is_image_motif.length > 0" class="max-h-[30vh] flex flex-col gap-2">
          <h2 class="text-xl font-bold text-primary outfit-headline">Bildmotiv von</h2>
          <div class="overflow-y-auto pb-2">
            <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
              <div v-for="photopoem in is_image_motif" :key="photopoem.id">
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
        <div v-if="keyword_item?.createdDate" class="flex flex-row space-x-2 roboto-plain">
          <p class="font-semibold">Erstellt am:</p>
          <p>{{ new Date(keyword_item.createdDate).toLocaleDateString() }}</p>
        </div>
        <div v-if="keyword_item?.lastModifiedDate" class="flex flex-row space-x-2 roboto-plain">
          <p class="font-semibold">Zuletzt geändert am:</p>
          <p>{{ new Date(keyword_item.lastModifiedDate).toLocaleDateString() }}</p>
        </div>
      </div>
    </template>
  </Card>
  <div v-else class="p-4 bg-red-100 rounded-md flex justify-center gap-2">
    <p class="text-red-700 font-bold roboto-plain text-3xl">Das Schlagwort konnte nicht gefunden werden</p>
  </div>
</template>

<style scoped>

</style>
