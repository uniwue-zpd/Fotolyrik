<script setup lang="ts">
import 'tify'
import 'tify/dist/tify.css'
import { onMounted } from "vue";

onMounted(() => {
  new Tify({
    container: '#tify-photopoem',
    manifestUrl: 'https://iiif.slub-dresden.de/iiif/2/358216435-19340200/manifest.json', //placeholder link
  })
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-3xl font-bold outfit-headline">Fotogedicht</h1>
    <div id="tify-photopoem" class="h-[500px]"/>
  </div>
</template>

<style scoped>

</style>
