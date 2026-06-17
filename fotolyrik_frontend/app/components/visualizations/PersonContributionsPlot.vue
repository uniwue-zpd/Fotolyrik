<script setup lang="ts">
import type { ContributorRole } from "~/utils/types";
import TimelinePlot from "./TimelinePlot.vue";

interface PersonContributions extends PhotoPoemDTO {
  role: ContributorRole;
}

defineProps<{
  width?: number;
  height?: number;
  data: PersonContributions[];
}>();

const colors: Record<ContributorRole, string> = {
  author: '#2563eb',
  photographer: '#dc2626',
  contributor: '#16a34a',
  depicted: '#9333ea'
};

const rolesMap: Record<ContributorRole, string> = {
  author: 'Autor:in',
  photographer: 'Fotograf:in',
  contributor: 'Mitgewirkt an',
  depicted: 'Abgebildet auf'
};

// Callback to determine circle fill color based on item role
const getCircleColor = (node: PersonContributions) => colors[node.role] || '#333';
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
        {{ rolesMap[role as ContributorRole] }}
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
