export const usePerson = () => {
    function fetchAll() {
        return $fetch<PersonDTO[]>('/api/persons');
    }

    function fetchById(id: number) {
        return $fetch<PersonDTO>(`/api/persons/${id}`);
    }

    function fetchNeighborsById(id: number) {
        return $fetch<IDSliceDTO>(`/api/persons/${id}/neighbor`);
    }

    function create(payload: Partial<PersonDTO>) {
        return $fetch<PersonDTO>('/api/persons', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<PersonDTO>) {
        return $fetch<PersonDTO>(`/api/persons/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/persons/${id}`, {
            method: 'DELETE'
        });
    }

    function fetchAuthorThemesById(id: number, limit?: number) {
        return $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats/themes`, {
            query: { limit }
        });
    }

    function fetchAuthorImageMotifsById(id: number, limit?: number) {
        return $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats/image-motifs`, {
            query: { limit }
        });
    }

    function fetchMetricsById(id: number) {
        return $fetch<PersonMetricsDTO>(`/api/persons/${id}/stats/metrics`);
    }

    function search(query: string) {
        return $fetch<PersonPreviewDTO[]>('/api/persons/search', {
            query: { query }
        });
    }
    function getAll(){
        return useAsyncData('person-list', fetchAll);
    }
    function getById(id: number){
        return useAsyncData( `person-${id}`, () => fetchById(id) );
    }
    function getNeighborsById(id: number){
        return useAsyncData(`person-${id}-neighbor`,() =>  fetchNeighborsById(id))
    }

    return {
        fetchAll,
        fetchById,
        fetchNeighborsById,
        create,
        update,
        deleteById,
        fetchAuthorThemesById,
        fetchAuthorImageMotifsById,
        fetchMetricsById,
        search,
        getAll,
        getById,
        getNeighborsById,
    };
};
