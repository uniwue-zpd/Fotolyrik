import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { PhotoPoemDTO } from "~/utils/types";

export const usePhotopoemStore = defineStore('photopoem', () => {
    // State
    const photopoems = ref<PhotoPoemDTO[]>([] as PhotoPoemDTO[]);
    const currentPhotopoem = ref<PhotoPoemDTO | null>(null);

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
            photopoems.value = data.value as PhotoPoemDTO[];

            console.log(JSON.stringify(data.value, null, 2))
            //console.log(photopoems.value)
        }
    }

        // GET refetch photopoems
    async function refreshPhotopoemsData() {
        try {
            const data = await $fetch('/api/photopoems');
            photopoems.value = data as PhotoPoemDTO[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
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
                currentPhotopoem.value = data.value as PhotoPoemDTO;
            }
        }
    }

        // Fetch photopoem by author's ID
    async function filterPhotopoems(params: Record<string, any>): Promise<PhotoPoemDTO[]> {
        try {
            const data = await $fetch('/api/photopoems/filter', { query: params });
            return data as PhotoPoemDTO[];
        } catch (err) {
            console.error('Error fetching photopoems by params:', err);
            return [];
        }
    }

        // Create new photopoem
    async function createPhotopoem(payload: Partial<PhotoPoemDTO>) {
        const { data, error } = await useFetch('/api/photopoems', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating photopoem:', error.value);
            return;
        }
        const response = data.value as PhotoPoemDTO;
        photopoems.value.push(response);
        return response;
    }

        // Update existing photopoem
    async function updatePhotopoem(payload: Partial<PhotoPoemDTO>, id: number) {
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
        const updatedPhotopoem = data.value as PhotoPoemDTO;
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
        refreshPhotopoemsData,
        fetchPhtotopoemById,
        filterPhotopoems,
        createPhotopoem,
        updatePhotopoem,
        deletePhotopoem,
        previousPhotopoem,
        nextPhotopem,
        clearPhotopoem
    }
})
