<script setup lang="ts">
import {ContributionRole} from "~/utils/types";

const props = defineProps({
  persons: Array
});
const roleOptions = [
  { label: 'Autor:in', value: ContributionRole.AUTHOR },
  { label: 'Fotograf:in', value: ContributionRole.PHOTOGRAPHER },
  { label: 'Abgebildete Person', value: ContributionRole.DEPICTED },
  { label: 'Sonstige', value: ContributionRole.OTHER }
];
interface Contribution {
    id: string;
    contributor: PersonPreviewDTO;
    pseudonym: PseudonymDTO;
    role: ContributionRole
};
const createEmptyContribution = (): Contribution => ({
  id: crypto.randomUUID(),
  contributor: {} as PersonPreviewDTO,
  pseudonym: {} as PseudonymDTO,
  role: ContributionRole.UNKNOWN as ContributionRole
});
const contributions = ref<Contribution[]>([]);
</script>
<template>
<Button icon="pi pi-plus" severity="secondary" aria-label="Add" label="Mitwirkende hinzufügen"
        @click="contributions.push(createEmptyContribution())"/>
  <Form @submit="(e) => console.log('Form Data:', e.values)">
<div v-if="contributions.length > 0" v-for="(contribution, index) in contributions" :key="contribution.id"  >
  <div class="flex">
    <FormField v-slot="$field" :name="`contributions[${index}].contributor`">
    <Select
        inputId="contributors"
        placeholder="Mitwirkende Person auswählen"
        :optionLabel="(opt) => opt.fullName ? opt.fullName : (opt.pseudonyms || []).join(', ')"
        :optionValue="opt => ({id: opt.id, fullName: opt.fullName, studioName: opt.studioName, pseudonyms: opt.pseudonyms})"
        :options="persons"
        :virtual-scroller-options="{ itemSize: 50 }"
        filter fluid
    > </Select>
    </FormField>
    <FormField v-slot="$field" :name="`contributions[${index}].pseudonym`">
    <InputText placeholder="Pseudonym"></InputText>
    </FormField>
    <FormField v-slot="$field" :name="`contributions[${index}].role`">
    <Select
        inputId="contributionRole"
        placeholder="In Rolle"
        :options="roleOptions"
        optionLabel="label"
        optionValue="value" ></Select> </FormField>
    <Button icon="pi pi-times" severity="secondary" aria-label="Remove"
            @click="contributions.splice(index, 1)"/>
  </div>
</div>
    <Button type="submit" label="Print Data" />
  </Form>
</template>
