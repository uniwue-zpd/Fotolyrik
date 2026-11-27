<script setup lang="ts">
import { z } from "zod";

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  keyword?: KeywordDTO;
}>();

const schema = z.object({
  value: z.string().min(1, "Bitte geben Sie einen Begriff an."),
  gndId: z.string().regex(/^(\d{7}-[\dX]|\d{9}[\dX])$/, "Ungültiges GND-ID-Format, z. B. 4056723-0").nullish(),
});

type Schema = z.output<typeof schema>;

const defaults = (): Partial<Schema> => ({
  value: '',
  gndId: null,
});

const state = reactive<Partial<KeywordDTO>>({
  ...defaults(),
  ...structuredClone(toRaw(props.keyword)),
});

const toast = useToast();
const keywordApi = useKeyword();

// GND suggestions: server-side lookup via GET /api/keywords/gnd-suggestions?term=…
const gndSearchTerm = ref('');
const gndSearchTermDebounced = ref('');
watch(gndSearchTerm, debounce((term: string) => (gndSearchTermDebounced.value = term), 300));

const { data: gndItems, status: gndStatus } = useLazyAsyncData(
  'keyword-gnd-suggestions',
  () => gndSearchTermDebounced.value.trim()
      ? keywordApi.getGNDSuggestions(gndSearchTermDebounced.value)
      : Promise.resolve([]),
  { watch: [gndSearchTermDebounced], default: () => [] }
);

const gndSelection = ref<GndSuggestionDTO | null>(null);
const clearGndSearch = () => {
  gndSelection.value = null;
  gndSearchTerm.value = '';
  gndSearchTermDebounced.value = '';
  gndItems.value = [];
}

// Prefill term and GND ID from the selected suggestion; both fields stay editable
const applySuggestion = (suggestion?: GndSuggestionDTO | null) => {
  if (!suggestion) return;
  state.value = suggestion.preferredName;
  state.gndId = suggestion.gndId;
  nextTick(clearGndSearch);
}

const onSubmit = async () => {
  try {
    if (props.action === "create") {
      await keywordApi.create(state);
      await refreshNuxtData("keyword-list");
      toast.add({ color: "success", title: "Erfolg", description: "Erfolgreich erstellt", duration: 3000 });
      Object.assign(state, defaults());
    } else if (props.action === "edit" && props.keyword?.id) {
      await keywordApi.update(props.keyword.id, state);
      await Promise.all([refreshNuxtData("keyword-list"), refreshNuxtData(`keyword-${props.keyword.id}`)]);
      toast.add({ color: "success", title: "Erfolg", description: "Erfolgreich aktualisiert", duration: 3000 });
      await navigateTo(`/keywords/${props.keyword.id}`);
    }
  } catch (error: any) {
    console.error(error);
    const description = error?.statusCode === 409
        ? `Das Schlagwort "${state.value}" existiert bereits.`
        : "Ein Fehler ist aufgetreten";
    toast.add({ color: "error", title: "Fehler", description, duration: 3000 });
  }
};
</script>

<template>
  <div class="flex flex-col md:mx-auto md:w-[70%] gap-4">
    <h1 class="text-2xl outfit-headline text-primary font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um ein Schlagwort zu erstellen oder anzupassen.
    </p>
    <div class="border-2 border-solid rounded-md p-5">
      <UForm :schema="schema" :state="state" @submit="onSubmit">
        <div class="flex flex-col gap-4">
          <UFormField
              label="Aus der GND übernehmen"
              description="Vorschlag auswählen, um Begriff und GND-ID vorauszufüllen – beide Felder bleiben editierbar."
          >
            <UInputMenu
                v-model.nullable="gndSelection"
                v-model:search-term="gndSearchTerm"
                :items="gndItems"
                :loading="gndStatus === 'pending'"
                label-key="preferredName"
                ignore-filter
                icon="i-lucide-library"
                placeholder="Begriff in der GND suchen"
                class="w-full"
                :ui="{ trailing: 'pe-1' }"
                @update:model-value="applySuggestion"
            >
              <template v-if="gndSearchTerm" #trailing>
                <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Suche löschen" @click="clearGndSearch" />
              </template>
              <template #item="{ item }">
                <div class="flex w-full items-center justify-between gap-2">
                  <div class="flex flex-col">
                    <span>{{ item.preferredName }}</span>
                    <span class="flex items-center gap-1 text-xs text-muted">
                      <img src="/gnd-logo.png" alt="GND" class="h-3.5 w-auto opacity-80" />
                      {{ item.gndId }}
                    </span>
                  </div>
                  <UBadge v-if="item.exactMatch" label="exakt" color="success" variant="subtle" size="sm" />
                </div>
              </template>
              <template #empty>
                {{ gndSearchTerm.trim() ? 'Keine GND-Sachbegriffe gefunden' : 'Suchbegriff eingeben' }}
              </template>
            </UInputMenu>
          </UFormField>
          <USeparator label="Schlagwort" />
          <div class="flex flex-col gap-4 md:flex-row">
            <UFormField label="Begriff" name="value" required class="flex-1">
              <UInput v-model="state.value" icon="i-lucide-tag" placeholder="Alter" class="w-full" :ui="{ trailing: 'pe-1' }">
                <template v-if="state.value" #trailing>
                  <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.value = ''" />
                </template>
              </UInput>
            </UFormField>
            <UFormField label="GND-ID" name="gndId" class="flex-1">
              <UInput v-model.nullable="state.gndId" placeholder="4001446-0" class="w-full" :ui="{ trailing: 'pe-1' }">
                <template #leading>
                  <img src="/gnd-logo.png" alt="GND" class="h-3.5 w-auto opacity-80" />
                </template>
                <template v-if="state.gndId" #trailing>
                  <UButton color="neutral" variant="link" size="sm" icon="i-lucide-external-link" :to="`https://d-nb.info/gnd/${state.gndId}`" target="_blank" aria-label="In der GND öffnen" />
                  <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.gndId = null" />
                </template>
              </UInput>
            </UFormField>
          </div>
          <UButton type="submit" :label="props.action === 'create' ? 'Erstellen' : 'Ändern'" class="self-start" />
        </div>
      </UForm>
    </div>
  </div>
</template>
