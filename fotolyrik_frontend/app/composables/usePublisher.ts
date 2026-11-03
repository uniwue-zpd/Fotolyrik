export const usePublisher = () => {
    function fetchAll() {
        return $fetch<PublisherDTO[]>('/api/publishers');
    }

    function fetchById(id: number) {
        return $fetch<PublisherDTO>(`/api/publishers/${id}`);
    }

    function searchPaginated(query: string, pageable: Pageable): Promise<Page<PublisherDTO>> {
        return $fetch<Page<PublisherDTO>>(`/api/publishers/search_paginated`, { query: { query, ...pageable } });
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
        searchPaginated,
        create,
        update,
        deleteById,
        getAll,
        getById,
    };
};
