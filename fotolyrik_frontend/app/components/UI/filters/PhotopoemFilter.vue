<script setup lang="ts">
import { useBreakpoints, breakpointsTailwind } from '@vueuse/core'
import PaginatedSelect from "~/components/UI/filters/PaginatedSelect.vue";

defineProps<{
  filters: PhotopoemPageable
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

const personApi = usePerson();
const searchPersons = (name: string, pageable: Pageable) => personApi.fetchPaginated({ ...pageable, name });
const pubMediumApi = usePubMedium();
const searchPubMedia = (query: string, pageable: Pageable) => pubMediumApi.fetchPaginated({ ...pageable, query });
const keywordApi = useKeyword();
const searchKeywords = (value: string, pageable: Pageable) => keywordApi.fetchPaginated({ ...pageable, value });
const languageApi = useLanguage();
const locationApi = useLocation();
const copyrightStatusApi = useCopyrightStatus();

const { data: languageList } = await languageApi.getAll();
const { data: locationList } = await locationApi.getAll();
const { data: copyrightStatusList } = await copyrightStatusApi.getAll();

const languages = computed(() => languageList.value?.map((l: LanguageDTO) => ({ id: l.id, name: l.name })) ?? []);
const locations = computed(() => locationList.value?.map(l => ({ id: l.id, name: l.name })) ?? []);
const copyrightStatuses = computed(() => copyrightStatusList.value?.map(cs => ({ id: cs.id, value: cs.value })) ?? []);

function getPersonOptionLabel(opt: PersonDTO) {
  if (!opt) return '';
  return [opt.firstName, opt.lastName].filter(Boolean).join(' ') || (opt.pseudonyms || []).join(', ');
}
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
          <UInput v-model="filters.title" icon="i-lucide-search" placeholder="z. B. Telephon-Tragödie" class="w-full">
            <template v-if="filters.title" #trailing>
              <UButton color="neutral" variant="link" size="sm" icon="i-lucide-x" aria-label="Titel leeren"
                @click="filters.title = undefined" />
            </template>
          </UInput>
        </UFormField>

        <UFormField label="Untertitel" name="subtitle">
          <UInput v-model="filters.subtitle" placeholder="z. B. Aschermittwoch" class="w-full" />
        </UFormField>

        <UFormField label="Alternativer Titel" name="alt-title">
          <UInput v-model="filters['alt-title']" placeholder="z. B. Frohsinn und Freude" class="w-full" />
        </UFormField>

        <UFormField label="Reihe" name="series">
          <UInput v-model="filters.series" class="w-full" />
        </UFormField>

        <div class="grid grid-cols-2 gap-3">
          <UFormField label="Jahrgang" name="volume">
            <UInputNumber v-model.optional="filters.volume" :min="0" :format-options="{ useGrouping: false }"
              :increment="false" :decrement="false" placeholder="z. B. 12" class="w-full" />
          </UFormField>
          <UFormField label="Ausgabe" name="issue">
            <UInputNumber v-model.optional="filters.issue" :min="0" :format-options="{ useGrouping: false }"
              :increment="false" :decrement="false" placeholder="z. B. 3" class="w-full" />
          </UFormField>
        </div>

        <UFormField label="Publikationsdatum" name="publication-date">
          <UInput v-model="filters['publication-date']" placeholder="z. B. 01.08.1936" class="w-full" />
        </UFormField>

        <UFormField label="Publikationsmedium" name="pubmedium-id">
          <PaginatedSelect
            id="pubmedium-id"
            v-model="filters['pubmedium-id']"
            :fetch-function="searchPubMedia"
            :get-value="pm => pm.id"
            :get-label="pm => pm.title"
            sort="title,asc"
            placeholder="Medium wählen"
          />
        </UFormField>

        <UFormField label="Fundort" name="location-id">
          <USelectMenu
            :model-value="filters['location-id']"
            :items="locations"
            value-key="id"
            label-key="name"
            placeholder="Ort wählen"
            clear
            class="w-full"
            @update:model-value="filters['location-id'] = $event ?? undefined"
          />
        </UFormField>

        <UFormField label="Autor:in" name="author-id">
          <PaginatedSelect
            id="author-id"
            v-model="filters['author-id']"
            :fetch-function="searchPersons"
            :get-value="p => p.id"
            :get-label="getPersonOptionLabel"
            sort="firstName,asc"
            placeholder="Autor:in suchen / wählen"
          />
        </UFormField>

        <UFormField label="Fotograf:in" name="photographer-id">
          <PaginatedSelect
            id="photographer-id"
            v-model="filters['photographer-id']"
            :fetch-function="searchPersons"
            :get-value="p => p.id"
            :get-label="getPersonOptionLabel"
            sort="firstName,asc"
            placeholder="Fotograf:in suchen / wählen"
          />
        </UFormField>

        <UFormField label="Abgebildete Person" name="depicted-person-id">
          <PaginatedSelect
            id="depicted-person-id"
            v-model="filters['depicted-person-id']"
            :fetch-function="searchPersons"
            :get-value="p => p.id"
            :get-label="getPersonOptionLabel"
            sort="firstName,asc"
            placeholder="Abgebildete Person wählen"
          />
        </UFormField>

        <UFormField label="Sonstige Mitwirkende" name="other-contributor-id">
          <PaginatedSelect
            id="other-contributor-id"
            v-model="filters['other-contributor-id']"
            :fetch-function="searchPersons"
            :get-value="p => p.id"
            :get-label="getPersonOptionLabel"
            sort="firstName,asc"
            placeholder="Mitwirkende Person wählen"
          />
        </UFormField>

        <UFormField label="Thematik" name="theme-id">
          <PaginatedSelect
            id="theme-id"
            v-model="filters['theme-id']"
            :fetch-function="searchKeywords"
            :get-value="k => k.id"
            :get-label="k => k.value"
            sort="value,asc"
            placeholder="Thema wählen"
          />
        </UFormField>

        <UFormField label="Bildmotiv" name="image-motif-id">
          <PaginatedSelect
            id="image-motif-id"
            v-model="filters['image-motif-id']"
            :fetch-function="searchKeywords"
            :get-value="k => k.id"
            :get-label="k => k.value"
            sort="value,asc"
            placeholder="Motiv wählen"
          />
        </UFormField>

        <div class="grid grid-cols-2 gap-3">
          <UFormField label="Copyright Bild" name="copyright-status-image-id">
            <USelectMenu
              :model-value="filters['copyright-status-image-id']"
              :items="copyrightStatuses"
              value-key="id"
              label-key="value"
              :search-input="false"
              placeholder="Status"
              clear
              class="w-full"
              @update:model-value="filters['copyright-status-image-id'] = $event ?? undefined"
            />
          </UFormField>
          <UFormField label="Copyright Text" name="copyright-status-text-id">
            <USelectMenu
              :model-value="filters['copyright-status-text-id']"
              :items="copyrightStatuses"
              value-key="id"
              label-key="value"
              :search-input="false"
              placeholder="Status"
              clear
              class="w-full"
              @update:model-value="filters['copyright-status-text-id'] = $event ?? undefined"
            />
          </UFormField>
        </div>

        <UFormField label="Sprache" name="language-id">
          <USelectMenu
            :model-value="filters['language-id']"
            :items="languages"
            value-key="id"
            label-key="name"
            placeholder="Sprache wählen"
            clear
            class="w-full"
            @update:model-value="filters['language-id'] = $event ?? undefined"
          />
        </UFormField>
      </div>
    </template>
  </UAccordion>
</template>
