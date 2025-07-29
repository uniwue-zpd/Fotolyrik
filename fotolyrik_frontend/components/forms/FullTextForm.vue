<script setup lang="ts">
import type { FullText } from "~/utils/types";
import { getNode } from "@formkit/core";
import { useToast } from "primevue/usetoast";
import { ref } from "vue";
import apiClient from "~/service/api";

const props = defineProps<{
  action: 'create' | 'edit' | 'edit-by-photopoem';
  header: string;
  fulltext?: FullText;
}>();

const toast = useToast();
const submitted = ref(false);
const photopoem_store = usePhotopoemStore();

type FullTextInput = Omit<FullText, 'id' | 'createdBy' | 'createdDate' | 'lastModifiedBy' | 'lastModifiedDate'>;

const submit = async (formData: Partial<FullTextInput>) => {
  try {
    if (props.action === 'create') {
      await apiClient.post('/fulltexts', formData);
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich erstellt', life: 3000});
      const form = getNode('fulltext_creation');
      form?.reset();
    } else if (props.action === 'edit' && props.fulltext?.id) {
      await apiClient.put(`/fulltexts/${props.fulltext.id}`, formData);
      submitted.value = true;
      toast.add({severity: 'success', summary: 'Erfolg', detail: 'Erfolgreich upgedated', life: 3000});
      navigateTo(`/fulltexts/${props.fulltext?.id}`);
    }
  } catch (error) {
    console.error(error);
    toast.add({
      severity: 'error',
      summary: 'Fehler',
      detail: 'Fehler beim Erstellen des Volltext-Objektes',
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
        id="fulltext_creation"
        submit-label="Erstellen"
        @submit="submit"
        :actions="false"
        :value="props.fulltext ? props.fulltext : {}"
        :key="props.fulltext?.id || 'create'"
        #default="{ value }"
    >
      <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-[#F1F2F2]">
        <FormKit
            type="select"
            :disabled="props.action === 'edit-by-photopoem'"
            name="photopoem"
            label="Fotogedicht"
            outer-class="max-w-full"
            select-icon="select"
            :options="photopoem_store.photopoems.map(p => ({label: `${p.title}`, value: {'id': p.id}}))"
        />
        <FormKit
            type="textarea"
            name="fullText"
            label="Volltext"
            placeholder="Die Sonne tönt nach alter Weise..."
            prefix-icon="textarea"
            outer-class="max-w-full min-w-[0%]"
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
