<script setup lang="ts">
import {ContributionRole} from "~/utils/types";
import type {AutoCompleteCompleteEvent} from "primevue";
const getContributions = () => {
  return contributions.value.map(({ renderId, ...rest }) => rest);
};
const checkRefetch = () => {
  const refetch = () =>{
    usePersonStore().refreshPersonsData();
  };
  for (const contribution of contributions.value){
    const trimmedPseudonyms = contribution.contributor?.pseudonyms.map(contribution => contribution.trim().toLowerCase() );
    if(!trimmedPseudonyms?.includes(contribution.pseudonym)){
      refetch();
    }
  }
};
const isValid = () =>{
  let valid = true;
  for(const contribution of contributions.value){
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
defineExpose({getContributions, isValid, checkRefetch})
const props = defineProps({
  persons: Array,
  contributions: Array
});
const roleOptions = [
  { label: 'Autor:in', value: ContributionRole.AUTHOR },
  { label: 'Fotograf:in', value: ContributionRole.PHOTOGRAPHER },
  { label: 'Sonstige', value: ContributionRole.OTHER }
];
interface Contribution {
    renderId?: string;
    errorMessage?: string;
    id?: number;
    contributor?: PersonPreviewDTO;
    pseudonym: string;
    role?: ContributionRole;
    suggestions?: string[];
}
const createEmptyContribution = (): Contribution => ({
  renderId: crypto.randomUUID(),
  pseudonym: ""
});
const searchPseudonyms = (event : AutoCompleteCompleteEvent, contribution: Contribution) => {
  const query = event.query.toLowerCase();
  const pseudonyms  = contribution.contributor?.pseudonyms;

  if (pseudonyms){
    contribution.suggestions = pseudonyms.filter(pseudonym => {
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
        <Select
            class="flex-1 min-w-0"
            inputId="contributors"
            placeholder="Mitwirkende Person auswählen"
            :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
            :optionValue="opt => ({id: opt.id, fullName: opt.fullName, studioName: opt.studioName, pseudonyms: opt.pseudonyms})"
            :options="persons"
            :virtual-scroller-options="{ itemSize: 50 }"
            v-model="contribution.contributor"
            filter fluid
        />
        <AutoComplete
            class="flex-2 min-w-0"
            placeholder="Pseudonym"
            v-model="contribution.pseudonym"
            :suggestions="contribution.suggestions"
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
