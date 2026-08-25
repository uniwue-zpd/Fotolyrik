export const useCopyrightStatus = ()=>{
    function fetchCopyrightStatuses() {
        return $fetch<CopyrightStatusDTO[]>('/api/copyright_statuses');
    }
    function fetchCopyrightStatusById(id: number) {
        return $fetch<CopyrightStatusDTO>(`/api/copyright_statuses/${id}`);
    }

    function createCopyrightStatus(payload: Partial<CopyrightStatusDTO>){
        return $fetch<CopyrightStatusDTO>('/api/copyright_statuses', {
            method: 'POST',
            body: payload
        });
    }

    function updateCopyrightStatus(id:number, payload: Partial<CopyrightStatusDTO>) {
        return $fetch<CopyrightStatusDTO>(`/api/copyright_statuses/${id}`, {
            method: 'PUT',
            body: payload
        });
    }
    function deleteCopyrightStatus(id: number) {
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
