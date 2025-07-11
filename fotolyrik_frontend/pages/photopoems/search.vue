<script setup lang="ts">
import apiClient from "~/service/api";
import { ref } from "vue";
import type { FullTextSearchResult } from "~/utils/types";

const submitted = ref(false);
const results = ref<FullTextSearchResult[]>([]);
const query_result_status = ref('');

const submit = async (formData: { query: string}) => {
  try {
    const response = await apiClient.get('/fulltexts/search', {
      params: {
        query: formData.query
      }
    });
    results.value = response.data;
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
    <h1 class="text-3xl text-[#063D79] outfit-headline font-bold">Volltextsuche</h1>
    <p class="roboto-plain">Geben Sie bitte den Suchbegriff ein, um Fotogedichte zu finden</p>
    <FormKit
        type="form"
        id="fulltext_search"
        @submit="submit"
        :actions="false"
        #default="{ value }"
    >
      <div class="flex flex-col gap-2 border-2 border-solid rounded-md p-5 bg-[#F1F2F2]">
        <FormKit
            type="search"
            placeholder="Ginkgo Biloba"
            label="Suchbegriff"
            outer-class="max-w-full"
            name="query"
        />
        <FormKit
            type="submit"
            label="Suchen"
            prefixIcon="search"
        />
      </div>
    </FormKit>
    <Card v-show="submitted">
      <template #title>
        <h2 class="text-2xl outfit-headline font-bold text-[#063D79]">Suchergebnisse</h2>
      </template>
      <template #content>
        <div v-if="query_result_status === 'success'">
          <div class="flex flex-col gap-2">
            <div class="roboto-plain">Es wurden insgesamt {{ results.length }} Treffer gefunden</div>
            <div v-for="(result, index) in results" :key="result.photopoem_id">
              <Panel>
                <template #header>
                  <div class="flex flex-row justify-center space-x-5">
                    <div class="roboto-plain">{{ index + 1 }}</div>
                    <NuxtLink
                        :to="`/photopoems/${result.photopoem_id}`"
                        class="outfit-headline font-semibold text-[#063D79]"
                    >
                      {{ result.photopoem_title }}
                    </NuxtLink>
                  </div>
                </template>
                <div class="italic" v-html="result.query_result"/>
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
