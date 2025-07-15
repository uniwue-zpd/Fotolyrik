<script setup lang="ts">
import 'tify'
import 'tify/dist/tify.css'
import { ref, onMounted } from "vue";
import type { PhotoPoem } from "~/utils/types";
import PageToolbar from "~/components/pagetools/PageToolbar.vue";

const router = useRoute();
const photopoem_id = Number(router.params.id);
const store = usePhotopoemStore();
const photopoem_item = ref<PhotoPoem | null>(null);
const has_iiif_manifest = ref(false);
const has_pages = ref(false);

onMounted(async () => {
  await store.fetchPhtotopoemById(photopoem_id);
  photopoem_item.value = store.currentPhotopoem;
  console.log('photopoem_item', photopoem_item.value);
  if (photopoem_item.value?.iiif_manifest) {
    has_iiif_manifest.value = !!photopoem_item.value.iiif_manifest;
    has_pages.value = photopoem_item.value.page_number !== undefined;
    new Tify({
      container: '#tify-photopoem',
      manifestUrl: photopoem_item.value.iiif_manifest,
      pages: has_pages.value ? [photopoem_item.value.page_number] : [1]
    });
  }
});
</script>

<template v-show="data_fetched">
  <div class="flex flex-col gap-2">
    <Card>
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
          <Accordion value="null">
            <AccordionPanel value="0">
              <AccordionHeader>
                <h2 class="text-2xl font-semibold text-[#063D79] outfit-headline">Details</h2>
              </AccordionHeader>
              <AccordionContent>
                <Card>
                  <template #content>
                    <table class="min-w-full divide-y divide-gray-200 roboto-plain">
                      <tbody v-if="photopoem_item" class="bg-white divide-y divide-gray-200">
                      <tr v-if="photopoem_item.volume">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Jahrgang</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">{{ photopoem_item.volume }}</td>
                      </tr>
                      <tr v-if="photopoem_item.issue">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Ausgabe</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">{{ photopoem_item.issue }}</td>
                      </tr>
                      <tr v-if="photopoem_item.page_number">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Seite</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">{{ photopoem_item.page_number }}</td>
                      </tr>
                      <tr v-if="photopoem_item.publication_date">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Erscheinungsdatum</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">{{ photopoem_item.publication_date }}</td>
                      </tr>
                      <tr v-if="photopoem_item.publication_medium">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Publikationsmedium</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">{{ photopoem_item.publication_medium.title }}</td>
                      </tr>
                      <tr v-if="photopoem_item.authors.length > 0">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Author:innen</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">
                          <span v-for="(author, index) in photopoem_item.authors" :key="author.id">
                          <NuxtLink :to="`/persons/${author.id}`" class="roboto-plain">
                            {{ author.full_name }}
                          </NuxtLink>
                          <span v-if="index < photopoem_item.authors.length -1">, </span>
                        </span>
                        </td>
                      </tr>
                      <tr v-if="photopoem_item.photographers.length > 0">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Fotograf:innen</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">
                          <span v-for="(photographer, index) in photopoem_item.photographers" :key="photographer.id">
                          <NuxtLink :to="`/persons/${photographer.id}`" class="roboto-plain">
                            {{ photographer.full_name }}
                          </NuxtLink>
                          <span v-if="index < photopoem_item.photographers.length -1">, </span>
                        </span>
                        </td>
                      </tr>
                      <tr v-if="photopoem_item.link">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Link</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">
                          <NuxtLink :to="photopoem_item.link" target="_blank" class="text-[#063D79]">
                            {{ photopoem_item.link }}
                          </NuxtLink>
                        </td>
                      </tr>
                      <tr v-if="photopoem_item.language">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">Sprache</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">{{ photopoem_item.language }}</td>
                      </tr>
                      </tbody>
                    </table>
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
