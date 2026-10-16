<script setup lang="ts">
import TimelinePlot from "./TimelinePlot.vue";
import type { PhotoPoemPublicationDateDTO } from "~/utils/types";

defineProps<{
  width?: number;
  height?: number;
  data: PhotoPoemPublicationDateDTO[];
}>();

const colors: Record<PersonRole, string> = {
  AUTHOR: '#2563eb',
  PHOTOGRAPHER: '#dc2626',
  PARTICIPANT: '#e3769e',
  OTHER: '#16a34a',
  DEPICTED: '#9333ea'
};

const rolesMap: Record<PersonRole, string> = {
  AUTHOR: 'Autor:in',
  PHOTOGRAPHER: 'Fotograf:in',
  PARTICIPANT: 'Beteiligt',
  OTHER: 'Mitgewirkt an',
  DEPICTED: 'Abgebildet auf'
};

const getCircleColor = (node: PhotoPoemPublicationDateDTO) => (node.role && colors[node.role]) || '#333333';
</script>

<template>
  <div class="contributor-timeline">
    <div class="legend">
      <div
          v-for="(color, role) in colors"
          :key="role"
          class="legend-item"
      >
        <span class="legend-dot" :style="{ backgroundColor: color }"></span>
        {{ rolesMap[role as PersonRole] }}
      </div>
    </div>

    <div class="timeline-wrapper">
      <TimelinePlot
          :data="data"
          :width="width ?? 1000"
          :height="height ?? 400"
          :fill-color="getCircleColor"
      />
    </div>
  </div>
</template>

<style scoped>
.contributor-timeline {
  width: 100%;
  color: currentColor;
}

.legend {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 20px;
  padding-left: 60px;
  margin-bottom: 12px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: currentColor;
}

.legend-dot {
  display: inline-block;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  flex-shrink: 0;
}

.timeline-wrapper {
  width: 100%;
  overflow: visible;
  padding-bottom: 60px;
}

@media (max-width: 640px) {
  .legend {
    padding-left: 10px;
  }

  .timeline-wrapper {
    padding-bottom: 80px;
  }
}
</style>
