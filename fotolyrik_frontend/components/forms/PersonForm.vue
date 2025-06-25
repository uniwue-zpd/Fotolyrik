<script setup lang="ts">
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import { getNode } from '@formkit/core';
import type { Person } from "~/utils/types";

const props = defineProps<{
  action: 'create' | 'edit';
  header: string;
  person?: Person;
}>();

const toast = useToast();
const submitted = ref(false);
const store = usePersonStore();

type PersonInput = Omit<Person, 'id' | 'created_by' | 'created_date' | 'last_modified_by' | 'last_modified_date'>;

const submit = async (formData: Partial<PersonInput>) => {
  try {
    if (props.action === 'create') {
      await store.createPerson(formData);
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich erstellt', life: 3000});
      const form = getNode('person_creation');
      form?.reset();
    } else if (props.action === 'edit' && props.person?.id) {
      await store.updatePerson(formData, props.person.id)
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich upgedated', life: 3000});
      navigateTo(`/persons/${props.person?.id}`);
    }
  } catch (error) {
    console.log(error)
    toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Erstellen des Person-Objektes', life: 3000});
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
        id="person_creation"
        :form-class="submitted ? 'hide' : 'show'"
        submit-label="Erstellen"
        @submit="submit"
        :actions="false"
        :value="props.person ? props.person : {}"
        :key="props.person?.id || 'create'"
        #default="{ value }"
    >
      <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-[#F1F2F2]">
        <div class="flex flex-row space-x-5">
          <FormKit
              type="text"
              name="first_name"
              label="Vorname"
              placeholder="Johann Wolfgang"
              prefix-icon="text"
              outer-class="max-w-full"
          />
          <FormKit
              type="text"
              name="last_name"
              label="Nachname"
              placeholder="von Goethe"
              prefix-icon="text"
              outer-class="max-w-full"
          />
        </div>
        <FormKit
            type="text"
            name="pseudonym"
            label="Pseudonym"
            placeholder="Filippo Möller"
            prefix-icon="text"
            outer-class="max-w-full"
        />
        <div class="flex flex-row space-x-5">
          <FormKit
              type="number"
              :number="true"
              name="birth_year"
              label="Geburtsjahr"
              placeholder="1749"
              prefix-icon="date"
              outer-class="max-w-full"
          />
          <FormKit
              type="number"
              :number="true"
              name="death_year"
              label="Sterbejahr"
              placeholder="1832"
              prefix-icon="date"
              outer-class="max-w-full"
          />
        </div>
        <FormKit
            type="select"
            name="sex"
            label="Geschlecht"
            prefix-icon="people"
            outer-class="max-w-full"
            :options="[
                { label: 'Unbekannt', value: null},
                { label: 'Weiblich', value: 'weiblich'},
                { label: 'Männlich', value: 'männlich'}
            ]"
        />
        <FormKit
            type="text"
            name="gnd_id"
            label="GND-ID"
            placeholder="118540238"
            prefix-icon="number"
            outer-class="max-w-full"
        />
        <div class="border-solid border-2 rounded-md p-5 bg-[#F1F2F5] mb-2">
          <div class="font-mono">JSON-Preview</div>
          <hr>
          <pre wrap>{{ value }}</pre>
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
