export const usePubRhythmStore = defineStore('publication_rhythm', () => {
    // State
    const publication_rhythms = ref<PubRhythmDTO[]>([] as PubRhythmDTO[]);
    const currentPubRhythm = ref<PubRhythmDTO | null>(null);

    // Getters
    const isLoaded = computed(() => publication_rhythms.value.length > 0);

    // Actions
    // GET Fetch all publication_rhythms
    async function fetchPubRhythms() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/publication_rhythms');
            if (error.value) {
                console.error('Error fetching publication_rhythms:', error.value);
                return;
            }
            publication_rhythms.value = data.value as PubRhythmDTO[];
        }
    }

    // GET refetch publication_rhythms
    async function refreshPubRhythmsData() {
        try {
            const data = await $fetch('/api/publication_rhythms');
            publication_rhythms.value = data as PubRhythmDTO[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

    // GET Fetch publication rhythm by ID
    async function fetchPubRhythmById(id: number) {
        if (!currentPubRhythm.value || currentPubRhythm.value.id !== id) {
            const cachedPubRhythm = publication_rhythms.value.find(k => k.id === id);
            if (cachedPubRhythm) {
                currentPubRhythm.value = cachedPubRhythm;
            } else {
                const { data, error } = await useFetch(`/api/publication_rhythms/${id}`);
                if (error.value) {
                    console.error(`Error fetching publication rhythm with id ${id}:`, error.value);
                    return;
                }
                currentPubRhythm.value = data.value as PubRhythmDTO;
            }
        }
    }

    // POST Create new publication rhythm
    async function createPubRhythm(payload: Partial<PubRhythmDTO>) {
        const { data, error } = await useFetch('/api/publication_rhythms', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating publication rhythm:', error.value);
            return;
        }
        const newPubRhythm = data.value as PubRhythmDTO;
        publication_rhythms.value.push(newPubRhythm);
        return newPubRhythm;
    }

    // PUT Update existing publication rhythm
    async function updatePubRhythm(payload: Partial<PubRhythmDTO>, id: number) {
        const { data, error } = await useFetch(`/api/publication_rhythms/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Error updating publication rhythm:', error.value);
            return;
        }
        const updatedPubRhythm = data.value as PubRhythmDTO;
        const index = publication_rhythms.value.findIndex(k => k.id === id);
        if (index !== -1) publication_rhythms.value[index] = updatedPubRhythm;
        if (currentPubRhythm.value?.id === id) currentPubRhythm.value = updatedPubRhythm;
        return updatedPubRhythm;
    }

    // DELETE publication rhythm
    async function deletePubRhythm(id: number) {
        const { error } = await useFetch(`/api/publication_rhythms/${id}`, { method: 'DELETE' })
        if (error.value) {
            console.error('Error deleting publication rhythm:', error.value);
            return;
        }
        publication_rhythms.value = publication_rhythms.value.filter(k => k.id !== id)
        if (currentPubRhythm.value?.id === id) currentPubRhythm.value = null;
    }

    // Clear current publication rhythm
    function clearCurrentPubRhythm() {
        currentPubRhythm.value = null;
    }

    return {
        publication_rhythms,
        currentPubRhythm,
        isLoaded,
        fetchPubRhythms,
        refreshPubRhythmsData,
        fetchPubRhythmById,
        createPubRhythm,
        updatePubRhythm,
        deletePubRhythm,
        clearCurrentPubRhythm
    }
});
