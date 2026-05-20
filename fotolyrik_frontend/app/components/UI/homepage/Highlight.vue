<script setup lang="ts">
const photopoem_store = usePhotopoemStore();
const highlight = computed(() => photopoem_store.currentHighlight);
</script>

<template>
  <div
      v-if="highlight"
      class="collection bg-gray-accent"
  >
    <div class="flex flex-col gap-2 px-6 py-10 md:p-15">
      <h2 class="text-3xl averia-layout italic text-primary">Highlight</h2>
      <Divider/>
      <div class="flex flex-col gap-4 md:flex-row md:space-x-5 md:gap-0">
        <div class="md:basis-3/5">
          <div class="flex flex-col gap-5">
            <span class="outfit-headline font-bold text-primary text-2xl">
              {{ highlight.title || highlight.altTitle }}
            </span>
            <div class="flex flex-col gap-2 text-sm roboto-plain">
              <div v-if="highlight.authors && highlight.authors.length" class="flex flex-row space-x-2">
                <div class="md:basis-2/5 font-semibold">Autor:innen</div>
                <ul>
                  <li
                      v-for="person in highlight.authors"
                      :key="person.id"
                      class="truncate"
                  >
                    <NuxtLink
                        :to="`/persons/${ person.id }`"
                        class="text-primary font-medium"
                    >
                      {{ person.fullName || person.pseudonyms[0] }}
                  </NuxtLink>
                  </li>
                </ul>
              </div>
              <div v-if="highlight.photographers && highlight.photographers.length" class="flex flex-row space-x-2">
                <div class="md:basis-2/5 font-semibold">Fotograf:innen</div>
                <ul>
                  <li
                      v-for="person in highlight.photographers"
                      :key="person.id"
                      class="truncate"
                  >
                    <NuxtLink
                        :to="`/person/${ person.id }`"
                        class="text-primary font-medium"
                    >
                    {{ person.fullName }}
                  </NuxtLink>
                  </li>
                </ul>
              </div>
              <div v-if="highlight.publicationMedium" class="flex flex-row space-x-2">
                <div class="md:basis-2/5 font-semibold">Erschienen in</div>
                <span class="truncate">
                  <NuxtLink
                      :to="`/publication_media/${ highlight.publicationMedium.id }`"
                      class="text-primary font-medium"
                  >
                    {{ highlight.publicationMedium.title }}
                  </NuxtLink>
                </span>
              </div>
              <div v-if="highlight.publicationDate" class="flex flex-row space-x-2">
                <div class="md:basis-2/5 font-semibold">Erscheinungsdatum</div>
                <div class="font-normal">
                  {{ highlight.publicationDate }}
                </div>
              </div>
            </div>
            <span class="flex flex-row justify-center md:justify-start">
                <NuxtLink
                    :to="`/photopoems/${ highlight.id }`"
                    class="averia-layout font-semibold text-sm text-[#F04E30] border-2 border-[#F04E30] p-1"
                >
                  Zum Gedicht
                </NuxtLink>
              </span>
          </div>
        </div>
        <div class="md:basis-2/5 flex flex-row justify-center items-center">
          <Icon name="i-material-symbols-menu-book-outline" class="text-9xl"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
