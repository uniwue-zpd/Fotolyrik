import {defineStore} from "pinia";
import { ref, computed } from "vue";
import apiClient from "~/service/api";

const usePubMediumStore = defineStore('pubMedium', () => {
    // State
    const pub_media = ref<PubMedium[]>([] as PubMedium[]);
    const current_pub_medium = ref<PubMedium | null>(null);

    // Getters
    const isLoaded = computed(() => pub_media.value.length > 0);

    // Actions
        // GET all  publication media
    async function fetchPubMedia() {
        if (!isLoaded.value) {
            try {
                const response = await apiClient.get<PubMedium[]>('/publication_media');
                pub_media.value = response.data;
            } catch (error) {
                console.log('Error fetching publication media:', error);
            }
        }
    }

        // GET publication medium by ID
    async function fetchPubMediumById(id: number) {
        if (!current_pub_medium.value || current_pub_medium.value.id !== id) {
            const cachedPubMedium = pub_media.value.find(p => p.id === id);
            if (cachedPubMedium) {
                current_pub_medium.value = cachedPubMedium;
            } else {
                try {
                    const response = await apiClient.get<PubMedium>(`/publication_media/${id}`);
                    current_pub_medium.value = response.data;
                } catch (error) {
                    console.log('Error fetching publication medium by ID:', error);
                }
            }
        }
    }

        // POST Create new publication medium
    async function createPubMedium(payload: Partial<PubMedium>) {
        try {
            const response = await apiClient.post('/publication_media', payload);
            pub_media.value.push(response.data);
            return response.data;
        } catch (error) {
            console.log('Error creating publication medium:', error);
            throw error;
        }
    }

        // PUT Update existing publication medium
    async function updatePubMedium(payload: Partial<PubMedium>, id: number) {
        try {
            const response = await apiClient.put(`/publication_media/${id}`, payload);
            const index = pub_media.value.findIndex(p => p.id === id);
            if (index !== -1) {
                pub_media.value[index] = response.data;
            }
            if (current_pub_medium.value?.id === id) {
                current_pub_medium.value = response.data;
            }
            return response.data;
        } catch (error) {
            console.log('Error updating publication medium:', error);
            throw error;
        }
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
        fetchPubMediumById,
        previousPubMedium,
        nextPubMedium,
        clearPubMedium,
   }
});