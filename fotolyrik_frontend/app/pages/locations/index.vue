<script setup lang="ts">
import CreateButton from "~/components/UI/buttons/CreateButton.vue";
import { ref } from "vue";
import { FilterMatchMode } from "@primevue/core";

const locationApi = useLocation();
const {data: locationList} = await locationApi.getAll();
const locations = computed(() => locationList.value?.map(l => ({
  id: l.id,
  name: l.name,
  description: l.description
})));

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  description: { value: null, matchMode: FilterMatchMode.CONTAINS }
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-2xl outfit-headline text-primary font-bold">Fundorte</h1>
    <p class="roboto-plain">
      Eine Übersicht aller Fundorte in der Datenbank. Klicken Sie auf einen Fundort, um Details anzuzeigen oder zu bearbeiten.
    </p>

    <DataTable
        :value="locations"
        v-model:filters="filters"
        :global-filter-fields="['name', 'description']"
        filter-display="row"
        stripedRows
        paginator
        :rows="10"
        class="border-2 border-solid border-[#F1F2F2] mt-4"
    >
      <template #header>
        <div class="flex flex-row justify-between items-center">
          <CreateButton to="/locations/create" />
          <IconField>
            <InputIcon>
              <i class="pi pi-search"/>
            </InputIcon>
            <InputText
                v-model="filters['global'].value"
                type="text"
                placeholder="Alles durchsuchen"
            />
          </IconField>
        </div>
      </template>

      <Column field="name" header="Ort" sortable>
        <template #body="slotProps">
          <NuxtLink
              :to="`/locations/${ slotProps.data.id }`"
              class="roboto-plain font-semibold"
          >
            {{ slotProps.data.name }}
          </NuxtLink>
        </template>
        <template #filter="{ filterModel, filterCallback }">
          <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              placeholder="Nach Name suchen"
          />
        </template>
      </Column>

      <Column field="description" header="Beschreibung" sortable>
        <template #filter="{ filterModel, filterCallback }">
          <InputText
              v-model="filterModel.value"
              type="text"
              @input="filterCallback()"
              placeholder="In Beschreibung suchen"
          />
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<style scoped>
</style>