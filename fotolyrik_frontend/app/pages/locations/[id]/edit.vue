<script setup lang="ts">
import LocationForm from "~/components/forms/LocationForm.vue";

const route = useRoute();
const location_id = Number(route.params.id);
const locationApi = useLocation();

const { data: location_item, status } = await locationApi.useLocationId(location_id);
</script>

<template>
  <div v-if="!location_item">
    <div class="flex flex-row space-x-2 items-center justify-center p-2 bg-[#F1F2F2] rounded-md">
      <i class="pi pi-spin pi-spinner"/>
      <p class="roboto-plain">Fundort wird geladen</p>
    </div>
  </div>
  <LocationForm
      v-else
      action="edit"
      header="Fundort bearbeiten"
      :location="location_item"
  />
</template>

<style scoped>
</style>
