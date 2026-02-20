<script setup lang="ts">
import {zodResolver} from "@primevue/forms/resolvers/zod";
import {z} from "zod";

const toast = useToast();
const keywordStore = useKeywordStore();

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  keyword?: Keyword;
}>();

const resolver = ref(
    zodResolver(
        z.object({
          value: z.string().nonoptional(),
          gndId: z.string().optional().nullable(),
        })
    )
);

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    try {
      if (props.action === "create") {
        await keywordStore.createKeyword(e.values);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich erstellt", life: 3000});
        e.reset();
      } else if (props.action === "edit" && props.keyword?.id) {
        await keywordStore.updateKeyword(e.values, props.keyword.id);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich aktualisiert", life: 3000});
        navigateTo(`/keywords/${props.keyword?.id}`);
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
      Füllen Sie bitte die untenstehenden Felder aus, um ein Schlagwort zu erstellen oder anzupassen.
    </p>
    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Form
          v-slot="$form"
          class="flex flex-col gap-4"
          :resolver
          :initialValues="props.keyword ? props.keyword : {}"
          :key="props.keyword ? props.keyword.id : 'new'"
          @submit="onFormSubmit"
      >
        <FormField v-slot="$field" name="value" class="flex flex-col gap-1 flex-1">
          <label for="value" class="font-bold">Begriff</label>
          <IconField>
            <InputIcon class="pi pi-user-edit" />
            <InputText
                id="value"
                placeholder="Alter"
                v-on:keydown.enter.prevent
                fluid
            />
          </IconField>
          <Message v-if="$form.value?.invalid" severity="error" size="small" variant="simple">
            {{ $form.value.error.message }}
          </Message>
        </FormField>
        <FormField v-slot="$field" name="gndId" class="flex flex-col gap-1 flex-1">
          <label for="gndId" class="font-bold">GND-ID</label>
          <IconField>
            <InputIcon class="pi pi-user-edit" />
            <InputText
                id="gndId"
                placeholder="4001446-0"
                v-on:keydown.enter.prevent
                fluid
            />
          </IconField>
          <Message v-if="$form.value?.invalid" severity="error" size="small" variant="simple">
            {{ $form.value.error.message }}
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