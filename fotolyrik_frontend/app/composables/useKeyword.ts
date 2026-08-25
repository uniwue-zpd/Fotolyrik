export const useKeyword = () => {
    function fetchKeywords() {
        return $fetch<KeywordDTO[]>('/api/keywords');
    }

    function fetchKeywordById(id: number) {
        return $fetch<KeywordDTO>(`/api/keywords/${id}`);
    }

    function createKeyword(payload: Partial<KeywordDTO>) {
        return $fetch<KeywordDTO>('/api/keywords', {
            method: 'POST',
            body: payload
        });
    }

    function updateKeyword(id: number, payload: Partial<KeywordDTO>) {
        return $fetch<KeywordDTO>(`/api/keywords/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteKeyword(id: number) {
        return $fetch<void>(`/api/keywords/${id}`, {
            method: 'DELETE'
        });
    }

    function useKeywordList(){
        return useAsyncData('keyword-list', fetchKeywords);
    }
    function useKeywordId(id: number){
        return useAsyncData( `keyword-${id}`, () => fetchKeywordById(id) );
    }

    return {
        fetchKeywords,
        fetchKeywordById,
        createKeyword,
        updateKeyword,
        deleteKeyword,
        useKeywordList,
        useKeywordId,
    };
};
