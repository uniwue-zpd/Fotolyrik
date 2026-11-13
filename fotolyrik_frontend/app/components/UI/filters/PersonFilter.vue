<script setup lang="ts">
import { useBreakpoints, breakpointsTailwind } from '@vueuse/core'

const props = defineProps<{
  filters: PersonPageable
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

const accordionItems = [{ label: 'Filtern nach:', value: 'filters', slot: 'filters' as const }]

const sexItems = [
  { label: 'Alle', value: 'all' },
  { label: 'Männlich', value: 'männlich' },
  { label: 'Weiblich', value: 'weiblich' }
]

// RadioGroup needs a non-null value for "Alle"; the filter itself uses null.
const sex = computed({
  get: () => props.filters.sex ?? 'all',
  set: (value: string) => {
    props.filters.sex = value === 'all' ? null : value as 'männlich' | 'weiblich'
  }
})
</script>

<template>
  <UAccordion v-model="openItem" :items="accordionItems">
    <template #filters-body>
      <div class="flex flex-col gap-3">
        <UFormField label="Name" name="name">
          <UInput
            v-model="filters.name"
            icon="i-lucide-search"
            placeholder="Name oder Pseudonym"
            class="w-full"
          >
            <template v-if="filters.name" #trailing>
              <UButton
                color="neutral"
                variant="link"
                size="sm"
                icon="i-lucide-x"
                aria-label="Name leeren"
                @click="filters.name = undefined"
              />
            </template>
          </UInput>
        </UFormField>

        <div class="grid grid-cols-2 gap-3">
          <UFormField label="Geburtsjahr" name="birth-year">
            <UInputNumber
              v-model="filters['birth-year']"
              :format-options="{ useGrouping: false }"
              :increment="false"
              :decrement="false"
              placeholder="z. B. 1890"
              class="w-full"
            />
          </UFormField>
          <UFormField label="Sterbejahr" name="death-year">
            <UInputNumber
              v-model="filters['death-year']"
              :format-options="{ useGrouping: false }"
              :increment="false"
              :decrement="false"
              placeholder="z. B. 1935"
              class="w-full"
            />
          </UFormField>
        </div>

        <UFormField label="Geburtsjahr von / bis" name="birth-year-range">
          <div class="grid grid-cols-2 gap-3">
            <UInputNumber
              v-model="filters['birth-year-from']"
              :format-options="{ useGrouping: false }"
              :increment="false"
              :decrement="false"
              placeholder="von"
              aria-label="Geburtsjahr von"
              class="w-full"
            />
            <UInputNumber
              v-model="filters['birth-year-to']"
              :format-options="{ useGrouping: false }"
              :increment="false"
              :decrement="false"
              placeholder="bis"
              aria-label="Geburtsjahr bis"
              class="w-full"
            />
          </div>
        </UFormField>

        <UFormField label="Sterbejahr von / bis" name="death-year-range">
          <div class="grid grid-cols-2 gap-3">
            <UInputNumber
              v-model="filters['death-year-from']"
              :format-options="{ useGrouping: false }"
              :increment="false"
              :decrement="false"
              placeholder="von"
              aria-label="Sterbejahr von"
              class="w-full"
            />
            <UInputNumber
              v-model="filters['death-year-to']"
              :format-options="{ useGrouping: false }"
              :increment="false"
              :decrement="false"
              placeholder="bis"
              aria-label="Sterbejahr bis"
              class="w-full"
            />
          </div>
        </UFormField>

        <UFormField label="Geschlecht" name="sex">
          <URadioGroup v-model="sex" :items="sexItems" orientation="horizontal" />
        </UFormField>

        <UButton
          block
          variant="outline"
          icon="i-lucide-rotate-ccw"
          label="Alle Eingaben zurücksetzen"
          @click="emit('reset-filters')"
        />
      </div>
    </template>
  </UAccordion>
</template>
