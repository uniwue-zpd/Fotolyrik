export const usePerson = () => {
    function fetchPersons() {
        return $fetch<PersonDTO[]>('/api/persons');
    }

    function fetchPersonById(id: number) {
        return $fetch<PersonDTO>(`/api/persons/${id}`);
    }

    function fetchPersonNeighborsById(id: number) {
        return $fetch<IDSliceDTO>(`/api/persons/${id}/neighbor`);
    }

    function createPerson(payload: Partial<PersonDTO>) {
        return $fetch<PersonDTO>('/api/persons', {
            method: 'POST',
            body: payload
        });
    }

    function updatePerson(id: number, payload: Partial<PersonDTO>) {
        return $fetch<PersonDTO>(`/api/persons/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deletePerson(id: number) {
        return $fetch<void>(`/api/persons/${id}`, {
            method: 'DELETE'
        });
    }

    function fetchAuthorThemes(id: number, limit?: number) {
        return $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats/themes`, {
            query: { limit }
        });
    }

    function fetchAuthorImageMotifs(id: number, limit?: number) {
        return $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats/image-motifs`, {
            query: { limit }
        });
    }

    function fetchPersonMetrics(id: number) {
        return $fetch<PersonMetricsDTO>(`/api/persons/${id}/stats/metrics`);
    }

    function searchPeople(query: string) {
        return $fetch<PersonPreviewDTO[]>('/api/persons/search', {
            query: { query }
        });
    }
    function usePersonList(){
        return useAsyncData('person-list', fetchPersons);
    }
    function usePersonId(id: number){
        return useAsyncData( `person-${id}`, () => fetchPersonById(id) );
    }
    function usePersonIdNeighbors(id: number){
        return useAsyncData(`person-${id}-neighbor`,() =>  fetchPersonNeighborsById(id))
    }

    return {
        fetchPersons,
        fetchPersonById,
        fetchPersonNeighborsById,
        createPerson,
        updatePerson,
        deletePerson,
        fetchAuthorThemes,
        fetchAuthorImageMotifs,
        fetchPersonMetrics,
        searchPeople,
        usePersonList,
        usePersonId,
        usePersonIdNeighbors,
    };
};
