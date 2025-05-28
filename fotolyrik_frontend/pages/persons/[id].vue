<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { Person } from "~/utils/types";
import apiClient from "~/service/api";
import PageToolbar from "~/components/pagetools/PageToolbar.vue";

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
      <div class="flex flex-row justify-between">
        <h1 class="text-3xl font-bold text-[#063D79] outfit-headline">{{ person_item.first_name }} {{ person_item.last_name }}</h1>
        <PageToolbar
          :page_url="`http://fotolyrik.de${router.fullPath}`"
          :api_url="`http://localhost:8080/persons/${person_id}`"
        />
      </div>
    </template>
    <template #content>
      <table class="min-w-full divide-y divide-gray-200 roboto-plain">
        <tbody class="bg-white divide-y divide-gray-200">
        <tr v-if="person_item.birth_year">
          <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Geburtsjahr</td>
          <td class="px-6 py-4 whitespace-nowrap text-sm">{{ person_item.birth_year }}</td>
        </tr>
        <tr v-if="person_item.death_year">
          <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Sterbejahr</td>
          <td class="px-6 py-4 whitespace-nowrap text-sm">{{ person_item.death_year }}</td>
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

<style>
.speeddialbutton > .p-button {
  background: #063D79;
}

.speeddialbutton > .p-button:hover {
  background: rgba(6, 61, 121, 0.8);
}
</style>
