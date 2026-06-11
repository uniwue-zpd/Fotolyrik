<script setup lang="ts">
import maplibregl, {LngLat, LngLatBounds} from 'maplibre-gl';
import 'maplibre-gl/dist/maplibre-gl.css';

defineExpose({ populatePlaces});

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
    const bounds = new LngLatBounds();
    let hasValidPoints = false;
    places.forEach(place => {
      if (place.latitude !== null && place.longitude !== null&& place.latitude !==0 && place.longitude !== 0) {
        const link = document.createElement('button');
        link.addEventListener('click', () => {
          navigateTo(`/places/${ place.id }`);
        });
        link.innerHTML = place.name;
        link.setAttribute('class', 'cursor-pointer roboto-plain font-bold');
        const popup = new maplibregl.Popup()
            .setDOMContent(link);
        const coords = new LngLat(place.longitude, place.latitude);
        new maplibregl.Marker()
            .setLngLat(coords)
            .setPopup(popup)
            .addTo(map);
        bounds.extend(coords);
        hasValidPoints = true;
      }
    });
    if (hasValidPoints) {
      map.fitBounds(bounds, {
        padding: 50,
        maxZoom: 15,
        duration: 1000
      });
    }
  })
}
</script>

<template>
  <div class="h-[500px] w-full mx-auto rounded-md shadow-md border border-gray-200" id="map"/>
</template>

