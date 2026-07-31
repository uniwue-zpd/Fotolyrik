<script setup lang="ts">

import { useBreakpoints, breakpointsTailwind } from '@vueuse/core'

const breakpoints = useBreakpoints(breakpointsTailwind)
const isMobile = breakpoints.smaller('lg')
const activeTab = ref<string|null>(null)
watchEffect(() => {
  activeTab.value = isMobile.value ? null : "0"
})

defineProps<{
  filters: PhotopoemPageable
}>()

const emit = defineEmits<{
  (e: 'reset-filters'): void
}>()



const personStore = usePersonStore();
const pubMediumStore = usePubMediumStore();
const languageStore = useLanguageStore();
const copyrightStatusStore = useCopyrightStatusStore();
const keywordStore = useKeywordStore();
const locationStore = useLocationStore();

const persons = computed(() => personStore.persons.map(p => ({ id: p.id, fullName: p.fullName, studioName: p.studioName, pseudonyms: p.pseudonyms })));
const keywords = computed(() => keywordStore.keywords.map((k: KeywordDTO) => ({ id: k.id, value: k.value })));
const languages = computed(() => languageStore.languages.map((l:LanguageDTO) => ({ id: l.id, name: l.name, isoDesignation: l.isoDesignation })));
const publicationMedia = computed(() => pubMediumStore.pub_media.map(pm => ({ id: pm.id, title: pm.title })));
const locations = computed(()=> locationStore.locations.map(l=>({id: l.id, name: l.name}) ));
const copyrightStatuses = computed(() => copyrightStatusStore.copyrightStatuses.map(cs => ({ id: cs.id, value: cs.value })));

</script>

<template>
  <Accordion  :value="activeTab">
    <AccordionPanel value="0">
      <AccordionHeader class="text-lg font-semibold border-b border-gray-200">
        Filtern nach:
      </AccordionHeader>
      <AccordionContent class="flex flex-col gap-2" >
        <div class="flex flex-col gap-3">
          <div class="flex flex-col gap-1">
            <label for="title" class="text-xs font-bold ">Titel</label>
            <InputText
                id="title"
                placeholder="z. B. Telephon-Tragödie"
                v-model="filters.title"
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="subtitle" class="text-xs font-bold ">Untertitel</label>
            <InputText
                id="subtitle"
                placeholder="z. B. Aschermittwoch"
                v-model="filters.subtitle"
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="alt-title" class="text-xs font-bold ">Alternativer Titel</label>
            <InputText
                id="alt-title"
                placeholder="z. B. Frohsinn und Freude"
                v-model="filters['alt-title']"
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="series" class="text-xs font-bold ">Reihe</label>
            <InputText
                id="series"
                v-model="filters.series"
                fluid
            />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="flex flex-col gap-1">
              <label for="volume" class="text-xs font-bold ">Jahrgang</label>
              <InputNumber
                  id="volume"
                  placeholder="z. B. 12"
                  v-model="filters.volume"
                  :min="0"
                  :useGrouping="false"
                  fluid
              />
            </div>

            <div class="flex flex-col gap-1">
              <label for="issue" class="text-xs font-bold ">Ausgabe</label>
              <InputNumber
                  id="issue"
                  placeholder="z. B. 3"
                  v-model="filters.issue"
                  :min="0"
                  :useGrouping="false"
                  fluid
              />
            </div>
          </div>

          <div class="flex flex-col gap-1">
            <label for="publication-date" class="text-xs font-bold ">Publikationsdatum</label>
            <InputText
                id="publication-date"
                placeholder="z. B. 01.08.1936"
                v-model="filters['publication-date']"
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="pub-medium-id" class="text-xs font-bold ">Publikationsmedium</label>
            <Select
                id="pub-medium-id"
                placeholder="Medium wählen"
                v-model="filters['pub-medium-id']"
                optionLabel="title"
                optionValue="id"
                :options="publicationMedia"
                filter
                showClear
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="location-id" class="text-xs font-bold ">Fundort</label>
            <Select
                id="location-id"
                placeholder="Ort wählen"
                v-model="filters['location-id']"
                optionLabel="name"
                optionValue="id"
                :options="locations"
                filter
                showClear
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="author-id" class="text-xs font-bold ">Autor:in</label>
            <Select
                id="author-id"
                placeholder="Autor:in suchen / wählen"
                v-model="filters['author-id']"
                :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
                optionValue="id"
                :options="persons"
                @click="()=>console.log(filters['author-id'])"
                filter
                showClear
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="photographer-id" class="text-xs font-bold ">Fotograf:in</label>
            <Select
                id="photographer-id"
                placeholder="Fotograf:in suchen / wählen"
                v-model="filters['photographer-id']"
                :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
                optionValue="id"
                :options="persons"
                filter
                showClear
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="depicted-person-id" class="text-xs font-bold ">Abgebildete Person</label>
            <Select
                id="depicted-person-id"
                placeholder="Abgebildete Person wählen"
                v-model="filters['depicted-person-id']"
                :options="persons"
                :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms[0] || opt.studioName)"
                optionValue="id"
                showClear
                filter
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="contributor-id" class="text-xs font-bold ">Sonstige Mitwirkende</label>
            <Select
                id="contributor-id"
                placeholder="Mitwirkende Person wählen"
                v-model="filters['contributor-id']"
                :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
                optionValue="id"
                :options="persons"
                filter
                showClear
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="theme-id" class="text-xs font-bold ">Thematik</label>
            <Select
                id="theme-id"
                placeholder="Thema wählen"
                v-model="filters['theme-id']"
                optionLabel="value"
                optionValue="id"
                :options="keywords"
                filter
                showClear
                fluid
            />
          </div>

          <div class="flex flex-col gap-1">
            <label for="image-motif-id" class="text-xs font-bold ">Bildmotiv</label>
            <Select
                id="image-motif-id"
                placeholder="Motiv wählen"
                v-model="filters['image-motif-id']"
                optionLabel="value"
                optionValue="id"
                :options="keywords"
                filter
                showClear
                fluid
            />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="flex flex-col gap-1">
              <label for="copyright-image-id" class="text-xs font-bold ">Copyright Bild</label>
              <Select
                  id="copyright-image-id"
                  placeholder="Status"
                  v-model="filters['copyright-image-id']"
                  optionLabel="value"
                  optionValue="id"
                  :options="copyrightStatuses"
                  showClear
                  fluid
              />
            </div>

            <div class="flex flex-col gap-1">
              <label for="copyright-text-id" class="text-xs font-bold ">Copyright Text</label>
              <Select
                  id="copyright-text-id"
                  placeholder="Status"
                  v-model="filters['copyright-text-id']"
                  optionLabel="value"
                  optionValue="id"
                  :options="copyrightStatuses"
                  showClear
                  fluid
              />
            </div>
          </div>

          <div class="flex flex-col gap-1">
            <label for="language-id" class="text-xs font-bold ">Sprache</label>
            <Select
                id="language-id"
                placeholder="Sprache wählen"
                v-model="filters['language-id']"
                optionLabel="name"
                optionValue="id"
                :options="languages"
                filter
                showClear
                fluid
            />
          </div>
          <button
              type="button"
              class="flex items-center justify-center w-full min-h-9 rounded-md border px-3 py-2 text-sm text-primary cursor-pointer shadow-sm hover:shadow-md"
              @click="emit('reset-filters')"
          >
            <i class="pi pi-refresh mr-2" />
            <span class="text-center">Alle Eingaben zurücksetzen</span>
          </button>
        </div>
      </AccordionContent>
    </AccordionPanel>
  </Accordion>

</template>