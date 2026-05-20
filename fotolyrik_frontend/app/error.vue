<script setup lang="ts">
import type { NuxtError } from "#app";
const props = defineProps({
  error: Object as () => NuxtError
});
</script>

<template>
      <div class="flex flex-col gap-5 items-center justify-center h-screen">
        <div class="flex flex-col gap-2 items-center justify-center">
          <div class="font-mono font-bold text-8xl animate-pulse text-primary">{{ error?.statusCode }}</div>
          <div class="font-mono font-bold text-xl text-primary">{{ error?.message }}</div>
        </div>
        <div class="text-2xl md:text-5xl outfit-headline font-bold">Es ist ein Fehler aufgetreten</div>
        <NuxtLink to="/" class="outfit-headline text-primary font-semibold p-3 text-lg md:text-3xl rounded-md hover:shadow-md">Zurück zur Startseite</NuxtLink>
      </div>
</template>

<style scoped>

</style>
