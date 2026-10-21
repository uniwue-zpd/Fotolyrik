<script setup lang="ts">
const { pageParameter, totalPages = 0 } = defineProps<{
  pageParameter: Pageable,
  totalPages?: number
}>();

const pageOptions = computed(() =>
  Array.from({ length: totalPages ?? 0 }, (_, index) => ({
    label: `${index + 1}`,
    value: index
  }))
);
</script>

<template>
  <div class="flex items-center justify-center align-bottom gap-3">
    <button type="button"
      class="px-2 h-9 rounded-md border border-primary text-primary hover:bg-primary hover:text-white transition disabled:opacity-40 disabled:cursor-not-allowed"
      :disabled="pageParameter.page === 0" aria-label="Vorherige Seite" @click="pageParameter.page!--">
      <i class="pi pi-chevron-left" />
    </button>
    <Select v-model="pageParameter.page" :options="pageOptions" optionLabel="label" optionValue="value"
      :disabled="totalPages <= 1" class="h-9 items-center" />
    <button type="button"
      class="px-2 h-9 rounded-md border border-primary text-primary hover:bg-primary hover:text-white transition disabled:opacity-40 disabled:cursor-not-allowed"
      :disabled="pageParameter.page! >= (totalPages ?? 1) - 1" aria-label="Nächste Seite"
      @click="pageParameter.page!++">
      <i class="pi pi-chevron-right" />
    </button>
  </div>
</template>
