<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { Person } from "~/utils/types";
import PersonForm from "~/components/forms/PersonForm.vue";

const route = useRoute();
const person_id = Number(route.params.id);
const store = usePersonStore();
const person_item = ref<Person |null>(null);

onMounted(async () => {
  await store.fetchPersonById(person_id);
  person_item.value = store.currentPerson;
});
</script>

<template>
  <PersonForm
      action="edit"
      header="Person-Objekt bearbeiten"
      :person="person_item ?? undefined"
  />
</template>

<style scoped>

</style>
