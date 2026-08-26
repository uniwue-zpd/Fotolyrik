export const useLanguage = () => {
    function fetchAll() {
        return $fetch<LanguageDTO[]>('/api/languages');
    }

    function fetchById(id: number) {
        return $fetch<LanguageDTO>(`/api/languages/${id}`);
    }

    function create(payload: Partial<LanguageDTO>) {
        return $fetch<LanguageDTO>('/api/languages', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<LanguageDTO>) {
        return $fetch<LanguageDTO>(`/api/languages/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/languages/${id}`, {
            method: 'DELETE'
        });
    }
    function getAll(){
        return useAsyncData('language-list', fetchAll);
    }

    return {
        fetchAll,
        fetchById,
        create,
        update,
        deleteById,
        getAll
    };
};