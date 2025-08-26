import { defineStore } from 'pinia';
import { ref } from 'vue';
import apiClient from "~/service/api";
import type { PhotoPoem } from "~/utils/types";

export const usePhotopoemStore = defineStore('photopoem', () => {
    // State
    const photopoems = ref<PhotoPoem[]>([] as PhotoPoem[]);
    const currentPhotopoem = ref<PhotoPoem | null>(null);

    // Getters
    const isLoaded = computed(() => photopoems.value.length > 0);

    // Actions
        // Fetch all photopoems
    async function fetchPhotopoems() {
        if (!isLoaded.value) {
            try {
                const response = await apiClient.get<PhotoPoem[]>('/photopoems');
                photopoems.value = response.data;
            } catch (error) {
                console.log('Error fetching photopoems:', error);
            }
        }
    }

        // Fetch photopoem by ID
    async function fetchPhtotopoemById(id: number) {
        if (!currentPhotopoem.value || currentPhotopoem.value.id !== id) {
            const cachedPhotopoem = photopoems.value.find(p => p.id === id);
            if (cachedPhotopoem) {
                currentPhotopoem.value = cachedPhotopoem;
            } else {
                try {
                    const response = await apiClient.get<PhotoPoem>(`/photopoems/${id}`);
                    currentPhotopoem.value = response.data;
                } catch (error) {
                    console.log('Error fetching photopoem by ID:', error);
                }
            }
        }
    }

        // Fetch photopoem by author's ID
    async function fetchPhotopoemsBy(params: Record<string, any>): Promise<PhotoPoem[]> {
        try {
            const response = await apiClient.get<PhotoPoem[]>(`/photopoems/filter`, {
                params
            });
            return response.data;
        } catch (error) {
            console.log('Error fetching photopoems by author ID:', error);
            return [];
        }
    }

        // Create new photopoem
    async function createPhotopoem(payload: Partial<PhotoPoem>) {
        try {
            const response = await apiClient.post('/photopoems', payload);
            photopoems.value.push(response.data);
            return response.data;
        } catch (error) {
            console.log('Error creating photopoem:', error);
            throw error;
        }
    }

        // Update existing photopoem
    async function updatePhotopoem(payload: Partial<PhotoPoem>, id: number) {
        try {
            const response = await apiClient.put(`/photopoems/${id}`, payload);
            const index = photopoems.value.findIndex(p => p.id === id);
            if (index !== -1) {
                photopoems.value[index] = response.data;
            }
            if (currentPhotopoem.value?.id === id) {
                currentPhotopoem.value = response.data;
            }
            return response.data;
        } catch (error) {
            console.log('Error updating photopoem:', error);
            throw error;
        }
    }

        // DELETE existing photopoem
    async function deletePhotopoem(id: number) {
        try {
            await apiClient.delete(`/photopoems/${id}`);
            photopoems.value = photopoems.value.filter(p => p.id !== id);
            if (currentPhotopoem.value?.id === id) {
                currentPhotopoem.value = null;
            }
        } catch (error) {
            console.log('Error deleting person:', error);
            throw error;
        }
    }

        // Navigation left
    function previousPhotopoem() {
        const currentIndex = photopoems.value.findIndex(p => p.id === currentPhotopoem.value?.id);
        if (currentIndex !== -1 && currentIndex) {
            return photopoems.value[currentIndex - 1] as PhotoPoem;
        } else {
            return null;
        }
    }

        // Navigation right
    function nextPhotopem() {
        const currentIndex = photopoems.value.findIndex(p => p.id === currentPhotopoem.value?.id);
        if (currentIndex !== -1 && currentIndex < photopoems.value.length - 1) {
            return photopoems.value[currentIndex + 1] as PhotoPoem;
        } else {
            return null;
        }
    }

        // Clear current photopoem
    function clearPhotopoem() {
        currentPhotopoem.value = null;
    }

    return {
        photopoems,
        currentPhotopoem,
        isLoaded,
        fetchPhotopoems,
        fetchPhtotopoemById,
        fetchPhotopoemsBy,
        createPhotopoem,
        updatePhotopoem,
        deletePhotopoem,
        previousPhotopoem,
        nextPhotopem,
        clearPhotopoem
    }
})
