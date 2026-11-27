<script setup lang="ts">
import { z } from "zod";
import type { SelectItem } from "@nuxt/ui";

const toast = useToast();
const personApi = usePerson();

const props = defineProps<{
  action: "create" | "edit";
  header: string;
  person?: PersonDTO;
}>();

const sex = ref<SelectItem[]>([
  { label: "Unbekannt", value: null },
  { label: "Weiblich", value: "weiblich" },
  { label: "Männlich", value: "männlich" }
]);

const schema = z.object({
  firstName: z.string().nullish(),
  lastName: z.string().nullish(),
  studioName: z.string().nullish(),
  sex: z.enum(["weiblich", "männlich"]).nullish(),
  birthYear: z.number().nullish(),
  deathYear: z.number().nullish(),
  pseudonyms: z.array(z.string()).optional(),
  gndId: z.string().nullish(),
}).refine(d => d.birthYear == null || d.deathYear == null || d.birthYear <= d.deathYear, {
  message: "Das Geburtsjahr muss vor dem Sterbejahr liegen.",
  path: ["deathYear"],
});

type Schema = z.output<typeof schema>;

const defaults = (): Partial<Schema> => ({
  firstName: null,
  lastName: null,
  studioName: null,
  sex: null,
  birthYear: null,
  deathYear: null,
  pseudonyms: [],
  gndId: null,
});

const state = reactive<Partial<PersonDTO>>({
  ...defaults(),
  ...structuredClone(toRaw(props.person)),
});

const onSubmit = async () => {
  try {
    if (props.action === "create") {
      await personApi.create(state);
      await refreshNuxtData("person-list");
      toast.add({ color: "success", title: "Erfolg", description: "Erfolgreich erstellt", duration: 3000 });
      Object.assign(state, defaults());
    } else if (props.action === "edit" && props.person?.id) {
      await personApi.update(props.person.id, state);
      await Promise.all([refreshNuxtData("person-list"), refreshNuxtData(`person-${props.person.id}`)]);
      toast.add({ color: "success", title: "Erfolg", description: "Erfolgreich aktualisiert", duration: 3000 });
      await navigateTo(`/persons/${props.person.id}`);
    }
  } catch (error) {
    console.error(error);
    toast.add({ color: "error", title: "Fehler", description: "Ein Fehler ist aufgetreten", duration: 3000 });
  }
};
</script>

<template>
  <div class="flex flex-col md:mx-auto md:w-[70%] gap-4">
    <h1 class="text-2xl outfit-headline text-primary font-bold">{{ props.header }}</h1>
    <p class="roboto-plain">
      Füllen Sie bitte die untenstehenden Felder aus, um eine Person zu erstellen oder anzupassen.
    </p>

    <div class="border-2 border-solid rounded-md p-5">
      <UForm :schema="schema" :state="state" @submit="onSubmit">
        <div class="flex flex-col gap-4">
          <div class="flex flex-col gap-4 md:flex-row flex-wrap">
            <UFormField label="Vorname" name="firstName" class="flex-1">
              <UInput v-model.nullable="state.firstName" placeholder="Johann Wolfgang" class="w-full" :ui="{ trailing: 'pe-1' }">
                <template v-if="state.firstName" #trailing>
                  <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.firstName = null" />
                </template>
              </UInput>
            </UFormField>
            <UFormField label="Nachname" name="lastName" class="flex-1">
              <UInput v-model.nullable="state.lastName" placeholder="Goethe" class="w-full" :ui="{ trailing: 'pe-1' }">
                <template v-if="state.lastName" #trailing>
                  <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.lastName = null" />
                </template>
              </UInput>
            </UFormField>
          </div>
          <UFormField label="Studio / Agentur" name="studioName">
            <UInput icon="i-lucide-store" v-model.nullable="state.studioName" placeholder="Studio Name" class="w-full" :ui="{ trailing: 'pe-1' }">
              <template v-if="state.studioName" #trailing>
                <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.studioName = null" />
              </template>
            </UInput>
          </UFormField>
          <UFormField label="Geschlecht" name="sex">
            <USelect icon="i-lucide-venus-and-mars" v-model.nullable="state.sex" :items="sex" placeholder="Unbekannt" class="w-full" />
          </UFormField>
          <UFormField label="Pseudonyme" name="pseudonyms" hint="Mit Eingabetaste bestätigen">
            <UInputTags
                v-model="state.pseudonyms"
                placeholder="Pseudonyme"
                class="w-full"
                :ui="{ trailing: 'pe-1' }"
                icon="i-lucide-venetian-mask"
            >
              <template v-if="state.pseudonyms?.length" #trailing>
                <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.pseudonyms = []" />
              </template>
            </UInputTags>
          </UFormField>
          <div class="flex flex-col gap-4 md:flex-row flex-wrap">
            <UFormField label="Geburtsjahr" name="birthYear" class="flex-1">
              <div class="flex gap-2">
                <UInputNumber
                    v-model="state.birthYear"
                    :increment="false"
                    :decrement="false"
                    :format-options="{ useGrouping: false }"
                    :min="0"
                    :max="3000"
                    placeholder="1749"
                    class="flex-1"
                />
                <UButton icon="i-lucide-circle-x" color="neutral" variant="ghost" aria-label="Eingabe löschen" @click="state.birthYear = null" />
              </div>
            </UFormField>
            <UFormField label="Sterbejahr" name="deathYear" class="flex-1">
              <div class="flex gap-2">
                <UInputNumber
                    v-model="state.deathYear"
                    :increment="false"
                    :decrement="false"
                    :format-options="{ useGrouping: false }"
                    :min="0"
                    :max="3000"
                    placeholder="1832"
                    class="flex-1"
                />
                <UButton icon="i-lucide-circle-x" color="neutral" variant="ghost" aria-label="Eingabe löschen" @click="state.deathYear = null" />
              </div>
            </UFormField>
          </div>
          <UFormField label="GND-ID" name="gndId">
            <UInput v-model.nullable="state.gndId" placeholder="118540238" class="w-full" :ui="{ trailing: 'pe-1' }">
              <template v-if="state.gndId" #trailing>
                <UButton color="neutral" variant="link" size="sm" icon="i-lucide-circle-x" aria-label="Eingabe löschen" @click="state.gndId = null" />
              </template>
            </UInput>
          </UFormField>
          <UButton type="submit" :label="props.action === 'create' ? 'Erstellen' : 'Bearbeiten'" class="self-start" />
        </div>
      </UForm>
    </div>
  </div>
</template>

<style scoped>

</style>
