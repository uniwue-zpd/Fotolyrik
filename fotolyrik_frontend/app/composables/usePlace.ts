export const usePlace = () => {
    function fetchAll() {
        return $fetch<PlaceDTO[]>('/api/places/all');
    }

    function fetchById(id: number) {
        return $fetch<PlaceDTO>(`/api/places/${id}`);
    }

    /** Removes undefined, null and empty-string values so they are not sent as query parameters. */
    function cleanQuery(params: Record<string, any>) {
        return Object.fromEntries(
            Object.entries(params).filter(([, value]) => value !== undefined && value !== null && value !== '')
        );
    }

    /** Flat view: unpaginated list of place previews. All filters are optional. */
    function fetchFlat(params: PlacePageable = {}) {
        return $fetch<PlacePreviewDTO[]>('/api/places', {
            query: { ...cleanQuery(params), view: 'flat' }
        });
    }

    /** Paginated view: page of full place DTOs. All filters are optional. */
    function fetchPaginated(params: PlacePageable = {}) {
        return $fetch<Page<PlaceDTO>>('/api/places', {
            query: { ...cleanQuery(params), view: 'paginated' }
        });
    }

    function getContributionPlacesById(personId: number) {
        return $fetch<PlaceDTO[]>(`/api/persons/${personId}/stats`, {
            query: { type: 'contribution-places' }
        });
    }

    function create(payload: Partial<PlaceDTO>) {
        return $fetch<PlaceDTO>('/api/places', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<PlaceDTO>) {
        return $fetch<PlaceDTO>(`/api/places/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/places/${id}`, {
            method: 'DELETE'
        });
    }

    function fetchMetricsById(id: number) {
        return $fetch<PlaceMetricsDTO>(`/api/places/${id}/stats`, {
            query: { type: 'metrics' }
        });
    }

    function fetchSamePubMediumGraph() {
        return $fetch<GraphDTO>('/api/places/stats', {
            query: { type: 'same-pub-medium', view: 'graph' }
        });
    }

    return {
        fetchAll,
        fetchById,
        fetchFlat,
        fetchPaginated,
        getContributionPlacesById,
        create,
        update,
        deleteById,
        fetchMetricsById,
        fetchSamePubMediumGraph,
    };
};
