<script setup lang="ts">
import KeywordForm from "~/components/forms/KeywordForm.vue";

const route = useRoute();
const keyword_id = Number(route.params.id);
const keywordStore = useKeywordStore();
const keyword_item = ref<Keyword | null>(null);
const loading = ref(true);

onMounted(async () => {
  try {
    await keywordStore.fetchKeywordById(keyword_id);
    keyword_item.value = keywordStore.currentKeyword ?? null;
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div v-if="!keyword_item">
    <div class="flex flex-row space-x-2 items-center justify-center p-2 bg-[#F1F2F2] rounded-md">
      <i class="pi pi-spin pi-spinner"/>
      <p class="roboto-plain">Schlagwort wird geladen</p>
    </div>
  </div>
  <KeywordForm
      v-else
      action="edit"
      header="Schlagwort bearbeiten"
      :keyword="keyword_item"
  />
</template>

<style scoped>

</style>