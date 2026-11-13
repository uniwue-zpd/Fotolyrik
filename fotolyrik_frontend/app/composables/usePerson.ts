export const usePerson = () => {
    function fetchAll() {
        return $fetch<PersonDTO[]>('/api/persons/all');
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
        return $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats`, {
            query: { type: 'themes', limit }
        });
    }

    function fetchAuthorImageMotifsById(id: number, limit?: number) {
        return $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats`, {
            query: { type: 'image-motifs', limit }
        });
    }

    function fetchMetricsById(id: number) {
        return $fetch<PersonMetricsDTO>(`/api/persons/${id}/stats`, {
            query: { type: 'metrics' }
        });
    }

    function fetchWorkedWithGraph(){
        return $fetch<GraphDTO>(`/api/persons/stats`, {
            query: { type: 'worked-with', view: 'graph' }
        });
    }

    /** Removes undefined, null and empty-string values so they are not sent as query parameters. */
    function cleanQuery(params: Record<string, any>) {
        return Object.fromEntries(
            Object.entries(params).filter(([, value]) => value !== undefined && value !== null && value !== '')
        );
    }

    /** Flat view: unpaginated list of lightweight previews matching the given filters. */
    function fetchFlat(params: PersonPageable): Promise<PersonPreviewDTO[]> {
        return $fetch<PersonPreviewDTO[]>('/api/persons', {
            query: { ...cleanQuery(params), view: 'flat' }
        });
    }

    /** Name search (first/last/full name, studio name, pseudonyms) for autocompletes. */
    async function search(query: string): Promise<PersonPreviewDTO[]> {
        if (!query || query.trim().length < 2) return [];
        return fetchFlat({ name: query.trim() });
    }

    /** Paginated view: page of full person DTOs. All filters are optional. */
    function fetchPaginated(params: PersonPageable): Promise<Page<PersonDTO>> {
        return $fetch<Page<PersonDTO>>('/api/persons', {
            query: { ...cleanQuery(params), view: 'paginated' }
        });
    }

    function searchPaginated(query: string, pageable: Pageable): Promise<Page<PersonDTO>> {
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
        fetchFlat,
        fetchPaginated,
        searchPaginated,
        getAll,
        getById,
        getNeighborsById,
    };
};
