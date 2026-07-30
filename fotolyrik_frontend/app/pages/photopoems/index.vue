n d<script setup lang="ts">
const photopoemStore = usePhotopoemStore();

const initialPageParameter: PhotopoemPageable = {
  page: 0,
  size: 15,
  sort: 'title,asc'
};

const initialFilters: PhotopoemPageable ={
  title: undefined,
  subtitle: undefined,
  'alt-title': undefined,
  series: undefined,
  volume: undefined,
  issue: undefined,
  'publication-date': undefined,
  'pub-medium-id': undefined,
  'pub-place-id': undefined,
  'location-id': undefined,
  'author-id': undefined,
  'photographer-id': undefined,
  'depicted-person-id': undefined,
  'contributor-id': undefined,
  'theme-id': undefined,
  'image-motif-id': undefined,
  'copyright-image-id': undefined,
  'copyright-text-id': undefined,
  'language-id': undefined
};

const pageParameter = reactive<PhotopoemPageable> ({...initialPageParameter});
const filters = reactive<PhotopoemPageable>({...initialFilters});
const resetFilter = () => {
  Object.assign(pageParameter, initialPageParameter);
  Object.assign(filters, initialFilters);
};

const sortOptions = ref([
  { label: 'Aufsteigend (A-Z)', value: 'title,asc' },
  { label: 'Absteigend (Z-A)', value: 'title,desc' }
]);

const pageOptions = computed(() =>
    Array.from({ length: photopoems.value?.totalPages ?? 0 }, (_, index) => ({
      label: `${ index + 1 }`,
      value: index
    }))
);

const { data: photopoems, pending: isLoading, error: hasError, refresh } = useAsyncData<Page<PhotoPoemDTO>>(
    'photopoems-paginated',
    () => photopoemStore.fetchPhotopoemsPaginated({...pageParameter, ...filters})
);
const titleSuggestions = computed<string[]>(() => {
  const items = photopoems.value?.content ?? []
  return items.map(item => item.title || '')
})

const debouncedRefresh = debounce(() => {
  if (pageParameter.page == 0){
    refresh();
  }else{
    pageParameter.page = 0; // this will trigger refresh on the other watcher
  }
}, 300);

watch(
    filters,
    () => {
      debouncedRefresh();
    },
    { deep: true }
);

watch(
    pageParameter,
    () => {
      refresh();
    },
    { deep: true }
);

useHead(() => ({
  title: 'Fotogedichte - Sammlung'
}));

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
  <div class="flex flex-col gap-4">
    <h1 class="text-3xl font-bold outfit-headline text-primary">Fotogedichte</h1>
    <div class="flex flex-row justify-end">
      <Select
          :options="sortOptions"
          v-model="pageParameter.sort"
          optionLabel="label"
          optionValue="value"
          class="h-9 items-center"
      />
    </div> <div class="flex flex-col gap-5 md:flex-row justify-between">
      <div class="md:w-1/5">
        <div class="flex flex-col gap-2">
          <button
              type="button"
              class="h-9 rounded-md border px-3 py-2 text-sm text-primary cursor-pointer shadow-sm hover:shadow-md"
              @click="resetFilter"
          >
            <i class="pi pi-refresh mr-2"/>
            Alle Eingaben zurücksetzen
          </button>
          <h3 class="text-lg font-semibold border-b border-gray-200">
            Filtern nach:
          </h3>
          <InputText
              id="title"
              placeholder="Titel"
              v-model="filters.title"
              fluid
          />

          <InputText
              id="subtitle"
              placeholder="Untertitel"
              v-model="filters.subtitle"
              fluid
          />

          <InputText
              id="alt-title"
              placeholder="Alternativer Titel"
              v-model="filters['alt-title']"
              fluid
          />

          <InputText
              id="series"
              placeholder="Reihe"
              v-model="filters.series"
              fluid
          />

          <InputNumber
              id="volume"
              placeholder="Jahrgang"
              v-model="filters.volume"
              :min="0"
              :useGrouping="false"
              fluid
          />

          <InputNumber
              id="issue"
              placeholder="Ausgabe"
              v-model="filters.issue"
              :min="0"
              :useGrouping="false"
              fluid
          />

          <InputText
              id="publication-date"
              placeholder="Publikationsdatum"
              v-model="filters['publication-date']"
              fluid
          />

          <Select
              id="pub-medium-id"
              placeholder="Publikationsmedium"
              v-model="filters['pub-medium-id']"
              optionLabel="title"
              optionValue="id"
              :options="publicationMedia"
              filter
              showClear
              fluid
          />

          <Select
              id="location-id"
              placeholder="Fundort auswählen"
              v-model="filters['location-id']"
              optionLabel="name"
              optionValue="id"
              :options="locations"
              filter
              showClear
              fluid
          />

          <Select
              id="author-id"
              placeholder="Autor:in auswählen"
              v-model="filters['author-id']"
              :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
              optionValue="id"
              :options="persons"
              @click="()=>console.log(filters['author-id'])"
              filter
              showClear
              fluid
          />

          <Select
              id="photographer-id"
              placeholder="Fotograf:in auswählen"
              v-model="filters['photographer-id']"
              :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
              optionValue="id"
              :options="persons"
              filter
              showClear
              fluid
          />

          <Select
              inputId="depicted-person-id"
              placeholder="Abgebildete Person auswählen"
              v-model="filters['depicted-person-id']"
              :suggestions="persons"
              :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms[0] || opt.studioName)"
              optionValue="id"
              showClear
              filter
              fluid
          />

          <Select
              id="contributor-id"
              placeholder="Sonstige Mitwirkende auswählen"
              v-model="filters['contributor-id']"
              :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
              optionValue="id"
              :options="persons"
              filter
              showClear
              fluid
          />

          <Select
              id="theme-id"
              placeholder="Thematik auswählen"
              v-model="filters['theme-id']"
              optionLabel="value"
              optionValue="id"
              :options="keywords"
              filter
              showClear
              fluid
          />

          <Select
              id="image-motif-id"
              placeholder="Bildmotiv auswählen"
              v-model="filters['image-motif-id']"
              optionLabel="value"
              optionValue="id"
              :options="keywords"
              filter
              showClear
              fluid
          />

          <Select
              id="copyright-image-id"
              placeholder="Bild Copyright-Status auswählen"
              v-model="filters['copyright-image-id']"
              optionLabel="value"
              optionValue="id"
              :options="copyrightStatuses"
              showClear
              fluid
          />

          <Select
              id="copyright-text-id"
              placeholder="Text Copyright-Status auswählen"
              v-model="filters['copyright-text-id']"
              optionLabel="value"
              optionValue="id"
              :options="copyrightStatuses"
              showClear
              fluid
          />

          <Select
              id="language-id"
              placeholder="Sprache auswählen"
              v-model="filters['language-id']"
              optionLabel="name"
              optionValue="id"
              :options="languages"
              filter
              showClear
              fluid
          />
        </div>
      </div>
      <div class="md:w-3/4">
        <div class="flex flex-col gap-2">
          <div v-if="isLoading" class="flex flex-col gap-2 items-center">
            <ProgressSpinner/>
            <div class="roboto-plain text-primary font-semibold text-lg">Inhalte werden geladen</div>
          </div>
          <div v-else-if="photopoems" class="flex flex-col gap-2 justify-between min-h-[70vh]">
            <div class="flex flex-col gap-2">
              <div v-for="photopoem in photopoems.content" :key="photopoem.id" class="border-2 border-primary rounded-md p-2 shadow-md">
                <div class="flex flex-col gap-1">
                  <NuxtLink
                      :to="`/photopoems/${photopoem.id}`"
                      class="text-lg group relative w-fit outfit-headline font-semibold text-primary"
                  >
                    {{ photopoem.title || photopoem.altTitle || 'Unbenanntes Fotogedicht' }}
                    <span class="absolute bottom-0 left-0 h-px w-0 bg-current transition-all duration-300 group-hover:w-full"/>
                  </NuxtLink>
                  <div v-if="photopoem.publicationDate" class="flex flex-row gap-2">
                    <span class="text-sm roboto-plain">Erschienen: </span>
                    <div class="text-sm text-primary outfit-headline font-medium">
                      {{ photopoem.publicationDate }}
                    </div>
                  </div>
                  <div v-if="photopoem.publicationMedium" class="flex flex-row gap-2">
                    <span class="text-sm roboto-plain">In: </span>
                    <NuxtLink
                        :to="`/publication_media/${ photopoem.publicationMedium.id }`"
                        class="text-sm text-primary outfit-headline font-medium"
                    >
                      {{ photopoem.publicationMedium.title }}
                    </NuxtLink>
                  </div>
                </div>
              </div>
            </div>
            <div class="flex items-center justify-center gap-3">
              <button
                  type="button"
                  class="px-2 h-9 rounded-md border border-primary text-primary hover:bg-primary hover:text-white transition disabled:opacity-40 disabled:cursor-not-allowed"
                  :disabled="pageParameter.page === 0"
                  aria-label="Vorherige Seite"
                  @click="pageParameter.page!--"
              >
                <i class="pi pi-chevron-left"/>
              </button>
              <Select
                  v-model="pageParameter.page"
                  :options="pageOptions"
                  optionLabel="label"
                  optionValue="value"
                  :disabled="photopoems?.totalPages <= 1"
                  class="h-9 items-center"
              />
              <button
                  type="button"
                  class="px-2 h-9 rounded-md border border-primary text-primary hover:bg-primary hover:text-white transition disabled:opacity-40 disabled:cursor-not-allowed"
                  :disabled="pageParameter.page! >= (photopoems?.totalPages ?? 1) - 1"
                  aria-label="Nächste Seite"
                  @click="pageParameter.page!++"
              >
                <i class="pi pi-chevron-right"/>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
