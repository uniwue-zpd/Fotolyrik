<script setup lang="ts">
import { useBreakpoints, breakpointsTailwind } from '@vueuse/core'

defineProps<{
  filters: PlacePageable
}>()

const emit = defineEmits<{
  (e: 'reset-filters'): void
}>()

const isMobile = useBreakpoints(breakpointsTailwind).smaller('lg')
const openItem = ref<string | undefined>(undefined)
const syncOpenState = () => {
  openItem.value = isMobile.value ? undefined : 'filters'
}
onMounted(syncOpenState)
watch(isMobile, syncOpenState)

const accordionItems = [{ label: 'Suchen & filtern:', value: 'filters', slot: 'filters' as const }]
</script>

<template>
  <UAccordion v-model="openItem" :items="accordionItems">
    <template #filters-body>
      <div class="flex flex-col gap-3">
        <UFormField label="Name" name="name">
          <UInput
            v-model="filters.name"
            icon="i-lucide-search"
            placeholder="Ortsname"
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
