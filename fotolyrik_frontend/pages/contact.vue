<script setup lang="ts">
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";
import apiClient from "~/service/api";

const toast = useToast();

const appellation = ref([
  { label: "Herr", value: "Herr" },
  { label: "Frau", value: "Frau" },
  { label: "Keine Angabe", value: null }
]);

const resolver = ref(
  zodResolver(
    z.object({
      appellation: z.string().optional().nullable(),
      name: z.string("Bitte geben Sie einen Namen an."),
      email: z.email("Bitte geben Sie eine gültige Email-Adresse an."),
      subject: z.string("Bitte geben Sie einen Betreff an."),
      message: z.string("Bitte geben Sie eine Nachricht an."),
      dataProtection: z.boolean("Bitte bestätigen Sie die Datenschutzerklärung.")
    })
  )
);

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    console.log(e.values)
    try {
      await apiClient.post("/contact", e.values)
      toast.add({severity: "success", detail: "Erfolgreich zugestellt", life: 3000});
      e.reset()
    } catch (error) {
      console.log(error)
      toast.add({severity: "error", summary: "Fehler", detail: "Fehler beim Senden der Nachricht", life: 3000})
    }
  }
};
</script>

<template>
  <div class="mb-2">
    <img src= "https://unsplash.it/1200/200" alt=""/>
  </div>
  <div class="flex flex-col mx-auto md:w-[80%] gap-4">
    <h1 class="text-2xl outfit-headline text-[#063D79] font-bold">Kontakt</h1>
    <p class="roboto-plain">
      Hier können Sie Ihre Nachricht an uns hinterlassen.
      Füllen Sie dafür bitte die unteren Felder aus
      und klicken Sie anschließend auf den Senden-Button.
    </p>
    <Form
      v-slot="$form"
      :resolver
      @submit="onFormSubmit"
      class="flex flex-col gap-4"
    >
      <div class="flex flex-row gap-6 flex-wrap">
        <FormField v-slot="$field" name="appellation" class="flex flex-col gap-1 w-45">
          <label for="appellation" class="font-bold">Anrede</label>
          <Select
            labelId="appellation"
            :options="appellation"
            optionLabel="label"
            optionValue="value"
            placeholder="Anrede"
            fluid
          />
          <Message v-if="$form.appellation?.invalid" severity="error" size="small" variant="simple">
            {{ $form.appellation.error.message }}
          </Message>
        </FormField>
        <FormField v-slot="$field" name="name" class="flex flex-col gap-1 flex-1">
          <label for="name" class="font-bold">Name*</label>
          <InputText
            id="name"
            placeholder="Name"
            v-on:keydown.enter.prevent
            fluid
          />
          <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
            {{ $form.name.error.message }}
          </Message>
        </FormField>
      </div>
      <FormField v-slot="$field" name="email" class="flex flex-col gap-1">
        <label for="email" class="font-bold">Email*</label>
        <InputText
          id="email"
          placeholder="Email"
          v-on:keydown.enter.prevent
          fluid
        />
        <Message v-if="$form.email?.invalid" severity="error" size="small" variant="simple">
          {{ $form.email.error.message }}
        </Message>
      </FormField>
      <FormField v-slot="$field" name="subject" class="flex flex-col gap-1">
        <label for="subject" class="font-bold">Betreff*</label>
        <InputText
          id="subject"
          placeholder="Betreff"
          v-on:keydown.enter.prevent
          fluid
        />
        <Message v-if="$form.subject?.invalid" severity="error" size="small" variant="simple">
          {{ $form.subject.error.message }}
        </Message>
      </FormField>
      <FormField v-slot="$field" name="message" class="flex flex-col gap-1">
        <label for="message" class="font-bold">Nachricht*</label>
        <Textarea
          id="message"
          placeholder="Ihre Nachricht"
          rows="5"
          autoResize
          fluid
        />
        <Message v-if="$form.message?.invalid" severity="error" size="small" variant="simple">
          {{ $form.message.error.message }}
        </Message>
      </FormField>
      <FormField v-slot="$field" name="data_protection" class="flex flex-col gap-1">
        <div class="flex gap-4 mt-2">
          <Checkbox inputId="dp" name="dataProtection" class="pt-0.5" binary/>
          <label for="dp">
            Ich habe die
            <NuxtLink to="/data-protection" class="text-[#004188] font-bold">
              Datenschutzerklärung
            </NuxtLink>
            zur Kenntnis genommen.*
          </label>
        </div>
        <Message v-if="$form.dataProtection?.invalid" severity="error" size="small" variant="simple">
          {{ $form.dataProtection.error.message }}
        </Message>
      </FormField>
      <Button type="submit" severity="primary" label="Senden" class="w-[20%]"/>
      <!--
      <Fieldset legend="Form States" class="h-80 overflow-auto">
        <pre class="whitespace-pre-wrap">{{ $form }}</pre>
      </Fieldset>
      -->
    </Form>
    <h1 class="text-2xl outfit-headline text-[#063D79] font-bold">Ansprechpartner</h1>
    <div class="flex flex-col">
      <div class="flex flex-col gap-2 md:flex-row md:space-x-5 md:items-start bg-[#F1F2F2] p-5">
        <img src="https://picsum.photos/200" alt="placeholder-image"/>
        <div class="flex flex-col gap-4">
          <div class="averia-layout italic text-2xl text-[#063D79]">Max Mustermann</div>
          <div class="roboto-plain break-words">
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
            sed diam nonumy eirmod tempor invidunt ut labore et dolore
            magna aliquyam erat, sed diam voluptua. At vero eos et
            accusam et justo duo dolores et ea rebum. Stet clita kasd
            gubergren, no sea takimata sanctus est Lorem ipsum dolor
            sit amet.
          </div>
        </div>
      </div>
      <div class="flex flex-col gap-2 md:flex-row md:space-x-5 md:items-start p-5">
        <div class="flex flex-col gap-4">
          <div class="averia-layout italic text-2xl text-[#063D79]">Max Mustermann</div>
          <div class="roboto-plain break-words">
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
            sed diam nonumy eirmod tempor invidunt ut labore et dolore
            magna aliquyam erat, sed diam voluptua. At vero eos et
            accusam et justo duo dolores et ea rebum. Stet clita kasd
            gubergren, no sea takimata sanctus est Lorem ipsum dolor
            sit amet.
          </div>
        </div>
        <img src="https://picsum.photos/200" alt="placeholder-image"/>
      </div>
      <div class="flex flex-col gap-2 md:flex-row md:space-x-5 md:items-start bg-[#F1F2F2] p-5">
        <img src="https://picsum.photos/200" alt="placeholder-image"/>
        <div class="flex flex-col gap-4">
          <div class="averia-layout italic text-2xl text-[#063D79]">Max Mustermann</div>
          <div class="roboto-plain break-words">
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
            sed diam nonumy eirmod tempor invidunt ut labore et dolore
            magna aliquyam erat, sed diam voluptua. At vero eos et
            accusam et justo duo dolores et ea rebum. Stet clita kasd
            gubergren, no sea takimata sanctus est Lorem ipsum dolor
            sit amet.
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
