
export const useCopyrightStatusStore = defineStore('copyrightStatus', () => {
        // State
    const copyrightStatuses = ref<CopyrightStatusDTO[]>([] as CopyrightStatusDTO[]);
    const currentCopyrightStatus = ref<CopyrightStatusDTO|null>(null);

    // Getters
    const isLoaded = computed(() => copyrightStatuses.value.length > 0);

    // Actions
        // GET Fetch all copyright statuses
    async function fetchCopyrightStatuses() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/copyright_statuses');
            if (error.value) {
                console.error(error.value);
                return;
            }
            copyrightStatuses.value = data.value as CopyrightStatusDTO[];
        }
    }

        // GET refetch copyright statues
    async function refreshCopyrighStatusesData() {
        try {
            const data = await $fetch('/api/copyright_statuses');
            copyrightStatuses.value = data as CopyrightStatusDTO[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
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
                currentCopyrightStatus.value = data.value as CopyrightStatusDTO;
            }
        }
    }

        // POST Create new copyright status
    async function createCopyrightStatus(payload: Partial<CopyrightStatusDTO>) {
        const { data, error } = await useFetch('/api/copyright_statuses', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Unable to create copyright status: ', error.value);
            return;
        }
        const newCopyrightStatus = data.value as CopyrightStatusDTO;
        copyrightStatuses.value.push(newCopyrightStatus);
        return newCopyrightStatus;
    }

        // PUT Update existing copyright status
    async function updateCopyrightStatus(payload: Partial<CopyrightStatusDTO>, id: number) {
        const { data, error } = await useFetch(`/api/copyright_statuses/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Unable to update copyright status :', error.value);
            return;
        }
        const updatedCopyrightStatus = data.value as CopyrightStatusDTO;
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
        refreshCopyrighStatusesData,
        fetchCopyrightStatusById,
        createCopyrightStatus,
        updateCopyrightStatus,
        deleteCopyrightStatus,
        clearCurrentCopyrightStatus
    }
})
