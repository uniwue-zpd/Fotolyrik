<script setup lang="ts">

import { useBreakpoints, breakpointsTailwind } from '@vueuse/core'
import PaginatedSelect from "~/components/UI/filters/PaginatedSelect.vue";

const breakpoints = useBreakpoints(breakpointsTailwind)
const isMobile = breakpoints.smaller('lg')
const activeTab = ref<string | undefined>(undefined)
watchEffect(() => {
  activeTab.value = isMobile.value ? undefined : "0"
})

const props = defineProps<{
  filters: PhotopoemPageable
}>()

const emit = defineEmits<{
  (e: 'reset-filters'): void
}>()



const personApi = usePerson();
const searchPersons = (name: string, pageable: Pageable) => personApi.fetchPaginated({ ...pageable, name });
const pubMediumApi = usePubMedium();
const searchPubMedia = (query: string, pageable: Pageable) => pubMediumApi.fetchPaginated({ ...pageable, query });
const languageApi = useLanguage();
const copyrightStatusApi = useCopyrightStatus();
const keywordApi = useKeyword();
const searchKeywords = (value: string, pageable: Pageable) => keywordApi.fetchPaginated({ ...pageable, value });
const locationApi = useLocation();

const {data: languageList} = await languageApi.getAll();
const {data: locationList} = await locationApi.getAll();
const {data: copyrightStatusList} = await copyrightStatusApi.getAll();

const languages = computed(() => languageList.value?.map((l:LanguageDTO) => ({ id: l.id, name: l.name, isoDesignation: l.isoDesignation })));
const locations = computed(()=> locationList.value?.map(l=>({id: l.id, name: l.name}) ));
const copyrightStatuses = computed(() => copyrightStatusList.value?.map(cs => ({ id: cs.id, value: cs.value })));


function getPersonOptionLabel(opt: PersonPreviewDTO) {
  if (!opt) return '';
  return [opt.firstName, opt.lastName].filter(Boolean).join(' ') || (opt.pseudonyms || []).join(', ');
}


</script>
<template>
  <UAccordion v-model="activeTab" :items="[{ label: 'Filtern nach:', value: '0' }]" :unmount-on-hide="false">
    <template #body>
        <div class="flex flex-col gap-3">
          <div class="grid grid-cols-1">
            <label for="title" class="text-xs font-bold ">Titel</label>
            <UInput
                id="title"
                placeholder="z. B. Telephon-Tragödie"
                v-model="filters.title"
                class="w-full"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="subtitle" class="text-xs font-bold ">Untertitel</label>
            <UInput
                id="subtitle"
                placeholder="z. B. Aschermittwoch"
                v-model="filters.subtitle"
                class="w-full"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="alt-title" class="text-xs font-bold ">Alternativer Titel</label>
            <UInput
                id="alt-title"
                placeholder="z. B. Frohsinn und Freude"
                v-model="filters['alt-title']"
                class="w-full"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="series" class="text-xs font-bold ">Reihe</label>
            <UInput
                id="series"
                v-model="filters.series"
                class="w-full"
            />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="flex flex-col gap-1">
              <label for="volume" class="text-xs font-bold ">Jahrgang</label>
              <UInputNumber
                  id="volume"
                  placeholder="z. B. 12"
                  v-model.optional="filters.volume"
                  :min="0"
                  :format-options="{ useGrouping: false }"
                  :increment="false"
                  :decrement="false"
                  class="w-full"
              />
            </div>

            <div class="flex flex-col gap-1">
              <label for="issue" class="text-xs font-bold ">Ausgabe</label>
              <UInputNumber
                  id="issue"
                  placeholder="z. B. 3"
                  v-model.optional="filters.issue"
                  :min="0"
                  :format-options="{ useGrouping: false }"
                  :increment="false"
                  :decrement="false"
                  class="w-full"
              />
            </div>
          </div>

          <div class="grid grid-cols-1">
            <label for="publication-date" class="text-xs font-bold ">Publikationsdatum</label>
            <UInput
                id="publication-date"
                placeholder="z. B. 01.08.1936"
                v-model="filters['publication-date']"
                class="w-full"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="pub-medium-id" class="text-xs font-bold ">Publikationsmedium</label>
            <PaginatedSelect
                :fetch-function="searchPubMedia"
                :get-value="pm => pm.id"
                :get-label="pm => pm.title"
                sort="title,asc"
                id="pub-medium-id"
                placeholder="Medium wählen"
                v-model="filters['pubmedium-id']"
                />
          </div>


          <div class="grid grid-cols-1">
            <label for="location-id" class="text-xs font-bold ">Fundort</label>
            <USelectMenu
                id="location-id"
                placeholder="Ort wählen"
                :model-value="filters['location-id']"
                @update:model-value="filters['location-id'] = $event ?? undefined"
                value-key="id"
                label-key="name"
                :items="locations"
                clear
                class="w-full"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="author-id" class="text-xs font-bold ">Autor:in</label>
            <PaginatedSelect
                :fetch-function="searchPersons"
                :get-value="p => p.id"
                :get-label="getPersonOptionLabel"
                sort="firstName,asc"
                id="author-id"
                placeholder="Autor:in suchen / wählen"
                v-model="filters['author-id']"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="photographer-id" class="text-xs font-bold ">Fotograf:in</label>
            <PaginatedSelect
                :fetch-function="searchPersons"
                :get-value="p => p.id"
                :get-label="getPersonOptionLabel"
                sort="firstName,asc"
                id="photographer-id"
                placeholder="Fotograf:in suchen / wählen"
                v-model="filters['photographer-id']"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="depicted-person-id" class="text-xs font-bold ">Abgebildete Person</label>
            <PaginatedSelect
                :fetch-function="searchPersons"
                :get-value="p => p.id"
                :get-label="getPersonOptionLabel"
                sort="firstName,asc"
                id="depicted-person-id"
                placeholder="Abgebildete Person wählen"
                v-model="filters['depicted-person-id']"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="contributor-id" class="text-xs font-bold ">Sonstige Mitwirkende</label>
            <PaginatedSelect
                :fetch-function="searchPersons"
                :get-value="p => p.id"
                :get-label="getPersonOptionLabel"
                sort="firstName,asc"
                id="contributor-id"
                placeholder="Mitwirkende Person wählen"
                v-model="filters['other-contributor-id']"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="theme-id" class="text-xs font-bold ">Thematik</label>
            <PaginatedSelect
                :fetch-function="searchKeywords"
                :get-value="k => k.id"
                :get-label="k => k.value"
                sort="value,asc"
                id="theme-id"
                placeholder="Thema wählen"
                v-model="filters['theme-id']"
            />
          </div>

          <div class="grid grid-cols-1">
            <label for="image-motif-id" class="text-xs font-bold ">Bildmotiv</label>
            <PaginatedSelect
                :fetch-function="searchKeywords"
                :get-value="k => k.id"
                :get-label="k => k.value"
                sort="value,asc"
                id="image-motif-id"
                v-model="filters['image-motif-id']"
                placeholder="Motiv wählen"
            />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="flex flex-col gap-1">
              <label for="copyright-image-id" class="text-xs font-bold ">Copyright Bild</label>
              <USelectMenu
                  :search-input="false"
                  id="copyright-image-id"
                  placeholder="Status"
                  v-model="filters['copyright-status-image-id']"
                  optionLabel="value"
                  optionValue="id"
                  :options="copyrightStatuses"
                  showClear
                  fluid
              />
            </div>

            <div class="flex flex-col gap-1">
              <label for="copyright-text-id" class="text-xs font-bold ">Copyright Text</label>
              <USelectMenu
                  :search-input="false"
                  id="copyright-text-id"
                  placeholder="Status"
                  v-model="filters['copyright-status-text-id']"
                  optionLabel="value"
                  optionValue="id"
                  :options="copyrightStatuses"
                  showClear
                  fluid
              />
            </div>
          </div>

          <div class="grid grid-cols-1">
            <label for="language-id" class="text-xs font-bold ">Sprache</label>
            <USelectMenu
                id="language-id"
                placeholder="Sprache wählen"
                :model-value="filters['language-id']"
                @update:model-value="filters['language-id'] = $event ?? undefined"
                label-key="name"
                value-key="id"
                :items="languages"
                clear
                class="w-full"
            />
          </div>
          <UButton type="button" variant="outline" icon="i-lucide-rotate-cw" class="w-full justify-center"
              @click="emit('reset-filters')">Alle Eingaben zurücksetzen</UButton>
        </div>
    </template>
  </UAccordion>

</template>