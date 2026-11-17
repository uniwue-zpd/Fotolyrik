<script setup lang="ts">
import ContentCard from "~/components/UI/cards/ContentCard.vue";
import PageSelection from "~/components/UI/buttons/PageSelection.vue";
import PlaceFilter from "~/components/UI/filters/PlaceFilter.vue";
import MultiPlaceMap from "~/components/visualizations/MultiPlaceMap.vue";
import type { Page, PlaceDTO, PlacePageable } from "~/utils/types";

const placeApi = usePlace();

// ---------- Tabs ----------
const tabItems = [
  { label: "Liste", value: "list", icon: "i-lucide-list", slot: "list" as const },
  { label: "Karte", value: "map", icon: "i-lucide-map", slot: "map" as const }
];
const activeTab = ref<string>("list");

// ---------- List tab (paginated) ----------
const initialPageParameter: PlacePageable = {
  page: 0,
  size: 10,
  sort: "name,asc"
};

const initialFilters: PlacePageable = {
  name: undefined
};

const pageParameter = reactive<PlacePageable>({ ...initialPageParameter });
const filters = reactive<PlacePageable>({ ...initialFilters });

const resetFilter = () => {
  Object.assign(pageParameter, initialPageParameter);
  Object.assign(filters, initialFilters);
};

const sortOptions = ref([
  { label: "Name aufsteigend", value: "name,asc" },
  { label: "Name absteigend", value: "name,desc" }
]);

const sort = computed({
  get: () => String(pageParameter.sort ?? initialPageParameter.sort),
  set: (value: string) => { pageParameter.sort = value; }
});

const pageSizeOptions = ref([
  { label: "5 pro Seite", value: 5 },
  { label: "10 pro Seite", value: 10 },
  { label: "15 pro Seite", value: 15 },
  { label: "30 pro Seite", value: 30 },
  { label: "50 pro Seite", value: 50 }
]);

const {
  data: places,
  pending: isLoading,
  refresh
} = useAsyncData<Page<PlaceDTO>>(
  "places-paginated",
  () => placeApi.fetchPaginated({ ...pageParameter, ...filters })
);

const debouncedRefresh = debounce(() => {
  if (pageParameter.page === 0) {
    refresh();
  } else {
    pageParameter.page = 0;
  }
}, 300);

watch(filters, () => debouncedRefresh(), { deep: true });
watch(pageParameter, () => refresh(), { deep: true });

// ---------- Map tab (all places, loaded on first activation) ----------
const map_ref = ref<InstanceType<typeof MultiPlaceMap> | null>(null);

const {
  data: allPlaces,
  status: mapStatus,
  execute: loadAllPlaces
} = useAsyncData("places-map", placeApi.fetchAll, { immediate: false, server: false });

// 0/0 is stored for places without known coordinates, so treat falsy values as missing
const mappablePlaces = computed(() => allPlaces.value?.filter(p => !!(p.latitude && p.longitude)) ?? []);

// The map tab stays mounted (unmount-on-hide=false), so the map is built once on first activation;
// building it earlier would size it against a hidden container.
const mapPopulated = ref(false);

watch(activeTab, async (tab) => {
  if (tab !== "map" || mapPopulated.value) return;
  if (!allPlaces.value) await loadAllPlaces();
  await nextTick();
  await map_ref.value?.populatePlaces(mappablePlaces.value);
  mapPopulated.value = true;
});

useHead(() => ({
  title: 'Orte - Übersicht'
}));
</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex flex-col gap-2">
      <h1 class="text-3xl font-bold outfit-headline text-primary">Orte</h1>
      <p class="roboto-plain">
        Eine Übersicht aller Orte in der Datenbank. Klicken Sie auf einen Ort, um Details anzuzeigen oder zu
        bearbeiten.
      </p>
    </div>

    <UTabs v-model="activeTab" :items="tabItems" variant="link" :unmount-on-hide="false" class="w-full">
      <template #list>
        <div class="flex flex-col gap-5 pt-2 lg:flex-row justify-between">
          <div class="lg:w-1/4">
            <PlaceFilter :filters="filters" @reset-filters="resetFilter" />
          </div>
          <div class="flex flex-col gap-4 lg:w-3/4">
            <div class="grid grid-cols-[1fr_auto] gap-2 sm:flex sm:flex-wrap sm:justify-end">
              <UButton to="/places/create" icon="i-lucide-square-pen" label="Neu anlegen" color="neutral" variant="outline"
                size="lg" class="col-span-2 justify-center text-primary hover:shadow-md" />
              <USelect v-model="sort" :items="sortOptions" size="lg" class="w-full sm:w-48" />
              <USelect v-model="pageParameter.size" :items="pageSizeOptions" size="lg" class="sm:w-36" />
            </div>
            <div class="flex flex-col gap-2 justify-between">
              <div v-if="isLoading" class="flex flex-col gap-2 items-center py-5">
                <ProgressSpinner />
                <div class="roboto-plain text-primary font-semibold text-lg">Inhalte werden geladen</div>
              </div>
              <div v-else-if="!places || places.content.length === 0"
                class="flex w-full justify-center text-primary text-2xl py-5">
                Keine Orte gefunden!
              </div>
              <div v-else class="flex flex-col gap-2">
                <ContentCard v-for="{ id, name } in places.content" :key="id"
                  :headerLink="`/places/${id}`">
                  <template #header>
                    {{ name }}
                  </template>
                </ContentCard>
              </div>
              <PageSelection :page-parameter="pageParameter" :total-pages="places?.totalPages" />
            </div>
          </div>
        </div>
      </template>

      <template #map>
        <div class="pt-2">
          <div v-if="mapStatus === 'pending'" class="flex flex-col gap-2 items-center py-5">
            <ProgressSpinner />
            <div class="roboto-plain text-primary font-semibold text-lg">Karte wird geladen</div>
          </div>
          <MultiPlaceMap v-show="mapStatus !== 'pending'" ref="map_ref" />
        </div>
      </template>
    </UTabs>
  </div>
</template>
