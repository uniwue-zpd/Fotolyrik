export const useFullText = () => {
    function fetchAll() {
        return $fetch<FullTextDTO[]>('/api/fulltexts');
    }

    function fetchById(id: number) {
        return $fetch<FullTextDTO>(`/api/fulltexts/${id}`);
    }

    function create(payload: Partial<FullTextDTO>) {
        return $fetch<FullTextDTO>('/api/fulltexts', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<FullTextDTO>) {
        return $fetch<FullTextDTO>(`/api/fulltexts/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/fulltexts/${id}`, {
            method: 'DELETE'
        });
    }

    function searchFullTexts(query: string) {
        if (!query || query.trim() === '') return Promise.resolve([]);
        return $fetch<FullTextSearchResult[]>(`/api/fulltexts/search?query=${encodeURIComponent(query)}`);
    }

    return {
        fetchAll,
        fetchById,
        create,
        update,
        deleteById,
        searchFullTexts
    };
};
