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
            validation="required"
            validation-visibility="live"
        />
        <Divider/>
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
        <Divider/>
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
          <FormKit
              type="select"
              multiple
              name="other_contributors"
              label="Sonstige Mitwirkende"
              outer-class="max-w-full"
              select-icon="select"
          />
        </div>
        <Divider/>
        <div class="flex flex-col gap-2">
          <FormKit type="list" :value="[]" name="themes" dynamic #default="{ items, node, value }">
            <FormKit
                v-for="(item, index) in items"
                :key="item"
                :index="index"
                label="Thematik"
                placeholder="Philosophie"
                suffix-icon="trash"
                @suffix-icon-click="() => node.input(value?.filter((_, i) => i !== index))"
                :sections-schema="{ suffixIcon: { $el: 'button' } }"
                outer-class="max-w-full"
            />
            <FormKit type="button" @click="() => node.input(value?.concat(''))">Thematik hinzufügen</FormKit>
          </FormKit>
          <FormKit type="list" :value="[]" name="topics" dynamic #default="{ items, node, value }">
            <FormKit
                v-for="(item, index) in items"
                :key="item"
                :index="index"
                label="Kategorie"
                placeholder="Verkehr"
                suffix-icon="trash"
                @suffix-icon-click="() => node.input(value?.filter((_, i) => i !== index))"
                :sections-schema="{ suffixIcon: { $el: 'button' } }"
                outer-class="max-w-full"
            />
            <FormKit type="button" @click="() => node.input(value?.concat(''))">Kategorien hinzufügen</FormKit>
          </FormKit>
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
              name="iiif_manifest"
              label="IIIF-Manifest"
              placeholder="https://www.example.com..."
              prefix-icon="link"
              outer-class="max-w-full"
          />
        </div>
        <Divider/>
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
        <Divider/>
        <div class="flex flex-row space-x-5">
          <FormKit
              type="select"
              name="copyright_status_image"
              label="Urheberrechtsstatus Bild"
              outer-class="max-w-full"
              select-icon="select"
              :options="[
                  {label: '', value: null},
                  {label: 'Ungeklärt', value: 'Ungeklärt'},
                  {label: 'rechtefrei 70 Jahre', value: 'rechtefrei 70 Jahre'},
                  {label: 'eingeholt (schriftlich)', value: 'eingeholt (schriftlich)'}
              ]"
          />
          <FormKit
              type="select"
              name="copyright_status_text"
              label="Urheberrechtsstatus Text"
              outer-class="max-w-full"
              select-icon="select"
              :options="[
                  {label: '', value: null},
                  {label: 'Ungeklärt', value: 'Ungeklärt'},
                  {label: 'rechtefrei 70 Jahre', value: 'rechtefrei 70 Jahre'},
                  {label: 'eingeholt (schriftlich)', value: 'eingeholt (schriftlich)'}
              ]"
          />
        </div>
        <FormKit
            type="select"
            name="language"
            label="Sprache"
            outer-class="max-w-full"
            select-icon="select"
            :options="[
                {label: '', value: null},
                {label: 'Deutsch', value: 'German'},
                {label: 'Englisch', value: 'English'},
                {label: 'Französisch', value: 'French'}
              ]"
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
