<script setup lang="ts">
import 'tify'
import 'tify/dist/tify.css'
import { onMounted } from "vue";
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";
import {ContributionRole} from "~/utils/types";
const router = useRoute();
const photopoem_id = Number(router.params.id);
const store = usePhotopoemStore();
const photopoem_item = computed(() => store.currentPhotopoem);
const file_store = useFileStore();

declare const Tify: any; // stops type errors, Tify comes from plain JS library

const authors = computed(() => photopoem_item.value?.contributions
    .filter(contribution => contribution.role === ContributionRole.AUTHOR)
    .map(contribution => ({...contribution.contributor, pseudonym: contribution.pseudonym || null })) || []);
const photographers = computed(() => photopoem_item.value?.contributions
    .filter(contribution => contribution.role === ContributionRole.PHOTOGRAPHER)
    .map(contribution => ({...contribution.contributor, pseudonym: contribution.pseudonym || null })) || []);
const otherContributors = computed(() => photopoem_item.value?.contributions
    .filter(contribution => contribution.role === ContributionRole.OTHER)
    .map(contribution => ({...contribution.contributor, pseudonym: contribution.pseudonym || null })) || []);

// TIFY Viewer setup
const has_iiif_manifest = computed(() => Boolean(photopoem_item.value?.iiifManifest));
const has_pages = computed(() => Boolean(photopoem_item.value?.manifestPageNumber));
const double_page = computed(() => photopoem_item.value?.pageCount === 2);

// Scans handling
const show_scans = computed(() => {
  return photopoem_item.value &&
      photopoem_item.value.images.length > 0 &&
      photopoem_item.value?.imagesVisible === AccessLevel.PUBLIC &&
      !has_iiif_manifest.value;
});
const scan_ids = computed(() => photopoem_item.value ? photopoem_item.value.images.map(image => image.id) : []);
const scans = ref<string[]>([]);

useHead(() => ({
  title: photopoem_item.value?.title ? `${photopoem_item.value?.title}` : photopoem_item.value?.altTitle
}));

onMounted(async () => {
  await store.fetchPhtotopoemById(photopoem_id);
  if (show_scans.value) {
    scans.value = (await Promise.all(scan_ids.value.map((id) => file_store.getImageContent(id)))
    ).filter((url) => url !== null) as string[];
  }
  const manifestPageNumber = photopoem_item.value?.manifestPageNumber ?? 1;
  if (photopoem_item.value?.iiifManifest) {
    new Tify({
      container: '#tify-photopoem',
      manifestUrl: photopoem_item.value.iiifManifest,
      pages: has_pages.value
          ? (double_page.value
              ? [manifestPageNumber, manifestPageNumber + 1]
              : [manifestPageNumber])
          : [1]
    });
  }
});
const roleText = {
  AUTHOR: 'Autor:in',
  PHOTOGRAPHER: 'Fotograf:in',
  OTHER: 'Sonstige',
};
</script>

<template>
  <div class="flex flex-col gap-2">
    <Card v-if="photopoem_item">
      <template #title>
        <div class="flex flex-row justify-between">
          <h1 class="text-3xl font-bold text-primary outfit-headline">{{ photopoem_item?.title ? photopoem_item.title : photopoem_item.altTitle}}</h1>
          <PageToolbar
              v-if="photopoem_item"
              :id="photopoem_item.id"
              entity_type="photopoem"
              :page_url="`${router.fullPath}`"
          />
        </div>
      </template>
      <template #content>
        <div class="flex flex-col gap-2">
          <div v-if="show_scans" class="flex flex-row items-start">
            <Galleria
                :value="scans"
                :numVisible="1"
            >
              <template #item="slotProps">
                <Image preview>
                  <template #image>
                    <img
                        :src="slotProps.item"
                        alt="Fotogedicht Bildvorschau"
                        class="max-h-[300px] w-auto"
                        oncontextmenu="return false;"
                    />
                  </template>
                  <template #preview="slotPropsPreview">
                    <img
                        :src="slotProps.item"
                        alt="Fotogedicht Bildvorschau"
                        class="max-h-[80vh] select-none pointer-events-none"
                        oncontextmenu="return false;"
                        :style="slotPropsPreview.style"
                    />
                  </template>
                </Image>
              </template>
            </Galleria>
          </div>
          <div v-show="has_iiif_manifest" id="tify-photopoem" class="h-[500px]"/>
          <Accordion value="0">
            <AccordionPanel value="0">
              <AccordionHeader>
                <h2 class="text-2xl font-semibold text-primary outfit-headline">Details</h2>
              </AccordionHeader>
              <AccordionContent>
                <div class="flex flex-col gap-2 rounded-lg shadow-lg p-3 overflow-x-auto">
                  <table class="min-w-full divide-y divide-gray-200 roboto-plain">
                    <tbody class="divide-y divide-gray-200">
                    <tr v-if="photopoem_item.subtitle">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Untertitel</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.subtitle }}</td>
                    </tr>
                    <tr v-if="photopoem_item.altTitle">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Alternativer Titel</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.altTitle }}</td>
                    </tr>
                    <tr v-if="photopoem_item.series">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Reihe</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.series }}</td>
                    </tr>
                    <tr v-if="photopoem_item.volume">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Jahrgang</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.volume }}</td>
                    </tr>
                    <tr v-if="photopoem_item.issue">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Ausgabe</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.issue }}</td>
                    </tr>
                    <tr v-if="photopoem_item.pageNumber">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Seite(n)</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.pageNumber }}</td>
                    </tr>
                    <tr v-if="photopoem_item.pageCount">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Umfang</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.pageCount }}</td>
                    </tr>
                    <tr v-if="photopoem_item.pictureCount">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Anzahl der Fotografien</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.pictureCount }}</td>
                    </tr>
                    <tr v-if="photopoem_item.publicationDate">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Erscheinungsdatum</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.publicationDate }}</td>
                    </tr>
                    <tr v-if="photopoem_item.publicationMedium">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Publikationsmedium</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <NuxtLink
                            :to="`/publication_media/${photopoem_item.publicationMedium.id}`"
                            class="p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md font-medium"
                        >
                          {{ photopoem_item.publicationMedium.title }}
                        </NuxtLink>
                      </td>
                    </tr>
                    <tr v-if="photopoem_item.foundIn.length > 0">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Fundorte</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex flex-wrap gap-3.5">
                            <span v-for="location in photopoem_item.foundIn" :key="location.id">
                              <NuxtLink
                                  :to="`/locations/${location.id}`"
                                  class="p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md font-medium"
                              >
                                {{ location.name }}
                              </NuxtLink>
                            </span>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="authors.length > 0">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Autor:innen</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex flex-wrap gap-3.5">
                            <span v-for="person in authors" :key="person.id">
                              <NuxtLink
                                  :to="`/persons/${person.id}`"
                                  class="flex flex-row space-x-2 p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md"
                              >
                                <span class="font-medium">
                                  {{ person.fullName || `${person.pseudonyms[0]} (Pseudonym)` }}
                                </span>
                                <span v-if="person.pseudonym" class="font-light italic">als {{  person.pseudonym }}</span>
                              </NuxtLink>
                            </span>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="photographers.length > 0">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Fotograf:innen</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex flex-wrap gap-3.5">
                            <span v-for="person in photographers" :key="person.id">
                              <NuxtLink
                                  :to="`/persons/${person.id}`"
                                  class="flex flex-row space-x-2 p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md"
                              >
                                <span class="font-medium">
                                  {{ person.fullName || `${person.pseudonyms[0]} (Pseudonym)` }}
                                </span>
                                <span v-if="person.pseudonym" class="font-light italic">als {{  person.pseudonym }}</span>
                              </NuxtLink>
                            </span>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="otherContributors.length > 0">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Sonstige Mitwirkende</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex flex-wrap gap-3.5">
                            <span v-for="person in otherContributors" :key="person.id">
                              <NuxtLink
                                  :to="`/persons/${person.id}`"
                                  class="flex flex-row space-x-2 p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md"
                              >
                                <span class="font-medium">
                                  {{ person.fullName || `${person.pseudonyms[0]} (Pseudonym)` }}
                                </span>
                                <span v-if="person.pseudonym" class="font-light italic">als {{  person.pseudonym }}</span>
                              </NuxtLink>
                            </span>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="photopoem_item.depictedPeople.length > 0">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Abgebildete Personen</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex flex-wrap gap-3.5">
                            <span v-for="person in photopoem_item.depictedPeople" :key="person.id">
                              <NuxtLink
                                  :to="`/persons/${person.id}`"
                                  class="p-1.5 bg-surface-100 rounded-md shadow-sm hover:shadow-md font-medium"
                              >
                                {{ person.fullName || `${person.pseudonyms[0]} (Pseudonym)` }}
                              </NuxtLink>
                            </span>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="photopoem_item.themes.length > 0">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Thematik</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex flex-wrap gap-3.5">
                            <span v-for="keyword in photopoem_item.themes" :key="keyword.id">
                              <NuxtLink
                                  :to="`/keywords/${keyword.id}`"
                                  class="p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md font-medium"
                              >
                                {{ keyword.value }}
                              </NuxtLink>
                            </span>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="photopoem_item.imageMotifs.length > 0">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Bildmotiv</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex flex-wrap gap-3.5">
                            <span v-for="keyword in photopoem_item.imageMotifs" :key="keyword.id">
                              <NuxtLink
                                  :to="`/keywords/${keyword.id}`"
                                  class="p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md font-medium"
                              >
                                {{ keyword.value }}
                              </NuxtLink>
                            </span>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="photopoem_item.form">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Format</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.form }}</td>
                    </tr>
                    <tr v-if="photopoem_item.link">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Link</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <NuxtLink :to="photopoem_item.link" target="_blank" :title="photopoem_item.link">
                          <Icon name="i-material-symbols-open-in-new-rounded" class="text-2xl text-primary" :title="photopoem_item.link"/>
                        </NuxtLink>
                      </td>
                    </tr>
                    <tr v-if="photopoem_item.languages.length > 0">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Sprachen</td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex flex-wrap gap-3.5">
                          <div v-for="language in photopoem_item.languages">
                            <div class="p-1.5 bg-gray-accent rounded-md shadow-sm hover:shadow-md font-medium">
                              {{ language.name }}
                            </div>
                          </div>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="photopoem_item.copyrightStatusText">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Urheberrecht Text</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.copyrightStatusText.value }}</td>
                    </tr>
                    <tr v-if="photopoem_item.copyrightStatusImage">
                      <td class="px-6 py-4 whitespace-nowrap font-semibold">Urheberrecht Bild</td>
                      <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.copyrightStatusImage.value }}</td>
                    </tr>
                    </tbody>
                  </table>
                  <Divider/>
                  <div class="flex flex-col text-base">
                    <div v-if="photopoem_item?.createdDate" class="flex flex-row space-x-2 roboto-plain">
                      <p class="font-semibold">Erstellt am:</p>
                      <p>{{ new Date(photopoem_item.createdDate).toLocaleDateString() }}</p>
                    </div>
                    <div v-if="photopoem_item?.lastModifiedDate" class="flex flex-row space-x-2 roboto-plain">
                      <p class="font-semibold">Zuletzt geändert am:</p>
                      <p>{{ new Date(photopoem_item.lastModifiedDate).toLocaleDateString() }}</p>
                    </div>
                  </div>
                </div>
              </AccordionContent>
            </AccordionPanel>
          </Accordion>
        </div>
      </template>
    </Card>
  </div>
</template>
