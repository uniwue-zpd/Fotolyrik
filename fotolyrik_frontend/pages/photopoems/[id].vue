<script setup lang="ts">
import 'tify'
import 'tify/dist/tify.css'
import { ref, onMounted } from "vue";
import type { PhotoPoem } from "~/utils/types";
import apiClient from "~/service/api";

const route = useRoute();
const photopoem_id = route.params.id;
const photopoem_item = ref<PhotoPoem>({} as PhotoPoem);
const has_iiif_manifest = ref(false);
const has_pages = ref(false);
const data_fetched = ref(false);

onMounted(async () => {
  try {
    const response = await apiClient.get<PhotoPoem>(`/photopoems/${photopoem_id}`);
    photopoem_item.value = response.data;
    data_fetched.value = true;

    if (photopoem_item.value.iiifManifest) {
      has_iiif_manifest.value = true;
      has_pages.value = photopoem_item.value.pageNumber !== undefined;
      new Tify({
        container: '#tify-photopoem',
        manifestUrl: photopoem_item.value.iiifManifest,
        pages: has_pages.value ? [photopoem_item.value.pageNumber] : [1]
      });
    }
  } catch (error) {
    console.log(error);
  }
});
</script>

<template v-show="data_fetched">
  <div class="flex flex-col gap-2">
    <h1 class="text-3xl font-bold outfit-headline">{{ photopoem_item.title }}</h1>
    <div v-show="has_iiif_manifest" id="tify-photopoem" class="h-[500px]"/>
  </div>
</template>
