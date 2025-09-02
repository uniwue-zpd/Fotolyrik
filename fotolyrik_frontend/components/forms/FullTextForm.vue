<script setup lang="ts">
import type { FullText } from "~/utils/types";
import { useToast } from "primevue/usetoast";
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";
import apiClient from "~/service/api";

const toast = useToast();
const photopoemStore = usePhotopoemStore();

const photopoemLoading = ref(false);

onMounted(() => {
  photopoemStore.fetchPhotopoems();
});

const props = defineProps<{
  action: "create" | "edit" | "edit-by-photopoem";
  header: string;
  fulltext?: FullText;
}>();

const resolver = ref(
  zodResolver(
    z.object({
      photopoem: z.object({}, {message: "Wählen Sie ein Fotogedicht aus."}),
      fullText: z.string("Geben Sie einen Volltext ein."),
    })
  )
);

const onPhotopoemReload = async () => {
  if (!photopoemLoading.value) {
    photopoemLoading.value = true;
    await photopoemStore.fetchPhotopoems(true);
    photopoemLoading.value = false;
  }
};

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    try {
      if (props.action === "create") {
        await apiClient.post("/fulltexts", e.values);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich erstellt", life: 3000});
        e.reset();
      } else if (props.action === "edit" && props.fulltext?.id) {
        await apiClient.put(`/fulltexts/${props.fulltext.id}`, e.values);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich upgedated", life: 3000});
      }
    } catch (error) {
      console.log(error);
      toast.add({severity: "error", summary: "Fehler", detail: "Fehler beim Senden der Nachricht", life: 3000});
      navigateTo(`/fulltexts/${props.fulltext?.id}`);
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
        :initialValues="props.fulltext ? props.fulltext : {}"
        :key="props.fulltext ? props.fulltext.id : 'new'"
        @submit="onFormSubmit" 
      >
        <!-- Photopoem select -->
        <FormField v-slot="$field" name="photopoem" class="flex flex-col gap-1 flex-1">
          <label for="photopoem" class="font-bold">Fotogedicht</label>
          <div class="flex flex-row gap-4 flex-nowrap">
            <IconField class="flex-1 min-w-0">
              <InputIcon class="pi pi-book"/>
              <Select 
                labelId="photopoem" 
                placeholder="Photogedicht auswählen"
                class="pl-7" 
                optionLabel="label"
                optionValue="value"
                :options="photopoemStore.photopoems.map(p => ({ label: `${p.title}`, value: p }))"
                :key="photopoemStore.photopoems.length"
                fluid
              />
            </IconField>
            <Button 
              icon="pi pi-refresh" 
              severity="secondary" 
              aria-label="Reload" 
              :loading="photopoemLoading" 
              @click="onPhotopoemReload"
            />
            <NuxtLink to="/photopoems/create" target="_blank">
              <Button icon="pi pi-plus" severity="secondary" aria-label="Add" />
            </NuxtLink>
          </div> 
          <Message v-if="$form.photopoem?.invalid" severity="error" size="small" variant="simple" class="flex-auto">
            {{ $form.photopoem.error.message }}
          </Message>
        </FormField>
        
        <!-- Fulltext field -->
        <FormField v-slot="$field" name="fullText" class="flex flex-col gap-1">
          <label for="fullText" class="font-bold">Volltext</label>
          <Textarea
            id="fullText"
            placeholder="Die Sonne tönt nach alter Weise..."
            rows="10"
            autoResize
            fluid
          />
          <Message v-if="$form.fullText?.invalid" severity="error" size="small" variant="simple">
            {{ $form.fullText.error.message }}
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
