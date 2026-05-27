<script setup lang="ts">
import maplibregl, { LngLat } from 'maplibre-gl';
import 'maplibre-gl/dist/maplibre-gl.css';
import { ref, onMounted } from  'vue';
import { FilterMatchMode } from "@primevue/core";

defineExpose({populatePlaces});

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  description: { value: null, matchMode: FilterMatchMode.CONTAINS }
});

async function populatePlaces (places: PlaceDTO[]) {
  if (!document.getElementById('map')) {
    return;
  }
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
  map.on('load', () => {
    if (!places) {
      return;
    }
    places.forEach(place => {
      if (place.latitude !== null && place.longitude !== null) {
        const popup = new maplibregl.Popup()
            .setHTML(`<a href="places/${place.id}" class="roboto-plain font-semibold cursor-pointer popup-link">${ place.name }</a>`);
        new maplibregl.Marker()
            .setLngLat(new LngLat(place.longitude, place.latitude))
            .setPopup(popup)
            .addTo(map);
      }
    });
  })
}
</script>

<template>
  <div class="h-[500px] w-full mx-auto rounded-md" id="map"/>
</template>

