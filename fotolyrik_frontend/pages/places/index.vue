<script setup lang="ts">
import maplibregl from 'maplibre-gl';
import 'maplibre-gl/dist/maplibre-gl.css';
import { ref, onMounted } from  'vue';
import type { Place } from '~/utils/types';
import apiClient from "~/service/api";

const router = useRoute();
const current_path = ref('');
const places = ref<Place[] | null>([]);

onMounted(async () => {
  current_path.value = router.path;
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
  try {
    const response = await apiClient.get<Place[]>('/places');
    places.value = response.data;
    console.log(places.value);
    places.value.forEach(place => {
      const Marker = new maplibregl.Marker()
          .setLngLat([place.longitude, place.latitude])
          .setPopup(new maplibregl.Popup()
              .setHTML(`<a href="/places/${place.id}">${ place.name }</a>`))
          .addTo(map)
    })
  }
  catch (error) {
    console.log(error);
  }
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-3xl font-bold outfit-headline">Orte</h1>
    <div class="h-[500px] w-full mx-auto rounded-md" id="map"/>
  </div>
</template>
