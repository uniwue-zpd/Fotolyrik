<script setup lang="ts">
import { useBreakpoints, breakpointsTailwind } from '@vueuse/core'

defineProps<{
  filters: KeywordPageable
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
</script>

<template>
  <UAccordion v-model="openItem" :items="accordionItems">
    <template #filters-body>
      <div class="flex flex-col gap-3">
        <UButton
          block
          variant="outline"
          icon="i-lucide-rotate-ccw"
          label="Alle Eingaben zurücksetzen"
          @click="emit('reset-filters')"
        />

        <UFormField label="Schlagwort" name="value">
          <UInput
            v-model="filters.value"
            icon="i-lucide-search"
            placeholder="z. B. Krieg"
            class="w-full"
          >
            <template v-if="filters.value" #trailing>
              <UButton
                color="neutral"
                variant="link"
                size="sm"
                icon="i-lucide-x"
                aria-label="Schlagwort leeren"
                @click="filters.value = undefined"
              />
            </template>
          </UInput>
        </UFormField>

        <UFormField label="GND-ID" name="gnd-id">
          <UInput
            v-model="filters['gnd-id']"
            placeholder="z. B. 4033114-3"
            class="w-full"
          >
            <template #leading>
              <img src="/gnd-logo.png" alt="GND" class="h-4 w-auto" />
            </template>
            <template v-if="filters['gnd-id']" #trailing>
              <UButton
                color="neutral"
                variant="link"
                size="sm"
                icon="i-lucide-x"
                aria-label="GND-ID leeren"
                @click="filters['gnd-id'] = undefined"
              />
            </template>
          </UInput>
        </UFormField>
      </div>
    </template>
  </UAccordion>
</template>
