<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { PersonDTO } from "~/utils/types";
import PersonForm from "~/components/forms/PersonForm.vue";

const route = useRoute();
const person_id = Number(route.params.id);
const person_api = usePerson();
const { data: person_item } =await person_api.usePersonId(person_id);
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
