<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { Person } from "~/utils/types";
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";
import PhotopoemPreview from "~/components/UI/PhotopoemPreview.vue";

const router = useRoute();
const person_id = Number(router.params.id);
const person_store = usePersonStore();
const photopoem_store = usePhotopoemStore();
const person_item = ref<Person | null>(null);
const previous_person = ref<Person | null>(null);
const next_person = ref<Person | null>(null);
const author_photopoems = ref<PhotoPoem[] | []>([]);
const photographer_photopoems = ref<PhotoPoem[] | []>([]);

onMounted(async () => {
  await person_store.fetchPersonById(person_id);
  person_item.value = person_store.currentPerson;
  previous_person.value = person_store.previousPerson();
  next_person.value = person_store.nextPerson();
  author_photopoems.value = await photopoem_store.fetchPhotopoemsBy({ author_id: person_id });
  photographer_photopoems.value = await photopoem_store.fetchPhotopoemsBy({ photographer_id: person_id });
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
        <div v-if="person_item" class="flex flex-row space-x-5 justify-between p-4">
          <div class="p-3 bg-[#F1F2F2]">
            <div v-if="person_item.image">
              <img :src="`/api/uploads/${person_item.image.filename}`" alt="image"/>
            </div>
            <div v-else>
              <Avatar icon="pi pi-user" size="xlarge"/>
            </div>
          </div>
          <div class="p-3 bg-[#F1F2F3]">
            <i class="pi pi-chart-line"/>
          </div>
        </div>
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
        <Divider/>
        <div class="flex flex-col gap-2">
          <div v-if="author_photopoems.length > 0">
            <div class="flex flex-col gap-2">
              <h2 class="text-xl font-bold text-[#063D79] outfit-headline">Fotogedichte vom Autor</h2>
              <div class="flex flex-col gap-3 md:grid md:grid-cols-4 md:justify-items-center">
                <div v-for="photopoem in author_photopoems" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="photographer_photopoems.length > 0">
            <div class="flex flex-col gap-2">
              <h2 class="text-xl font-bold text-[#063D79] outfit-headline">Fotogedichte vom Fotografen</h2>
              <div class="flex flex-col gap-3 md:grid md:grid-cols-4 md:justify-items-center">
                <div v-for="photopoem in photographer_photopoems" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
        </div>
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
