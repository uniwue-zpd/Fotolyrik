<script setup lang="ts">
import * as d3 from "d3";
import type {PersonMetricsDTO, PubMediumMetricsDTO} from "~/utils/types";
import MetricsBarChart from "~/components/visualizations/MetricsBarChart.vue";

const props = defineProps<{
  data: PubMediumMetricsDTO;
  width?: number;
  height?: number;
}>();

const legend = {
  photopoems: "Fotogedichte",
  authorsWorkedWith: "Gearbeitet mit Autor:innen",
  photographersWorkedWith: "Gearbeitet mit Fotograf:innen",
  depictedPeople: "Abgebildete Personen",
  keywords: "Schlagworte"
};
const width = props.width ?? 350;
const height = props.height ?? 200;

</script>
<template>
  <MetricsBarChart :data="props.data" :width="width" :height="height" :legend="legend"></MetricsBarChart>
</template>

