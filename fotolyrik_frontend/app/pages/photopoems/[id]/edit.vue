<script setup lang="ts">
import PhotopoemForm from "~/components/forms/PhotopoemForm.vue";

const route = useRoute();
const photopoem_id = Number(route.params.id);
const {data: photopoem_item} =await usePhotopoem().usePhotopoemId(photopoem_id);
</script>

<template>
  <PhotopoemForm
      action="edit"
      header="Fotogedicht-Objekt bearbeiten"
      :photopoem="photopoem_item ?? undefined"
  />
</template>

<style scoped>

</style>
