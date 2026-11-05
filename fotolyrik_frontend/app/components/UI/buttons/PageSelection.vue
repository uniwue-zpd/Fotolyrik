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

const page = computed({
  get: () => pageParameter.page ?? 0,
  set: (value: number) => {
    pageParameter.page = value;
  }
});

const isFirst = computed(() => page.value <= 0);
const isLast = computed(() => page.value >= (totalPages ?? 1) - 1);
</script>

<template>
  <div class="flex items-center justify-center gap-2">
    <UButton
      icon="i-lucide-chevrons-left"
      variant="outline"
      size="lg"
      aria-label="Erste Seite"
      :disabled="isFirst"
      @click="page = 0"
    />
    <UButton
      icon="i-lucide-chevron-left"
      variant="outline"
      size="lg"
      aria-label="Vorherige Seite"
      :disabled="isFirst"
      @click="page--"
    />
    <USelect
      v-model="page"
      :items="pageOptions"
      :disabled="totalPages <= 1"
      size="lg"
      class="min-w-20"
      aria-label="Seite wählen"
    />
    <UButton
      icon="i-lucide-chevron-right"
      variant="outline"
      size="lg"
      aria-label="Nächste Seite"
      :disabled="isLast"
      @click="page++"
    />
    <UButton
      icon="i-lucide-chevrons-right"
      variant="outline"
      size="lg"
      aria-label="Letzte Seite"
      :disabled="isLast"
      @click="page = (totalPages ?? 1) - 1"
    />
  </div>
</template>
