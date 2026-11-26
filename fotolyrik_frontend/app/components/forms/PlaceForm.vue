<script setup lang="ts">
import { z } from "zod";
import maplibregl from "maplibre-gl";
import "maplibre-gl/dist/maplibre-gl.css";

const props = defineProps<{
  action: 'create' | 'edit';
  header: string;
  place?: PlaceDTO;
}>();

const schema = z.object({
  name: z.string().min(1, { message: "Mindestens ein Zeichen erforderlich" }),
  description: z.string().nullish(),
  latitude: z.number().min(-90).max(90).nullish(),
  longitude: z.number().min(-180).max(180).nullish()
}).superRefine((d, ctx) => {
  // Coordinates come in pairs: either both are set or both are empty
  if (d.latitude != null && d.longitude == null) {
    ctx.addIssue({ code: "custom", message: "Bitte auch den Längengrad angeben.", path: ["longitude"] });
  }
  if (d.latitude == null && d.longitude != null) {
    ctx.addIssue({ code: "custom", message: "Bitte auch den Breitengrad angeben.", path: ["latitude"] });
  }
});

type Schema = z.output<typeof schema>;

const defaults = (): Partial<Schema> => ({
  name: '',
  description: null,
  latitude: null,
  longitude: null
});

const state = reactive<Partial<PlaceDTO>>({
  ...defaults(),
  ...structuredClone(toRaw(props.place))
});

const placeApi = usePlace();
const toast = useToast();

let map: maplibregl.Map | null = null;
let marker: maplibregl.Marker | null = null;

const coords = (): [number, number] | null =>
    state.latitude != null && state.longitude != null ? [state.longitude, state.latitude] : null;

// Sync the marker on the map with the coordinates pair in the state.
const syncMarker = () => {
  if (!map) return;
  const c = coords();
  if (!c) {
    marker?.remove();
    marker = null;
  } else if (marker) {
    marker.setLngLat(c);
  } else {
    marker = new maplibregl.Marker({ color: "#063D79" }).setLngLat(c).addTo(map);
  }
};
watch(() => [state.latitude, state.longitude], syncMarker);

const centerMap = () => {
  const c = coords();
  if (map && c) map.jumpTo({ center: c, zoom: 7 });
};

const onSubmit = async () => {
  try {
    if (props.action === 'create') {
      await placeApi.create(state);
      await refreshNuxtData('place-list');
      toast.add({ color: 'success', title: 'Erfolg', description: 'Erfolgreich erstellt', duration: 3000 });
      await navigateTo('/places');
    } else if (props.action === 'edit' && props.place?.id) {
      await placeApi.update(props.place.id, state);
      await Promise.all([refreshNuxtData('place-list'), refreshNuxtData(`place-${props.place.id}`)]);
      toast.add({ color: 'success', title: 'Erfolg', description: 'Erfolgreich aktualisiert', duration: 3000 });
      await navigateTo(`/places/${props.place.id}`);
    }
  } catch (error) {
    console.error(error);
    toast.add({ color: 'error', title: 'Fehler', description: 'Ein Fehler ist aufgetreten', duration: 3000 });
  }
};


onMounted(() => {
  map = new maplibregl.Map({
    container: "map",
    style: {
      version: 8,
      sources: {
        osm: {
          type: 'raster',
          tiles: ['https://tile.openstreetmap.de/{z}/{x}/{y}.png'],
          tileSize: 256,
          attribution: '&copy; OpenStreetMap Contributors'
        }
      },
      layers: [
        {
          id: 'osm-layer',
          type: 'raster',
          source: 'osm'
        }
      ]
    },
    center: [10.4515, 51.1657],
    zoom: 5
  });
  map.addControl(new maplibregl.NavigationControl(), 'top-right');
  syncMarker();
  centerMap();
  // map -> state: left click sets the coordinates, right click clears them
  map.on('click', (e) => {
    state.latitude = Number(e.lngLat.lat.toFixed(6));
    state.longitude = Number(e.lngLat.lng.toFixed(6));
  });
  map.on('contextmenu', () => {
    state.latitude = null;
    state.longitude = null;
  });
});

onBeforeUnmount(() => map?.remove());
</script>

<template>
  <div class="flex flex-col md:mx-auto md:w-[70%] gap-4">
    <h1 class="text-2xl outfit-headline text-primary font-bold">{{ props.header }}</h1>
    <UAlert icon="i-lucide-info" title="Anleitung" color="neutral" variant="subtle">
      <template #description>
        <p>Füllen Sie bitte die untenstehenden Felder aus, um einen Ort zu erstellen oder anzupassen.</p>
        <p>Sie können die Koordinaten entweder manuell einfügen oder einen Marker <UIcon name="i-lucide-map-pin" class="text-primary align-middle" /> auf der Karte setzen.</p>
        <p>Falls Sie den Marker löschen wollen, drücken Sie bitte die rechte Maustaste.</p>
      </template>
    </UAlert>
    <div class="border-2 border-solid rounded-md p-5">
      <UForm :schema="schema" :state="state" @submit="onSubmit">
        <div class="flex flex-col gap-4">
          <UFormField label="Ortsname" name="name" required>
            <UInput v-model="state.name" icon="i-lucide-map-pin" placeholder="Berlin" class="w-full" :ui="{ trailing: 'pe-1' }">
              <template v-if="state.name" #trailing>
                <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.name = ''" />
              </template>
            </UInput>
          </UFormField>
          <UFormField label="Beschreibung" name="description">
            <UTextarea v-model.nullable="state.description" placeholder="Hauptstadt Deutschlands" autoresize :rows="2" class="w-full" />
          </UFormField>
          <UFormField label="Karte">
            <div id="map" class="h-150 w-full rounded-md"/>
          </UFormField>
          <div class="flex flex-col gap-4 md:flex-row">
            <UFormField label="Breitengrad" name="latitude" class="flex-1">
              <UInputNumber
                  v-model="state.latitude"
                  :increment="false"
                  :decrement="false"
                  :step="0.000001"
                  :min="-90"
                  :max="90"
                  :format-options="{ useGrouping: false, maximumFractionDigits: 6 }"
                  placeholder="52.5162"
                  class="w-full"
                  @update:model-value="centerMap"
              />
            </UFormField>
            <UFormField label="Längengrad" name="longitude" class="flex-1">
              <UInputNumber
                  v-model="state.longitude"
                  :increment="false"
                  :decrement="false"
                  :step="0.000001"
                  :min="-180"
                  :max="180"
                  :format-options="{ useGrouping: false, maximumFractionDigits: 6 }"
                  placeholder="13.3777"
                  class="w-full"
                  @update:model-value="centerMap"
              />
            </UFormField>
          </div>
          <UButton type="submit" :label="props.action === 'create' ? 'Erstellen' : 'Ändern'" class="self-start" />
        </div>
      </UForm>
    </div>
  </div>
</template>
