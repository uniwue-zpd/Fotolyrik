<script setup lang="ts">
import PhotopoemForm from "~/components/forms/PhotopoemForm.vue";

const route = useRoute();
const photopoem_id = Number(route.params.id);
const store = usePhotopoemStore();
const photopoem_item = ref<PhotoPoem | null>(null);

onMounted(async () => {
  await store.fetchPhtotopoemById(photopoem_id);
  photopoem_item.value = store.currentPhotopoem;
});
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
