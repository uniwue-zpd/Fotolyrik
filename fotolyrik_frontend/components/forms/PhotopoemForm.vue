<script setup lang="ts">
import type { PhotoPoem } from "~/utils/types";

const props = defineProps<{
  action: 'create' | 'edit';
  header: string;
  photopoem?: PhotoPoem;
}>();
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
        :actions="false"
        :value="props.photopoem ? props.photopoem : {}"
        :key="props.photopoem?.id || 'create'"
        #default="{ value }"
    >
      <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-[#F1F2F2]">
        <FormKit
            type="text"
            name="title"
            label="Titel"
            placeholder="Telephon-Tragödie"
            prefix-icon="text"
            outer-class="max-w-full"
        />
        <div class="flex flex-row space-x-5">
          <FormKit
              type="number"
              name="volume"
              label="Jahrgang"
              placeholder="5"
              prefix-icon="number"
              outer-class="max-w-full"
          />
          <FormKit
              type="number"
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
              name="page_number"
              label="Seite"
              placeholder="23"
              prefix-icon="number"
              outer-class="max-w-full"
          />
          <FormKit
              type="number"
              name="page_count"
              label="Seitenanzahl"
              placeholder="2"
              prefix-icon="number"
              outer-class="max-w-full"
          />
        </div>
        <FormKit
            type="text"
            name="publication_date"
            label="Publikationsdatum"
            placeholder="01.03.1930"
            prefix-icon="date"
            outer-class="max-w-full"
        />
        <FormKit
            type="select"
            name="publication_medium"
            label="Publikationsmedium"
            placeholder="Revue des Monats"
            outer-class="max-w-full"
            select-icon="select"
        />
        <div class="flex flex-row space-x-5">
          <FormKit
              type="select"
              name="author"
              label="Autor:in"
              outer-class="max-w-full"
              select-icon="select"
          />
          <FormKit
              type="select"
              name="photographer"
              label="Fotograf:in"
              outer-class="max-w-full"
              select-icon="select"
          />
        </div>
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
              name="iiif_manifest"
              label="IIIF-Manifest"
              placeholder="https://www.example.com..."
              prefix-icon="link"
              outer-class="max-w-full"
          />
        </div>
        <FormKit
            type="file"
            name="images"
            label="Bilder"
            accept=".jpg,.png"
            multiple="true"
            file-item-icon="fileImage"
            no-files-icon="fileImage"
            file-remove-icon="trash"
            outer-class="max-w-full"
        />
        <div class="border-solid border-2 rounded-md p-5 bg-[#F1F2F5] mb-2">
          <div class="font-mono">JSON-Preview</div>
          <hr>
          <pre wrap>{{ value }}</pre>
        </div>
        <FormKit
            type="submit"
            label="Erstellen"
        />
      </div>
    </FormKit>
  </div>
</template>

<style scoped>

</style>
