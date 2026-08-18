export const useFullText = () => {
    async function fetchFullTexts() {
        return $fetch<FullTextDTO[]>('/api/fulltexts');
    }

    async function fetchFullTextById(id: number) {
        return $fetch<FullTextDTO>(`/api/fulltexts/${id}`);
    }

    async function createFullText(payload: Partial<FullTextDTO>) {
        return $fetch<FullTextDTO>('/api/fulltexts', {
            method: 'POST',
            body: payload
        });
    }

    async function updateFullText(id: number, payload: Partial<FullTextDTO>) {
        return $fetch<FullTextDTO>(`/api/fulltexts/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deleteFullText(id: number) {
        return $fetch<void>(`/api/fulltexts/${id}`, {
            method: 'DELETE'
        });
    }

    async function searchFullTexts(query: string) {
        if (!query || query.trim() === '') return [];
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