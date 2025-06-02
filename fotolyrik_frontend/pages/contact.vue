<script setup lang="ts">
import { ref } from "vue";
import apiClient from "~/service/api";
import { getNode } from '@formkit/core';
import type {ContactForm} from "~/utils/types";

const formData = {}

const toast = useToast();

const submitted = ref(false);

const submit = async (formData: ContactForm) => {
  try {
    await apiClient.post('/contact', formData)
    submitted.value = true;
    toast.add({severity: 'success', detail: 'Erfolgreich zugestellt', life: 3000});
    await new Promise((r) => setTimeout(r, 1000));
    const form = getNode('submitForm');
    if (form) {
      form.reset();
    }
  } catch (error) {
    console.log(error)
    toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Senden der Nachricht', life: 3000})
  }
};
</script>

<template>
  <div class="mb-2">
    <img src= "https://unsplash.it/1200/200" alt=""/>
  </div>
  <div class="columns-1 w-[40%] mx-auto">
    <div class="flex flex-col gap-5 place-content-center">
      <h1 class="mt-4 text-3xl outfit-headline font-bold">Kontakt</h1>
      <div>
        <p>Hier können Sie Ihre Nachricht an uns hinterlassen.
        Füllen Sie dafür bitte die unteren Felder aus
        und klicken Sie anschließend auf den Senden-Button.</p>
      </div>
      <FormKit type="form" id='submitForm' @submit="submit" submit-label="Senden"  #default ="{ value }" incomplete-message="Nicht alle Felder wurden ausfüllt.">
        <div class="flex flex-row gap-4">
          <FormKit
              type="text"
              name="name"
              id="name"
              label="Name*"
              placeholder="Name"
              validation="required"
              :validation-messages="{required: 'Bitte geben Sie einen Namen an.'}"
          />
          <FormKit
              type="email"
              name="email"
              label="Email*"
              placeholder="Email"
              validation="required"
              :validation-messages="{required: 'Bitte geben Sie eine gültige E-Mail-Adresse an.'}"
          />
        </div>
        <div class="flex flex-col">
          <FormKit
              outer-class="max-w-[30rem]"
              type="text"
              name="subject"
              id="subject"
              label="Betreff*"
              placeholder="Ihr Betreff"
              validation="required"
              :validation-messages="{required: 'Bitte geben Sie einen Betreff an.'}"
          />
        </div>
        <div class="flex flex-col">
          <FormKit
              outer-class="max-w-[30rem]"
              input-class="min-h-[10rem]"
              type="textarea"
              name="message"
              label="Nachricht*"
              placeholder="Ihre Nachricht"
              validation="required"
              :validation-messages="{required: 'Bitte geben Sie eine Nachricht ein.'}"
          />
          <FormKit
              type="checkbox"
              name="Datenschutzerklärung"
              decorator-icon="check"
              :value="false"
              validation="accepted"
              :validation-messages="{accepted: 'Datenschutzerklärung bitte bestätigen.'}"
          >
            <template #label="context">
              <span :class="context.classes.label">Ich habe die <NuxtLink to="/data-protection" class="text-[#0073C9] font-bold">Datenschutzerklärung</NuxtLink> zur Kenntnis genommen.</span>
            </template>
          </FormKit>
        </div>
      </FormKit>
    </div>
  </div>
</template>

<style scoped>

</style>
