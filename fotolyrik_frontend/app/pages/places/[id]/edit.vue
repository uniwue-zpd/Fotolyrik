<script setup lang="ts">
import PlaceForm from "~/components/forms/PlaceForm.vue";

const route = useRoute();
const place_id = Number(route.params.id);
const placeApi = usePlace();
const {data: place_item} = await placeApi.getById(place_id);
</script>

<template>
  <div v-if="!place_item">
    <div class="flex flex-row space-x-2 items-center justify-center p-2 bg-[#F1F2F2] rounded-md">
      <i class="pi pi-spin pi-spinner"></i>
      <p class="roboto-plain">Ort wird geladen</p>
    </div>
  </div>
  <PlaceForm
      v-else
      action="edit"
      header="Ort bearbeiten"
      :place="place_item"
  />
</template>

<style scoped>

</style>
