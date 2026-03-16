<script setup lang="ts">
import {ContributionRole} from "~/utils/types";
const getContributions = () => {
  return contributions.value.map(({ renderId, ...rest }) => rest);
};
defineExpose({getContributions})
const props = defineProps({
  persons: Array
});
const roleOptions = [
  { label: 'Autor:in', value: ContributionRole.AUTHOR },
  { label: 'Fotograf:in', value: ContributionRole.PHOTOGRAPHER },
  { label: 'Sonstige', value: ContributionRole.OTHER }
];
interface Contribution {
    renderId: string;
    id?: number;
    contributor: PersonPreviewDTO;
    pseudonym: string;
    role: ContributionRole
};
const createEmptyContribution = (): Contribution => ({
  renderId: crypto.randomUUID(),
  contributor: {} as PersonPreviewDTO,
  pseudonym: "",
  role: ContributionRole.UNKNOWN
});
const contributions = ref<Contribution[]>([]);
</script>
<template>
<Button icon="pi pi-plus" severity="secondary" aria-label="Add" label="Mitwirkende hinzufügen"
        @click="contributions.push(createEmptyContribution())"/>
<Form @submit="(e) => console.log('Form Data:', e.values)">
  <div v-if="contributions.length > 0" v-for="(contribution, index) in contributions" :key="contribution.renderId"  >
    <div class="flex">
      <Select
          inputId="contributors"
          placeholder="Mitwirkende Person auswählen"
          :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
          :optionValue="opt => ({id: opt.id, fullName: opt.fullName, studioName: opt.studioName, pseudonyms: opt.pseudonyms})"
          :options="persons"
          :virtual-scroller-options="{ itemSize: 50 }"
          v-model="contributions[index].contributor"
          filter fluid
      > </Select>
      <InputText placeholder="Pseudonym" v-model="contributions[index].pseudonym"></InputText>
      <Select
          inputId="contributionRole"
          placeholder="In Rolle"
          :options="roleOptions"
          optionLabel="label"
          optionValue="value"
          v-model="contributions[index].role"
      ></Select>
      <Button icon="pi pi-times" severity="secondary" aria-label="Remove"
              @click="contributions.splice(index, 1)"/>
    </div>
  </div>
</Form>
</template>
