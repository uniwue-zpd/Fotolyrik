<script setup lang="ts">
import type { PhotoPoemDTO } from "~/utils/types";
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";
import { Form } from '@primevue/forms';
import { FormField } from '@primevue/forms';

const toast = useToast();
const personStore = usePersonStore();
const photopoemStore = usePhotopoemStore();
const pubMediumStore = usePubMediumStore();
const fileStore = useFileStore();
const languageStore = useLanguageStore();
const copyrightStatusStore = useCopyrightStatusStore();
const keywordStore = useKeywordStore();

const persons = computed(() => personStore.persons.map(p => ({ id: p.id, fullName: p.fullName, studioName: p.studioName, pseudonyms: p.pseudonyms })));
const keywords = computed(() => keywordStore.keywords.map(k => ({ id: k.id, value: k.value })));
const languages = computed(() => languageStore.languages.map(l => ({ id: l.id, name: l.name, isoDesignation: l.isoDesignation })));
const files = computed(() => fileStore.files.map(f => ({ id: f.id, filename: f.filename, originalFilename: f.originalFilename })));
const publicationMedia = computed(() => pubMediumStore.pub_media.map(pm => ({ id: pm.id, title: pm.title })));

const data_refreshing = ref(false);

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  photopoem?: PhotoPoemDTO;
}>();

const resolver = ref(
  zodResolver(
    z.object({
      title: z.any(),
      subtitle: z.any(),
      altTitle: z.any(),
      volume: z.any(),
      issue: z.any(),
      pageNumber: z.any(),
      manifestPageNumber: z.any(),
      pageCount: z.any(),
      pictureCount: z.any(),
      publicationDate: z.any(),
      publicationMedium: z.any(),
      authors: z.any(),
      photographers: z.any(),
      //depictedPeople: z.any(),
      otherContributors: z.any(),
      themes: z.any(),
      imageMotifs: z.any(),
      form: z.any(),
      link: z.any(),
      iiifManifest: z.any(),
      images: z.any(),
      copyrightStatusImage: z.any(),
      copyrightStatusText: z.any(),
      languages: z.any(),
    })
  )
);

async function handleRefresh() {
  data_refreshing.value = true;
  try {
    await useRefreshStoreData();
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
  <div class="flex flex-col mx-auto w-[90%] gap-4">
    <h1 class="text-2xl outfit-headline text-primary font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um ein Objekt zu erstellen oder anzupassen.
    </p>
    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Button :disabled="data_refreshing" label="Daten aktualisieren" @click="handleRefresh">
        <div class="flex flex-row space-x-3 items-center">
          <div class="roboto-plain font-semibold">Datenbankdaten aktualisieren</div>
          <i v-show="data_refreshing" :class="['pi', data_refreshing ? 'pi-spin pi-spinner' : 'pi-spinner']"/>
        </div>
      </Button>
      <Form
        v-slot="$form"
        class="flex flex-col gap-4"
        :resolver
        :initialValues="props.photopoem ? props.photopoem : {} as PhotoPoemDTO"
        :key="props.photopoem ? props.photopoem.id : 'new'"
        @submit="onFormSubmit"
      >
        <FormField v-slot="$field" name="title" class="flex flex-col gap-1 flex-auto">
          <label for="title" class="font-bold">Titel</label>
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
        <FormField v-slot="$field" name="subtitle" class="flex flex-col gap-1 flex-auto">
          <label for="title" class="font-bold">Untertitel</label>
          <IconField>
            <InputIcon class="pi pi-pen-to-square" />
            <InputText
                id="subtitle"
                v-on:keydown.enter.prevent
                fluid
            />
          </IconField>
        </FormField>
        <FormField v-slot="$field" name="altTitle" class="flex flex-col gap-1 flex-auto">
          <label for="altTitle" class="font-bold">Alternativer Titel</label>
          <IconField>
            <InputIcon class="pi pi-pen-to-square" />
            <InputText
                id="altTitle"
                v-on:keydown.enter.prevent
                fluid
            />
          </IconField>
        </FormField>
        <Divider align="center">
          <b class="px-2">Daten</b>
        </Divider>
        <div class="flex flex-row space-x-3">
          <FormField v-slot="$field" name="volume" class="flex flex-col gap-1 w-full">
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
          <FormField v-slot="$field" name="issue" class="flex flex-col gap-1 w-full">
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
        <div class="flex flex-row space-x-3">
          <FormField v-slot="$field" name="pageNumber" class="flex flex-col gap-1 w-full">
            <label for="pageNumber" class="font-bold">Seite(n)</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputText
                  id="pageNumber"
                  placeholder="12-15"
                  v-on:keydown.enter.prevent
                  fluid
              />
            </IconField>
          </FormField>
          <FormField v-slot="$field" name="pageCount" class="flex flex-col gap-1 w-full">
            <label for="pageCount" class="font-bold">Umfang</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputNumber
                id="pageCount"
                placeholder="2"
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
        <FormField v-slot="$field" name="pictureCount" class="flex flex-col gap-1 flex-auto">
          <label for="pictureCount" class="font-bold">Anzahl der Fotografien</label>
          <IconField>
            <InputIcon class="pi pi-pen-to-square" />
            <InputText
                id="pictureCount"
                placeholder="vier Fotos"
                v-on:keydown.enter.prevent
                fluid
            />
          </IconField>
        </FormField>
        <div class="flex flex-row gap-6 flex-wrap">
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
          <FormField v-slot="$field" name="languages" class="flex flex-col gap-1 w-full">
            <label for="languages" class="font-bold">Sprache(n)</label>
            <MultiSelect
                inputId="authors"
                placeholder="Sprachen auswählen"
                selectedItemsLabel="{0} Sprachen ausgewählt"
                optionLabel="name"
                :maxSelectedLabels="3"
                :options="languages"
                :key="languages.length"
                :virtual-scroller-options="{ itemSize: 50 }"
                filter fluid showClear
            />
            <Message v-if="$form.languages?.invalid" severity="error" size="small" variant="simple">
              {{ $form.languages.error.message }}
            </Message>
          </FormField>
        </div>
        <FormField v-slot="$field" name="publicationMedium" class="flex flex-col gap-1">
          <label for="publicationMedium" class="font-bold">Publikationsmedium</label>
          <div class="flex flex-row gap-4 flex-nowrap">
            <IconField class="flex-1 min-w-0">
              <InputIcon class="pi pi-book"/>
              <Select
                labelId="publicationMedium"
                placeholder="Publikationsmedium auswählen"
                class="pl-7"
                optionLabel="title"
                :options="publicationMedia"
                :key="publicationMedia.length"
                fluid
              />
            </IconField>
            <NuxtLink to="/publication_media/create" target="_blank">
              <Button icon="pi pi-plus" severity="secondary" aria-label="Add" />
            </NuxtLink>
          </div>
          <Message v-if="$form.publicationMedium?.invalid" severity="error" size="small" variant="simple">
            {{ $form.publicationMedium.error.message }}
          </Message>
        </FormField>
        <Divider align="center">
          <b class="px-2">Personen</b>
        </Divider>
        <div class="flex flex-row gap-4">
          <NuxtLink to="/persons/create" target="_blank">
            <Button icon="pi pi-plus" severity="secondary" aria-label="Add" label="Person erstellen"/>
          </NuxtLink>
        </div>
        <div class="flex flex-row space-x-3">
          <FormField v-slot="$field" name="authors" class="flex flex-col gap-1 w-full">
            <label for="authors" class="font-bold">Autor:innen</label>
            <MultiSelect
                inputId="authors"
                placeholder="Autor:innen auswählen"
                selectedItemsLabel="{0} Personen ausgewählt"
                :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
                :optionValue="opt => ({id: opt.id, fullName: opt.fullName, studioName: opt.studioName, pseudonyms: opt.pseudonyms})"
                :maxSelectedLabels="2"
                :options="persons"
                :key="persons.length"
                :virtual-scroller-options="{ itemSize: 50 }"
                filter fluid
            />
            <Message v-if="$form.authors?.invalid" severity="error" size="small" variant="simple">
              {{ $form.authors.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="photographers" class="flex flex-col gap-1 w-full">
            <label for="photographers" class="font-bold">Fotograf:innen</label>
            <MultiSelect
                inputId="photographers"
                placeholder="Fotograf:innen auswählen"
                selectedItemsLabel="{0} Personen ausgewählt"
                :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
                :optionValue="opt => ({id: opt.id, fullName: opt.fullName, studioName: opt.studioName, pseudonyms: opt.pseudonyms})"
                :maxSelectedLabels="2"
                :options="persons"
                :key="persons.length"
                :virtual-scroller-options="{ itemSize: 50 }"
                filter fluid
            />
            <Message v-if="$form.photographers?.invalid" severity="error" size="small" variant="simple">
              {{ $form.photographers.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="otherContributors" class="flex flex-col gap-1 w-full">
            <label for="otherContributors" class="font-bold">Sonstige Mitwirkende</label>
            <MultiSelect
                inputId="otherContributors"
                placeholder="Sonstige Mitwirkende auswählen"
                selectedItemsLabel="{0} Personen ausgewählt"
                :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
                :optionValue="opt => ({id: opt.id, fullName: opt.fullName, studioName: opt.studioName, pseudonyms: opt.pseudonyms})"
                :maxSelectedLabels="2"
                :options="persons"
                :key="persons.length"
                :virtual-scroller-options="{ itemSize: 50 }"
                filter fluid
            />
            <Message v-if="$form.otherContributors?.invalid" severity="error" size="small" variant="simple">
              {{ $form.otherContributors.error.message }}
            </Message>
          </FormField>
        </div>
        <Divider align="center">
          <b class="px-2">Tags</b>
        </Divider>
        <div class="flex flex-row space-x-3">
          <FormField v-slot="$field" name="themes" class="flex flex-col gap-1 w-full">
            <label for="themes" class="font-bold">Thematik</label>
            <MultiSelect
                inputId="themes"
                placeholder="Thematiken auswählen"
                selectedItemsLabel="{0} Thematiken ausgewählt"
                optionLabel="value"
                :options="keywords"
                :key="keywords.length"
                :virtual-scroller-options="{ itemSize: 50 }"
                :maxSelectedLabels="3"
                filter fluid
            />
            <Message v-if="$form.themes?.invalid" severity="error" size="small" variant="simple">
              {{ $form.themes.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="imageMotifs" class="flex flex-col gap-1 w-full">
            <label for="authors" class="font-bold">Bildmotiv</label>
            <MultiSelect
                inputId="imageMotifs"
                placeholder="Bildmotive auswählen"
                selectedItemsLabel="{0} Bildmotive ausgewählt"
                optionLabel="value"
                :options="keywords"
                :key="keywords.length"
                :virtual-scroller-options="{ itemSize: 50 }"
                :maxSelectedLabels="3"
                filter fluid
            />
            <Message v-if="$form.imageMotifs?.invalid" severity="error" size="small" variant="simple">
              {{ $form.imageMotifs.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="form" class="flex flex-col gap-1 w-full">
            <label for="form" class="font-bold">Format</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputText
                  id="form"
                  v-on:keydown.enter.prevent
                  fluid
              />
            </IconField>
          </FormField>
        </div>
        <Divider align="center">
          <b class="px-2">Links</b>
        </Divider>
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
        <div class="flex flex-row space-x-3">
          <FormField v-slot="$field" name="iiifManifest" class="flex flex-col gap-1 w-full">
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
          <FormField v-slot="$field" name="manifestPageNumber" class="flex flex-col gap-1 w-full">
            <label for="pageCount" class="font-bold">IIIF-Seite</label>
            <IconField>
              <InputIcon class="pi pi-pen-to-square" />
              <InputNumber
                  id="manifestPageNumber"
                  placeholder="257"
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
        <Divider align="center">
          <b class="px-2">Dateien</b>
        </Divider>
        <FormField v-slot="$field" name="images" class="flex flex-col gap-1">
          <label for="images" class="font-bold">Dateien</label>
          <div class="flex flex-row gap-4 flex-nowrap">
            <MultiSelect
                inputId="images"
                placeholder="Dateien auswählen"
                class="flex-1 min-w-0"
                selectedItemsLabel="{0} Dateien ausgewählt"
                :options="files"
                optionLabel="originalFilename"
                :virtual-scroller-options="{ itemSize: 50 }"
                :key="files.length"
                :maxSelectedLabels="2"
                fluid filter
            >
              <template #option="slotProps">
                <div class="flex flex-row space-x-2">
                  <Avatar
                      :image="fileStore.getImagePreview(`/api/uploads/${slotProps.option.filename}`)"
                      shape="square"
                      oncontextmenu="return false;"
                  />
                  <p>{{ slotProps.option.originalFilename }}</p>
                </div>
              </template>
            </MultiSelect>
            <NuxtLink to="/files" target="_blank">
              <Button icon="pi pi-plus" severity="secondary" aria-label="Add" />
            </NuxtLink>
          </div>
          <Message v-if="$form.files?.invalid" severity="error" size="small" variant="simple">
            {{ $form.files.error.message }}
          </Message>
        </FormField>
        <Divider align="center">
          <b class="px-2">Urheberrecht</b>
        </Divider>
        <div class="flex flex-row space-x-3">
          <FormField v-slot="$field" name="copyrightStatusImage" class="flex flex-col gap-1 w-full">
            <label for="copyrightStatusImage" class="font-bold">Urheberrechtsstatus Bild</label>
            <IconField>
              <InputIcon class="pi pi-shield"/>
              <Select
                labelId="copyrightStatusImage"
                placeholder="Status auswählen"
                class="pl-7"
                optionLabel="value"
                :options="copyrightStatusStore.copyrightStatuses.map(status => ({id: status.id, value: status.value, description: status.description}))"
                fluid
              />
            </IconField>
            <Message v-if="$form.copyrightStatusImage?.invalid" severity="error" size="small" variant="simple">
              {{ $form.copyrightStatusImage.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="copyrightStatusText" class="flex flex-col gap-1 w-full">
            <label for="copyrightStatusText" class="font-bold">Urheberrechtsstatus Text</label>
            <IconField>
              <InputIcon class="pi pi-shield"/>
              <Select
                labelId="copyrightStatusText"
                placeholder="Status auswählen"
                class="pl-7"
                optionLabel="value"
                :options="copyrightStatusStore.copyrightStatuses.map(status => ({id: status.id, value: status.value, description: status.description}))"
                fluid
              />
            </IconField>
            <Message v-if="$form.copyrightStatusText?.invalid" severity="error" size="small" variant="simple">
              {{ $form.copyrightStatusText.error.message }}
            </Message>
          </FormField>
        </div>
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
