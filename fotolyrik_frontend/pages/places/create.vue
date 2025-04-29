<script setup lang="ts">
import type { Place } from "~/utils/types";
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import apiClient from "~/service/api";
import { navigateTo } from "#app";

const toast = useToast();

type PlaceInput = Omit<Place, 'id' | 'created_by' | 'created_date' | 'last_modified_by' | 'last_modified_date'>;
const submitted = ref(false);

const submit = async (formData: Partial<PlaceInput>) => {
  try {
    const response = await apiClient.post('/places', formData)
    submitted.value = true;
    toast.add({severity: 'success', detail: 'Erfolgreich erstellt', life: 3000})
    navigateTo('/places');
  } catch (error) {
    console.log(error)
    toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Erstellen des Ortes', life: 3000})
  }
};
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-2xl outfit-headline font-bold">Neuen Ort erstellen</h1>
    <p class="roboto-plain">Füllen Sie bitte die untenstehenden Felder aus, um einen Ort zu erstellen</p>
    <FormKit
        type="form"
        id="place_creation"
        :form-class="submitted ? 'hide' : 'show'"
        submit-label="Erstellen"
        @submit="submit"
        #default="{ value }"
        :actions="false"
    >
      <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-[#F1F2F2]">
        <FormKit
            type="text"
            name="name"
            label="Ortsname"
            placeholder="Berlin"
            validation="required"
            prefix-icon="text"
            outer-class="max-w-full"
        />
        <FormKit
            type="textarea"
            name="description"
            label="Beschreibung"
            placeholder="Hauptstadt Deutschlands"
            prefix-icon="textarea"
            outer-class="max-w-full min-w-[0%]"
        />
        <div class="flex flex-row space-x-5">
          <FormKit
              type="number"
              name="latitude"
              label="Breitengrad"
              placeholder="52.5162"
              prefix-icon="number"
              outer-class="max-w-full"
          />
          <FormKit
              type="number"
              name="longitude"
              label="Längengrad"
              placeholder="13.3777"
              prefix-icon="number"
              outer-class="max-w-full"
          />
        </div>
        <div class="border-solid border-2 rounded-md p-5 bg-[#F1F2F5] mb-2">
          <div class="font-mono">JSON-Preview</div>
          <hr>
          <pre>{{ value }}</pre>
        </div>
        <FormKit
            type="submit"
            label="Erstellen"
        />
      </div>
    </FormKit>
  </div>
</template>

<style scoped>

</style>
