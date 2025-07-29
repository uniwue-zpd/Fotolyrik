<script setup lang="ts">
import { ref, onMounted } from "vue";
import PubMediumForm from "~/components/forms/PubMediumForm.vue";

const route = useRoute();
const pub_medium_id = Number(route.params.id);
const store = usePubMediumStore();
const pub_medium_item = ref<PubMedium | null>(null);

onMounted(async () => {
  await store.fetchPubMediumById(pub_medium_id);
  pub_medium_item.value = store.current_pub_medium;
});
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
