export const useLanguage = () => {
    function fetchLanguages() {
        return $fetch<LanguageDTO[]>('/api/languages');
    }

    function fetchLanguageById(id: number) {
        return $fetch<LanguageDTO>(`/api/languages/${id}`);
    }

    function createLanguage(payload: Partial<LanguageDTO>) {
        return $fetch<LanguageDTO>('/api/languages', {
            method: 'POST',
            body: payload
        });
    }

    function updateLanguage(id: number, payload: Partial<LanguageDTO>) {
        return $fetch<LanguageDTO>(`/api/languages/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteLanguage(id: number) {
        return $fetch<void>(`/api/languages/${id}`, {
            method: 'DELETE'
        });
    }
    function useLanguageList(){
        return useAsyncData('language-list', fetchLanguages);
    }

    return {
        fetchLanguages,
        fetchLanguageById,
        createLanguage,
        updateLanguage,
        deleteLanguage,
        useLanguageList
    };
};