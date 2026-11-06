<script setup lang="ts">
import { useBreakpoints, breakpointsTailwind } from '@vueuse/core'
import type { PubMediumPageable, PubMediumPreviewDTO } from "~/utils/types";
import PaginatedSelect from './PaginatedSelect.vue';

const breakpoints = useBreakpoints(breakpointsTailwind)
const isMobile = breakpoints.smaller('lg')
const activeTab = ref<string | null>(null)
watchEffect(() => {
  activeTab.value = isMobile.value ? null : "0"
})

const props = defineProps<{
  filters: PubMediumPageable
}>()

const emit = defineEmits<{
  (e: 'reset-filters'): void
}>()

const publisherApi = usePublisher();
const pubMediumApi = usePubMedium();

async function fetchEditorialOffice(query: string, pageable: Pageable): Promise<Page<PubMediumPreviewDTO>> {
  const filter = {
    ...pageable,
    'editorialOffice': query,
    'editorialOfficeNotNull': true
  }
  const res = await pubMediumApi.filterPaginated(filter);

  return res;
}

</script>
<template>
  <Accordion :value="activeTab">
    <AccordionPanel value="0">
      <AccordionHeader class="text-lg font-semibold border-b border-gray-200">
        Filtern nach:
      </AccordionHeader>
      <AccordionContent class="flex flex-col gap-2">
        <div class="flex flex-col gap-3">
          <div class="grid grid-cols-1">
            <label for="title" class="text-xs font-bold ">Titel</label>
            <InputText id="title" placeholder="z. B. Das Gedicht" v-model="filters.title" fluid />
          </div>

          <div class="grid grid-cols-1">
            <label for="subtitle" class="text-xs font-bold ">Untertitel</label>
            <InputText id="subtitle" placeholder="z. B. Wochenschrift" v-model="filters.subtitle" fluid />
          </div>

          <div class="grid grid-cols-1">
            <label for="publisher-name" class="text-xs font-bold ">Verlag</label>
            <PaginatedSelect
              :fetchFunction="publisherApi.searchPaginated"
              :cullFunction="pb => ({ name: pb.name })"
              sort="name,asc"
              id="publisher-name"
              placeholder="Verlag wählen"
              v-model="filters['publisher.name']"
              option-value="name"
              :option-label="(p) => p.name"
              showClear
              fluid
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="editorial-office" class="text-xs font-bold ">Redaktion</label>
            <PaginatedSelect
              :fetchFunction="fetchEditorialOffice"
              :cullFunction="pm => ({ editorialOffice: pm.editorialOffice })"
              sort="editorialOffice,asc"
              id="editorial-office"
              placeholder="Redaktion wählen"
              v-model="filters['editorialOffice']"
              option-value="editorialOffice"
              :option-label="(pm) => pm.editorialOffice"
              showClear
              fluid
            />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="flex flex-col gap-1">
              <label for="start-year" class="text-xs font-bold ">Erscheinungsbeginn</label>
              <InputNumber id="start-year" placeholder="z. B. 1920" v-model="filters.startYear" :min="0"
                :useGrouping="false" fluid />
            </div>

            <div class="flex flex-col gap-1">
              <label for="end-year" class="text-xs font-bold ">Erscheinungsende</label>
              <InputNumber id="end-year" placeholder="z. B. 1945" v-model="filters.endYear" :min="0"
                :useGrouping="false" fluid />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="flex flex-col gap-1">
              <label for="amount-volumes" class="text-xs font-bold ">Anzahl Jahrgänge</label>
              <InputNumber id="amount-volumes" v-model="filters.amountVolumes" :min="0" :useGrouping="false" fluid />
            </div>

            <div class="flex flex-col gap-1">
              <label for="amount-issues" class="text-xs font-bold ">Anzahl Ausgaben</label>
              <InputNumber id="amount-issues" v-model="filters.amountIssues" :min="0" :useGrouping="false" fluid />
            </div>
          </div>
          <button type="button"
            class="flex items-center justify-center w-full min-h-9 rounded-md border px-3 py-2 text-sm text-primary cursor-pointer shadow-sm hover:shadow-md"
            @click="emit('reset-filters')">
            <i class="pi pi-refresh mr-2" />
            <span class="text-center">Alle Eingaben zurücksetzen</span>
          </button>
        </div>
      </AccordionContent>
    </AccordionPanel>
  </Accordion>

</template>