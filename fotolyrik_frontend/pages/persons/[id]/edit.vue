<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { Person } from "~/utils/types";
import apiClient from "~/service/api";
import PersonForm from "~/components/forms/PersonForm.vue";

const route = useRoute();
const id = route.params.id;
const person_item = ref<Person>({} as Person);

onMounted(async () => {
  try {
    const response = await apiClient.get(`persons/${id}`);
    person_item.value = response.data;
  } catch (error) {
    console.log(error);
  }
});
</script>

<template>
  <PersonForm
      action="edit"
      header="Person-Objekt bearbeiten"
      :person="person_item ? person_item : undefined"
  />
</template>

<style scoped>

</style>
