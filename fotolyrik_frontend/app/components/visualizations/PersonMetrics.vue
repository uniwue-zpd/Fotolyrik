<script setup lang="ts">
import * as d3 from "d3";
import type { PersonMetricsDTO } from "~/utils/types";
import MetricsBarChart from "~/components/visualizations/MetricsBarChart.vue";

const props = defineProps<{
  data: PersonMetricsDTO;
  width?: number;
  height?: number;
}>();


const width = props.width ?? 700;
const height = props.height ?? 200;

const legend = {
  photopoems: "Fotogedichte",
  pubMedia: "Publikationsmedien",
  authorsWorkedWith: "Gearbeitet mit Autor:innen",
  photographersWorkedWith: "Gearbeitet mit Fotograf:innen",
  keywords: "Schlagworte"
};
</script>

<template>
  <MetricsBarChart :data="props.data" :legend="legend" :width="width" :height="height" ></MetricsBarChart>
</template>
