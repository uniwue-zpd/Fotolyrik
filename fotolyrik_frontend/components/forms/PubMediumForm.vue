<script setup lang="ts">
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";

const toast = useToast();

const props = defineProps<{
  action: 'create' | 'edit';
  header: string;
  pub_medium?: PubMedium;
}>();

const pub_media_store = usePubMediumStore();
const place_store = usePlaceStore();

const rythm = ref([
    {key: 'Unbekannt', value: null},
    {key: 'Wöchentlich', value: 'W'},
    {key: 'Halbmonatlich', value: 'HM'},
    {key: 'Monatlich', value: 'M'},
])

const resolver = ref(
  zodResolver(
    z.object({
      title: z.string("Bitte geben Sie einen Titel an."),
      subtitle: z.any(),
      publicationPlaces: z.any(),
      publisher: z.any(),
      pubRhytm: z.any(),
      startYear: z.any(),
      endYear: z.any(),
      amountVolumes: z.any(),
      amountIssues: z.any(),
      zdbId: z.any(),
    })
  )
);

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    console.log(e.values)
    try {
      if (props.action === 'create') {
        await pub_media_store.createPubMedium(e.values);
        toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich erstellt', life: 3000});
        e.reset();
      } else if (props.action === 'edit' && props.pub_medium?.id) {
        await pub_media_store.updatePubMedium(e.values, props.pub_medium.id);
        toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich upgedated', life: 3000});
        navigateTo(`/publication_media/${props.pub_medium?.id}`);
      }
    } catch (error) {
      console.log(error);
      toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Senden der Nachricht', life: 3000});
    }
  }
};
</script>

<template>
  <div class="flex flex-col mx-auto w-[70%] gap-4">
    <h1 class="text-2xl outfit-headline text-[#063D79] font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um ein Objekt zu erstellen oder anzupassen.
    </p>
    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Form 
        v-slot="$form" 
        :resolver 
        :initialValues="props.pub_medium ? props.pub_medium : {}"
        :key="props.pub_medium ? props.pub_medium.id : 'new'"
        @submit="onFormSubmit" 
        class="flex flex-col gap-4"
      >
        <div class="flex flex-row gap-6 flex-wrap">
          <FormField v-slot="$field" name="title" class="flex flex-col gap-1 flex-auto">
            <label for="title" class="font-bold">Titel</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputText 
                id="title" 
                placeholder="Hamburger Anzeiger" 
                v-on:keydown.enter.prevent 
                fluid 
              />
            </IconField>
            <Message v-if="$form.title?.invalid" severity="error" size="small" variant="simple">
              {{ $form.title.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="subtitle" class="flex flex-col gap-1 flex-auto">
            <label for="subtitle" class="font-bold">Untertitel</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputText 
                id="subtitle" 
                placeholder="Illustrierte Wochenbeilage in Tiefdruck" 
                v-on:keydown.enter.prevent 
                fluid 
              />
            </IconField>
            <Message v-if="$form.subtitle?.invalid" severity="error" size="small" variant="simple">
              {{ $form.subtitle.error.message }}
            </Message>
          </FormField>
        </div>
        <FormField v-slot="$field" name="publicationPlaces" class="flex flex-col gap-1 flex-1">
          <label for="publicationPlaces" class="font-bold">Publikationsorte</label>
          <MultiSelect
            display="chip"
            inputId="publicationPlaces"
            placeholder="Orte auswählen"
            :options="place_store.places.map(p => ({ key: `${p.name}`, value: p }))"
            optionLabel="key"
            optionValue="value"
            filter 
          />
          <Message v-if="$form.publicationPlaces?.invalid" severity="error" size="small" variant="simple">
            {{ $form.publicationPlaces.error.message }}
          </Message>
        </FormField>
        <div class="flex flex-row gap-6 flex-wrap">
          <FormField v-slot="$field" name="publisher" class="flex flex-col gap-1 flex-1">
            <label for="publisher" class="font-bold">Herausgeber</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputText 
                id="publisher" 
                placeholder="Girardet" 
                v-on:keydown.enter.prevent 
                fluid 
              />
            </IconField>
            <Message v-if="$form.publisher?.invalid" severity="error" size="small" variant="simple">
              {{ $form.publisher.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="pubRhytm" class="flex flex-col gap-1 flex-1">
            <label for="pubRhytm" class="font-bold">Publikationsrythmus</label>
            <IconField>
              <InputIcon class="pi pi-calendar-clock"/>
              <Select 
                labelId="pubRhytm" 
                optionLabel="key"
                optionValue="value"
                :options="rythm"
                class="pl-7" 
                fluid
              />
            </IconField>
            <Message v-if="$form.pubRhytm?.invalid" severity="error" size="small" variant="simple">
              {{ $form.pubRhytm.error.message }}
            </Message>
          </FormField>
        </div>
        <div class="flex flex-row gap-6 flex-wrap">
          <FormField v-slot="$field" name="startYear" class="flex flex-col gap-1 flex-1">
            <label for="startYear" class="font-bold">Startjahr</label>
            <IconField>
              <InputIcon class="pi pi-calendar" />
              <InputNumber 
                id="startYear"
                placeholder="1924"
                :min="0" 
                :max="3000" 
                :useGrouping="false" 
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
            <Message v-if="$form.startYear?.invalid" severity="error" size="small" variant="simple">
              {{ $form.startYear.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="endYear" class="flex flex-col gap-1 flex-1">
            <label for="endYear" class="font-bold">Endjahr</label>
            <IconField>
              <InputIcon class="pi pi-calendar" />
              <InputNumber 
                id="endYear" 
                placeholder="1938"
                :min="0" 
                :max="3000" 
                :useGrouping="false" 
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
            <Message v-if="$form.endYear?.invalid" severity="error" size="small" variant="simple">
              {{ $form.endYear.error.message }}
            </Message>
          </FormField>
        </div>
        <div class="flex flex-row gap-6 flex-wrap">
          <FormField v-slot="$field" name="amountVolumes" class="flex flex-col gap-1 flex-1">
            <label for="amountVolumes" class="font-bold">Anzahl Jahrgänge</label>
            <IconField>
              <InputIcon class="pi pi-book" />
              <InputNumber 
                id="amountVolumes"
                placeholder="18"
                :min="0" 
                :useGrouping="false" 
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
            <Message v-if="$form.amountVolumes?.invalid" severity="error" size="small" variant="simple">
              {{ $form.amountVolumes.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="amountIssues" class="flex flex-col gap-1 flex-1">
            <label for="amountIssues" class="font-bold">Anzahl Ausgaben</label>
            <IconField>
              <InputIcon class="pi pi-book" />
              <InputNumber 
                id="amountIssues" 
                placeholder="100"
                :min="0" 
                :useGrouping="false" 
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
            <Message v-if="$form.amountIssues?.invalid" severity="error" size="small" variant="simple">
              {{ $form.amountIssues.error.message }}
            </Message>
          </FormField>
        </div>
        <FormField v-slot="$field" name="zdbId" class="flex flex-col gap-1 flex-auto">
          <label for="zdbId" class="font-bold">ZDB-ID</label>
          <IconField>
            <InputIcon class="pi pi-pen-to-square" />
            <InputText 
              id="zdbId" 
              placeholder="2650224-0" 
              v-on:keydown.enter.prevent 
              fluid 
            />
          </IconField>
          <Message v-if="$form.zdbId?.invalid" severity="error" size="small" variant="simple">
            {{ $form.zdbId.error.message }}
          </Message>
        </FormField>
        <Button type="submit" severity="primary">{{ (props.action === "create") ? "Erstellen" : "Bearbeiten" }}</Button>
      </Form>
    </div>
  </div>
</template>

<style scoped>

</style>
