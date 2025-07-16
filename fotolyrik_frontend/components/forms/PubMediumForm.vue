<script setup lang="ts">
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import { getNode } from '@formkit/core';
import type { PubMedium } from "~/utils/types";

const props = defineProps<{
  action: 'create' | 'edit';
  header: string;
  pub_medium?: PubMedium;
}>();

const toast = useToast();
const submitted = ref(false);
const pub_media_store = usePubMediumStore();
const place_store = usePlaceStore();

type PubMediumInput = Omit<PubMedium, 'id' | 'created_by' | 'created_date' | 'last_modified_by' | 'last_modified_date'>;

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
            name="publication_places"
            label="Publikationsorte"
            outer-class="max-w-full"
            select-icon="select"
            :options="place_store.places.map(p => ({ label: `${p.name}`, value: p }))"
            help="Halten Sie die Strg-Taste gedrückt, um mehrere Personen auszuwählen"
        />
        <FormKit
            type="text"
            name="publisher"
            label="Herausgeber"
            placeholder="Girardet"
            prefix-icon="text"
            outer-class="max-w-full"
        />
        <FormKit
            type="select"
            name="pub_rhytm"
            label="Publikationsrhythmus"
            outer-class="max-w-full"
            select-icon="select"
            :options="[
                  {label: '', value: null},
                  {label: 'Wöchentlich', value: 'wöchentlich'},
                  {label: 'Halbmonatlich', value: 'halbmonatlich'},
                  {label: 'Monatlich', value: 'monatlich'},
                  {label: 'Halbjährlich', value: 'jährlich'},
                  {label: 'Jährlich', value: 'jährlich'}
              ]"
        />
        <div class="flex flex-row space-x-5">
          <FormKit
              type="text"
              name="start_year"
              label="Startjahr"
              placeholder="1924"
              prefix-icon="text"
              outer-class="max-w-full"
          />
          <FormKit
              type="text"
              name="end_year"
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
              name="amount_volumes"
              label="Anzahl Jahrgänge"
              placeholder="18"
              prefix-icon="number"
              outer-class="max-w-full"
          />
          <FormKit
              type="number"
              number
              name="amount_issues"
              label="Anzahl Ausgaben"
              placeholder="100"
              prefix-icon="number"
              outer-class="max-w-full"
          />
        </div>
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
