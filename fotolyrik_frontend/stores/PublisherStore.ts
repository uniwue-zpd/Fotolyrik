export const usePublisherStore = defineStore('publisher', () => {
    // State
    const publishers = ref<Publisher[]>([] as Publisher[]);
    const currentPublisher = ref<Publisher | null>(null);

    // Getters
    const isLoaded = computed(() => publishers.value.length > 0);

    // Actions
    // GET Fetch all publishers
    async function fetchPublishers() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/publishers');
            if (error.value) {
                console.error('Error fetching publishers:', error.value);
                return;
            }
            publishers.value = data.value as Publisher[];
        }
    }

    // GET refetch publishers
    async function refreshPublishersData() {
        try {
            const data = await $fetch('/api/publishers');
            publishers.value = data as Publisher[];
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
                const { data, error } = await useFetch(`/api/publishers/${id}`);
                if (error.value) {
                    console.error(`Error fetching publisher with id ${id}:`, error.value);
                    return;
                }
                currentPublisher.value = data.value as Publisher;
            }
        }
    }

    // POST Create new publisher
    async function createPublisher(payload: Partial<Publisher>) {
        const { data, error } = await useFetch('/api/publishers', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating publisher:', error.value);
            return;
        }
        const newPublisher = data.value as Publisher;
        publishers.value.push(newPublisher);
        return newPublisher;
    }

    // PUT Update existing publisher
    async function updatePublisher(payload: Partial<Publisher>, id: number) {
        const { data, error } = await useFetch(`/api/publishers/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Error updating publisher:', error.value);
            return;
        }
        const updatedPublisher = data.value as Publisher;
        const index = publishers.value.findIndex(k => k.id === id);
        if (index !== -1) publishers.value[index] = updatedPublisher;
        if (currentPublisher.value?.id === id) currentPublisher.value = updatedPublisher;
        return updatedPublisher;
    }

    // DELETE publisher
    async function deletePublisher(id: number) {
        const { error } = await useFetch(`/api/publishers/${id}`, { method: 'DELETE' })
        if (error.value) {
            console.error('Error deleting publisher:', error.value);
            return;
        }
        publishers.value = publishers.value.filter(k => k.id !== id)
        if (currentPublisher.value?.id === id) currentPublisher.value = null;
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
