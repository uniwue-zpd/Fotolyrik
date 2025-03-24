<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { Person } from "~/utils/types";
import apiClient from "~/service/api";

const router = useRoute();
const person_id = router.params.id;
const person_item = ref<Person>({} as Person);
const data_fetched = ref(false);

onMounted(async () => {
  try {
    const response = await apiClient.get(`persons/${person_id}`)
    person_item.value = response.data;
    data_fetched.value = true;
  }
  catch(error) {
    console.log(error);
  }
});
</script>

<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold outfit-headline">{{ person_item.firstName }} {{ person_item.lastName }}</h1>
    </template>
    <template #content>
      <table class="min-w-full divide-y divide-gray-200 roboto-plain">
        <tbody class="bg-white divide-y divide-gray-200">
        <tr v-if="person_item.birthYear">
          <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Geburtsjahr</td>
          <td class="px-6 py-4 whitespace-nowrap text-sm">{{ person_item.birthYear }}</td>
        </tr>
        <tr v-if="person_item.deathYear">
          <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Sterbejahr</td>
          <td class="px-6 py-4 whitespace-nowrap text-sm">{{ person_item.deathYear }}</td>
        </tr>
        <tr v-if="person_item.pseudonym">
          <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Pseudonym</td>
          <td class="px-6 py-4 whitespace-nowrap text-sm">{{ person_item.pseudonym }}</td>
        </tr>
        <tr v-if="person_item.sex">
          <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Geschlecht</td>
          <td v-if="person_item.sex === 'MALE'" class="px-6 py-4 whitespace-nowrap text-sm">Männlich</td>
          <td v-else class="px-6 py-4 whitespace-nowrap text-sm">Weiblich</td>
        </tr>
        </tbody>
      </table>
    </template>
  </Card>
</template>
