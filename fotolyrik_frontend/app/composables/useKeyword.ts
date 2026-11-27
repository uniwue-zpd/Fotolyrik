export const useKeyword = () => {
    function fetchAll() {
        return $fetch<KeywordDTO[]>('/api/keywords/all');
    }

    function fetchById(id: number) {
        return $fetch<KeywordDTO>(`/api/keywords/${id}`);
    }

    /** Removes undefined, null and empty-string values so they are not sent as query parameters. */
    function cleanQuery(params: Record<string, any>) {
        return Object.fromEntries(
            Object.entries(params).filter(([, value]) => value !== undefined && value !== null && value !== '')
        );
    }

    /** Flat view: unpaginated list of keyword previews. All filters are optional. */
    function fetchFlat(params: KeywordPageable = {}) {
        return $fetch<KeywordPreviewDTO[]>('/api/keywords', {
            query: { ...cleanQuery(params), view: 'flat' }
        });
    }

    /** Paginated view: page of keyword previews. All filters are optional. */
    function fetchPaginated(params: KeywordPageable = {}) {
        return $fetch<Page<KeywordPreviewDTO>>('/api/keywords', {
            query: { ...cleanQuery(params), view: 'paginated' }
        });
    }

    function create(payload: Partial<KeywordDTO>) {
        return $fetch<KeywordDTO>('/api/keywords', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<KeywordDTO>) {
        return $fetch<KeywordDTO>(`/api/keywords/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/keywords/${id}`, {
            method: 'DELETE'
        });
    }

    function getGNDSuggestions(term: string): Promise<GndSuggestionDTO[]> {
        return $fetch<GndSuggestionDTO[]>(`/api/keywords/gnd-suggestions`, {
            query: { term }
        });
    }

    return {
        fetchAll,
        fetchById,
        fetchFlat,
        fetchPaginated,
        create,
        update,
        deleteById,
        getGNDSuggestions
    };
};
