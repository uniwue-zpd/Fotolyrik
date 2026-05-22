export const usePublisherStore = defineStore('publisher', () => {
    // State
    const publishers = ref<PublisherDTO[]>([] as PublisherDTO[]);
    const currentPublisher = ref<PublisherDTO | null>(null);

    // Getters
    const isLoaded = computed(() => publishers.value.length > 0);

    // Actions
    // GET Fetch all publishers
    async function fetchPublishers() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/publishers', {
                deep: true
            });
            if (error.value) {
                console.error('Error fetching publishers:', error.value);
                return;
            }
            publishers.value = data.value as PublisherDTO[];
        }
    }

    // GET refetch publishers
    async function refreshPublishersData() {
        try {
            const data = await $fetch('/api/publishers');
            publishers.value = data as PublisherDTO[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

        // GET Fetch publisher by ID
    async function fetchPublisherById(id: number) {
        if (!currentPublisher.value || currentPublisher.value.id !== id) {
            const cachedPublisher = publishers.value.find(k => k.id === id);
            if (cachedPublisher) {
                currentPublisher.value = cachedPublisher;
            } else {
                try {
                    const data = await $fetch<PublisherDTO>(`/api/publishers/${id}`);
                    currentPublisher.value = data as PublisherDTO;
                } catch (err) {
                    console.error(`Error fetching publisher with id ${id}:`, err);
                    return;
                }
            }
        }
    }

        // POST Create new publisher
    async function createPublisher(payload: Partial<PublisherDTO>) {
        try {
            const newPublisher = await $fetch<PublisherDTO>('/api/publishers', {
                method: 'POST',
                body: payload
            });
            publishers.value.push(newPublisher);
            return newPublisher;
        } catch (err) {
            console.error('Error creating publisher:', err);
            return;
        }
    }

        // PUT Update existing publisher
    async function updatePublisher(payload: Partial<PublisherDTO>, id: number) {
        try {
            const updatedPublisher = await $fetch<PublisherDTO>(`/api/publishers/${id}`, {
                method: 'PUT',
                body: payload
            });
            const index = publishers.value.findIndex(k => k.id === id);
            if (index !== -1) publishers.value[index] = updatedPublisher;
            if (currentPublisher.value?.id === id) currentPublisher.value = updatedPublisher;
            return updatedPublisher;
        } catch (err) {
            console.error('Error updating publisher:', err);
            return;
        }
    }

        // DELETE publisher
    async function deletePublisher(id: number) {
        try {
            await $fetch(`/api/publishers/${id}`, { method: 'DELETE' });
            publishers.value = publishers.value.filter(k => k.id !== id);
            if (currentPublisher.value?.id === id) currentPublisher.value = null;
        } catch (err) {
            console.error('Error deleting publisher:', err);
            return;
        }
    }

    // Clear current publisher
    function clearCurrentPublisher() {
        currentPublisher.value = null;
    }

    return {
        publishers,
        currentPublisher,
        isLoaded,
        fetchPublishers,
        refreshPublishersData,
        fetchPublisherById,
        createPublisher,
        updatePublisher,
        deletePublisher,
        clearCurrentPublisher
    }
});
