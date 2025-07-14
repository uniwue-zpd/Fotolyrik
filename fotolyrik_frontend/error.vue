<script setup lang="ts">
import type { NuxtError } from "#app";
const props = defineProps({
  error: Object as () => NuxtError
});
</script>

<template>
      <div class="flex flex-col gap-5 items-center justify-center h-[100vh]">
        <div class="font-mono font-bold text-8xl animate-pulse text-[#063D79]">{{ error?.statusCode }}</div>
        <div class="text-2xl md:text-5xl outfit-headline font-bold">Es ist ein Fehler aufgetreten</div>
        <NuxtLink to="/" class="roboto-plain text-[#063D79] font-semibold p-3 text-lg md:text-3xl rounded-md hover:shadow-md">Zurück zur Startseite</NuxtLink>
      </div>
</template>

<style scoped>

</style>
