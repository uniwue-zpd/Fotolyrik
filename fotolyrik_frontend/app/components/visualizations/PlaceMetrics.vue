<script setup lang="ts">
import * as d3 from "d3";
import type {PersonMetricsDTO, PubMediumMetricsDTO} from "~/utils/types";
import MetricsBarChart from "~/components/visualizations/MetricsBarChart.vue";

const props = defineProps<{
  data: PlaceMetricsDTO;
  width?: number;
  height?: number;
}>();

const legend = {
  pubMedia: "Publikationsmedien",
  photopoems: "Fotogedichte",
  authors: "Autor:innen",
  photographers: "Fotograf:innen",
  depictedPeople: "Abgebildete Persönlichkeitenn",
  keywords: "Schlagworte"
};
const width = props.width ?? 350;
const height = props.height ?? 200;

</script>
<template>
  <MetricsBarChart :data="props.data" :width="width" :height="height" :legend="legend"></MetricsBarChart>
</template>

