<script setup lang="ts">
import apiClient from "~/service/api";
import { ref } from "vue";
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";

const toast = useToast();

const appellation = ref([
  { key: 'Herr', value: 'Herr' },
  { key: 'Frau', value: 'Frau' },
  { key: 'Keine Angabe', value: null }
])

const resolver = ref(zodResolver(
  z.object({
    appellation: z.any(),
    name: z.string("Bitte geben Sie einen Namen an."),
    email: z.email("Bitte geben Sie eine gültige Email-Adresse an."),
    subject: z.string("Bitte geben Sie einen Betreff an."),
    message: z.string("Bitte geben Sie eine Nachricht an."),
    dataProtection: z.boolean("Datenschutzerklärung bitte bestätigen.")
  })
));

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    console.log(e.values)
    try {
      await apiClient.post('/contact', e.values)
      toast.add({severity: 'success', detail: 'Erfolgreich zugestellt', life: 3000});
      e.reset()
    } catch (error) {
      console.log(error)
      toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Senden der Nachricht', life: 3000})
    }
  }
};
</script>

<template>
  <div class="mb-2">
    <img src= "https://unsplash.it/1200/200" alt=""/>
  </div>
  <div class="columns-1 w-[40%] mx-auto">
    <div class="flex flex-col gap-5 place-content-center">
      <h1 class="mt-4 text-3xl outfit-headline font-bold text-[#063D79]">Kontakt</h1>
      <div>
        <p>Hier können Sie Ihre Nachricht an uns hinterlassen.
        Füllen Sie dafür bitte die unteren Felder aus
        und klicken Sie anschließend auf den Senden-Button.</p>
      </div>
      <Form v-slot="$form" :resolver @submit="onFormSubmit" class="flex flex-col gap-4 w-full">
        <div class="flex gap-4 w-full">
          <FormField v-slot="$field" name="appellation" class="flex flex-col gap-1 w-45">
            <label for="appellation" class="font-bold">Anrede</label>
            <Select labelId="appellation" :options="appellation " optionLabel="key" optionValue="value" placeholder="Anrede" fluid/>
          </FormField>
          <FormField v-slot="$field" name="name" class="flex flex-col gap-1 flex-auto">
            <label for="name" class="font-bold">Name*</label>
            <InputText id="name" type="text" placeholder="Name"/>
            <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">{{ $form.name.error.message }}</Message>
          </FormField>
        </div>
        <FormField v-slot="$field" name="email" class="flex flex-col gap-1">
          <label for="email" class="font-bold">Email*</label>
          <InputText id="email" type="text" placeholder="Email"/>
          <Message v-if="$form.email?.invalid" severity="error" size="small" variant="simple">{{ $form.email.error.message }}</Message>
        </FormField>
        <FormField v-slot="$field" name="subject" class="flex flex-col gap-1">
          <label for="subject" class="font-bold">Betreff*</label>
          <InputText id="subject" type="text" placeholder="Ihr Betreff"/>
          <Message v-if="$form.subject?.invalid" severity="error" size="small" variant="simple">{{ $form.subject.error.message }}</Message>
        </FormField>
        <FormField v-slot="$field" name="message" class="flex flex-col gap-1">
          <label for="message" class="font-bold">Nachricht*</label>
          <Textarea id="message" type="text" placeholder="Ihre Nachricht"/>
          <Message v-if="$form.message?.invalid" severity="error" size="small" variant="simple">{{ $form.message.error.message }}</Message>
        </FormField>
        <FormField v-slot="$field" name="data_protection" class="flex flex-col gap-1">
          <div class="flex gap-4 mt-2">
            <Checkbox inputId="dp" name="dataProtection" class="pt-0.5" binary/>
            <label for="dp">Ich habe die <NuxtLink to="/data-protection" class="text-[#004188] font-bold">Datenschutzerklärung</NuxtLink> zur Kenntnis genommen.*</label>
          </div>
          <Message v-if="$form.dataProtection?.invalid" severity="error" size="small" variant="simple">{{ $form.dataProtection.error.message }}</Message>
        </FormField>
        <Button type="submit" severity="primary" label="Senden"/>
      </Form>
    </div>
  </div>
</template>

<style scoped>

</style>
