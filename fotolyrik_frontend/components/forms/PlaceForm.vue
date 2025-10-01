<script setup lang="ts">
import type { Place } from "~/utils/types";
import { onMounted, ref } from "vue";
import { useToast } from "primevue/usetoast";
import { getNode } from "@formkit/core";
import maplibregl from "maplibre-gl";
import "maplibre-gl/dist/maplibre-gl.css";

const props = defineProps<{
  action: 'create' | 'edit';
  header: string;
  place?: Place;
}>();

const toast = useToast();
const submitted = ref(false);
const place_store = usePlaceStore();

type PlaceInput = Omit<Place, 'id' | 'createdBy' | 'createdDate' | 'lastModifiedBy' | 'lastModifiedDate'>;

const submit = async (formData: Partial<PlaceInput>) => {
  try {
    if (props.action === 'create') {
      await place_store.createPlace(formData);
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich erstellt', life: 3000});
      const form = getNode('place_creation');
      form?.reset();
      if (current_marker) {
        current_marker.remove();
        current_marker = null;
      }
    } else if (props.action === 'edit' && props.place?.id) {
      await place_store.updatePlace(formData, props.place.id);
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich upgedated', life: 3000});
      navigateTo(`/photopoems/${props.place?.id}`);
    }
  } catch (error) {
    console.log(error)
    toast.add({
      severity: 'error',
      summary: 'Fehler',
      detail: 'Fehler beim Erstellen des Ort-Objektes',
      life: 3000
    });
  }
};

let current_marker: maplibregl.Marker | null = null;
const latitude = ref<number|null>(null);
const longitude = ref<number|null>(null);

onMounted(async () => {
  const map = new maplibregl.Map({
    container: "map",
    zoom: 4.5,
    center: [11, 51],
    style: {
      version: 8,
      glyphs: "https://demotiles.maplibre.org/font/{fontstack}/{range}.pbf",
      sources: {
        osm: {
          type: "raster",
          tiles: ["https://tile.openstreetmap.de/{z}/{x}/{y}.png"],
          tileSize: 256,
          attribution: "&copy; OpenStreetMap Contributors",
        },
      },
      layers: [
        {
          id: "osm-layer",
          type: "raster",
          source: "osm",
        },
      ],
    },
  });

  map.on('click', (e) => {
    let { lng, lat } = e.lngLat
    lng = Number(lng.toFixed(5))
    lat = Number(lat.toFixed(5))

    if (current_marker) {
      current_marker.remove();
    }
    current_marker = new maplibregl.Marker()
        .setLngLat([lng, lat])
        .setPopup(new maplibregl.Popup())
        .addTo(map);

    latitude.value = lat
    longitude.value = lng
  });
  map.on('contextmenu', () => {
    if (current_marker) {
      current_marker.remove();
      current_marker = null;
    }
    latitude.value = null;
    longitude.value = null;
  });
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-2xl outfit-headline font-bold">{{ props.header }}</h1>
    <div>
      Klicken Sie auf die Karte, um einen Ort zu platzieren. Der Längen- und Breitengrad werden automatisch in die entsprechenden Formularfelder übernommen.
    </div>
    <div>
      <div id="map" class="h-[400px] w-full"></div>
    </div>
    <h1 class="text-2xl outfit-headline font-bold text-[#063D79]">Neuen Ort erstellen</h1>
    <p class="roboto-plain">Füllen Sie bitte die untenstehenden Felder aus, um einen Ort zu erstellen</p>
    <FormKit
        type="form"
        id="place_creation"
        :form-class="submitted ? 'hide' : 'show'"
        submit-label="Erstellen"
        @submit="submit"
        #default="{ value }"
        :actions="false"
    >
      <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-[#F1F2F2]">
        <FormKit
            type="text"
            name="name"
            label="Ortsname"
            placeholder="Berlin"
            validation="required"
            prefix-icon="text"
            outer-class="max-w-full"
        />
        <FormKit
            type="textarea"
            name="description"
            label="Beschreibung"
            placeholder="Hauptstadt Deutschlands"
            prefix-icon="textarea"
            outer-class="max-w-full min-w-[0%]"
        />
        <div class="flex flex-row space-x-5">
          <FormKit
              type="number"
              number
              name="latitude"
              v-model="latitude"
              label="Breitengrad"
              placeholder="52.5162"
              prefix-icon="number"
              outer-class="max-w-full"
          />
          <FormKit
              type="number"
              number
              name="longitude"
              v-model="longitude"
              label="Längengrad"
              placeholder="13.3777"
              prefix-icon="number"
              outer-class="max-w-full"
          />
        </div>
        <div class="border-solid border-2 rounded-md p-5 bg-[#F1F2F5] mb-2">
          <div class="font-mono">JSON-Preview</div>
          <hr>
          <pre wrap class="text-sm md:text-base">{{ value }}</pre>
        </div>
        <FormKit
            type="submit"
            label="Erstellen"
        />
      </div>
    </FormKit>
  </div>
</template>

<style scoped>

</style>
