export const useCopyrightStatus = ()=>{
    function fetchAll() {
        return $fetch<CopyrightStatusDTO[]>('/api/copyright_statuses');
    }
    function fetchById(id: number) {
        return $fetch<CopyrightStatusDTO>(`/api/copyright_statuses/${id}`);
    }

    function create(payload: Partial<CopyrightStatusDTO>){
        return $fetch<CopyrightStatusDTO>('/api/copyright_statuses', {
            method: 'POST',
            body: payload
        });
    }

    function update(id:number, payload: Partial<CopyrightStatusDTO>) {
        return $fetch<CopyrightStatusDTO>(`/api/copyright_statuses/${id}`, {
            method: 'PUT',
            body: payload
        });
    }
    function deleteByID(id: number) {
        return $fetch<void>(`/api/copyright_statuses/${id}`, { method: 'DELETE' })
    }
    function getAll(){
        return useAsyncData('copyright-status-list', fetchAll);
    }

    return {
        fetchAll,
        fetchById,
        create,
        update,
        deleteByID,
        getAll,
    }
}
