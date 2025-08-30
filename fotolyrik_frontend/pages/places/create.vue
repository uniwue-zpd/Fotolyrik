<script setup lang="ts">
/*
import type { Place } from "~/utils/types";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import apiClient from "~/service/api";
import { navigateTo } from "#app";

const toast = useToast();

type PlaceInput = Omit<Place, 'id' | 'created_by' | 'created_date' | 'last_modified_by' | 'last_modified_date'>;
const submitted = ref(false);

const submit = async (formData: Partial<PlaceInput>) => {
  try {
    const response = await apiClient.post('/places', formData)
    submitted.value = true;
    toast.add({severity: 'success', detail: 'Erfolgreich erstellt', life: 3000})
    navigateTo('/places');
  } catch (error) {
    console.log(error)
    toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Erstellen des Ortes', life: 3000})
  }
};*/
import type { Place } from "~/utils/types";
import { navigateTo } from "#app";
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";
import maplibregl from "maplibre-gl";
import apiClient from "~/service/api";

const lat = ref<number | null>(null);
const lng = ref<number | null>(null);
let map: maplibregl.Map;
let marker: maplibregl.Marker | null;

const toast = useToast();

onMounted(() => {
  map = new maplibregl.Map({
    container: "map",
    //style: "https://demotiles.maplibre.org/style.json",
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
    center: [10.4515, 51.1657], // Germany Center
    zoom: 5
  });

  map.addControl(new maplibregl.NavigationControl(), "top-right");

  map.on("click", (e) => {
    lat.value = Number(e.lngLat.lat.toFixed(6));
    lng.value = Number(e.lngLat.lng.toFixed(6));
    if (marker) {
      marker.setLngLat(e.lngLat);
    } else {
      marker = new maplibregl.Marker({ color: "#063D79" })
      .setLngLat(e.lngLat)
      .addTo(map);
    }
  })
})

const resolver = ref(
  zodResolver(
    z.object({
      name: z.string("Bitte geben Sie einen Ortsnamen an."),
      description: z.any(),
      latitude: z.any(),
      longitude: z.any(),
    })
  )
);

const onCoordinatesUpdate = () => {
  if (lat.value == null || lng.value == null) return;
  const coords: [number, number] = [lng.value, lat.value];
  if (marker) {
    marker.setLngLat(coords);
  } else {
    marker = new maplibregl.Marker({ color: "#063D79" })
    .setLngLat(coords)
    .addTo(map);
  }
  map.setCenter(coords);
  map.setZoom(7);
}

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    e.values.longitude = lng.value;
    e.values.latitude = lat.value;
    try {
      const response = await apiClient.post('/places', e.values)
      toast.add({severity: 'success', detail: 'Erfolgreich erstellt', life: 3000})
      navigateTo('/places');
    } catch (error) {
      console.log(error)
      toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Erstellen des Ortes', life: 3000})
    }
  }
};
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-2xl outfit-headline font-bold text-[#063D79]">Neuen Ort erstellen</h1>
    <p class="roboto-plain">Füllen Sie bitte die untenstehenden Felder aus, um einen Ort zu erstellen</p>
    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Form
        v-slot="$form"
        :resolver
        @submit="onFormSubmit"
        class="flex flex-col gap-4"
      >
        <FormField v-slot="$field" name="name" class="flex flex-col gap-1 flex-auto">
          <label for="name" class="font-bold">Ortsname*</label>
          <IconField>
            <InputIcon class="pi pi-map-marker" />
            <InputText 
              id="name" 
              placeholder="Berlin" 
              v-on:keydown.enter.prevent
              fluid 
            />
          </IconField>
          <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
            {{ $form.name.error.message }}
          </Message>
        </FormField>
        <FormField v-slot="$field" name="description" class="flex flex-col gap-1 flex-auto">
          <label for="description" class="font-bold">Beschreibung</label>
          <Textarea
            id="description"
            placeholder="Hauptstadt Deutschlands"
            rows="2"
            autoResize
            fluid
          />
          <Message v-if="$form.description?.invalid" severity="error" size="small" variant="simple">
            {{ $form.description.error.message }}
          </Message>
        </FormField>
        <div class="flex flex-col gap-1 flex-auto">
          <label for="map" class="font-bold">Karte</label>
          <div id="map" class="h-[600px] w-full rounded-md"/>
        </div>
        <div class="flex flex-row gap-6 flex-wrap">
          <FormField v-slot="$field" name="latitude" class="flex flex-col gap-1 flex-1"> 
            <label for="latitude" class="font-bold">Breitengrad</label>
            <IconField>
              <InputIcon class="pi pi-map" />
              <InputNumber 
                id="latitude"
                v-model="lat"
                placeholder="52.5162"
                :min="-90"
                :max="90"
                :useGrouping="false"
                :minFractionDigits="0" 
                :maxFractionDigits="6"
                @update:modelValue="onCoordinatesUpdate"
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
          </FormField>
          <FormField v-slot="$field" name="longitude" class="flex flex-col gap-1 flex-1"> 
            <label for="longitude" class="font-bold">Längengrad</label>
            <IconField>
              <InputIcon class="pi pi-map" />
              <InputNumber 
                id="longitude"
                v-model="lng"
                placeholder="13.3777"
                :min="-180"
                :max="180"
                :useGrouping="false" 
                :minFractionDigits="0" 
                :maxFractionDigits="6"
                @update:modelValue="onCoordinatesUpdate"
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
          </FormField>
        </div>
        <Button type="submit" severity="primary">Erstellen</Button>

        <!--
        <Fieldset legend="Form States" class="h-80 overflow-auto">
          <pre class="whitespace-pre-wrap">{{ $form }}</pre>
        </Fieldset>
        -->
      </Form>
    </div>
  </div> 
</template>

<style scoped>

</style>
