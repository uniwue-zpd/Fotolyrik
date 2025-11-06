<script setup lang="ts">
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import { getNode } from '@formkit/core';
import type { PubMedium } from "~/utils/types";
import {useRefreshStoreData} from "~/composables/RefreshStores";

const props = defineProps<{
  action: 'create' | 'edit';
  header: string;
  pub_medium?: PubMedium;
}>();

const toast = useToast();
const submitted = ref(false);
const pub_media_store = usePubMediumStore();
const place_store = usePlaceStore();
const pub_rhythm_store = usePubRhythmStore();
const publisher_store = usePublisherStore();
const data_refreshing = ref(false);

type PubMediumInput = Omit<PubMedium, 'id' | 'createdBy' | 'createdDate' | 'lastModifiedBy' | 'lastModifiedDate'>;

async function handleRefresh() {
  data_refreshing.value = true;
  try {
    await useRefreshStoreData();
    toast.add({severity: 'success', summary: 'Erfolg', detail: 'Datenbankdaten erfolgreich aktualisiert', life: 2000});
  } catch (err) {
    toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Aktualisieren der Datenbankdaten', life: 2000});
  } finally {
    data_refreshing.value = false;
  }
}

const submit = async (formData: Partial<PubMediumInput>) => {
  try {
    if (props.action === 'create') {
      await pub_media_store.createPubMedium(formData);
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich erstellt', life: 3000});
      const form = getNode('pub_medium_creation');
      form?.reset();
    } else if (props.action === 'edit' && props.pub_medium?.id) {
      await pub_media_store.updatePubMedium(formData, props.pub_medium.id)
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich upgedated', life: 3000});
      navigateTo(`/publication_media/${props.pub_medium?.id}`);
    }
  } catch (error) {
    console.log(error)
    toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Erstellen des Publikationsmedium-Objektes', life: 3000});
  }
};
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-2xl outfit-headline text-[#063D79] font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um ein Objekt zu erstellen oder anzupassen
    </p>
    <FormKit
        type="form"
        id="pub_medium_creation"
        :form-class="submitted ? 'hide' : 'show'"
        submit-label="Erstellen"
        @submit="submit"
        :actions="false"
        :value="props.pub_medium ? props.pub_medium : {}"
        :key="props.pub_medium?.id || 'create'"
        #default="{ value }"
    >
      <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-[#F1F2F2]">
        <div>
          <Button :disabled="data_refreshing" label="Daten aktualisieren" @click="handleRefresh">
            <div class="flex flex-row space-x-3 items-center">
              <div class="roboto-plain font-semibold">Datenbankdaten aktualisieren</div>
              <i v-show="data_refreshing" :class="['pi', data_refreshing ? 'pi-spin pi-spinner' : 'pi-spinner']"/>
            </div>
          </Button>
        </div>
        <div class="flex flex-row space-x-5">
          <FormKit
              type="text"
              name="title"
              label="Titel"
              placeholder="Hamburger Anzeiger"
              prefix-icon="text"
              outer-class="max-w-full"
          />
          <FormKit
              type="text"
              name="subtitle"
              label="Untertitel"
              placeholder="	Illustrierte Wochenbeilage in Tiefdruck"
              prefix-icon="text"
              outer-class="max-w-full"
          />
        </div>
        <FormKit
            type="select"
            multiple
            name="publicationPlaces"
            label="Publikationsorte"
            outer-class="max-w-full"
            select-icon="select"
            :options="place_store.places.map(p => ({ label: `${p.name}`, value: {id: p.id, name: p.name} }))"
            help="Halten Sie die Strg-Taste gedrückt, um mehrere Personen auszuwählen"
        />
        <FormKit
            type="select"
            name="publisher"
            label="Verlag"
            outer-class="max-w-full"
            select-icon="select"
            :options="[{label: 'Keine Auswahl', value: null},
            ...publisher_store.publishers.map(p => ({ label: `${p.name}`, value: { id: p.id, name: p.name } })) as any
            ]"
        />
        <FormKit
            type="select"
            multiple
            name="pubRhythms"
            label="Publikationsrhythmen"
            outer-class="max-w-full"
            select-icon="select"
            :options="pub_rhythm_store.publication_rhythms.map(p => ({ label: `${p.value}`, value: { id: p.id, value: p.value } }))"
        />
        <div class="flex flex-row space-x-5">
          <FormKit
              type="text"
              name="startYear"
              label="Startjahr"
              placeholder="1924"
              prefix-icon="text"
              outer-class="max-w-full"
          />
          <FormKit
              type="text"
              name="endYear"
              label="Endjahr"
              placeholder="1938"
              prefix-icon="text"
              outer-class="max-w-full"
          />
        </div>
        <div class="flex flex-row space-x-5">
          <FormKit
              type="number"
              number
              name="amountVolumes"
              label="Anzahl Jahrgänge"
              placeholder="18"
              prefix-icon="number"
              outer-class="max-w-full"
          />
          <FormKit
              type="number"
              number
              name="amountIssues"
              label="Anzahl Ausgaben"
              placeholder="100"
              prefix-icon="number"
              outer-class="max-w-full"
          />
        </div>
        <FormKit
            type="text"
            name="zdbId"
            label="ZDB-ID"
            placeholder="2650224-0"
            prefix-icon="text"
            outer-class="max-w-full"
        />
        <FormKit
            type="text"
            name="notes"
            label="Notizen"
            placeholder="Untertitel stimmt hier nicht?"
            prefix-icon="text"
            outer-class="max-w-full"
        />
        <div class="border-solid border-2 rounded-md p-5 bg-[#F1F2F5] mb-2">
          <div class="font-mono">JSON-Preview</div>
          <hr>
          <pre wrap>{{ value }}</pre>
        </div>
        <FormKit
            type="submit"
            :label="props.action === 'create' ? 'Erstellen' : 'Aktualisieren'"
        />
      </div>
    </FormKit>
  </div>
</template>

<style scoped>

</style>
