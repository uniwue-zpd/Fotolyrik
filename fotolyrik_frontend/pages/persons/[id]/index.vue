<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { Person } from "~/utils/types";
import PageToolbar from "~/components/pagetools/PageToolbar.vue";

const router = useRoute();
const person_id = Number(router.params.id);
const store = usePersonStore();
const person_item = ref<Person | null>(null);
const previous_person = ref<Person | null>(null);
const next_person = ref<Person | null>(null);

onMounted(async () => {
  await store.fetchPersonById(person_id);
  person_item.value = store.currentPerson;
  previous_person.value = store.previousPerson();
  next_person.value = store.nextPerson();
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <Card>
      <template #title>
        <div class="flex flex-row justify-between">
          <h1 class="text-3xl font-bold text-[#063D79] outfit-headline">{{ person_item?.first_name }} {{ person_item?.last_name }}</h1>
          <PageToolbar
              v-if="person_item"
              :id="person_item.id"
              entity_type="person"
              :page_url="`${router.fullPath}`"
          />
        </div>
      </template>
      <template #content>
        <table class="min-w-full divide-y divide-gray-200 roboto-plain">
          <tbody v-if="person_item" class="bg-white divide-y divide-gray-200">
          <tr v-if="person_item.birth_year">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Geburtsjahr</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ person_item.birth_year }}</td>
          </tr>
          <tr v-if="person_item.death_year">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Sterbejahr</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ person_item.death_year }}</td>
          </tr>
          <tr v-if="person_item.pseudonyms.length > 0">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Pseudonyme</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm">{{ person_item.pseudonyms.join(', ') }}</td>
          </tr>
          <tr v-if="person_item.sex">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Geschlecht</td>
            <td v-if="person_item.sex === 'männlich'" class="px-6 py-4 whitespace-nowrap text-sm">Männlich</td>
            <td v-else class="px-6 py-4 whitespace-nowrap text-sm">Weiblich</td>
          </tr>
          </tbody>
        </table>
      </template>
    </Card>
    <div class="flex flex-row justify-between">
      <div class="previus">
        <div v-if="previous_person" class="p-2 border-[1px] border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/persons/${previous_person.id}`"
              class="flex flex-row items-center space-x-2"
          >
            <i class="pi pi-arrow-left"/>
            <div class="text-[#063D79] roboto-plain">Vorheriger Eintrag</div>
          </NuxtLink>
        </div>
      </div>
      <div class="next">
        <div v-if="next_person" class="p-2 border-[1px] border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/persons/${next_person.id}`"
              class="flex flex-row items-center space-x-2"
          >
            <div class="text-[#063D79] roboto-plain">Nächster Eintrag</div>
            <i class="pi pi-arrow-right"/>
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>
