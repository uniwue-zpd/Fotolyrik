<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { PhotoPoem } from "~/utils/types";
import apiClient from "~/service/api";

const photopoems = ref<PhotoPoem[]>([] as PhotoPoem[]);

onMounted(async ()=> {
  try {
    const response = await apiClient.get<PhotoPoem[]>("/photopoems");
    photopoems.value = response.data;
  } catch (error) {
    console.log(error);
  }
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-3xl font-bold outfit-headline">Fotogedichte</h1>
    <ul class="flex flex-col gap-1 list-disc list-inside">
      <li v-for="photopoem in photopoems" :key="photopoem.id">
        <NuxtLink :to="`/photopoems/${photopoem.id}`">{{ photopoem.title }}</NuxtLink>
      </li>
    </ul>
  </div>
</template>

<style scoped>

</style>
