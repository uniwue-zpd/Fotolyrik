<script setup lang="ts">
import { ref, onMounted } from "vue";
import PubMediumForm from "~/components/forms/PubMediumForm.vue";

const route = useRoute();
const pub_medium_id = Number(route.params.id);
const pub_medium_api = usePubMedium();
const {data:pub_medium_item} =pub_medium_api.usePubMediumId(pub_medium_id)

</script>

<template>
  <PubMediumForm
      action="edit"
      header="Publikationsmedium-Objekt bearbeiten"
      :pub_medium="pub_medium_item ?? undefined"
  />
</template>

<style scoped>

</style>
