
export const useCopyrightStatus = ()=>{
    async function fetchCopyrightStatuses() {
        return await $fetch<CopyrightStatusDTO[]>('/api/copyright_statuses');
    }
    async function fetchCopyrightStatusById(id: number) {
        return $fetch<CopyrightStatusDTO>(`/api/copyright_statuses/${id}`);
    }

    async function createCopyrightStatus(payload: Partial<CopyrightStatusDTO>){
        return $fetch<CopyrightStatusDTO>('/api/copyright_statuses', {
            method: 'POST',
            body: payload
        });
    }

    async function updateCopyrightStatus(id:number, payload: Partial<CopyrightStatusDTO>) {
        return $fetch<CopyrightStatusDTO>(`/api/copyright_statuses/${id}`, {
            method: 'PUT',
            body: payload
        });
    }
    async function deleteCopyrightStatus(id: number) {
        return $fetch<void>(`/api/copyright_statuses/${id}`, { method: 'DELETE' })
    }
    return {
        fetchCopyrightStatuses,
        fetchCopyrightStatusById,
        createCopyrightStatus,
        updateCopyrightStatus,
        deleteCopyrightStatus,
    }
}
