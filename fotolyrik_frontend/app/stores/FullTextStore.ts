export const useFullTextStore = defineStore('fullText', () => {
    // State
    const fullTexts = ref<FullTextDTO[]>([] as FullTextDTO[]);
    const currentFullText = ref<FullTextDTO | null>(null);

    // Getters
    const isLoaded = computed(() => fullTexts.value.length > 0);

    // Actions
        // GET Fetch all full texts
    async function fetchFullTexts() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/fulltexts', {
                deep: true
            });
            if (error.value) {
                console.error('Error fetching full texts:', error.value);
                return;
            }
            fullTexts.value = data.value as FullTextDTO[];
        }
    }

        // GET refetch fulltexts
    async function refreshFullTextsData() {
        try {
            const data = await $fetch('/api/fulltexts');
            fullTexts.value = data as FullTextDTO[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

        // GET Fetch fulltext by ID
    async function fetchFullTextById(id: number) {
        if (!currentFullText.value || currentFullText.value.id !== id) {
            const cachedFullText = fullTexts.value.find(ft => ft.id === id);
            if (cachedFullText) {
                currentFullText.value = cachedFullText;
            } else {
                const { data, error } = await useFetch(`/api/fulltexts/${id}`, {
                    deep: true
                });
                if (error.value) {
                    console.error(`Error fetching full text with id ${id}:`, error.value);
                    return;
                }
                currentFullText.value = data.value as FullTextDTO;
            }
        }
    }

        // POST Create new full text
    async function createFullText(payload: Partial<FullTextDTO>) {
        try {
            const newFullText = await $fetch<FullTextDTO>('/api/fulltexts', {
                method: 'POST',
                body: payload
            });
            fullTexts.value.push(newFullText);
            return newFullText;
        } catch (err) {
            console.error('Error creating full text:', err);
            return;
        }
    }

        // PUT Update existing full text
    async function updateFullText(id: number, payload: Partial<FullTextDTO>) {
        try {
            const updatedFullText = await $fetch<FullTextDTO>(`/api/fulltexts/${id}`, {
                method: 'PUT',
                body: payload
            });
            const index = fullTexts.value.findIndex(ft => ft.id === id);
            if (index !== -1) fullTexts.value[index] = updatedFullText;
            if (currentFullText.value && currentFullText.value.id === id) currentFullText.value = updatedFullText;
            return updatedFullText;
        } catch (err) {
            console.error(`Error updating full text with id ${id}:`, err);
            return;
        }
    }

        // DELETE Remove full text
    async function deleteFullText(id: number) {
        try {
            await $fetch(`/api/fulltexts/${id}`, {
                method: 'DELETE'
            });
            fullTexts.value = fullTexts.value.filter(ft => ft.id !== id);
            if (currentFullText.value && currentFullText.value.id === id) currentFullText.value = null;
        } catch (err) {
            console.error(`Error deleting full text with id ${id}:`, err);
            return;
        }
    }

        // GET Search full texts by query
    async function searchFullTexts(query: string): Promise<FullTextSearchResult[]> {
        if (!query || query.trim() === '') return [];
        try {
            const data = await $fetch<FullTextSearchResult[]>(`/api/fulltexts/search?query=${encodeURIComponent(query)}`);
            return data as FullTextSearchResult[];
        } catch (err) {
            console.error('Error searching full texts:', err);
            return [];
        }
    }

    // Return
    return {
        fullTexts,
        currentFullText,
        isLoaded,
        fetchFullTexts,
        refreshFullTextsData,
        fetchFullTextById,
        createFullText,
        updateFullText,
        deleteFullText,
        searchFullTexts
    };
})
