<script setup lang="ts">
import { onMounted } from "vue";

const photopoem_store = usePhotopoemStore();
const person_store = usePersonStore();
const pub_media_store = usePubMediumStore();

onMounted(async () => {
  await photopoem_store.fetchPhotopoems();
  await person_store.fetchPersons();
  await pub_media_store.fetchPubMedia();
});
</script>

<template>
  <AppHeader/>
  <MainContent>
    <NuxtPage/>
  </MainContent>
  <AppFooter/>
</template>