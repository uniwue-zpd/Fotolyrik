export const useFullText = () => {
    function fetchFullTexts() {
        return $fetch<FullTextDTO[]>('/api/fulltexts');
    }

    function fetchFullTextById(id: number) {
        return $fetch<FullTextDTO>(`/api/fulltexts/${id}`);
    }

    function createFullText(payload: Partial<FullTextDTO>) {
        return $fetch<FullTextDTO>('/api/fulltexts', {
            method: 'POST',
            body: payload
        });
    }

    function updateFullText(id: number, payload: Partial<FullTextDTO>) {
        return $fetch<FullTextDTO>(`/api/fulltexts/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteFullText(id: number) {
        return $fetch<void>(`/api/fulltexts/${id}`, {
            method: 'DELETE'
        });
    }

    function searchFullTexts(query: string) {
        if (!query || query.trim() === '') return Promise.resolve([]);
        return $fetch<FullTextSearchResult[]>(`/api/fulltexts/search?query=${encodeURIComponent(query)}`);
    }

    return {
        fetchFullTexts,
        fetchFullTextById,
        createFullText,
        updateFullText,
        deleteFullText,
        searchFullTexts
    };
};
