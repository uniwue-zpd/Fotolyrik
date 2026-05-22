<script setup lang="ts">
import maplibregl from "maplibre-gl";
import "maplibre-gl/dist/maplibre-gl.css";

const props = defineProps<{
  action: 'create' | 'edit';
  header: string;
  place?: PlaceDTO;
}>();

const placeStore = usePlaceStore();
const toast = useToast();

const formRef = ref<any>(null);

let map: maplibregl.Map;
let marker: maplibregl.Marker | null;

const lat = ref<number | null>(null);
const lng = ref<number | null>(null);

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    try {
      if (props.action === 'create') {
        await placeStore.createPlace(e.values);
        toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich erstellt', life: 3000});
        navigateTo('/places')
      } else if (props.action === 'edit' && props.place?.id) {
        await placeStore.updatePlace(e.values, props.place.id);
        toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich aktualisiert', life: 3000});
        navigateTo(`/places/${props.place?.id}`);
      }
    } catch (error) {
      console.log(error);
      toast.add({severity: 'error', summary: 'Fehler', detail: 'Ein Fehler ist aufgetreten', life: 3000});
    }
  }
};

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
};

onMounted(async () => {
  if (props.place?.latitude && props.place?.longitude) {
    lat.value = props.place.latitude;
    lng.value = props.place.longitude;
  }
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
          id:'osm-layer',
          type: 'raster',
          source: 'osm'
        }
      ]
    },
    center: [10.4515, 51.1657],
    zoom: 5
  });
  map.addControl(new maplibregl.NavigationControl(), 'top-right');
  if (lat.value != null && lng.value != null) {
    const coords: [number, number] = [lng.value, lat.value];
    marker = new maplibregl.Marker({ color: "#063D79" })
        .setLngLat(coords)
        .addTo(map);
    map.setCenter(coords);
    map.setZoom(7);
  }
  map.on('click', (e) => {
    lat.value = Number(e.lngLat.lat.toFixed(6));
    lng.value = Number(e.lngLat.lng.toFixed(6));
    formRef.value.setFieldValue('latitude', lat.value);
    formRef.value.setFieldValue('longitude', lng.value);
    if (marker) {
      marker.setLngLat(e.lngLat);
    } else {
      marker = new maplibregl.Marker({ color: "#063D79" })
          .setLngLat(e.lngLat)
          .addTo(map);
    }
  });
  map.on('contextmenu', (e) => {
    lat.value = null;
    lng.value = null;
    formRef.value.setFieldValue('latitude', null);
    formRef.value.setFieldValue('longitude', null);
    if (marker) {
      marker.remove();
      marker = null;
    }
  })
});
</script>

<template>
  <div class="flex flex-col mx-auto w-[70%] gap-4">
    <h1 class="text-2xl outfit-headline text-primary font-bold">{{ props.header }}</h1>
    <Fieldset legend="Anleitung" class="border-2 border-solid rounded-md overflow-auto">
      <div class="flex flex-col gap-1 p-2 rounded-md roboto-plain text-black">
        <p>Füllen Sie bitte die untenstehenden Felder aus, um einen Ort zu erstellen oder anzupassen.</p>
        <p>Sie können die Koordinaten entweder manuell einfügen oder einen Marker <i class="pi pi-map-marker text-primary"/> auf der Karte setzen.</p>
        <p>Falls Sie den Marker löschen wollen, drücken Sie bitte die rechte Maustaste.</p>
      </div>
    </Fieldset>
    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Form
          ref="formRef"
          v-slot="$form"
          @submit="onFormSubmit"
          class="flex flex-col gap-4"
          :initial-values="props.place ? props.place : {} as PlaceDTO"
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
        <div class="flex flex-row space-x-3">
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
                  v-on:keydown.enter.prevent
                  @update:modelValue="onCoordinatesUpdate"
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
                  v-on:keydown.enter.prevent
                  @update:modelValue="onCoordinatesUpdate"
                  fluid
              />
            </IconField>
          </FormField>
        </div>
        <!--
        <Fieldset legend="Form States" class="h-80 overflow-auto">
          <pre class="whitespace-pre-wrap">{{ $form }}</pre>
        </Fieldset>
        -->
        <Button type="submit" severity="primary" :label="props.action === 'create' ? 'Erstellen' : 'Ändern'"/>
      </Form>
    </div>
  </div>
</template>
