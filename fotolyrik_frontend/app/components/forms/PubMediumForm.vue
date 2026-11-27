<script setup lang="ts">
import { z } from "zod";

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  pub_medium?: PubMediumDTO;
}>();

// Related objects (match PlacePreviewDTO, PublisherPreviewDTO, PubRhythmPreviewDTO)
const placePreview = z.object({ id: z.number(), name: z.string() });
const publisherPreview = z.object({ id: z.number(), name: z.string().nullable() });
const pubRhythmPreview = z.object({ id: z.number(), value: z.string().nullable() });

const schema = z.object({
  title: z.string().min(1, "Bitte geben Sie einen Titel an."),
  subtitle: z.string().nullish(),
  publicationPlaces: z.array(placePreview),
  publisher: publisherPreview.nullish(),
  pubRhythms: z.array(pubRhythmPreview),
  editorialOffice: z.string().nullish(),
  startYear: z.string().regex(/^\d{4}$/, "Bitte ein vierstelliges Jahr angeben.").nullish(),
  endYear: z.string().regex(/^\d{4}$/, "Bitte ein vierstelliges Jahr angeben.").nullish(),
  amountVolumes: z.number().int().nonnegative().nullish(),
  amountIssues: z.number().int().nonnegative().nullish(),
  zdbId: z.string().nullish(),
  notes: z.string().nullish(),
}).superRefine((d, ctx) => {
  if (d.startYear && d.endYear && Number(d.startYear) > Number(d.endYear)) {
    ctx.addIssue({ code: "custom", message: "Das Endjahr darf nicht vor dem Startjahr liegen.", path: ["endYear"] });
  }
});

type Schema = z.output<typeof schema>;

const defaults = (): Partial<Schema> => ({
  title: '',
  subtitle: null,
  publicationPlaces: [],
  publisher: null,
  pubRhythms: [],
  editorialOffice: null,
  startYear: null,
  endYear: null,
  amountVolumes: null,
  amountIssues: null,
  zdbId: null,
  notes: null,
});

const state = reactive<Partial<PubMediumDTO>>({
  ...defaults(),
  ...structuredClone(toRaw(props.pub_medium)),
});

const toast = useToast();
const pubMediumApi = usePubMedium();
const placeApi = usePlace();
const publisherApi= usePublisher();
const pubRhythmApi = usePubRhythm();

const { data: pubRhythms } = await pubRhythmApi.getAll();
const publication_rhythms = computed(() => pubRhythms.value?.map(pr => ({ id: pr.id, value: pr.value })));

// Publication places: all places without a search term, server-side search with one (GET /api/places?view=flat&name=…)
const placeSearchTerm = ref('');
const placeSearchTermDebounced = ref('');
watch(placeSearchTerm, debounce((term: string) => (placeSearchTermDebounced.value = term), 300));

const { data: placeItems, status: placeStatus, execute: loadPlaces } = useLazyAsyncData(
  'pub-medium-place-search',
  () => placeApi.fetchFlat({ name: placeSearchTermDebounced.value.trim() || undefined }),
  { watch: [placeSearchTermDebounced], immediate: false, default: () => [] }
);

// Load the full list only when the menu is opened for the first time
function onPlacesOpen(open: boolean) {
  if (open && !placeItems.value.length) loadPlaces();
}

// Publisher: all publishers without a search term, server-side search with one (GET /api/publishers?view=flat&name=…)
const publisherSearchTerm = ref('');
const publisherSearchTermDebounced = ref('');
watch(publisherSearchTerm, debounce((term: string) => (publisherSearchTermDebounced.value = term), 300));

const { data: publisherItems, status: publisherStatus, execute: loadPublishers } = useLazyAsyncData(
  'pub-medium-publisher-search',
  () => publisherApi.fetchFlat({ name: publisherSearchTermDebounced.value.trim() || undefined }),
  { watch: [publisherSearchTermDebounced], immediate: false, default: () => [] }
);

// Load the full list only when the menu is opened for the first time
function onPublishersOpen(open: boolean) {
  if (open && !publisherItems.value.length) loadPublishers();
}

const onSubmit = async () => {
  try {
    if (props.action === "create") {
      await pubMediumApi.create(state);
      await refreshNuxtData("pubMedium-list");
      toast.add({ color: "success", title: "Erfolg", description: "Erfolgreich erstellt", duration: 3000 });
      await navigateTo("/publication_media");
    } else if (props.action === "edit" && props.pub_medium?.id) {
      await pubMediumApi.update(props.pub_medium.id, state);
      await Promise.all([refreshNuxtData("pubMedium-list"), refreshNuxtData(`pubMedium-${props.pub_medium.id}`)]);
      toast.add({ color: "success", title: "Erfolg", description: "Erfolgreich aktualisiert", duration: 3000 });
      await navigateTo(`/publication_media/${props.pub_medium.id}`);
    }
  } catch (error) {
    console.error(error);
    toast.add({ color: "error", title: "Fehler", description: "Ein Fehler ist aufgetreten", duration: 3000 });
  }
};

</script>

<template>
  <div class="flex flex-col md:mx-auto md:w-[70%] gap-4">
    <h1 class="text-2xl outfit-headline text-primary font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um ein Publikationsmedium zu erstellen oder anzupassen.
    </p>
    <div class="border-2 border-solid rounded-md p-5">
      <UForm :schema="schema" :state="state" @submit="onSubmit">
        <div class="flex flex-col gap-4">
          <div class="flex flex-col gap-4 md:flex-row flex-wrap">
            <UFormField label="Titel" name="title" class="flex-1">
              <UInput v-model.nullable="state.title" placeholder="Augsburger Allgemeine" class="w-full" :ui="{ trailing: 'pe-1' }">
                <template v-if="state.title" #trailing>
                  <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.title = ''"/>
                </template>
              </UInput>
            </UFormField>
            <UFormField label="Untertitel" name="subtitle" class="flex-1">
              <UInput v-model.nullable="state.subtitle" placeholder="Das Wochenblatt" class="w-full" :ui="{ trailing: 'pe-1' }">
                <template v-if="state.subtitle" #trailing>
                  <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.subtitle = null"/>
                </template>
              </UInput>
            </UFormField>
          </div>
          <UFormField label="Publikationsorte" name="publicationPlaces">
            <div class="flex gap-2">
            <UInputMenu
                v-model="state.publicationPlaces"
                v-model:search-term="placeSearchTerm"
                :items="placeItems"
                :loading="placeStatus === 'pending'"
                label-key="name"
                by="id"
                multiple
                virtualize
                ignore-filter
                icon="i-lucide-map-pin"
                placeholder="Publikationsorte suchen"
                class="w-full"
                @update:open="onPlacesOpen"
            />
              <UButton icon="i-lucide-plus" color="neutral" variant="outline" to="/places/create" target="_blank" aria-label="Neuen Ort anlegen" />
            </div>
          </UFormField>
          <div class="flex flex-col gap-4 md:flex-row flex-wrap">
            <UFormField label="Verlag" name="publisher" class="flex-1">
              <div class="flex gap-2">
                <UInputMenu
                    v-model="state.publisher"
                    v-model:search-term="publisherSearchTerm"
                    :items="publisherItems"
                    :loading="publisherStatus === 'pending'"
                    label-key="name"
                    by="id"
                    virtualize
                    ignore-filter
                    icon="i-lucide-book-open"
                    placeholder="Verlag suchen"
                    clear
                    class="flex-1"
                    @update:open="onPublishersOpen"
                />
                <UButton icon="i-lucide-plus" color="neutral" variant="outline" to="/publishers/create" target="_blank" aria-label="Neuen Verlag anlegen" />
              </div>
            </UFormField>
            <UFormField label="Publikationsrhythmen" name="pubRhythms" class="flex-1">
              <USelectMenu
                  v-model="state.pubRhythms"
                  :items="publication_rhythms ?? []"
                  label-key="value"
                  by="id"
                  multiple
                  icon="i-lucide-calendar-sync"
                  placeholder="Rhythmen auswählen"
                  class="w-full"
              />
            </UFormField>
          </div>
          <UFormField label="Schriftleitung" name="editorialOffice">
            <UInput v-model.nullable="state.editorialOffice" class="w-full" :ui="{ trailing: 'pe-1' }">
              <template v-if="state.editorialOffice" #trailing>
                <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.editorialOffice = null"/>
              </template>
            </UInput>
          </UFormField>
          <div class="flex flex-col gap-4 md:flex-row flex-wrap">
            <UFormField label="Startjahr" name="startYear" class="flex-1">
              <UInput v-model.nullable="state.startYear" inputmode="numeric" maxlength="4" icon="i-lucide-calendar" placeholder="1924" class="w-full" :ui="{ trailing: 'pe-1' }">
                <template v-if="state.startYear" #trailing>
                  <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.startYear = null"/>
                </template>
              </UInput>
            </UFormField>
            <UFormField label="Endjahr" name="endYear" class="flex-1">
              <UInput v-model.nullable="state.endYear" inputmode="numeric" maxlength="4" icon="i-lucide-calendar" placeholder="1938" class="w-full" :ui="{ trailing: 'pe-1' }">
                <template v-if="state.endYear" #trailing>
                  <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.endYear = null"/>
                </template>
              </UInput>
            </UFormField>
          </div>
          <div class="flex flex-col gap-4 md:flex-row flex-wrap">
            <UFormField label="Anzahl Jahrgänge" name="amountVolumes" class="flex-1">
              <div class="flex gap-2">
                <UInputNumber
                    v-model="state.amountVolumes"
                    :increment="false"
                    :decrement="false"
                    :min="0"
                    :format-options="{ useGrouping: false }"
                    placeholder="18"
                    class="flex-1"
                />
                <UButton icon="i-lucide-circle-x" color="neutral" variant="ghost" aria-label="Eingabe löschen" @click="state.amountVolumes = null" />
              </div>
            </UFormField>
            <UFormField label="Anzahl Ausgaben" name="amountIssues" class="flex-1">
              <div class="flex gap-2">
                <UInputNumber
                    v-model="state.amountIssues"
                    :increment="false"
                    :decrement="false"
                    :min="0"
                    :format-options="{ useGrouping: false }"
                    placeholder="100"
                    class="flex-1"
                />
                <UButton icon="i-lucide-circle-x" color="neutral" variant="ghost" aria-label="Eingabe löschen" @click="state.amountIssues = null" />
              </div>
            </UFormField>
          </div>
          <UFormField label="ZDB-ID" name="zdbId">
            <UInput v-model.nullable="state.zdbId" placeholder="2650224-0" class="w-full" :ui="{ trailing: 'pe-1' }">
              <template v-if="state.zdbId" #trailing>
                <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.zdbId = null"/>
              </template>
            </UInput>
          </UFormField>
          <UFormField label="Notizen" name="notes">
            <UTextarea v-model.nullable="state.notes" autoresize :rows="3" class="w-full" />
          </UFormField>
          <UButton type="submit" :label="props.action === 'create' ? 'Erstellen' : 'Bearbeiten'" class="self-start" />
        </div>
      </UForm>
    </div>
  </div>
</template>

<style scoped>

</style>
