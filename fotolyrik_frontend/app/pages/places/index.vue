<script setup lang="ts">
import { ref, onMounted } from  'vue';
import { FilterMatchMode } from "@primevue/core";
import MultiPlaceMap from "~/components/UI/MultiPlaceMap.vue";

const place_store = usePlaceStore();
const places = computed(() => place_store.places);
const map_ref = ref<InstanceType<typeof MultiPlaceMap> | null>(null);

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  description: { value: null, matchMode: FilterMatchMode.CONTAINS }
});

onMounted(async () => {
  await map_ref.value?.populatePlaces(places.value);
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-3xl font-bold outfit-headline text-primary">Orte</h1>
    <MultiPlaceMap ref="map_ref"></MultiPlaceMap>
    <DataTable
      :value="places"
      v-model:filters="filters"
      :global-filter-fields="['name', 'description']"
      filter-display="row"
      stripedRows paginator :rows="10"
      class="border-2 border-solid border-[#F1F2F2]"
    >
      <template #header>
        <div class="flex flex-row justify-between items-center">
          <div class="p-2 border border-solid rounded-md hover:shadow-md">
            <NuxtLink to="/places/create" class="flex items-center">
              <i class="pi pi-pen-to-square mr-2"/>
              <div class="text-primary roboto-plain">Neu anlegen</div>
            </NuxtLink>
          </div>
          <IconField>
            <InputIcon>
              <i class="pi pi-search"/>
            </InputIcon>
            <InputText
                v-model="filters['global'].value"
                type="text"
                placeholder="Alle Felder durchsuchen"
            />
          </IconField>
        </div>
      </template>
      <Column field="name" header="Name" class="roboto-plain w-1/3" sortable>
        <template #body="slotProps">
          <NuxtLink
              :to="`/places/${ slotProps.data.id }`"
              class="roboto-plain font-semibold"
          >
            {{ slotProps.data.name }}
          </NuxtLink>
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText
              v-model="filterModel.value"
              type="text" @input="filterCallback()"
              placeholder="Nach Namen suchen"
          />
        </template>
      </Column>
      <Column field="description" header="Beschreibung" class="roboto-plain max-w-1/3">
        <template #body="slotProps">
          <p class="roboto-plain text-black">
            {{ slotProps.data.description }}
          </p>
        </template>
      </Column>
      <Column
          field="longitude"
          header="Koordinaten vorhanden"
          class="roboto-plain"
          :sortable="true"
      >
        <template #body="slotProps">
          <i :class="[(slotProps.data.longitude && slotProps.data.latitude) ? 'pi pi-check text-green-500' : 'pi pi-times text-red-500']"/>
        </template>
      </Column>
    </DataTable>
  </div>
</template>
