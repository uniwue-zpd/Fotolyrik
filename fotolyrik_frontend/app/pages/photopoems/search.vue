<script setup lang="ts">
import {ref} from "vue";
import type {FullTextSearchResult} from "~/utils/types";

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
          leading: 'bg-primary rounded-full text-white aspect-square justify-end '
        }"
        v-model="query">
      <template #leading>
        <UButton
            color="text-white"
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
    <Card v-show="submitted">
      <template #title>
        <h2 class="text-2xl outfit-headline font-bold text-primary">Suchergebnisse</h2>
      </template>
      <template #content>
        <div v-if="query_result_status === 'success'">
          <div class="flex flex-col gap-2">
            <div class="roboto-plain">Es wurden insgesamt <b>{{ results.length }}</b> Gedichte gefunden</div>
            <div v-for="(result, index) in results" :key="result.photopoemId">
              <Panel class="hover:shadow-md">
                <template #header>
                  <div class="flex flex-row justify-center space-x-5">
                    <div class="roboto-plain">{{ index + 1 }}</div>
                    <NuxtLink
                        :to="`/photopoems/${ result.photopoemId }`"
                        class="outfit-headline font-semibold text-primary"
                    >
                      {{ result.photopoemTitle }}
                    </NuxtLink>
                  </div>
                </template>
                <div class="italic" v-html="result.queryResult"/>
              </Panel>
            </div>
          </div>
        </div>
        <div v-else-if="query_result_status === 'empty'">Es wurden keine Treffer gefunden</div>
        <div v-else-if="query_result_status === 'error'">Es ist ein Fehler aufgetreten. Bitte versuchen Sie es später erneut.</div>
      </template>
    </Card>
  </div>
</template>
