<script setup lang="ts">
import type { PhotoPoem } from "~/utils/types";
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";

const toast = useToast();
const personStore = usePersonStore();
const photopoemStore = usePhotopoemStore();
const pubMediumStore = usePubMediumStore();
const fileStore = useFileStore();

const personLoading = ref(false);
const pubMediumLoading = ref(false);
const fileLoading = ref(false);

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  photopoem?: PhotoPoem;
}>();

onMounted(() => {
  personStore.fetchPersons();
  pubMediumStore.fetchPubMedia();
  fileStore.fetchFiles();
});

const dateRegex = /^(0?[1-9]|[12][0-9]|3[01])\.(0?[1-9]|1[0-2])\.(\d{4})$/;
const copyright = ref([
  { label: "Ungeklärt", value: "Ungeklärt" },
  { label: "Rechtefrei 70 Jahre", value: "rechtefrei 70 Jahre" },
  { label: "Eingeholt (schriftlich)", value: "eingeholt (schriftlich)" }
]);
const languages = ref([
  { label: "Unbekannt", value: null, code: null},
  { label: "Deutsch", value: "German", code: "DE"},
  { label: "Englisch", value: "English", code: "GB" },
  { label: "Französisch", value: "French", code: "FR" }
]);

const resolver = ref(
  zodResolver(
    z.object({
      title: z.string("Bitte geben Sie einen Titel an."),
      volume: z.any(),
      issue: z.any(),
      pageNumber: z.any(),
      pageCount: z.any(),
      publicationDate: z
        .string().optional().nullable()
        .refine((val) => {
          if (!val || val.trim() === "") return true;
          if (!dateRegex.test(val)) return false;
          const [day, month, year] = val.split(".").map(Number);
          const d = new Date(year, month - 1, day);
          return (d.getFullYear() === year && d.getMonth() === month - 1 && d.getDate() === day);
        },
        { message: "Bitte geben Sie ein gültiges Datum im Format DD.MM.YYYY an." }
      ),
      publicationMedium: z.any(),
      authors: z.any(),
      photographers: z.any(),
      otherContributors: z.any(),
      themes: z.any(),
      topics: z.any(),
      link: z.url().optional().nullable(),
      iiifManifest: z.url().optional().nullable(),
      copyrightStatusImage: z.any(),
      copyrightStatusText: z.any(),
      language: z.any()
    })
  )
);

const onPersonReload = async () => {
  if (!personLoading.value) {
    personLoading.value = true;
    await personStore.fetchPersons(true);
    personLoading.value = false;
  }  
};

const onPubMediumReload = async () => {
  if (!pubMediumLoading.value) {
    pubMediumLoading.value = true;
    await pubMediumStore.fetchPubMedia(true);
    pubMediumLoading.value = false;
  }  
};

const onFileReload = async () => {
  if (!fileLoading.value) {
    fileLoading.value = true;
    await fileStore.fetchFiles(true);
    fileLoading.value = false;
  }  
};

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    try {
      if (props.action === "create") {
        await photopoemStore.createPhotopoem(e.values);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich erstellt", life: 3000});
        navigateTo("/photopoems")
      } else if (props.action === "edit" && props.photopoem?.id) {
        await photopoemStore.updatePhotopoem(e.values, props.photopoem.id);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich aktualisiert", life: 3000});
        navigateTo(`/photopoems/${props.photopoem?.id}`);
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
      Füllen Sie bitte die untenstehenden Felder aus, um ein Objekt zu erstellen oder anzupassen.
    </p>

    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Form 
        v-slot="$form" 
        class="flex flex-col gap-4"
        :resolver 
        :initialValues="props.photopoem ? props.photopoem : {}"
        :key="props.photopoem ? props.photopoem.id : 'new'"
        @submit="onFormSubmit" 
      >
        <!-- Title field -->
        <FormField v-slot="$field" name="title" class="flex flex-col gap-1 flex-auto">
          <label for="title" class="font-bold">Titel*</label>
          <IconField>
            <InputIcon class="pi pi-pen-to-square" />
            <InputText 
              id="title" 
              placeholder="Telephon-Tragödie" 
              v-on:keydown.enter.prevent 
              fluid 
            />
          </IconField>
          <Message v-if="$form.title?.invalid" severity="error" size="small" variant="simple">
            {{ $form.title.error.message }}
          </Message>
        </FormField>

        <Divider align="center"><b class="px-2">Daten</b></Divider>

        <div class="flex flex-row gap-6 flex-wrap">
          <!-- Volume field -->
          <FormField v-slot="$field" name="volume" class="flex flex-col gap-1 flex-1">
            <label for="volume" class="font-bold">Jahrgang</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputNumber 
                id="volume"
                placeholder="5"
                :min="0" 
                :useGrouping="false" 
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
            <Message v-if="$form.volume?.invalid" severity="error" size="small" variant="simple">
              {{ $form.volume.error.message }}
            </Message>
          </FormField>

          <!-- Issue field -->
          <FormField v-slot="$field" name="issue" class="flex flex-col gap-1 flex-1">
            <label for="issue" class="font-bold">Ausgabe</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputNumber 
                id="issue" 
                placeholder="1"
                :min="0" 
                :useGrouping="false" 
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
            <Message v-if="$form.issue?.invalid" severity="error" size="small" variant="simple">
              {{ $form.issue.error.message }}
            </Message>
          </FormField>
        </div>

        <div class="flex flex-row gap-6 flex-wrap">
          <!-- Page number field -->
          <FormField v-slot="$field" name="pageNumber" class="flex flex-col gap-1 flex-1">
            <label for="pageNumber" class="font-bold">Seite</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputNumber 
                id="pageNumber"
                placeholder="23"
                :min="0" 
                :useGrouping="false" 
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
            <Message v-if="$form.pageNumber?.invalid" severity="error" size="small" variant="simple">
              {{ $form.pageNumber.error.message }}
            </Message>
          </FormField>

          <!-- Page count field -->
          <FormField v-slot="$field" name="pageCount" class="flex flex-col gap-1 flex-1">
            <label for="pageCount" class="font-bold">Seitenzahl</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputNumber 
                id="pageCount" 
                placeholder="1832"
                :min="0" 
                :useGrouping="false" 
                v-on:keydown.enter.prevent 
                fluid
              />
            </IconField>
            <Message v-if="$form.pageCount?.invalid" severity="error" size="small" variant="simple">
              {{ $form.pageCount.error.message }}
            </Message>
          </FormField>
        </div>

        <div class="flex flex-row gap-6 flex-wrap">
          <!-- Publication date field -->
          <FormField v-slot="$field" name="publicationDate" class="flex flex-col gap-1 flex-1">
            <label for="publicationDate" class="font-bold">Publikationsdatum</label>
            <IconField>
              <InputIcon class="pi pi-calendar" />
              <InputText 
                id="publicationDate" 
                placeholder="01.03.1930" 
                v-on:keydown.enter.prevent 
                fluid 
              />
            </IconField>
            <Message v-if="$form.publicationDate?.invalid" severity="error" size="small" variant="simple">
              {{ $form.publicationDate.error.message }}
            </Message>
          </FormField>

          <!-- Language field -->
          <FormField v-slot="$field" name="language" class="flex flex-col gap-1 flex-1">
            <label for="language" class="font-bold">Sprache</label>
            <IconField>
              <InputIcon class="pi pi-language"/>
              <Select 
                labelId="language" 
                placeholder="Sprache auswählen"
                class="pl-7" 
                optionLabel="label"
                optionValue="value"
                :options="languages"
                fluid
              />
            </IconField>
            <Message v-if="$form.language?.invalid" severity="error" size="small" variant="simple">
              {{ $form.language.error.message }}
            </Message>
          </FormField>
        </div>

        <!-- Publication medium field -->
        <FormField v-slot="$field" name="publicationMedium" class="flex flex-col gap-1">
          <label for="publicationMedium" class="font-bold">Publikationsmedium</label>
          <div class="flex flex-row gap-4 flex-nowrap">
            <IconField class="flex-1 min-w-0">
              <InputIcon class="pi pi-book"/>
              <Select 
                labelId="publicationMedium" 
                placeholder="Publikationsmedium auswählen"
                class="pl-7"
                optionLabel="label"
                optionValue="value"
                :options="pubMediumStore.pub_media.map(p => ({ label: `${p.title}`, value: p }))"
                :key="pubMediumStore.pub_media.length"
                fluid
              />
            </IconField>
            <Button 
              icon="pi pi-refresh" 
              severity="secondary" 
              aria-label="Reload" 
              :loading="pubMediumLoading" 
              @click="onPubMediumReload"
            />
            <NuxtLink to="/publication_media/create" target="_blank">
              <Button icon="pi pi-plus" severity="secondary" aria-label="Add" />
            </NuxtLink>
          </div>
          <Message v-if="$form.publicationMedium?.invalid" severity="error" size="small" variant="simple">
            {{ $form.publicationMedium.error.message }}
          </Message>
        </FormField>

        <Divider align="center"><b class="px-2">Personen</b></Divider>

        <div class="flex flex-row gap-4">
          <Button 
            icon="pi pi-refresh" 
            label="Personen aktualisieren"
            severity="secondary" 
            aria-label="Reload" 
            :loading="personLoading" 
            @click="onPersonReload" 
          />
          <NuxtLink to="/persons/create" target="_blank">
            <Button icon="pi pi-plus" severity="secondary" aria-label="Add" label="Person erstellen"/>
          </NuxtLink>
        </div>
        
        <!-- Authors field -->
        <FormField v-slot="$field" name="authors" class="flex flex-col gap-1">
          <label for="authors" class="font-bold">Author:innen</label>
            <MultiSelect
              inputId="authors"
              placeholder="Author:innen auswählen"
              selectedItemsLabel="{0} Personen ausgewählt"
              optionLabel="label"
              optionValue="value"
              :options="personStore.persons.map(p => ({ label: `${p.fullName}`, value: p }))"
              :key="personStore.persons.length"
              :maxSelectedLabels="2"
              filter
              fluid
            />
          <Message v-if="$form.authors?.invalid" severity="error" size="small" variant="simple">
            {{ $form.authors.error.message }}
          </Message>
        </FormField>

        <!-- Photographers field -->
        <FormField v-slot="$field" name="photographers" class="flex flex-col gap-1">
          <label for="photographers" class="font-bold">Fotograf:innen</label>
          <MultiSelect
            inputId="photographers"
            placeholder="Fotograf:innen auswählen"
            selectedItemsLabel="{0} Personen ausgewählt"
            optionLabel="label"
            optionValue="value"
            :options="personStore.persons.map(p => ({ label: `${p.fullName}`, value: p }))"
            :key="personStore.persons.length"
            :maxSelectedLabels="2"
            filter 
            fluid
          />          
          <Message v-if="$form.photographers?.invalid" severity="error" size="small" variant="simple">
            {{ $form.photographers.error.message }}
          </Message>
        </FormField>

        <!-- Other contributors field -->
        <FormField v-slot="$field" name="otherContributors" class="flex flex-col gap-1">
          <label for="otherContributors" class="font-bold">Sonstige Mitwirkende</label>
          <MultiSelect
            inputId="otherContributors"
            placeholder="Sonstige Mitwirkende auswählen"
            selectedItemsLabel="{0} Personen ausgewählt"
            optionLabel="label"
            optionValue="value"
            :options="personStore.persons.map(p => ({ label: `${p.fullName}`, value: p }))"
            :key="personStore.persons.length"
            :maxSelectedLabels="2"
            filter 
            fluid
          />
          <Message v-if="$form.otherContributors?.invalid" severity="error" size="small" variant="simple">
            {{ $form.otherContributors.error.message }}
          </Message>
        </FormField>

        <Divider align="center"><b class="px-2">Tags</b></Divider>

        <!-- Themes field -->
        <FormField v-slot="$field" name="themes">
          <label for="themes" class="font-bold">Thematik</label>
          <AutoComplete 
            inputId="themes"
            placeholder="Eingabe mit Enter bestätigen"
            :typeahead="false" 
            multiple 
            fluid
          />
          <Message v-if="$form.themes?.invalid" severity="error" size="small" variant="simple">
            {{ $form.themes.error.message }}
          </Message>
        </FormField>

        <!-- Topics field -->
        <FormField v-slot="$field" name="topics">
          <label for="topics" class="font-bold">Kategorie</label>
          <AutoComplete 
            inputId="topics"
            placeholder="Eingabe mit Enter bestätigen"
            :typeahead="false" 
            multiple 
            fluid
          />
          <Message v-if="$form.topics?.invalid" severity="error" size="small" variant="simple">
            {{ $form.topics.error.message }}
          </Message>
        </FormField>

        <Divider align="center"><b class="px-2">Links</b></Divider>

        <!-- File select field -->
        <FormField v-slot="$field" name="files" class="flex flex-col gap-1">
          <label for="files" class="font-bold">Dateien</label>
          <div class="flex flex-row gap-4 flex-nowrap">
            <MultiSelect
              inputId="files"
              placeholder="Dateien auswählen"
              class="flex-1 min-w-0"
              selectedItemsLabel="{0} Dateien ausgewählt"
              optionLabel="label"
              optionValue="value"
              :options="fileStore.files.map(f => ({ label: `${f.filename}`, value: f }))"
              :key="fileStore.files.length"
              :maxSelectedLabels="2"
              fluid
              filter
            />
            <Button 
              icon="pi pi-refresh" 
              severity="secondary" 
              aria-label="Reload" 
              :loading="fileLoading" 
              @click="onFileReload"
            />
            <NuxtLink to="/files" target="_blank">
              <Button icon="pi pi-plus" severity="secondary" aria-label="Add" />
            </NuxtLink>
          </div>
          <Message v-if="$form.files?.invalid" severity="error" size="small" variant="simple">
            {{ $form.files.error.message }}
          </Message>
        </FormField>

        <div class="flex flex-row gap-6 flex-wrap">
          <!-- Link field -->
          <FormField v-slot="$field" name="link" class="flex flex-col gap-1 flex-1">
            <label for="link" class="font-bold">Link</label>
            <IconField>
              <InputIcon class="pi pi-link" />
              <InputText 
                id="link" 
                placeholder="https://www.example.com" 
                v-on:keydown.enter.prevent 
                fluid 
              />
            </IconField>
            <Message v-if="$form.link?.invalid" severity="error" size="small" variant="simple">
              {{ $form.link.error.message }}
            </Message>
          </FormField>

          <!-- IIIF manifest field -->
          <FormField v-slot="$field" name="iiifManifest" class="flex flex-col gap-1 flex-1">
            <label for="iiifManifest" class="font-bold">IIIF-Manifest</label>
            <IconField>
              <InputIcon class="pi pi-link" />
              <InputText 
                id="iiifManifest" 
                placeholder="https://www.example.com" 
                v-on:keydown.enter.prevent 
                fluid 
              />
            </IconField>
            <Message v-if="$form.iiifManifest?.invalid" severity="error" size="small" variant="simple">
              {{ $form.iiifManifest.error.message }}
            </Message>
          </FormField>
        </div>

        <div class="flex flex-row gap-6 flex-wrap">
          <!-- Copyright image field -->
          <FormField v-slot="$field" name="copyrightStatusImage" class="flex flex-col gap-1 flex-1">
            <label for="copyrightStatusImage" class="font-bold">Urheberrechtsstatus Bild</label>
            <IconField>
              <InputIcon class="pi pi-shield"/>
              <Select 
                labelId="copyrightStatusImage" 
                placeholder="Status auswählen"
                class="pl-7"
                optionLabel="label"
                optionValue="value"
                :options="copyright"
                fluid
              />
            </IconField>
            <Message v-if="$form.copyrightStatusImage?.invalid" severity="error" size="small" variant="simple">
              {{ $form.copyrightStatusImage.error.message }}
            </Message>
          </FormField>

          <!-- Copyright text field -->
          <FormField v-slot="$field" name="copyrightStatusText" class="flex flex-col gap-1 flex-1">
            <label for="copyrightStatusText" class="font-bold">Urheberrechtsstatus Text</label>
            <IconField>
              <InputIcon class="pi pi-shield"/>
              <Select 
                labelId="copyrightStatusText" 
                placeholder="Status auswählen"
                class="pl-7" 
                optionLabel="label"
                optionValue="value"
                :options="copyright"
                fluid
              />
            </IconField>
            <Message v-if="$form.copyrightStatusText?.invalid" severity="error" size="small" variant="simple">
              {{ $form.copyrightStatusText.error.message }}
            </Message>
          </FormField>
        </div>

        <!-- Submit Button -->
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
