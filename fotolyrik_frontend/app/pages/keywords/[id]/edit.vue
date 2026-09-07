<script setup lang="ts">
import KeywordForm from "~/components/forms/KeywordForm.vue";

const route = useRoute();
const keyword_id = Number(route.params.id);
const keywordApi = useKeyword();

const { data: keyword_item, status } = await keywordApi.getById(keyword_id);
</script>

<template>
  <div v-if="status === 'pending'">
    <div class="flex flex-row space-x-2 items-center justify-center p-2 bg-[#F1F2F2] rounded-md">
      <i class="pi pi-spin pi-spinner"/>
      <p class="roboto-plain">Schlagwort wird geladen</p>
    </div>
  </div>
  <KeywordForm
      v-else-if="keyword_item"
      action="edit"
      header="Schlagwort bearbeiten"
      :keyword="keyword_item"
  />
</template>

<style scoped>

</style>