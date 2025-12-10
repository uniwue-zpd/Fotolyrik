<script setup lang="ts">
import maplibregl from "maplibre-gl";
import "maplibre-gl/dist/maplibre-gl.css"
import { ref, onMounted } from "vue";
import type { Place } from "~/utils/types";
import PubMediumPreview from "~/components/UI/PubMediumPreview.vue";
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";

const place_store = usePlaceStore();
const pubmedium_store = usePubMediumStore();

const route = useRoute();
const place_id = Number(route.params.id);
const place_item = ref<Place | null>(null);
const place_pub_media = ref<PubMediumDTO[] | []>([]);

useHead(() => ({
  title: place_item.value?.name ? `${place_item.value?.name}` : 'Nicht gefunden',
}));

onMounted(async () => {
  await place_store.fetchPlaceById(place_id);
  place_item.value = place_store.current_place;
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
  map.setCenter([10.0, 51.0]);
  if (place_item.value) {
    if (place_item.value.latitude != null && place_item.value.longitude != null) {
      map.setCenter([place_item.value.longitude, place_item.value.latitude]);
      new maplibregl.Marker()
          .setLngLat([place_item.value.longitude, place_item.value.latitude])
          .addTo(map);
    }
  }
  place_pub_media.value = await pubmedium_store.filterPubMedia({ 'pubplace-id': place_id });
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <div class="flex flex-row justify-between">
      <h1 class="text-3xl font-bold outfit-headline text-[#063D79]">{{ place_item?.name }}</h1>
      <PageToolbar
          v-if="place_item"
          :id="place_item.id"
          entity_type="place"
          :page_url="`${route.fullPath}`"
      />
    </div>
    <div class="flex flex-col gap-2 md:grid md:grid-cols-2">
      <div id="map" class="h-[500px] rounded-md"/>
      <div class="bg-[#063D79] rounded-md"/>
    </div>
    <div class="text-md roboto-plain">{{ place_item?.description }}</div>
    <h2 class="text-xl font-bold text-[#063D79] outfit-headline">Häufigkeitsverteilung</h2>
    <div class="h-[250px] bg-[#063D79] rounded-md"/>
    <h2 class="text-xl font-bold text-[#063D79] outfit-headline">Netzwerke</h2>
    <div class="flex flex-col gap-2 md:grid md:grid-cols-2">
      <div class="h-[250px] bg-[#063D79] rounded-md"/>
      <div class="h-[250px] bg-[#063D79] rounded-md"/>
    </div>
  </div>
  <div v-if="place_pub_media.length > 0" class="max-h-[30vh] flex flex-col gap-2">
    <h2 class="text-xl font-bold text-[#063D79] outfit-headline">Publikationsort von</h2>
    <div class="overflow-y-auto pb-2">
      <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
        <div v-for="pubmedium in place_pub_media" :key="pubmedium.id">
          <PubMediumPreview :pubmedium="pubmedium"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
