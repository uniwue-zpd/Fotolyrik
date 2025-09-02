<script setup lang="ts">
import type { PubMedium } from "~/utils/types";
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";

const toast = useToast();
const pubMediumStore = usePubMediumStore();
const placeStore = usePlaceStore();

const placeLoading = ref(false);

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  pub_medium?: PubMedium;
}>();

onMounted(() => {
  placeStore.fetchPlaces();
});

const rythm = ref([
    { label: "Unbekannt", value: null },
    { label: "Wöchentlich", value: "wöchentlich" },
    { label: "Halbmonatlich", value: "halbmonatlich" },
    { label: "Monatlich", value: "monatlich" },
    { label: "Halbjährlich", value: "halbjährlich"},
    { label: "Jährlich", value: "jährlich"}
]);

const resolver = ref(
  zodResolver(
    z.object({
      title: z.string("Bitte geben Sie einen Titel an."),
      subtitle: z.string().optional(),
      publicationPlaces: z.array(z.object()).optional(),
      publisher: z.string().optional(),
      pubRhytm: z.string().optional(),
      startYear: z.number().optional(),
      endYear: z.number().optional(),
      amountVolumes: z.number().optional(),
      amountIssues: z.number().optional(),
      zdbId: z.string().optional(),
    })
  )
);

const onPlaceReload = async () => {
  if (!placeLoading.value) {
    placeLoading.value = true;
    await placeStore.fetchPlaces(true);
    placeLoading.value = false;
  }
};

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    try {
      if (props.action === "create") {
        await pubMediumStore.createPubMedium(e.values);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich erstellt", life: 3000});
        navigateTo("/publication_media")
      } else if (props.action === "edit" && props.pub_medium?.id) {
        await pubMediumStore.updatePubMedium(e.values, props.pub_medium.id);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich aktualisiert", life: 3000});
        navigateTo(`/publication_media/${props.pub_medium?.id}`);
      }
    } catch (error) {
      console.log(error);
      toast.add({severity: "error", summary: "Fehler", detail: "Ein Fehler ist aufgetreten", life: 3000});
    }
  }
};
</script>

<template>
  <div class="flex flex-col mx-auto w-[70%] gap-4">
    <h1 class="text-2xl outfit-headline text-[#063D79] font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um ein Publikationsmedium zu erstellen oder anzupassen.
    </p>

    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Form 
        v-slot="$form" 
        class="flex flex-col gap-4"
        :resolver 
        :initialValues="props.pub_medium ? props.pub_medium : {}"
        :key="props.pub_medium ? props.pub_medium.id : 'new'"
        @submit="onFormSubmit" 
      >
        <div class="flex flex-row gap-6 flex-wrap">
          <!-- Title field -->
          <FormField v-slot="$field" name="title" class="flex flex-col gap-1 flex-1">
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

          <!-- Subtitle field -->
          <FormField v-slot="$field" name="subtitle" class="flex flex-col gap-1 flex-1">
            <label for="subtitle" class="font-bold">Untertitel</label>
            <IconField class="flex-auto">
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

        <!-- Place field -->
        <FormField v-slot="$field" name="publicationPlaces" class="flex flex-col gap-1 flex-1">
          <label for="publicationPlaces" class="font-bold">Publikationsorte</label>
          <div class="flex flex-row gap-4 flex-nowrap">
            <MultiSelect
              inputId="publicationPlaces"
              placeholder="Orte auswählen"
              selectedItemsLabel="{0} Orte ausgewählt"
              class="flex-1 min-w-0"
              optionLabel="label"
              optionValue="value"
              :options="placeStore.places.map(p => ({ label: `${p.name}`, value: p }))"
              :key="placeStore.places.length"
              :maxSelectedLabels="2"
              filter
              fluid
            />
            <Button 
              icon="pi pi-refresh" 
              severity="secondary" 
              aria-label="Reload" 
              :loading="placeLoading" 
              @click="onPlaceReload"
            />
            <NuxtLink to="/places/create" target="_blank">
              <Button icon="pi pi-plus" severity="secondary" aria-label="Add" />
            </NuxtLink>
          </div>
          <Message v-if="$form.publicationPlaces?.invalid" severity="error" size="small" variant="simple" class="flex-auto">
            {{ $form.publicationPlaces.error.message }}
          </Message>
        </FormField>

        <div class="flex flex-row gap-6 flex-wrap">
          <!-- Publisher field -->
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

          <!-- Publication rythm field -->
          <FormField v-slot="$field" name="pubRhytm" class="flex flex-col gap-1 flex-1">
            <label for="pubRhytm" class="font-bold">Publikationsrythmus</label>
            <IconField>
              <InputIcon class="pi pi-calendar-clock"/>
              <Select 
                labelId="pubRhytm"
                placeholder="Rythmus auswählen"
                class="pl-7" 
                optionLabel="label"
                optionValue="value"
                :options="rythm"
                fluid
              />
            </IconField>
            <Message v-if="$form.pubRhytm?.invalid" severity="error" size="small" variant="simple">
              {{ $form.pubRhytm.error.message }}
            </Message>
          </FormField>
        </div>

        <div class="flex flex-row gap-6 flex-wrap">
          <!-- Start year field -->
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

          <!-- End year field -->
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
          <!-- Amount of volumes field -->
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

          <!-- Amount of issues field -->
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

        <!-- ZDB-ID field -->
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

        <!-- Submit button -->
        <Button type="submit" severity="primary">
          {{ (props.action === "create") ? "Erstellen" : "Bearbeiten" }}
        </Button>

        <!--
        <Fieldset legend="Form States" class="h-80 overflow-auto">
          <pre class="whitespace-pre-wrap">{{ $form }}</pre>
        </Fieldset>
        -->
      </Form>
    </div>
  </div>
</template>

<style scoped>

</style>
