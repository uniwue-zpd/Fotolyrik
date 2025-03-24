<script setup lang="ts">
import maplibregl from "maplibre-gl";
import "maplibre-gl/dist/maplibre-gl.css"
import { ref, onMounted } from "vue";
import type { Place } from "~/utils/types";
import apiClient from "~/service/api";

const route = useRoute();
const place_id = route.params.id;
const place_item = ref<Place>({} as Place);
const data_fetched = ref(false);

onMounted(async () => {
  const map = new maplibregl.Map({
    container: "map",
    zoom: 5,
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
    }
  });
  map.addControl(new maplibregl.NavigationControl({
    showCompass: true,
    showZoom: true,
    visualizePitch: true,
    visualizeRoll: true
  }));
  try {
    const response = await apiClient.get(`places/${place_id}`);
    data_fetched.value = true;
    place_item.value = response.data;

    map.setCenter([place_item.value.longitude, place_item.value.latitude]);
    const marker = new maplibregl.Marker()
        .setLngLat([place_item.value.longitude, place_item.value.latitude])
        .addTo(map);
  }
  catch(error) {
    console.log(error)
  }
});
</script>

<template v-if="data_fetched">
  <div class="flex flex-col gap-2">
    <h1 class="text-3xl font-bold outfit-headline">{{ place_item.name }}</h1>
    <div id="map" class="h-[500px] w-full rounded-md"/>
    <div class="text-md roboto-plain">{{ place_item.description }}</div>
  </div>
</template>

<style scoped>

</style>
