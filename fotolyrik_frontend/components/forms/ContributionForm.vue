<script setup lang="ts">
import {ContributionRole} from "~/utils/types";
const getContributions = () => {
  return contributions.value.map(({ renderId, ...rest }) => rest);
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
defineExpose({getContributions, isValid})
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
    role?: ContributionRole
}
const createEmptyContribution = (): Contribution => ({
  renderId: crypto.randomUUID(),
  pseudonym: ""
});
const contributions = ref<Contribution[]>(
    (props.contributions as ContributionDTO[])?? []
);
</script>
<template>
  {{console.log(contributions)}}
<div v-if="contributions.length > 0" v-for="(contribution, index) in contributions" :key="contribution.renderId"  >
  <div>
    <div class="flex">
      <Select
          inputId="contributors"
          placeholder="Mitwirkende Person auswählen"
          :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
          :optionValue="opt => ({id: opt.id, fullName: opt.fullName, studioName: opt.studioName, pseudonyms: opt.pseudonyms})"
          :options="persons"
          :virtual-scroller-options="{ itemSize: 50 }"
          v-model="contribution.contributor"
          filter fluid
      > </Select>
      <InputText placeholder="Pseudonym" v-model="contribution.pseudonym"></InputText>
      <Select
          class="w-64"
          inputId="contributionRole"
          placeholder="In Rolle"
          :options="roleOptions"
          optionLabel="label"
          optionValue="value"
          v-model="contribution.role"
      ></Select>
      <Button icon="pi pi-times" severity="secondary" aria-label="Remove"
              @click="contributions.splice(index, 1)"/>
    </div>
    <p class="text-red-500 text-center">{{contribution.errorMessage}}</p>
  </div>
</div>
<Button icon="pi pi-plus" severity="secondary" aria-label="Add" label="Mitwirkende hinzufügen"
        @click="contributions.push(createEmptyContribution())"/>
</template>
