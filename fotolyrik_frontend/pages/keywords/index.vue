<script setup lang="ts">
import {ref} from "vue";
import {FilterMatchMode} from "@primevue/core";

function getRandomRgb(): string {
  const r = Math.floor(Math.random() * 256);
  const g = Math.floor(Math.random() * 256);
  const b = Math.floor(Math.random() * 256);
  return `rgba(${r}, ${g}, ${b}, 0.4)`;
}
const keywordStore = useKeywordStore();
const keywords = keywordStore.keywords.map(k => ({id: k.id, value: k.value, gndId: k.gndId, color: getRandomRgb()}));

const filters = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  value: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  gndId: { value: null, matchMode: FilterMatchMode.CONTAINS }
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-2xl outfit-headline text-primary font-bold">Schlagwörter</h1>
    <p class="roboto-plain">
      Eine Übersicht aller Schlagwörter in der Datenbank. Klicken Sie auf ein Schlagwort, um Details anzuzeigen oder zu bearbeiten.
    </p>
    <Tabs value="0">
      <TabList>
        <Tab value="0">
          <p class="roboto-plain font-bold">Tabellarische Übersicht</p>
        </Tab>
        <Tab value="1">
          <p class="roboto-plain font-bold">Tag-Ansicht</p>
        </Tab>
      </TabList>
      <TabPanels>
        <TabPanel value="0">
          <DataTable
              :value="keywords"
              v-model:filters="filters"
              :global-filter-fields="['value', 'gndId']"
              filter-display="row"
              stripedRows paginator :rows="10"
              class="border-2 border-solid border-[#F1F2F2]"
          >
            <template #header>
              <div class="flex flex-row justify-between items-center">
                <div class="p-2 border border-solid rounded-md hover:shadow-md">
                  <NuxtLink to="/keywords/create" class="flex items-center">
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
                      placeholder="Alles durchsuchen"
                  />
                </IconField>
              </div>
            </template>
            <Column field="value" header="Schlagwort" sortable>
              <template #body="slotProps">
                <NuxtLink
                    :to="`/keywords/${ slotProps.data.id }`"
                    class="roboto-plain font-semibold"
                >
                  {{ slotProps.data.value }}
                </NuxtLink>
              </template>
              <template #filter="{ filterModel, filterCallback }">
                <InputText v-model="filterModel.value" type="text" @input="filterCallback()" placeholder="Nach Name suchen" />
              </template>
            </Column>
            <Column field="gndId" header="GND-ID" sortable/>
          </DataTable>
        </TabPanel>
        <TabPanel value="1">
          <div class="flex flex-wrap gap-3">
            <NuxtLink
                :to="`/keywords/${keyword.id}`"
                v-for="keyword in keywords"
                class="font-medium roboto-plain p-1 rounded-lg transition-transform hover:scale-105 shadow-md hover:shadow-lg"
                :style="[{background: keyword.color}]"
            >
              {{keyword.value}}
            </NuxtLink>
          </div>
        </TabPanel>
      </TabPanels>
    </Tabs>
  </div>
</template>

<style scoped>

</style>