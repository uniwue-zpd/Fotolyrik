export const useLanguage = () => {
    async function fetchLanguages() {
        return $fetch<LanguageDTO[]>('/api/languages');
    }

    async function fetchLanguageById(id: number) {
        return $fetch<LanguageDTO>(`/api/languages/${id}`);
    }

    async function createLanguage(payload: Partial<LanguageDTO>) {
        return $fetch<LanguageDTO>('/api/languages', {
            method: 'POST',
            body: payload
        });
    }

    async function updateLanguage(id: number, payload: Partial<LanguageDTO>) {
        return $fetch<LanguageDTO>(`/api/languages/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deleteLanguage(id: number) {
        return $fetch<void>(`/api/languages/${id}`, {
            method: 'DELETE'
        });
    }

    return {
        fetchLanguages,
        fetchLanguageById,
        createLanguage,
        updateLanguage,
        deleteLanguage
    };
};