<script setup lang="ts">
import 'tify'
import 'tify/dist/tify.css'
import { onMounted } from "vue";
import PageToolbar from "~/components/UI/pagetools/PageToolbar.vue";

const router = useRoute();
const photopoem_id = Number(router.params.id);
const store = usePhotopoemStore();
const photopoem_item = computed(() => store.currentPhotopoem);
const has_iiif_manifest = computed(() => Boolean(photopoem_item.value?.iiifManifest));
const has_pages = computed(() => Boolean(photopoem_item.value?.pageNumber));

onMounted(async () => {
  await store.fetchPhtotopoemById(photopoem_id);
  if (photopoem_item.value?.iiifManifest) {
    new Tify({
      container: '#tify-photopoem',
      manifestUrl: photopoem_item.value.iiifManifest,
      pages: has_pages.value ? [photopoem_item.value.pageNumber] : [1]
    });
  }
});
</script>

<template>
  <div class="flex flex-col gap-2">
    <Card v-if="photopoem_item">
      <template #title>
        <div class="flex flex-row justify-between">
          <h1 class="text-3xl font-bold text-[#063D79] outfit-headline">{{ photopoem_item?.title }}</h1>
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
          <div v-show="has_iiif_manifest" id="tify-photopoem" class="h-[500px]"/>
          <div v-if="photopoem_item.images.length > 0">
            <img
                :src="`/api/uploads/${photopoem_item?.images[0].filename}`"
                alt="Photopoem scan"
                class="max-h-[250px] select-none pointer-events-none"
                oncontextmenu="return false;"
            >
          </div>
          <Accordion value="0">
            <AccordionPanel value="0">
              <AccordionHeader>
                <h2 class="text-2xl font-semibold text-[#063D79] outfit-headline">Details</h2>
              </AccordionHeader>
              <AccordionContent>
                <Card>
                  <template #content>
                    <table class="min-w-full divide-y divide-gray-200 roboto-plain text-base">
                      <tbody class="bg-white divide-y divide-gray-200">
                      <tr v-if="photopoem_item.subtitle">
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Untertitel</td>
                        <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.subtitle }}</td>
                      </tr>
                      <tr v-if="photopoem_item.altTitle">
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Alternativer Titel</td>
                        <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.altTitle }}</td>
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
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Seite</td>
                        <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.pageNumber }}</td>
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
                              class="p-1.5 bg-[#F1F2F2] rounded-md shadow-sm hover:shadow-md font-medium"
                          >
                            {{ photopoem_item.publicationMedium.title }}
                          </NuxtLink>
                        </td>
                      </tr>
                      <tr v-if="photopoem_item.authors.length > 0">
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Author:innen</td>
                        <td class="px-6 py-4 whitespace-nowrap">
                          <div class="grid grid-cols-2 justify-items-start gap-y-3.5">
                            <span v-for="person in photopoem_item.authors" :key="person.id">
                              <NuxtLink
                                  :to="`/persons/${person.id}`"
                                  class="p-1.5 bg-[#F1F2F2] rounded-md shadow-sm hover:shadow-md font-medium"
                              >
                                {{ person.fullName }}
                              </NuxtLink>
                            </span>
                          </div>
                        </td>
                      </tr>
                      <tr v-if="photopoem_item.photographers.length > 0">
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Fotograf:innen</td>
                        <td class="px-6 py-4 whitespace-nowrap">
                          <div class="grid grid-cols-2 justify-items-start gap-y-3.5">
                            <span v-for="person in photopoem_item.photographers" :key="person.id">
                              <NuxtLink
                                  :to="`/persons/${person.id}`"
                                  class="p-1.5 bg-[#F1F2F2] rounded-md shadow-sm hover:shadow-md font-medium"
                              >
                                {{ person.fullName }}
                              </NuxtLink>
                            </span>
                          </div>
                        </td>
                      </tr>
                      <tr v-if="photopoem_item.otherContributors.length > 0">
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Sonstige Mitwirkende</td>
                        <td class="px-6 py-4 whitespace-nowrap">
                          <div class="grid grid-cols-2 justify-items-start gap-y-3.5">
                            <span v-for="person in photopoem_item.otherContributors" :key="person.id">
                              <NuxtLink
                                  :to="`/persons/${person.id}`"
                                  class="p-1.5 bg-[#F1F2F2] rounded-md shadow-sm hover:shadow-md font-medium"
                              >
                                {{ person.fullName }}
                              </NuxtLink>
                            </span>
                          </div>
                        </td>
                      </tr>
                      <tr v-if="photopoem_item.themes.length > 0">
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Thematik</td>
                        <td class="px-6 py-4 whitespace-nowrap">
                          <div class="grid grid-cols-2 justify-items-start gap-y-3.5">
                            <span v-for="keyword in photopoem_item.themes" :key="keyword.id">
                              <NuxtLink
                                  :to="`/keywords/${keyword.id}`"
                                  class="p-1.5 bg-[#F1F2F2] rounded-md shadow-sm hover:shadow-md font-medium"
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
                          <div class="grid grid-cols-2 justify-items-start gap-y-3.5">
                            <span v-for="keyword in photopoem_item.imageMotifs" :key="keyword.id">
                              <NuxtLink
                                  :to="`/keywords/${keyword.id}`"
                                  class="p-1.5 bg-[#F1F2F2] rounded-md shadow-sm hover:shadow-md font-medium"
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
                          <a :href="photopoem_item.link" target="_blank" class="text-[#063D79]">
                            {{ photopoem_item.link }}
                          </a>
                        </td>
                      </tr>
                      <tr v-if="photopoem_item.languages.length > 0">
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Sprachen</td>
                        <td class="px-6 py-4 whitespace-nowrap">
                          <div class="grid grid-cols-2 justify-items-start gap-y-3.5">
                            <div v-for="language in photopoem_item.languages" :key="language">
                              <div class="p-1.5 bg-[#F1F2F2] rounded-md shadow-sm hover:shadow-md font-medium">
                                {{ language }}
                              </div>
                            </div>
                          </div>
                        </td>
                      </tr>
                      <tr v-if="photopoem_item.copyrightStatusText">
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Urheberrecht Text</td>
                        <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.copyrightStatusText }}</td>
                      </tr>
                      <tr v-if="photopoem_item.copyrightStatusImage">
                        <td class="px-6 py-4 whitespace-nowrap font-semibold">Urheberrecht Bild</td>
                        <td class="px-6 py-4 whitespace-nowrap">{{ photopoem_item.copyrightStatusImage }}</td>
                      </tr>
                      </tbody>
                    </table>
                  </template>
                  <template #footer>
                    <Divider/>
                    <div class="flex flex-col text-base">
                      <div v-if="photopoem_item?.createdDate" class="flex flex-row space-x-2 text-black roboto-plain">
                        <p class="font-semibold">Erstellt am:</p>
                        <p>{{ new Date(photopoem_item.createdDate).toLocaleDateString() }}</p>
                      </div>
                      <div v-if="photopoem_item?.lastModifiedDate" class="flex flex-row space-x-2 text-black roboto-plain">
                        <p class="font-semibold">Zuletzt geändert am:</p>
                        <p>{{ new Date(photopoem_item.lastModifiedDate).toLocaleDateString() }}</p>
                      </div>
                    </div>
                  </template>
                </Card>
              </AccordionContent>
            </AccordionPanel>
          </Accordion>
        </div>
      </template>
    </Card>
  </div>
</template>
