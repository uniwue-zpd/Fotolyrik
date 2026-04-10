<script setup lang="ts">
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";

const toast = useToast();
const locationStore = useLocationStore();

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  location?: LocationDTO;
}>();

const resolver = ref(
    zodResolver(
        z.object({
          name: z.string().min(1, "Name ist erforderlich"),
          description: z.string().optional().nullable(),
        })
    )
);

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    try {
      if (props.action === "create") {
        await locationStore.createLocation(e.values);
        toast.add({ severity: "success", summary: "Erfolg", detail: "Ort erfolgreich erstellt", life: 3000 });
        e.reset();
      } else if (props.action === "edit" && props.location?.id) {
        await locationStore.updateLocation(e.values, props.location.id);
        toast.add({ severity: "success", summary: "Erfolg", detail: "Ort erfolgreich aktualisiert", life: 3000 });
        navigateTo(`/locations/${props.location?.id}`);
      }
    } catch (error) {
      console.error(error);
      toast.add({ severity: "error", summary: "Fehler", detail: "Ein Fehler ist aufgetreten", life: 3000 });
    }
  }
};
</script>

<template>
  <div class="flex flex-col mx-auto w-[70%] gap-4">
    <h1 class="text-2xl outfit-headline text-primary font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um einen Ort zu erstellen oder anzupassen.
    </p>
    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Form
          v-slot="$form"
          class="flex flex-col gap-4"
          :resolver
          :initialValues="props.location ? props.location : {}"
          :key="props.location ? props.location.id : 'new'"
          @submit="onFormSubmit"
      >
        <FormField v-slot="$field" name="name" class="flex flex-col gap-1 flex-1">
          <label for="name" class="font-bold">Fundort Name</label>
          <IconField>
            <InputIcon class="pi pi-map-marker" />
            <InputText
                id="name"
                placeholder="z.B. Janusz-Korczak-Bibliothek"
                v-on:keydown.enter.prevent
                fluid
            />
          </IconField>
          <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
            {{ $form.name.error.message }}
          </Message>
        </FormField>

        <FormField v-slot="$field" name="description" class="flex flex-col gap-1 flex-1">
          <label for="description" class="font-bold">Beschreibung</label>
          <IconField>
            <InputIcon class="pi pi-link" />
            <InputText
                id="description"
                placeholder="Eine Moderne Bibliothek in Berlin-Pankow..."
                v-on:keydown.enter.prevent
                fluid
            />
          </IconField>
          <Message v-if="$form.gndId?.invalid" severity="error" size="small" variant="simple">
            {{ $form.gndId.error.message }}
          </Message>
        </FormField>

        <Button type="submit" severity="primary">
          {{ (props.action === "create") ? "Erstellen" : "Bearbeiten" }}
        </Button>
      </Form>
    </div>
  </div>
</template>

<style scoped>
</style>
