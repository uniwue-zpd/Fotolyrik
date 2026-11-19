export const usePublisher = () => {
    function fetchAll() {
        return $fetch<PublisherDTO[]>('/api/publishers/all');
    }

    function fetchById(id: number) {
        return $fetch<PublisherDTO>(`/api/publishers/${id}`);
    }

    /** Removes undefined, null and empty-string values so they are not sent as query parameters. */
    function cleanQuery(params: Record<string, any>) {
        return Object.fromEntries(
            Object.entries(params).filter(([, value]) => value !== undefined && value !== null && value !== '')
        );
    }

    /** Paginated view: page of publishers, optionally filtered by name via `query`. */
    function fetchPaginated(params: { query?: string } & Pageable = {}) {
        return $fetch<Page<PublisherDTO>>('/api/publishers', {
            query: cleanQuery(params)
        });
    }

    function create(payload: Partial<PublisherDTO>) {
        return $fetch<PublisherDTO>('/api/publishers', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<PublisherDTO>) {
        return $fetch<PublisherDTO>(`/api/publishers/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/publishers/${id}`, {
            method: 'DELETE'
        });
    }

    function getAll(){
        return useAsyncData('publisher-list', fetchAll);
    }
    function getById(id: number){
        return useAsyncData( `publisher-${id}`, () => fetchById(id) );
    }

    return {
        fetchAll,
        fetchById,
        fetchPaginated,
        create,
        update,
        deleteById,
        getAll,
        getById,
    };
};
