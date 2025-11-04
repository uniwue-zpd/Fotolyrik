<script setup lang="ts">
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import { getNode } from '@formkit/core';
import type { PhotoPoemDTO } from "~/utils/types";

const props = defineProps<{
  action: 'create' | 'edit';
  header: string;
  photopoem?: PhotoPoemDTO;
}>();

const toast = useToast();
const submitted = ref(false);
const person_store = usePersonStore();
const photopoem_store = usePhotopoemStore();
const pub_medium_store = usePubMediumStore();
const keyword_store = useKeywordStore();
const file_store = useFileStore();

type PhotoPoemInput = Omit<PhotoPoemDTO, 'id' | 'createdBy' | 'createdDate' | 'lastModifiedBy' | 'lastModifiedDate'>;

const submit = async (formData: Partial<PhotoPoemInput>) => {
  try {
    if (props.action === 'create') {
      await photopoem_store.createPhotopoem(formData);
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich erstellt', life: 3000});
      const form = getNode('photopoem_creation');
      form?.reset();
    } else if (props.action === 'edit' && props.photopoem?.id) {
      await photopoem_store.updatePhotopoem(formData, props.photopoem.id);
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich upgedated', life: 3000});
      navigateTo(`/photopoems/${props.photopoem?.id}`);
    }
  } catch (error) {
    console.log(error)
    toast.add({
      severity: 'error',
      summary: 'Fehler',
      detail: 'Fehler beim Erstellen des Fotogedicht-Objektes',
      life: 3000
    });
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
        id="photopoem_creation"
        submit-label="Erstellen"
        @submit="submit"
        :actions="false"
        :value="props.photopoem ? props.photopoem : {}"
        :key="props.photopoem?.id || 'create'"
        #default="{ value }"
    >
      <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-[#F1F2F2]">
        <div class="flex flex-row space-x-3">
          <FormKit
              type="text"
              name="title"
              label="Titel"
              placeholder="Telephon-Tragödie"
              prefix-icon="text"
              outer-class="max-w-full"
              validation="required"
              validation-visibility="live"
          />
          <FormKit
              type="text"
              name="subtitle"
              label="Untertitel"
              prefix-icon="text"
              outer-class="max-w-full"
          />
          <FormKit
              type="text"
              name="altTitle"
              label="Alternativer Titel"
              prefix-icon="text"
              outer-class="max-w-full"
          />
        </div>
        <Divider/>
        <div class="flex flex-row space-x-5">
          <FormKit
              type="number"
              number
              name="volume"
              label="Jahrgang"
              placeholder="5"
              prefix-icon="number"
              outer-class="max-w-full"
          />
          <FormKit
              type="number"
              number
              name="issue"
              label="Ausgabe"
              placeholder="1"
              prefix-icon="number"
              outer-class="max-w-full"
          />
        </div>
        <div class="flex flex-row space-x-5">
          <FormKit
              type="number"
              number
              name="pageNumber"
              label="Seite"
              placeholder="23"
              prefix-icon="number"
              outer-class="max-w-full"
          />
          <FormKit
              type="number"
              number
              name="pageCount"
              label="Seitenanzahl"
              placeholder="2"
              prefix-icon="number"
              outer-class="max-w-full"
          />
        </div>
        <FormKit
            type="text"
            name="publicationDate"
            label="Publikationsdatum"
            placeholder="01.03.1930"
            prefix-icon="date"
            outer-class="max-w-full"
        />
        <FormKit
            type="select"
            name="publicationMedium"
            label="Publikationsmedium"
            outer-class="max-w-full"
            select-icon="select"
            :options="pub_medium_store.pub_media.map(p => ({ label: `${p.title}`, value: { id: p.id, title: p.title } }))"
        />
        <Divider/>
        <div class="flex flex-row space-x-5">
          <FormKit
              type="select"
              multiple
              name="authors"
              label="Autor:innen"
              outer-class="max-w-full"
              select-icon="select"
              :options="person_store.persons.map(p => ({ label: `${p.fullName}`, value: {id: p.id, fullName: p.fullName} }))"
              help="Halten Sie die Strg-Taste gedrückt, um mehrere Personen auszuwählen"
          />
          <FormKit
              type="select"
              multiple
              name="photographers"
              label="Fotograf:innen"
              outer-class="max-w-full"
              select-icon="select"
              :options="person_store.persons.map(p => ({ label: `${p.fullName}`, value: {id: p.id, fullName: p.fullName} }))"
              help="Halten Sie die Strg-Taste gedrückt, um mehrere Personen auszuwählen"
          />
          <FormKit
              type="select"
              multiple
              name="otherContributors"
              label="Sonstige Mitwirkende"
              outer-class="max-w-full"
              select-icon="select"
              :options="person_store.persons.map(p => ({ label: `${p.fullName}`, value: {id: p.id, fullName: p.fullName} }))"
              help="Halten Sie die Strg-Taste gedrückt, um mehrere Personen auszuwählen"
          />
        </div>
        <Divider/>
        <div class="flex flex-col gap-2">
          <FormKit
              type="select"
              multiple
              name="themes"
              label="Thematik"
              outer-class="max-w-full"
              select-icon="select"
              :options="keyword_store.keywords.map(p => ({ label: `${p.value}`, value: {id: p.id, value: p.value} }))"
              help="Halten Sie die Strg-Taste gedrückt, um mehrere Schlagworte auszuwählen"
          />
          <FormKit
              type="select"
              multiple
              name="imageMotifs"
              label="Bildmotiv"
              outer-class="max-w-full"
              select-icon="select"
              :options="keyword_store.keywords.map(p => ({ label: `${p.value}`, value: {id: p.id, value: p.value} }))"
              help="Halten Sie die Strg-Taste gedrückt, um mehrere Schlagworte auszuwählen"
          />
          <FormKit
              type="text"
              name="form"
              label="Format"
              placeholder="B x H x L"
              prefix-icon="text"
              outer-class="max-w-full"
          />
        </div>
        <Divider/>
        <div class="flex flex-row space-x-5">
          <FormKit
              type="url"
              name="link"
              label="Link"
              placeholder="https://www.example.com..."
              prefix-icon="link"
              outer-class="max-w-full"
          />
          <FormKit
              type="url"
              name="iiifManifest"
              label="IIIF-Manifest"
              placeholder="https://www.example.com..."
              prefix-icon="link"
              outer-class="max-w-full"
          />
        </div>
        <Divider/>
        <FormKit
            type="select"
            id="images"
            multiple
            name="images"
            label="Bilder"
            outer-class="max-w-full"
            select-icon="select"
            :options="file_store.files.map(p => ({label: `${p.originalFilename}`, value: {id: p.id, filename: p.filename, originalFilename: p.originalFilename}}))"
            help="Halten Sie die Strg-Taste gedrückt, um mehrere Schlagworte auszuwählen"
        />
        <Divider/>
        <!-- TODO: Languages and copyright statuses have to be fetched from corresponding stores -->
        <div class="border-solid border-2 rounded-md p-5 bg-[#F1F2F5] mb-2">
          <div class="font-mono">JSON-Preview</div>
          <hr>
          <pre wrap class="text-sm">{{ value }}</pre>
        </div>
        <FormKit
            type="submit"
            :label="action === 'create' ? 'Erstellen' : 'Ändern'"
        />
      </div>
    </FormKit>
  </div>
</template>

<style scoped>

</style>
