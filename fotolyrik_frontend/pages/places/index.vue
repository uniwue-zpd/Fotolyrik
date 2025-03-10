<script setup lang="ts">
import maplibregl from 'maplibre-gl';
import 'maplibre-gl/dist/maplibre-gl.css';
import { onMounted } from  'vue';

onMounted(() => {
  const map = new maplibregl.Map({
    container: 'map',
    style: {
      version: 8,
      sources: {
        osm: {
          type: "raster",
          tiles: ["https://tile.openstreetmap.de/{z}/{x}/{y}.png"],
          tileSize: 256,
          attribution: "&copy; OpenStreetMap Contributors"
        }
      },
      layers: [
        {
          id: "osm-layer",
          type: "raster",
          source: "osm"
        }
      ]
    },
    center: [10.447683, 51.163361],
    zoom: 5,
    maplibreLogo: true
  });
  map.addControl(new maplibregl.NavigationControl({
    showCompass: true,
    showZoom: true,
    visualizePitch: true,
    visualizeRoll: true
  }));
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-3xl font-bold outfit-headline">Orte</h1>
    <div class="h-[500px] w-full mx-auto rounded-md" id="map"/>
  </div>
</template>
