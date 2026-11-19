export const usePubMedium = () => {
    function fetchAll() {
        return $fetch<PubMediumDTO[]>('/api/publication_media/all');
    }

    function fetchById(id: number) {
        return $fetch<PubMediumDTO>(`/api/publication_media/${id}`);
    }

    function fetchNeighborsById(id: number) {
        return $fetch<IDSliceDTO>(`/api/publication_media/${id}/neighbor`);
    }

    /** Removes undefined, null and empty-string values so they are not sent as query parameters. */
    function cleanQuery(params: Record<string, any>) {
        return Object.fromEntries(
            Object.entries(params).filter(([, value]) => value !== undefined && value !== null && value !== '')
        );
    }

    /** Flat view: unpaginated list of lightweight previews. All filters are optional. */
    function fetchFlat(params: PubMediumPageable = {}) {
        return $fetch<PubMediumPreviewDTO[]>('/api/publication_media', {
            query: { ...cleanQuery(params), view: 'flat' }
        });
    }

    /** Paginated view: page of full publication medium DTOs. All filters are optional. */
    function fetchPaginated(params: PubMediumPageable = {}) {
        return $fetch<Page<PubMediumDTO>>('/api/publication_media', {
            query: { ...cleanQuery(params), view: 'paginated' }
        });
    }

    function create(payload: Partial<PubMediumDTO>) {
        return $fetch<PubMediumDTO>('/api/publication_media', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<PubMediumDTO>) {
        return $fetch<PubMediumDTO>(`/api/publication_media/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/publication_media/${id}`, {
            method: 'DELETE'
        });
    }

    function fetchMetricsById(id: number) {
        return $fetch<PubMediumMetricsDTO>(`/api/publication_media/${id}/stats`, {
            query: { type: 'metrics' }
        });
    }

    function fetchSamePlaceGraph() {
        return $fetch<GraphDTO>('/api/publication_media/stats', {
            query: { type: 'same-place', view: 'graph' }
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
        fetchMetricsById,
        fetchSamePlaceGraph,
    };
};
