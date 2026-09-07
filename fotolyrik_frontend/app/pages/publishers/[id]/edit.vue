<script setup lang="ts">
import PublisherForm from "~/components/forms/PublisherForm.vue";

const route = useRoute();
const publisher_id = Number(route.params.id);
const publisherApi = usePublisher();
const {data: publisher_item}= await publisherApi.getById(publisher_id)

</script>

<template>
  <div v-if="!publisher_item">
    <div class="flex flex-row space-x-2 items-center justify-center p-2 bg-[#F1F2F2] rounded-md">
      <i class="pi pi-spin pi-spinner"/>
      <p class="roboto-plain">Schlagwort wird geladen</p>
    </div>
  </div>
  <PublisherForm
    action="edit"
    header="Verlag bearbeiten"
    :publisher="publisher_item"
  />
</template>

<style scoped>

</style>