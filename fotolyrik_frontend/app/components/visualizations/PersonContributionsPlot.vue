<script setup lang="ts">
import TimelinePlot from "./TimelinePlot.vue";
import type {PhotoPoemPublicationDateDTO} from "~/utils/types";


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
    <div class="legend" :style="{ paddingLeft: '60px', display: 'flex', gap: '20px', marginBottom: '10px' }">
      <div
          v-for="(color, role) in colors"
          :key="role"
          style="display: flex; align-items: center; gap: 6px; font-size: 12px; color: #333;"
      >
        <span :style="{ display: 'inline-block', width: '10px', height: '10px', borderRadius: '50%', backgroundColor: color }"></span>
        {{ rolesMap[role as PersonRole] }}
      </div>
    </div>

    <TimelinePlot
        :data="data"
        :width="width ?? 1000"
        :height="height ?? 400"
        :fill-color="getCircleColor"
    />
  </div>
</template>
