<script setup lang="ts">
import type { Person } from "~/utils/types";
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { z } from "zod";

const toast = useToast();
const personStore = usePersonStore();

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  person?: Person;
}>();

const sex = ref([
  { label: "Unbekannt", value: null },
  { label: "Weiblich", value: "weiblich" },
  { label: "Männlich", value: "männlich" }
]);

const resolver = ref(
  zodResolver(
    z.object({
      firstName: z.string().optional().nullable(),
      lastName: z.string().optional().nullable(),
      sex: z.string().optional().nullable(),
      birthYear: z.number().optional().nullable(),
      deathYear: z.number().optional().nullable(),
      pseudonyms: z.array(z.string()).optional(),
      gndId: z.string().optional().nullable(),
    }).refine(data => {
      if (typeof data.birthYear === "number" && typeof data.deathYear === "number") return data.birthYear <= data.deathYear
      return true
    }, {
      message: "Das Geburtsjahr muss älter als das Sterbejahr sein.",
      path: ["deathYear"],
    })
  )
);

const onFormSubmit = async (e: any) => {
  if (e.valid) {
    try {
      if (props.action === "create") {
        await personStore.createPerson(e.values);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich erstellt", life: 3000});
        e.reset();
      } else if (props.action === "edit" && props.person?.id) {
        await personStore.updatePerson(e.values, props.person.id);
        toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich aktualisiert", life: 3000});
        navigateTo(`/persons/${props.person?.id}`);
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
    <h1 class="text-2xl outfit-headline text-primary-blue font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um eine Person zu erstellen oder anzupassen.
    </p>

    <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-none">
      <Form
        v-slot="$form"
        class="flex flex-col gap-4"
        :resolver
        :initialValues="props.person ? props.person : {}"
        :key="props.person ? props.person.id : 'new'"
        @submit="onFormSubmit"
      >
        <div class="flex flex-row gap-6 flex-wrap">
          <FormField v-slot="$field" name="firstName" class="flex flex-col gap-1 flex-1">
            <label for="firstName" class="font-bold">Vorname</label>
            <IconField>
              <InputIcon class="pi pi-user-edit" />
              <InputText
                id="firstName"
                placeholder="Johann Wolfgang"
                v-on:keydown.enter.prevent
                fluid
              />
            </IconField>
            <Message v-if="$form.firstName?.invalid" severity="error" size="small" variant="simple">
              {{ $form.firstName.error.message }}
            </Message>
          </FormField>

          <FormField v-slot="$field" name="lastName" class="flex flex-col gap-1 flex-1">
            <label for="lastName" class="font-bold">Nachname</label>
            <IconField>
              <InputIcon class="pi pi-user-edit" />
              <InputText
                id="lastName"
                placeholder="von Goethe"
                v-on:keydown.enter.prevent
                fluid
              />
            </IconField>
            <Message v-if="$form.lastName?.invalid" severity="error" size="small" variant="simple">
              {{ $form.lastName.error.message }}
            </Message>
          </FormField>
        </div>
        <div class="flex flex-row gap-6 flex-wrap">
          <FormField v-slot="$field" name="sex" class="flex flex-col gap-1 flex-1">
            <label for="sex" class="font-bold">Geschlecht</label>
            <IconField>
              <InputIcon class="pi pi-mars"/>
              <Select
                labelId="sex"
                placeholder="Geschlecht auswählen"
                class="pl-7"
                optionLabel="label"
                optionValue="value"
                :options="sex"
                fluid
              />
            </IconField>
            <Message v-if="$form.sex?.invalid" severity="error" size="small" variant="simple">
              {{ $form.sex.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="birthYear" class="flex flex-col gap-1 flex-1">
            <label for="birthYear" class="font-bold">Geburtsjahr</label>
            <IconField>
              <InputIcon class="pi pi-calendar" />
              <InputNumber
                id="birthYear"
                placeholder="1749"
                :min="0"
                :max="3000"
                :useGrouping="false"
                v-on:keydown.enter.prevent
                fluid
              />
            </IconField>
            <Message v-if="$form.birthYear?.invalid" severity="error" size="small" variant="simple">
              {{ $form.birthYear.error.message }}
            </Message>
          </FormField>
          <FormField v-slot="$field" name="deathYear" class="flex flex-col gap-1 flex-1">
            <label for="deathYear" class="font-bold">Sterbejahr</label>
            <IconField>
              <InputIcon class="pi pi-calendar" />
              <InputNumber
                id="deathYear"
                placeholder="1832"
                :min="0"
                :max="3000"
                :useGrouping="false"
                v-on:keydown.enter.prevent
                fluid
              />
            </IconField>
            <Message v-if="$form.deathYear?.invalid" severity="error" size="small" variant="simple">
              {{ $form.deathYear.error.message }}
            </Message>
          </FormField>
        </div>
        <FormField v-slot="$field" name="pseudonyms">
          <label for="pseudonyms" class="font-bold">Pseudonyme</label>
          <AutoComplete
            inputId="pseudonyms"
            placeholder="Eingabe mit Enter bestätigen"
            :typeahead="false"
            multiple
            fluid
          />
          <Message v-if="$form.pseudonyms?.invalid" severity="error" size="small" variant="simple">
            {{ $form.pseudonyms.error.message }}
          </Message>
        </FormField>
        <FormField v-slot="$field" name="gndId" class="flex flex-col gap-1 flex-auto">
          <label for="gndId" class="font-bold">GND-ID</label>
          <IconField>
            <InputIcon class="pi pi-book" />
            <InputText
              id="gndId"
              placeholder="118540238"
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
