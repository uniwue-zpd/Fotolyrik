import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Keyword } from '~/utils/types';

export const useKeywordStore = defineStore('keyword', () => {
    // State
    const keywords = ref<Keyword[]>([] as Keyword[]);
    const currentKeyword = ref<Keyword | null>(null);

    // Getters
    const isLoaded = computed(() => keywords.value.length > 0);

    // Actions
        // GET Fetch all keywords
    async function fetchKeywords() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/keywords');
            if (error.value) {
                console.error('Error fetching keywords:', error.value);
                return;
            }
            keywords.value = data.value as Keyword[];
        }
    }

        // GET refetch keywords
    async function refreshKeywordsData() {
        try {
            const data = await $fetch('/api/keywords');
            keywords.value = data as Keyword[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

        // GET Fetch keyword by ID
    async function fetchKeywordById(id: number) {
        if (!currentKeyword.value || currentKeyword.value.id !== id) {
            const cachedKeyword = keywords.value.find(k => k.id === id);
            if (cachedKeyword) {
                currentKeyword.value = cachedKeyword;
            } else {
                const { data, error } = await useFetch(`/api/keywords/${id}`);
                if (error.value) {
                    console.error(`Error fetching keyword with id ${id}:`, error.value);
                    return;
                }
                currentKeyword.value = data.value as Keyword;
            }
        }
    }

        // POST Create new keyword
    async function createKeyword(payload: Partial<Keyword>) {
        const { data, error } = await useFetch('/api/keywords', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating keyword:', error.value);
            return;
        }
        const newKeyword = data.value as Keyword;
        keywords.value.push(newKeyword);
        return newKeyword;
    }

        // PUT Update existing keyword
    async function updateKeyword(payload: Partial<Keyword>, id: number) {
        const { data, error } = await useFetch(`/api/keywords/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Error updating keyword:', error.value);
            return;
        }
        const updatedKeyword = data.value as Keyword;
        const index = keywords.value.findIndex(k => k.id === id);
        if (index !== -1) keywords.value[index] = updatedKeyword;
        if (currentKeyword.value?.id === id) currentKeyword.value = updatedKeyword;
        return updatedKeyword;
    }

        // DELETE keyword
    async function deleteKeyword(id: number) {
        const { error } = await useFetch(`/api/keywords/${id}`, { method: 'DELETE' })
        if (error.value) {
            console.error('Error deleting keyword:', error.value);
            return;
        }
        keywords.value = keywords.value.filter(k => k.id !== id)
        if (currentKeyword.value?.id === id) currentKeyword.value = null;
    }

        // Clear current keyword
    function clearCurrentKeyword() {
        currentKeyword.value = null;
    }

    return {
        keywords,
        currentKeyword,
        isLoaded,
        fetchKeywords,
        refreshKeywordsData,
        fetchKeywordById,
        createKeyword,
        updateKeyword,
        deleteKeyword,
        clearCurrentKeyword
    }
})
