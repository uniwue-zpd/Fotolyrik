export const useKeyword = () => {
    async function fetchKeywords() {
        return $fetch<KeywordDTO[]>('/api/keywords');
    }

    async function fetchKeywordById(id: number) {
        return $fetch<KeywordDTO>(`/api/keywords/${id}`);
    }

    async function createKeyword(payload: Partial<KeywordDTO>) {
        return $fetch<KeywordDTO>('/api/keywords', {
            method: 'POST',
            body: payload
        });
    }

    async function updateKeyword(id: number, payload: Partial<KeywordDTO>) {
        return $fetch<KeywordDTO>(`/api/keywords/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deleteKeyword(id: number) {
        return $fetch<void>(`/api/keywords/${id}`, {
            method: 'DELETE'
        });
    }

    return {
        fetchKeywords,
        fetchKeywordById,
        createKeyword,
        updateKeyword,
        deleteKeyword
    };
};
