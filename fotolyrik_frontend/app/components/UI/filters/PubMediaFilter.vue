<script setup lang="ts">
import { useBreakpoints, breakpointsTailwind } from '@vueuse/core'
import type { PubMediumPageable, PubMediumPreviewDTO } from "~/utils/types";
import PaginatedSelect from './PaginatedSelect.vue';

const breakpoints = useBreakpoints(breakpointsTailwind)
const isMobile = breakpoints.smaller('lg')
const activeTab = ref<string | undefined>(undefined)
watchEffect(() => {
  activeTab.value = isMobile.value ? undefined : "0"
})

const props = defineProps<{
  filters: PubMediumPageable
}>()

const emit = defineEmits<{
  (e: 'reset-filters'): void
}>()

const publisherApi = usePublisher();
const pubMediumApi = usePubMedium();

async function fetchEditorialOffices(query: string, pageable: Pageable): Promise<Page<PubMediumPreviewDTO>> {
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
  <UAccordion v-model="activeTab" :items="[{ label: 'Filtern nach:', value: '0' }]" :unmount-on-hide="false">
    <template #body>
        <div class="flex flex-col gap-3">
          <div class="grid grid-cols-1">
            <label for="title" class="text-xs font-bold ">Titel</label>
            <UInput id="title" placeholder="z. B. Das Gedicht" v-model="filters.title" class="w-full" />
          </div>

          <div class="grid grid-cols-1">
            <label for="subtitle" class="text-xs font-bold ">Untertitel</label>
            <UInput id="subtitle" placeholder="z. B. Wochenschrift" v-model="filters.subtitle" class="w-full" />
          </div>

          <div class="grid grid-cols-1">
            <label for="publisher-name" class="text-xs font-bold ">Verlag</label>
            <PaginatedSelect
              :fetchFunction="publisherApi.searchPaginated"
              :get-value="pb => pb.name"
              :get-label="pb => pb.name"
              sort="name,asc"
              id="publisher-name"
              placeholder="Verlag wählen"
              v-model="filters['publisher.name']"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="editorial-office" class="text-xs font-bold ">Redaktion</label>
            <PaginatedSelect
              :fetchFunction="fetchEditorialOffices"
              :get-value="pm => pm.editorialOffice"
              :get-label="pm => pm.editorialOffice"
              sort="editorialOffice,asc"
              id="editorial-office"
              placeholder="Redaktion wählen"
              v-model="filters['editorialOffice']"
            />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="flex flex-col gap-1">
              <label for="start-year" class="text-xs font-bold ">Erscheinungsbeginn</label>
              <UInputNumber id="start-year" placeholder="z. B. 1920" v-model.optional="filters.startYear" :min="0"
                :format-options="{ useGrouping: false }" :increment="false" :decrement="false" class="w-full" />
            </div>

            <div class="flex flex-col gap-1">
              <label for="end-year" class="text-xs font-bold ">Erscheinungsende</label>
              <UInputNumber id="end-year" placeholder="z. B. 1945" v-model.optional="filters.endYear" :min="0"
                :format-options="{ useGrouping: false }" :increment="false" :decrement="false" class="w-full" />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="flex flex-col gap-1">
              <label for="amount-volumes" class="text-xs font-bold ">Anzahl Jahrgänge</label>
              <UInputNumber id="amount-volumes" v-model.optional="filters.amountVolumes" :min="0" :format-options="{ useGrouping: false }" :increment="false" :decrement="false" class="w-full" />
            </div>

            <div class="flex flex-col gap-1">
              <label for="amount-issues" class="text-xs font-bold ">Anzahl Ausgaben</label>
              <UInputNumber id="amount-issues" v-model.optional="filters.amountIssues" :min="0" :format-options="{ useGrouping: false }" :increment="false" :decrement="false" class="w-full" />
            </div>
          </div>
          <UButton type="button" variant="outline" icon="i-lucide-rotate-cw" class="w-full justify-center"
            @click="emit('reset-filters')">Alle Eingaben zurücksetzen</UButton>
        </div>
    </template>
  </UAccordion>

</template>