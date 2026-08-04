<script setup lang="ts">
import {ContributionRole} from "~/utils/types";
import type {AutoCompleteCompleteEvent} from "primevue";

const props = defineProps({
  contributions: Array
});

const personStore = usePersonStore();

const getContributions = () => {
  return contributions.value.map(({ renderId, errorMessage, pseudonymSuggestions, ...rest }) => rest);
};

const loading = ref(false);
const personSuggestions = ref<PersonPreviewDTO[]>([]);

const debouncedSearch = debounce(async (query: string) => {
  loading.value = true;
  personSuggestions.value = await personStore.searchPeople(query);
  loading.value = false;
}, 300);

const onPersonComplete = (event: any) => {
  debouncedSearch(event.query);
}

const checkRefetch = () => {
  const refetch = (id: number) => {
    usePersonStore().refreshPersonsDataById(id);
  };
  for (const contribution of contributions.value) {
    if (contribution.contributor ===  undefined) {
      console.error("Contribution Missing contributor on submit, form validation failed.")
      return;
    }
    const trimmedPseudonyms = contribution.contributor.pseudonyms.map(contribution => contribution.trim().toLowerCase());
    if (!trimmedPseudonyms.includes(contribution.pseudonym)) {
      refetch(contribution.contributor.id);
    }
  }
};

const isValid = () => {
  let valid = true;
  for (const contribution of contributions.value){
    contribution.errorMessage = "";
    if (!contribution.contributor) {
      contribution.errorMessage += "Invalid contributor. ";
      valid = false;
    }
    if (!contribution.role) {
      contribution.errorMessage += "Invalid role. ";
      valid = false;
    }
  }
  return valid;
};

defineExpose({ getContributions, isValid, checkRefetch })

const roleOptions = [
  { label: 'Autor:in', value: ContributionRole.AUTHOR },
  { label: 'Fotograf:in', value: ContributionRole.PHOTOGRAPHER },
  { label: 'Beteiligt', value: ContributionRole.PARTICIPANT },
  { label: 'Sonstige', value: ContributionRole.OTHER }
];

interface Contribution {
    renderId?: string;
    errorMessage?: string;
    id?: number;
    contributor?: PersonPreviewDTO;
    pseudonym: string;
    role?: ContributionRole;
    pseudonymSuggestions?: string[];
}

const createEmptyContribution = (): Contribution => ({
  renderId: crypto.randomUUID(),
  pseudonym: ""
});

const searchPseudonyms = (event : AutoCompleteCompleteEvent, contribution: Contribution) => {
  const query = event.query.toLowerCase();
  const pseudonyms  = contribution.contributor?.pseudonyms;

  if (pseudonyms) {
    contribution.pseudonymSuggestions = pseudonyms.filter(pseudonym => {
      return pseudonym.toLowerCase().includes(query);
    });
  }
};

const contributions = ref<Contribution[]>(
    (props.contributions as ContributionDTO[])?? []
);
</script>

<template>
  <div
      v-if="contributions.length > 0"
      v-for="(contribution, index) in contributions"
      :key="contribution.renderId"
  >
    <div>
      <div class="flex flex-row space-x-2 p-2 bg-surface-50 rounded-md shadow-sm">
        <AutoComplete
            class="flex-1 min-w-0"
            inputId="contributors"
            placeholder="Mitwirkende Person auswählen"
            v-model="contribution.contributor"
            :suggestions="personSuggestions"
            @complete="onPersonComplete"
            :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms[0] || opt.studioName)"
            showClear fluid
        />
        <AutoComplete
            class="flex-2 min-w-0"
            inputId="pseudonym"
            placeholder="Pseudonym"
            v-model="contribution.pseudonym"
            :suggestions="contribution.pseudonymSuggestions"
            @complete="searchPseudonyms($event, contribution)"
            dropdown
        />
        <Select
            class="min-w-0 flex-1"
            inputId="contributionRole"
            placeholder="In Rolle"
            :options="roleOptions"
            optionLabel="label"
            optionValue="value"
            v-model="contribution.role"
        />
        <Button
            icon="pi pi-times"
            severity="secondary"
            aria-label="Remove"
            @click="contributions.splice(index, 1)"
        />
      </div>
      <p class="text-red-500 text-center">{{contribution.errorMessage}}</p>
    </div>
  </div>
  <Button icon="pi pi-plus"
          severity="secondary"
          aria-label="Add"
          label="Mitwirkende hinzufügen"
          @click="contributions.push(createEmptyContribution())"
  />
</template>
