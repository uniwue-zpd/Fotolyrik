export const useKeyword = () => {
    function fetchAll() {
        return $fetch<KeywordDTO[]>('/api/keywords');
    }

    function fetchById(id: number) {
        return $fetch<KeywordDTO>(`/api/keywords/${id}`);
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

    function getAll(){
        return useAsyncData('keyword-list', fetchAll);
    }
    function getById(id: number){
        return useAsyncData( `keyword-${id}`, () => fetchById(id) );
    }

    return {
        fetchAll,
        fetchById,
        create,
        update,
        deleteById,
        getAll,
        getById,
    };
};
