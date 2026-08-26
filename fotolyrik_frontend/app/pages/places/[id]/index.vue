<script setup lang="ts">
import maplibregl, { LngLat } from "maplibre-gl";
import "maplibre-gl/dist/maplibre-gl.css"
import { ref, onMounted } from "vue";
import type {PlaceDTO, PlaceMetricsDTO} from "~/utils/types";
import PubMediumPreview from "~/components/UI/PubMediumPreview.vue";
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";
import SkeletonPlaceholder from "~/components/UI/placeholders/SkeletonPlaceholder.vue";
import NotFoundPlaceholder from "~/components/UI/placeholders/NotFoundPlaceholder.vue";
import PlaceMetrics from "~/components/visualizations/PlaceMetrics.vue";
import PhotopoemDatePlot from "~/components/visualizations/PhotopoemDatePlot.vue";


const place_api = usePlace();
const pubmedium_api = usePubMedium();
const photopoem_api = usePhotopoem();

const route = useRoute();
const place_id = Number(route.params.id);
const has_coords = computed(() => {
  return place_item.value && place_item.value.latitude && place_item.value.longitude;
});

const center = computed(() => {
  if (place_item.value && place_item.value.latitude && place_item.value.longitude) {
    return new LngLat(place_item.value.longitude, place_item.value.latitude);
  }
  return new LngLat(10.0, 51.0);
});

useHead(() => ({
  title: place_item.value?.name ? `${place_item.value?.name}` : 'Nicht gefunden',
}));

const {data: place_item, status}  = await place_api.usePlaceId(place_id)
const {data: place_pub_media}  = await pubmedium_api.useFilteredPubMedium({ 'pubplace-id': place_id })
const {data:place_metrics}  = await place_api.usePlaceMetricsId(place_id)
const {data: place_photopoems}  = await photopoem_api.useFilteredPhotopoems({ 'pubplace-id': place_id })

onMounted(async () => {
  if (!document.getElementById("map")) {
    return;
  }
  const map = new maplibregl.Map({
    container: "map",
    zoom: 5,
    center: center.value,
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
  map.on('load', () => {
    if (has_coords.value) {
      new maplibregl.Marker()
        .setLngLat(center.value)
        .addTo(map);
      map.setZoom(10);
    }
  });
});
</script>

<template>
  <SkeletonPlaceholder v-if="status ==='pending'"/>
  <NotFoundPlaceholder v-else-if="!place_item"/>
  <div v-else class="flex flex-col gap-4 mb-9">
    <div class="flex flex-row justify-between">
      <h1 class="text-3xl font-bold outfit-headline text-primary">{{ place_item?.name }}</h1>
      <PageToolbar
          v-if="place_item"
          :id="place_item.id"
          entity_type="place"
          :page_url="`${ route.fullPath }`"
      />
    </div>
    <div class="flex flex-col gap-2 md:grid md:grid-cols-2">
      <div>
        <div v-if="has_coords" id="map" class="h-[500px] rounded-md"/>
        <div v-else class="flex flex-col gap-2 items-center justify-center h-[500px] bg-[#F1F2F2] rounded-md">
          <Icon name="material-symbols:error-outline" class="text-8xl text-primary"/>
          <p class="roboto-plain text-center">Für diesen Ort sind bisher keine Koordinaten hinterlegt</p>
        </div>
      </div>
      <div class="rounded-md flex items-center">
        <PlaceMetrics v-if="place_metrics" :data="place_metrics"></PlaceMetrics>
      </div>

    </div>
    <div class="text-md roboto-plain">{{ place_item?.description }}</div>
    <h2 class="text-xl font-bold text-primary outfit-headline" v-if=" place_photopoems && place_photopoems.length > 0">Häufigkeitsverteilung</h2>
    <div class="h-[250px]  rounded-md" v-if="place_photopoems && place_photopoems.length > 0">
      <PhotopoemDatePlot :data="place_photopoems ?? []" />
    </div>
    <h2 class="text-xl font-bold text-primary outfit-headline">Netzwerke</h2>
    <div class="flex flex-col gap-2 md:grid md:grid-cols-2">
      <div class="h-[250px] bg-primary rounded-md"/>
      <div class="h-[250px] bg-primary rounded-md"/>
    </div>
    <div v-if="place_pub_media&& place_pub_media.length > 0" class="max-h-[40vh] flex flex-col gap-4">
      <h2 class="text-xl font-bold text-primary outfit-headline">Publikationsort von</h2>
      <div class="overflow-y-auto pb-2">
        <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
          <div v-for="pubmedium in place_pub_media" :key="pubmedium.id">
            <PubMediumPreview :pubmedium="pubmedium"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
