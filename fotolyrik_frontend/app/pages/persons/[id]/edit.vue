<script setup lang="ts">
import PersonForm from "~/components/forms/PersonForm.vue";

const route = useRoute();
const person_id = Number(route.params.id);
const person_api = usePerson();
const { data: person_item } = await useAsyncData(`person-${person_id}`, () => person_api.fetchById(person_id));
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
