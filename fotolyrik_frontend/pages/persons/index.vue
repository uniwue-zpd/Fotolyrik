<script setup lang="ts">
import {ref, onMounted} from "vue";
import type { Person } from "~/utils/types";
import apiClient from "~/service/api";

const router = useRoute();
const currentPath = ref('');
const persons = ref<Person[] | null>([]);

onMounted(async () => {
  currentPath.value = router.path;
  try {
    const response = await apiClient.get('/persons');
    persons.value = response.data;
  }
  catch (error) {
    console.log(error)
  }
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-3xl font-bold outfit-headline">Personen</h1>
    <ul class="flex flex-col gap-1 list-disc list-inside">
      <li v-for="person in persons" :key="person.id">
        <NuxtLink :to="`/persons/${person.id}`">{{ person.first_name }} {{ person.last_name }}</NuxtLink>
      </li>
    </ul>
  </div>
</template>
