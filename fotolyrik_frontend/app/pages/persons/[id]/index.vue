<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { PersonDTO } from "~/utils/types";
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";
import PhotopoemPreview from "~/components/UI/PhotopoemPreview.vue";
import AuthorKeywordsTreemap from "~/components/visualizations/AuthorKeywordsTreemap.vue";

const router = useRoute();
const person_id = Number(router.params.id);
const person_store = usePersonStore();
const photopoem_store = usePhotopoemStore();
const person_item = ref<PersonDTO | null>(null);
const previous_person = ref<PersonDTO | null>(null);
const next_person = ref<PersonDTO | null>(null);
const author_photopoems = ref<PhotoPoemDTO[] | []>([]);
const photographer_photopoems = ref<PhotoPoemDTO[] | []>([]);
const depicted_person_photopoems = ref<PhotoPoemDTO[] | []>([]);
const contributor_photopoems = ref<PhotoPoemDTO[] | []>([]);
const { data: authorThemes } = await useAsyncData(`author-${ person_id }-themes`, () => person_store.fetchAuthorThemes(person_id));
const { data: authorImageMotifs } = await useAsyncData(`author-${ person_id }-image-motifs`, () => person_store.fetchAuthorImageMotifs(person_id));

onMounted(async () => {
  await person_store.fetchPersonById(person_id);
  person_item.value = person_store.currentPerson;
  previous_person.value = person_store.previousPerson();
  next_person.value = person_store.nextPerson();
  author_photopoems.value = await photopoem_store.filterPhotopoems({ 'author-id': person_id });
  photographer_photopoems.value = await photopoem_store.filterPhotopoems({ 'photographer-id': person_id });
  depicted_person_photopoems.value = await photopoem_store.filterPhotopoems({ 'depicted-person-id': person_id });
  contributor_photopoems.value = await photopoem_store.filterPhotopoems({ 'other-contributor-id': person_id });
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <Card>
      <template #title>
        <div class="flex flex-row justify-between">
          <h1 class="text-3xl font-bold text-primary outfit-headline">{{ person_item?.fullName || person_item?.pseudonyms[0] }}</h1>
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
          <div class="p-3 bg-gray-accent">
            <div v-if="person_item.image">
              <img :src="`/api/uploads/${person_item.image.filename}`" alt="image"/>
            </div>
            <div v-else>
              <Avatar icon="pi pi-user" size="xlarge"/>
            </div>
          </div>
          <div class="p-3 bg-gray-accent">
            <i class="pi pi-chart-line"/>
          </div>
        </div>
        <table class="min-w-full divide-y divide-gray-200 roboto-plain">
          <tbody v-if="person_item" class=" divide-y divide-gray-200">
          <tr v-if="person_item.studioName">
            <td class="px-6 py-4 whitespace-nowrap font-semibold">Studio / Agentur</td>
            <td class="px-6 py-4 whitespace-nowrap">{{ person_item.studioName }}</td>
          </tr>
          <tr v-if="person_item.birthYear">
            <td class="px-6 py-4 whitespace-nowrap font-semibold">Geburtsjahr</td>
            <td class="px-6 py-4 whitespace-nowrap">{{ person_item.birthYear }}</td>
          </tr>
          <tr v-if="person_item.deathYear">
            <td class="px-6 py-4 whitespace-nowrap font-semibold">Sterbejahr</td>
            <td class="px-6 py-4 whitespace-nowrap">{{ person_item.deathYear }}</td>
          </tr>
          <tr v-if="person_item.pseudonyms.length > 0">
            <td class="px-6 py-4 whitespace-nowrap font-semibold">Pseudonyme</td>
            <td class="px-6 py-4 whitespace-nowrap">{{ person_item.pseudonyms.join(', ') }}</td>
          </tr>
          <tr v-if="person_item.sex">
            <td class="px-6 py-4 whitespace-nowrap font-semibold">Geschlecht</td>
            <td v-if="person_item.sex === 'männlich'" class="px-6 py-4 whitespace-nowrap">Männlich</td>
            <td v-else class="px-6 py-4 whitespace-nowrap">Weiblich</td>
          </tr>
          </tbody>
        </table>
        <Divider/>
        <div class="flex flex-col gap-4">
          <div v-if="author_photopoems.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Autor:in von</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in author_photopoems" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="photographer_photopoems.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Fotograf:in von</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in photographer_photopoems" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="depicted_person_photopoems.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Abgebildet in</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in depicted_person_photopoems" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="contributor_photopoems.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Mitgewirkt an</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in contributor_photopoems" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="authorThemes || authorImageMotifs" class="flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Themen und Motive</h2>
            <div class="flex flex-col md:flex-row gap-2">
              <div class="flex flex-col gap-1 w-full" v-if="authorThemes && authorThemes.length > 0">
                <h3 class="text-lg font-bold text-primary outfit-headline">Themen</h3>
                <AuthorKeywordsTreemap :data="authorThemes" :width="400" :height="400"/>
              </div>
              <div class="flex flex-col gap-1 w-full" v-if="authorImageMotifs && authorImageMotifs.length > 0">
                <h3 class="text-lg font-bold text-primary outfit-headline">Bildmotive</h3>
                <AuthorKeywordsTreemap :data="authorImageMotifs" :width="400" :height="400"/>
              </div>
            </div>
          </div>
        </div>
      </template>
      <template #footer>
        <Divider/>
        <div class="flex flex-col text-base">
          <div v-if="person_item?.createdDate" class="flex flex-row space-x-2  roboto-plain">
            <p class="font-semibold">Erstellt am:</p>
            <p>{{ new Date(person_item.createdDate).toLocaleDateString() }}</p>
          </div>
          <div v-if="person_item?.lastModifiedDate" class="flex flex-row space-x-2  roboto-plain">
            <p class="font-semibold">Zuletzt geändert am:</p>
            <p>{{ new Date(person_item.lastModifiedDate).toLocaleDateString() }}</p>
          </div>
        </div>
      </template>
    </Card>
    <div class="flex flex-row justify-between">
      <div class="previus">
        <div v-if="previous_person" class="p-2 border border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/persons/${previous_person.id}`"
              class="flex flex-row items-center space-x-2"
          >
            <i class="pi pi-arrow-left"/>
            <div class=" roboto-plain">Vorheriger Eintrag</div>
          </NuxtLink>
        </div>
      </div>
      <div class="next">
        <div v-if="next_person" class="p-2 border border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/persons/${next_person.id}`"
              class="flex flex-row items-center space-x-2"
          >
            <div class="roboto-plain">Nächster Eintrag</div>
            <i class="pi pi-arrow-right"/>
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>
