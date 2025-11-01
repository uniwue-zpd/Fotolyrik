<script setup lang="ts">
import { onMounted } from "vue";

const person_store = usePersonStore();
const pub_media_store = usePubMediumStore();
const place_store = usePlaceStore();
const keyword_store = useKeywordStore();

onMounted(async () => {
  await person_store.fetchPersons();
  await pub_media_store.fetchPubMedia();
  await place_store.fetchPlaces();
  await keyword_store.fetchKeywords();
});
</script>

<template>
  <AppHeader/>
  <MainContent>
    <NuxtPage/>
  </MainContent>
  <AppFooter/>
</template>
