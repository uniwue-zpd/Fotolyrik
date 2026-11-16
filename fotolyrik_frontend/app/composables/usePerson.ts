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

    /** Flat view: unpaginated list of lightweight previews. All filters are optional. */
    function fetchFlat(params: PersonPageable = {}) {
        return $fetch<PersonPreviewDTO[]>('/api/persons', {
            query: { ...cleanQuery(params), view: 'flat' }
        });
    }

    /** Paginated view: page of full person DTOs. All filters are optional. */
    function fetchPaginated(params: PersonPageable = {}) {
        return $fetch<Page<PersonDTO>>('/api/persons', {
            query: { ...cleanQuery(params), view: 'paginated' }
        });
    }

    return {
        fetchAll,
        fetchById,
        fetchNeighborsById,
        fetchFlat,
        fetchPaginated,
        create,
        update,
        deleteById,
        fetchAuthorThemesById,
        fetchAuthorImageMotifsById,
        fetchMetricsById,
        fetchWorkedWithGraph,
    };
};
