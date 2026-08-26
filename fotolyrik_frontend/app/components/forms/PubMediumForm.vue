<script setup lang="ts">
import type { PubMediumDTO } from "~/utils/types";
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";
import {FormField} from "@primevue/forms";

const toast = useToast();
const pubMediumApi = usePubMedium();
const placeApi = usePlace();
const publisherApi= usePublisher();
const pubRhythmApi = usePubRhythm();

const [placeHandle, publisherHandle, pubRhythmHandle] = await Promise.all([
  placeApi.usePlaceList(),
  publisherApi.usePublisherList(),
  pubRhythmApi.usePubRhythmList()
]);

const publication_places = computed(() => placeHandle.data.value?.map(p => ({id: p.id, name: p.name})));
const publishers = computed(() => publisherHandle.data.value?.map(pu => ({id: pu.id, name: pu.name})));
const publication_rhythms = computed(() => pubRhythmHandle.data.value?.map(pr => ({id: pr.id, value: pr.value})));

const data_refreshing = ref(false);

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  pub_medium?: PubMediumDTO;
}>();

const resolver = ref(
  zodResolver(
    z.object({
      title: z.string("Bitte geben Sie einen Titel an."),
      subtitle: z.string().optional().nullable(),
      publicationPlaces: z.any(),
      publisher: z.any().nullable(),
      pubRhythms: z.any(),
      editorialOffice: z.string().optional().nullable(),
      startYear: z.any().optional().nullable(),
      endYear: z.any().optional().nullable(),
      amountVolumes: z.number().optional().nullable(),
      amountIssues: z.number().optional().nullable(),
      zdbId: z.string().optional().nullable(),
      notes: z.string().optional().nullable(),
    })
  )
);

async function handleRefresh() {
  data_refreshing.value = true;
  try {
    await Promise.all([placeHandle.refresh,publisherHandle.refresh,pubRhythmHandle.refresh])
    toast.add({severity: 'success', summary: 'Erfolg', detail: 'Datenbankdaten erfolgreich aktualisiert', life: 2000});
  } catch (err) {
    toast.add({severity: 'error', summary: 'Fehler', detail: 'Fehler beim Aktualisieren der Datenbankdaten', life: 2000});
  } finally {
    data_refreshing.value = false;
  }
}

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    try {
      if (props.action === "create") {
        await pubMediumApi.createPubMedium(e.values);
        await refreshNuxtData('pubMedium-list');
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich erstellt", life: 3000});
        navigateTo("/publication_media")
      } else if (props.action === "edit" && props.pub_medium?.id) {
        await pubMediumApi.updatePubMedium(props.pub_medium.id,e.values );
        await  Promise.all([refreshNuxtData('pubMedium-list'), await refreshNuxtData(`pubMedium-${props.pub_medium.id}`)])
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
    <h1 class="text-2xl outfit-headline text-primary font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um ein Publikationsmedium zu erstellen oder anzupassen.
    </p>
    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Button :disabled="data_refreshing" label="Daten aktualisieren" @click="handleRefresh">
        <div class="flex flex-row space-x-3 items-center">
          <div class="roboto-plain font-semibold">Datenbankdaten aktualisieren</div>
          <i v-show="data_refreshing" :class="['pi', data_refreshing ? 'pi-spin pi-spinner' : 'pi-spinner']"/>
        </div>
      </Button>
      <Divider/>
      <Form 
        v-slot="$form" 
        class="flex flex-col gap-4"
        :resolver 
        :initialValues="props.pub_medium ? props.pub_medium : {}"
        :key="props.pub_medium ? props.pub_medium.id : 'new'"
        @submit="onFormSubmit" 
      >
        <div class="flex flex-row gap-6 flex-wrap">
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
        <FormField v-slot="$field" name="publicationPlaces" class="flex flex-col gap-1 flex-1">
          <label for="publicationPlaces" class="font-bold">Publikationsorte</label>
          <div class="flex flex-row gap-4 flex-nowrap">
            <MultiSelect
              inputId="publicationPlaces"
              placeholder="Orte auswählen"
              selectedItemsLabel="{0} Orte ausgewählt"
              class="flex-1 min-w-0"
              optionLabel="name"
              :options="publication_places"
              :key="publication_places?.length"
              :maxSelectedLabels="2"
              filter
              fluid
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
          <FormField v-slot="$field" name="publisher" class="flex flex-col gap-1">
            <label for="publisher" class="font-bold">Herausgeber</label>
            <div class="flex flex-row gap-4 flex-nowrap">
              <IconField class="flex-1 min-w-0">
                <InputIcon class="pi pi-book"/>
                <Select
                    labelId="publisher"
                    placeholder="Herausgeber auswählen"
                    class="pl-7"
                    optionLabel="name"
                    :options="publishers"
                    :key="publishers?.length"
                    editable
                    showClear
                    fluid
                />
              </IconField>
              <NuxtLink to="/publishers/create" target="_blank">
                <Button icon="pi pi-plus" severity="secondary" aria-label="Add" />
              </NuxtLink>
            </div>
            <Message v-if="$form.publicationMedium?.invalid" severity="error" size="small" variant="simple">
              {{ $form.publicationMedium.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="pubRhythms" class="flex flex-col gap-1 flex-1">
            <label for="pubRhythms" class="font-bold">Publikationsrhythmen</label>
            <MultiSelect
                inputId="pubRhythms"
                placeholder="Rhythmen auswählen"
                selectedItemsLabel="{0} Rhythmen ausgewählt"
                class="flex-1 min-w-0"
                optionLabel="value"
                :options="publication_rhythms"
                :key="publication_rhythms?.length"
                :maxSelectedLabels="2"
                filter
                fluid
            />
            <Message v-if="$form.pubRhythms?.invalid" severity="error" size="small" variant="simple" class="flex-auto">
              {{ $form.pubRhythms.error.message }}
            </Message>
          </FormField>
        </div>
        <FormField v-slot="$field" name="editorialOffice" class="flex flex-col gap-1 flex-1">
          <label for="editorialOffice" class="font-bold">Schriftleitung</label>
          <IconField>
            <InputIcon class="pi pi-pen-to-square" />
            <InputText
                id="editorialOffice"
                v-on:keydown.enter.prevent
                fluid
            />
          </IconField>
          <Message v-if="$form.editorialOffice?.invalid" severity="error" size="small" variant="simple">
            {{ $form.editorialOffice.error.message }}
          </Message>
        </FormField>
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
        <FormField v-slot="$field" name="notes" class="flex flex-col gap-1 flex-1">
          <label for="notes" class="font-bold">Notizen</label>
          <Textarea
              id="notes"
              autoResize
              fluid
          />
          <Message v-if="$form.notes?.invalid" severity="error" size="small" variant="simple">
            {{ $form.notes.error.message }}
          </Message>
        </FormField>
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
