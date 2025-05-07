<script setup lang="ts">
// import { ref } from "vue";

// const input = ref<string>("");
// function send(input_body: string) {
//  input.value = input_body;
//  console.log(input.value);

import type {ContactForm} from "~/utils/types";

const formData = {
}

const submit = async (formData: Partial<ContactForm>) => {
  try {
    const response = await apiClient.post('/contact', formData)  // wird automatisch in JSON angegeben ?
    submitted.value = true;
    toast.add({severity: 'success', detail: 'Erfolgreich erstellt', life: 3000})
    navigateTo('/contact');
  } catch (error) {
    console.log(error)
    toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Erstellen des Ortes', life: 3000})
  }
};
</script>

<template>
  <div class="mb-2">
    <img src= "https://unsplash.it/1200/200" alt=""/>
  </div>
  <div class="grid place-content-center gap-2">
    <h1 class="text-3xl outfit-headline font-bold">Kontakt</h1>
    <div>
      <p>Hier können Sie Ihre Nachricht an uns hinterlassen.</p>
      <p>Füllen Sie bitte dafür die untenstehenden Felder aus</p>
      <p>und klicken Sie anschließend auf den Senden-Button.</p>
    </div>
    <FormKit type="form" @submit="send" submit-label="Senden" #default ="{ value }" incomplete-message="Nicht alle Felder wurden ausfüllt.">
      <div class="flex flex-row gap-4">
        <FormKit
            type="text"
            name="name"
            id="name"
            label="Name"
            placeholder="Name"
            validation="required"
            :validation-messages="{required: 'Bitte geben Sie einen Namen an.'}"
        />
        <FormKit
            type="email"
            label="Email"
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
            label="Betreff"
            placeholder="Ihr Betreff"
            validation="required"
            :validation-messages="{required: 'Bitte geben Sie einen Betreff an.'}"
        />
      </div>
      <div class="flex flex-col">
        <FormKit
            outer-class="max-w-[30rem]"
            type="textarea"
            auto-height
            name="message"
            label="Nachricht"
            placeholder="Ihre Nachricht"
            validation="required"
            :validation-messages="{required: 'Bitte geben Sie eine Nachricht ein.'}"
        />
        <FormKit
            type="checkbox"
            label="Ich habe die Datenschutzerklärung zur Kenntnis genommen."
            name="Datenschutzerklärung"
            decorator-icon="check"
            :value="false"
            validation="accepted"
            :validation-messages="{accepted: 'Datenschutzerklärung bitte bestätigen.'}"
        />
      </div>
    </FormKit>
  </div>
</template>

<style scoped>

</style>
