<script setup lang="ts">
import 'tify'
import 'tify/dist/tify.css'
import { ref, onMounted } from "vue";
import type { PhotoPoem } from "~/utils/types";
import PageToolbar from "~/components/pagetools/PageToolbar.vue";

const router = useRoute();
const photopoem_id = Number(router.params.id);
const store = usePhotopoemStore();
const photopoem_item = ref<PhotoPoem | null>(null);
const has_iiif_manifest = ref(false);
const has_pages = ref(false);

onMounted(async () => {
  await store.fetchPhtotopoemById(photopoem_id);
  photopoem_item.value = store.currentPhotopoem;
  console.log('photopoem_item', photopoem_item.value);
  if (photopoem_item.value?.iiif_manifest) {
    has_iiif_manifest.value = !!photopoem_item.value.iiif_manifest;
    has_pages.value = photopoem_item.value.page_number !== undefined;
    new Tify({
      container: '#tify-photopoem',
      manifestUrl: photopoem_item.value.iiif_manifest,
      pages: has_pages.value ? [photopoem_item.value.page_number] : [1]
    });
  }
});
</script>

<template v-show="data_fetched">
  <div class="flex flex-col gap-2">
    <Card>
      <template #title>
        <div class="flex flex-row justify-between">
          <h1 class="text-3xl font-bold text-[#063D79] outfit-headline">{{ photopoem_item?.title }}</h1>
          <PageToolbar
              v-if="photopoem_item"
              :id="photopoem_item.id"
              entity_type="photopoem"
              :page_url="`${router.fullPath}`"
          />
        </div>
      </template>
      <template #content>
        <div v-show="has_iiif_manifest" id="tify-photopoem" class="h-[500px]"/>
      </template>
    </Card>
  </div>
</template>
