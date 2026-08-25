<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { PersonDTO } from "~/utils/types";
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";
import PhotopoemPreview from "~/components/UI/PhotopoemPreview.vue";
import MultiPlaceMap from "~/components/visualizations/MultiPlaceMap.vue";
import AuthorKeywordsTreemap from "~/components/visualizations/AuthorKeywordsTreemap.vue";
import PersonMetrics from "~/components/visualizations/PersonMetrics.vue";
import PersonContributionsPlot from "~/components/visualizations/PersonContributionsPlot.vue";

const router = useRoute();
const person_id = Number(router.params.id);
const person_api = usePerson();
const photopoem_store = usePhotopoemStore();
const place_store = usePlaceStore();
const { data: authorThemes } = await useAsyncData(`author-${ person_id }-themes`, () => person_api.fetchAuthorThemes(person_id));
const { data: authorImageMotifs } = await useAsyncData(`author-${ person_id }-image-motifs`, () => person_api.fetchAuthorImageMotifs(person_id));
const { data: personMetrics } = await useAsyncData(`person-${ person_id }-metrics`, () => person_api.fetchPersonMetrics(person_id));
const { data: authorOf } = await useAsyncData(`author-${ person_id }-of`, () => photopoem_store.filterPhotopoems({ 'author-id': person_id }));
const { data: photographerOf } = await useAsyncData(`photographer-${ person_id }-of`, () => photopoem_store.filterPhotopoems({ 'photographer-id': person_id }));
const { data: participatedOn } = await useAsyncData(`participant-${ person_id }-of`, () => photopoem_store.filterPhotopoems({ 'participant-id': person_id }));
const { data: contributorOf } = await useAsyncData(`contributor-${ person_id }-of`, () => photopoem_store.filterPhotopoems({ 'other-contributor-id': person_id }));
const { data: depictedOn } = await useAsyncData(`depicted-${ person_id }-on`, () => photopoem_store.filterPhotopoems({ 'depicted-person-id': person_id }));

const contributionsSummary = computed<PhotoPoemPublicationDateDTO[]>(() => {
  const normalize = (list: any[] | undefined, role: PersonRole) =>
      (list ?? []).map(({id, title, publicationDate}) => ({id, title, publicationDate, role}));

  return [
    ...normalize(authorOf.value, 'AUTHOR' as PersonRole),
    ...normalize(photographerOf.value, 'PHOTOGRAPHER' as PersonRole),
    ...normalize(participatedOn.value, 'PARTICIPANT' as PersonRole),
    ...normalize(contributorOf.value, 'OTHER' as PersonRole),
    ...normalize(depictedOn.value, 'DEPICTED' as PersonRole)
  ];
});
const map_ref = ref<InstanceType<typeof MultiPlaceMap> | null>(null);
const show_map = ref<boolean>(true);


const { data: person_item } = person_api.usePersonId(person_id);
const { data: person_neighbors } = person_api.usePersonIdNeighbors(person_id);


onMounted(async () => {
  const places = await place_store.getContributionPlaces(person_id);
  show_map.value = places.length > 0;
  await map_ref.value?.populatePlaces(places);
});

useHead(() => {
  return {
    title: person_item.value?.fullName || `Person ${ person_id }`,
    meta: [
      {
        name: 'description',
        content: `Fotolyrik. Details zur Person ${ person_item.value?.fullName || person_id }`
      }
    ]
  }
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
        <div v-if="person_item" class="flex flex-col md:flex-row gap-2 justify-between p-4">
          <div v-if="person_item.image"  class="p-3 bg-gray-accent w-1/2">
            <img :src="`/api/uploads/${ person_item.image.filename }`" alt="image"/>
          </div>
          <PersonMetrics v-if="personMetrics" :data="personMetrics"/>
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
        <div v-if="person_item === null"> Fehler. Person konnte nicht gefunden werden.</div>
        <div v-if=" show_map">
        <Divider/>
        <h2 class="text-xl font-bold text-primary outfit-headline">Veröffentlichungsorte</h2>
        <MultiPlaceMap  ref="map_ref"></MultiPlaceMap>
        </div>
        <div class="flex flex-col gap-4">
          <div v-if="authorThemes && authorThemes.length > 0 || authorImageMotifs && authorImageMotifs.length > 0" class="flex flex-col gap-2">
            <Divider/>
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
          <div v-if="contributionsSummary && contributionsSummary.length > 0" class="flex flex-col gap-2">
            <Divider/>
            <h2 class="text-xl font-bold text-primary outfit-headline">Beiträge nach Veröffentlichungsdatum</h2>
            <PersonContributionsPlot :data="contributionsSummary"/>
          </div>
          <div v-if="authorOf && authorOf.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Autor:in von</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in authorOf" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="photographerOf && photographerOf.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Fotograf:in von</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in photographerOf" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="depictedOn && depictedOn.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Abgebildet in</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in depictedOn" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="contributorOf && contributorOf.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Mitgewirkt an</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in contributorOf" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="participatedOn && participatedOn.length > 0" class="max-h-[30vh] flex flex-col gap-2">
            <h2 class="text-xl font-bold text-primary outfit-headline">Beteligt an</h2>
            <div class="overflow-y-auto pb-2">
              <div class="flex flex-col gap-3 md:grid md:grid-cols-5">
                <div v-for="photopoem in participatedOn" :key="photopoem.id">
                  <PhotopoemPreview :photopoem="photopoem"/>
                </div>
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
        <div v-if="person_neighbors?.previous" class="p-2 border border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/persons/${person_neighbors.previous}`"
              class="flex flex-row items-center space-x-2"
          >
            <i class="pi pi-arrow-left"/>
            <div class=" roboto-plain">Vorheriger Eintrag</div>
          </NuxtLink>
        </div>
      </div>
      <div class="next">
        <div v-if="person_neighbors?.next" class="p-2 border border-solid rounded-md hover:shadow-md">
          <NuxtLink
              :to="`/persons/${person_neighbors.next}`"
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
