import { defineStore } from 'pinia';
import { ref } from 'vue';
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
            const { data, error } = await useFetch('/api/photopoems');
            if (error.value) {
                console.error('An error occured while fetching photopoems:', error.value);
                return;
            }
            photopoems.value = data.value as PhotoPoem[];
        }
    }

        // Fetch photopoem by ID
    async function fetchPhtotopoemById(id: number) {
        if (!currentPhotopoem.value || currentPhotopoem.value.id !== id) {
            const cachedPhotopoem = photopoems.value.find(p => p.id === id);
            if (cachedPhotopoem) {
                currentPhotopoem.value = cachedPhotopoem;
            } else {
                const { data, error } = await useFetch(`/api/photopoems/${id}`);
                if (error.value) {
                    console.error(`Error fetching photopoem with id ${id}`);
                    return;
                }
                currentPhotopoem.value = data.value as PhotoPoem;
            }
        }
    }

        // Fetch photopoem by author's ID
    async function fetchPhotopoemsBy(params: Record<string, any>): Promise<PhotoPoem[]> {
        const { data, error } = await useFetch('/api/photopoems/filter', { query: params });
        if (error.value) {
            console.error('Error fetching photopoems by params:', error.value);
            return [];
        }
        return data.value as PhotoPoem[] || [];
    }

        // Create new photopoem
    async function createPhotopoem(payload: Partial<PhotoPoem>) {
        const { data, error } = await useFetch('/api/photopoems', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating photopoem:', error.value);
            return;
        }
        const response = data.value as PhotoPoem;
        photopoems.value.push(response);
        return response;
    }

        // Update existing photopoem
    async function updatePhotopoem(payload: Partial<PhotoPoem>, id: number) {
        if (!photopoems.value) {
            console.error('Photopoems data is not loaded');
            return;
        }
        const { data, error } = await useFetch(`/api/photopoems/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Error updating photopoem:', error.value);
            return;
        }
        const updatedPhotopoem = data.value as PhotoPoem;
        const index = photopoems.value.findIndex(p => p.id === id);
        if (index !== -1) photopoems.value[index] = updatedPhotopoem;
        if (currentPhotopoem.value?.id === id) currentPhotopoem.value = updatedPhotopoem;
        return updatedPhotopoem;
    }

        // DELETE existing photopoem
    async function deletePhotopoem(id: number) {
        if (!photopoems.value) {
            console.error('Photopoems data is not loaded');
            return;
        }
        const { error } = await useFetch(`/api/photopoems/${id}`, { method: 'DELETE' });
        if (error.value) {
            console.error('Error deleting photopoem:', error.value)
            return;
        }
        photopoems.value = photopoems.value.filter(p => p.id !== id);
        if (currentPhotopoem.value?.id === id) currentPhotopoem.value = null;
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
