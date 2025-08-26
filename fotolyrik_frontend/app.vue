<script setup lang="ts">
import { onMounted } from "vue";

const photopoem_store = usePhotopoemStore();
const person_store = usePersonStore();
const pub_media_store = usePubMediumStore();
const place_store = usePlaceStore();

onMounted(async () => {
  await photopoem_store.fetchPhotopoems();
  await person_store.fetchPersons();
  await pub_media_store.fetchPubMedia();
  await place_store.fetchPlaces();
});
</script>

<template>
  <AppHeader/>
  <MainContent>
    <NuxtPage/>
  </MainContent>
  <AppFooter/>
</template>
