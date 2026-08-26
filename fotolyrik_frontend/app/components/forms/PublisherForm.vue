<script setup lang="ts">
import {zodResolver} from "@primevue/forms/resolvers/zod";
import {z} from "zod";

const publisherApi = usePublisher();
const toast = useToast();

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  publisher?: PublisherDTO | null;
}>();

const resolver = ref(
    zodResolver(
        z.object({
          name: z.string("Bitte geben Sie einen Namen ein").nonempty("Name darf nicht leer sein"),
          description: z.string().optional().nullable(),
        })
    )
);

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    try {
      if (props.action === "create") {
        await publisherApi.createPublisher(e.values);
        await refreshNuxtData('publisher-list');
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich erstellt", life: 3000});
        e.reset();
      } else if (props.action === "edit" && props.publisher?.id) {
        await publisherApi.updatePublisher(props.publisher.id, e.values);
        await Promise.all( [refreshNuxtData('publisher-list'), await refreshNuxtData(`publisher-${props.publisher.id}`)])
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich aktualisiert", life: 3000});
        navigateTo(`/keywords/${props.publisher?.id}`);
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
      Füllen Sie bitte die untenstehenden Felder aus, um einen Verlag zu erstellen oder anzupassen.
    </p>
    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Form
          v-slot="$form"
          class="flex flex-col gap-4"
          :resolver
          :initialValues="props.publisher ? props.publisher : {}"
          :key="props.publisher ? props.publisher.id : 'new'"
          @submit="onFormSubmit"
      >
        <FormField v-slot="$field" name="name" class="flex flex-col gap-1 flex-1">
          <label for="name" class="font-bold">Name</label>
          <IconField>
            <InputIcon class="pi pi-user-edit" />
            <InputText
                id="name"
                placeholder="Girardet"
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
            <InputIcon class="pi pi-user-edit" />
            <InputText
                id="description"
                placeholder="Verlag für ..."
                v-on:keydown.enter.prevent
                fluid
            />
          </IconField>
          <Message v-if="$form.description?.invalid" severity="error" size="small" variant="simple">
            {{ $form.description.error.message }}
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