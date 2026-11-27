<script setup lang="ts">
import { useBreakpoints, breakpointsTailwind } from '@vueuse/core'
import PaginatedSelect from '~/components/UI/filters/PaginatedSelect.vue'

defineProps<{
  filters: PubMediumPageable
}>()

const emit = defineEmits<{
  (e: 'reset-filters'): void
}>()

const isMobile = useBreakpoints(breakpointsTailwind).smaller('lg')
// Start closed on server and first client render to avoid a hydration mismatch,
// then open on desktop once the viewport is known.
const openItem = ref<string | undefined>(undefined)
const syncOpenState = () => {
  openItem.value = isMobile.value ? undefined : 'filters'
}
onMounted(syncOpenState)
watch(isMobile, syncOpenState)

const accordionItems = [{ label: 'Suchen & filtern', value: 'filters', slot: 'filters' as const }]

const publisherApi = usePublisher()

const searchPublishers = (query: string, pageable: Pageable) =>
  publisherApi.fetchPaginated({ ...pageable, name: query })
</script>

<template>
  <UAccordion v-model="openItem" :items="accordionItems" :unmount-on-hide="false">
    <template #filters-body>
      <div class="flex flex-col gap-3">
        <UButton
          block
          variant="outline"
          icon="i-lucide-rotate-ccw"
          label="Alle Eingaben zurücksetzen"
          @click="emit('reset-filters')"
        />

        <UFormField label="Titel" name="title">
          <UInput v-model="filters.title" icon="i-lucide-search" placeholder="z. B. Die Weltbühne" class="w-full">
            <template v-if="filters.title" #trailing>
              <UButton color="neutral" variant="link" size="sm" icon="i-lucide-x" aria-label="Titel leeren"
                @click="filters.title = undefined" />
            </template>
          </UInput>
        </UFormField>

        <UFormField label="Untertitel" name="subtitle">
          <UInput v-model="filters.subtitle" placeholder="z. B. Wochenschrift" class="w-full" />
        </UFormField>

        <UFormField label="Verlag" name="publisher">
          <PaginatedSelect
            id="publisher"
            v-model="filters.publisher"
            :fetch-function="searchPublishers"
            :get-value="pb => pb.name"
            :get-label="pb => pb.name"
            sort="name,asc"
            placeholder="Verlag wählen"
          />
        </UFormField>

        <UFormField label="Redaktion" name="editorial-office">
          <UInput v-model="filters['editorial-office']" placeholder="z. B. Carl von Ossietzky" class="w-full" />
        </UFormField>

        <div class="grid grid-cols-2 gap-3">
          <UFormField label="Erscheinungsbeginn" name="start-year">
            <UInputNumber v-model.optional="filters['start-year']" :min="0" :format-options="{ useGrouping: false }"
              :increment="false" :decrement="false" placeholder="z. B. 1920" class="w-full" />
          </UFormField>
          <UFormField label="Erscheinungsende" name="end-year">
            <UInputNumber v-model.optional="filters['end-year']" :min="0" :format-options="{ useGrouping: false }"
              :increment="false" :decrement="false" placeholder="z. B. 1945" class="w-full" />
          </UFormField>
        </div>

        <div class="grid grid-cols-2 gap-3">
          <UFormField label="Anzahl Jahrgänge" name="amount-volumes">
            <UInputNumber v-model.optional="filters['amount-volumes']" :min="0" :format-options="{ useGrouping: false }"
              :increment="false" :decrement="false" class="w-full" />
          </UFormField>
          <UFormField label="Anzahl Ausgaben" name="amount-issues">
            <UInputNumber v-model.optional="filters['amount-issues']" :min="0" :format-options="{ useGrouping: false }"
              :increment="false" :decrement="false" class="w-full" />
          </UFormField>
        </div>
      </div>
    </template>
  </UAccordion>
</template>
