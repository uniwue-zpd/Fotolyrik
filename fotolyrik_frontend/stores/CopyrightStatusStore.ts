import type { CopyrightStatus } from "~/utils/types";

export const useCopyrightStatusStore = defineStore('copyrightStatus', () => {
        // State
    const copyrightStatuses = ref<CopyrightStatus[]>([] as CopyrightStatus[]);
    const currentCopyrightStatus = ref<CopyrightStatus|null>(null);

    // Getters
    const isLoaded = computed(() => copyrightStatuses.value.length > 0);

    // Actions
        // GET Fetch all copyright statuses
    async function fetchCopyrightStatuses() {
        const { data, error } = await useFetch('/api/copyright_statuses');
        if (error.value) {
            console.error(error.value);
            return;
        }
        copyrightStatuses.value = data.value as CopyrightStatus[];
    }

        // GET a copyright status by its ID
    async function fetchCopyrightStatusById(id: number) {
        if (!currentCopyrightStatus.value || currentCopyrightStatus.value.id !== id) {
            const cachedCopyrightStatus = copyrightStatuses.value.find(k => k.id === id);
            if (cachedCopyrightStatus) {
                currentCopyrightStatus.value = cachedCopyrightStatus;
            } else {
                const { data, error } = await useFetch(`/api/copyright_statuses/${id}`);
                if (error.value) {
                    console.error('Unable to fetch the copyright status: ', error.value);
                    return;
                }
                currentCopyrightStatus.value = data.value as CopyrightStatus;
            }
        }
    }

        // POST Create new copyright status
    async function createCopyrightStatus(payload: Partial<CopyrightStatus>) {
        const { data, error } = await useFetch('/api/copyright_statuses', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Unable to create copyright status: ', error.value);
            return;
        }
        const newCopyrightStatus = data.value as CopyrightStatus;
        copyrightStatuses.value.push(newCopyrightStatus);
        return newCopyrightStatus;
    }

        // PUT Update existing copyright status
    async function updateCopyrightStatus(payload: Partial<CopyrightStatus>, id: number) {
        const { data, error } = await useFetch(`/api/copyright_statuses/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Unable to update copyright status :', error.value);
            return;
        }
        const updatedCopyrightStatus = data.value as CopyrightStatus;
        const index = copyrightStatuses.value.findIndex(k => k.id === id);
        if (index !== -1) copyrightStatuses.value[index] = updatedCopyrightStatus;
        if (currentCopyrightStatus.value?.id === id) currentCopyrightStatus.value = updatedCopyrightStatus;
        return updatedCopyrightStatus;
    }

        // DELETE copyright status
    async function deleteCopyrightStatus(id: number) {
        const { error } = await useFetch(`/api/copyright_statuses/${id}`, { method: 'DELETE' })
        if (error.value) {
            console.error('Unable to delete copyright status: ', error.value);
            return;
        }
        copyrightStatuses.value = copyrightStatuses.value.filter(k => k.id !== id)
        if (currentCopyrightStatus.value?.id === id) currentCopyrightStatus.value = null;
    }

        // Clear current copyright status
    function clearCurrentCopyrightStatus() {
        currentCopyrightStatus.value = null;
    }

    return {
        copyrightStatuses,
        currentCopyrightStatus,
        isLoaded,
        fetchCopyrightStatuses,
        fetchCopyrightStatusById,
        createCopyrightStatus,
        updateCopyrightStatus,
        deleteCopyrightStatus,
        clearCurrentCopyrightStatus
    }
})
