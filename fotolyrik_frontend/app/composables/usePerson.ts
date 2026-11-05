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

    function fetchWorkedWithGraph(){
        return $fetch<GraphDTO>(`/api/persons/stats/graph/worked_with`);
    }

    function search(query: string) {
        return $fetch<PersonPreviewDTO[]>('/api/persons/search', {
            query: { query }
        });
    }

    function fetchPaginated(params: PersonPageable): Promise<Page<PersonPreviewDTO>> {
        const query = Object.fromEntries(
            Object.entries(params).filter(([, value]) => value !== undefined && value !== null && value !== '')
        );
        return $fetch<Page<PersonPreviewDTO>>('/api/persons/search_paginated', { query });
    }

    function searchPaginated(query: string, pageable: Pageable): Promise<Page<PersonPreviewDTO>> {
        return fetchPaginated({ ...pageable, name: query });
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
        fetchWorkedWithGraph,
        search,
        fetchPaginated,
        searchPaginated,
        getAll,
        getById,
        getNeighborsById,
    };
};
