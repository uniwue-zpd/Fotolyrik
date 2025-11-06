import {defineStore} from "pinia";
import { ref, computed } from "vue";

export const usePubMediumStore = defineStore('pubMedium', () => {
    // State
    const pub_media = ref<PubMedium[]>([] as PubMedium[]);
    const current_pub_medium = ref<PubMedium | null>(null);

    // Getters
    const isLoaded = computed(() => pub_media.value.length > 0);

    // Actions
        // GET all  publication media
    async function fetchPubMedia() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/publication_media');
            if (error.value) {
                console.error('Error fetching publication media:', error.value);
                return;
            }
            pub_media.value = data.value as PubMedium[];
        }
    }

        // GET refetch publication media
    async function refreshPubMediaData() {
        try {
            const data = await $fetch('/api/publication_media');
            pub_media.value = data as PubMedium[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

        // GET publication medium by ID
    async function fetchPubMediumById(id: number) {
        if (!current_pub_medium.value || current_pub_medium.value.id !== id) {
            const cachedPubMedium = pub_media.value.find(p => p.id === id);
            if (cachedPubMedium) {
                current_pub_medium.value = cachedPubMedium;
            } else {
                const { data, error } = await useFetch(`/api/publication_media/${id}`);
                if (error.value) {
                    console.error(`Error fetching publication medium with id ${id}:`, error.value);
                    return;
                }
                current_pub_medium.value = data.value as PubMedium;
            }
        }
    }

        // POST Create new publication medium
    async function createPubMedium(payload: Partial<PubMedium>) {
        const { data, error } = await useFetch('/api/publication_media', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating publication medium:', error.value);
            return;
        }
        const newPubMedium = data.value as PubMedium;
        pub_media.value.push(newPubMedium);
        return newPubMedium;
    }

        // PUT Update existing publication medium
    async function updatePubMedium(payload: Partial<PubMedium>, id: number) {
        const { data, error } = await useFetch(`/api/publication_media/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Error updating publication medium:', error.value);
            return;
        }
        const updatedPubMedium = data.value;
        const index = pub_media.value.findIndex(p => p.id === id);
        if (index !== -1) pub_media.value[index] = updatedPubMedium;
        if (current_pub_medium.value?.id === id) current_pub_medium.value = updatedPubMedium;
        return updatedPubMedium;
    }

        // DELETE existing publication medium
    async function deletePubMedium(id: number) {
        const { error } = await useFetch(`/api/publication_media/${id}`, { method: 'DELETE' });
        if (error.value) {
            console.error('Error deleting publication medium:', error.value);
            return;
        }
        pub_media.value = pub_media.value.filter(p => p.id !== id);
        if (current_pub_medium.value?.id === id) current_pub_medium.value = null;
    }

        // Navigation left
    function previousPubMedium() {
        const currentIndex = pub_media.value.findIndex(p => p.id === current_pub_medium.value?.id);
        if (currentIndex !== -1 && currentIndex) {
            return pub_media.value[currentIndex - 1] as PubMedium;
        } else {
            return null;
        }
    }

        // Navigation right
    function nextPubMedium() {
        const currentIndex = pub_media.value.findIndex(p => p.id === current_pub_medium.value?.id);
        if (currentIndex !== -1 && currentIndex < pub_media.value.length - 1) {
            return pub_media.value[currentIndex + 1] as PubMedium;
        } else {
            return null;
        }
    }

        // Clear current publication medium
    function clearPubMedium() {
        current_pub_medium.value = null;
    }

    return {
        pub_media,
        current_pub_medium,
        fetchPubMedia,
        refreshPubMediaData,
        fetchPubMediumById,
        createPubMedium,
        updatePubMedium,
        deletePubMedium,
        previousPubMedium,
        nextPubMedium,
        clearPubMedium,
   }
});
