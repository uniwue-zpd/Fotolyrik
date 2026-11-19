<script setup lang="ts">
import {ref} from "vue";
import type {FullTextSearchResult} from "~/utils/types";
import ContentCard from "~/components/UI/cards/ContentCard.vue";

const fullTextApi = useFullText();
const submitted = ref(false);
const results = ref<FullTextSearchResult[]>([]);
const query_result_status = ref('');
const query = ref('')

const submit = async (formData: { query: string}) => {
  try {
    results.value = await fullTextApi.searchFullTexts(formData.query);
    submitted.value = true;
    results.value.length > 0 ? query_result_status.value = 'success': query_result_status.value = 'empty';
  } catch (error) {
    query_result_status.value = 'error';
    console.error(error);
  }
};
</script>

<template>
  <div class="flex flex-col gap-2">
    <h1 class="text-3xl text-primary outfit-headline font-bold">Volltextsuche</h1>
    <p class="roboto-plain">Geben Sie bitte den Suchbegriff ein, um Fotogedichte zu finden</p>
    <UInput
        icon="i-lucide-search"
        size="xl"
        variant="outline"
        placeholder="Die Sonne tönt nach alter Wiese..."
        @keydown.enter="submit({query})"
        :ui="{
          base: 'rounded-full ps-12',
          trailing: 'pe-1' ,
          leading: 'bg-primary rounded-full text-white aspect-square justify-end'
        }"
        v-model="query">
      <template #leading>
        <UButton
            class="text-white"
            variant="link"
            size="xl"
            icon="i-lucide-search"
            aria-label="Search Button"
            @click="submit({query})"
        />
      </template>
      <template v-if="query?.length" #trailing>
        <UButton
            color="neutral"
            variant="link"
            size="sm"
            icon="i-lucide-circle-x"
            aria-label="Clear input"
            @click="query = ''"
        />
      </template>
    </UInput>
    <div v-if="submitted" class="flex gap-2 flex-col">
      <ContentCard v-for="(fulltext, index) in results" :key="fulltext.photopoemId"
                   :headerLink="`/photopoems/${fulltext.photopoemId}`" >
        <template #header>
          {{console.log(fulltext)}}
          {{ fulltext.photopoemTitle || "Unbenanntes Fotogedicht" }}
        </template>
        <template #subContent>
          <div v-if="fulltext.queryResult" class="">
            <div class="gap-2" v-html=" fulltext.queryResult"></div>
          </div>
        </template>
      </ContentCard>
    </div>
  </div>
</template>
