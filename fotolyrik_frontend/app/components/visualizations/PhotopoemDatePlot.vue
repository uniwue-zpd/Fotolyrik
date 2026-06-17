<script setup lang="ts">
import TimelinePlot from "./TimelinePlot.vue";

defineProps<{
  width?: number;
  height?: number;
  data: PhotoPoemDTO[];
}>();
</script>

<template>
  <TimelinePlot
      :data="data"
      :width="width ?? 1000"
      :height="height ?? 200"
      enable-zoom
  />
</template>
