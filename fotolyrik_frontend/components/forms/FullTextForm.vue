<script setup lang="ts">
import type { FullText } from "~/utils/types";
import { useToast } from "primevue/usetoast";
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";

const toast = useToast();
const photopoemStore = usePhotopoemStore();
const fullTextStore = useFullTextStore();
const photopoems = computed(() => photopoemStore.photopoems.map(p => ({ id: p.id, title: p.title, altTitle: p.altTitle })));

const photopoemLoading = ref(false);

const props = defineProps<{
  action: "create" | "edit" | "edit-by-photopoem";
  header: string;
  fulltext?: FullText;
}>();

const resolver = ref(
  zodResolver(
    z.object({
      photopoem: z.object({
        id: z.number().min(1),
        title: z.string().optional().nullable(),
        altTitle: z.string().optional().nullable(),
      }),
      fullText: z.string("Geben Sie einen Volltext ein."),
    })
  )
);

const onPhotopoemReload = async () => {
  if (!photopoemLoading.value) {
    photopoemLoading.value = true;
    await photopoemStore.refreshPhotopoemsData();
    photopoemLoading.value = false;
  }
};

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    console.log("Submitting form with values:", e.values);
    try {
      if (props.action === "create") {
        await fullTextStore.createFullText(e.values);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich erstellt", life: 3000});
        e.reset();
      } else if (props.action === "edit" && props.fulltext?.id) {
        await fullTextStore.updateFullText(props.fulltext.id, e.values);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich upgedated", life: 3000});
      }
    } catch (error) {
      console.log(error);
      toast.add({severity: "error", summary: "Fehler", detail: "Fehler beim Senden der Nachricht", life: 3000});
    }
  }
};
</script>

<template>
  <div class="flex flex-col mx-auto w-[70%] gap-4">
    <h1 class="text-2xl outfit-headline text-primary font-bold">{{ props.header }}</h1>
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
        <FormField v-slot="$field" name="photopoem" class="flex flex-col gap-1 flex-1">
          <label for="photopoem" class="font-bold">Fotogedicht</label>
          <div class="flex flex-row gap-4 flex-nowrap">
            <IconField class="flex-1 min-w-0">
              <InputIcon class="pi pi-book"/>
              <Select
                labelId="photopoem"
                placeholder="Photogedicht auswählen"
                class="pl-7"
                :optionLabel="(opt) => opt.title ? opt.title : opt.altTitle"
                :options="photopoems"
                :key="photopoems.length"
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
        <Button type="submit" severity="primary">
          {{ (props.action === "create") ? "Erstellen" : "Bearbeiten" }}
        </Button>
        <Fieldset legend="Form States" class="h-80 overflow-auto">
          <pre class="whitespace-pre-wrap">{{ $form }}</pre>
        </Fieldset>
      </Form>
    </div>
  </div>
</template>

<style scoped>

</style>
