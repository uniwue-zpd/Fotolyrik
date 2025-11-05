import type { Language } from "~/utils/types";

export const useLanguageStore = defineStore('language', () => {
    // State
    const languages = ref<Language[]>([] as Language[]);
    const currentLanguage = ref<Language|null>(null);

    // Getters
    const isLoaded = computed(() => languages.value.length > 0);

    // Actions
        // GET Fetch all languages
    async function fetchLanguages() {
        const { data, error } = await useFetch('/api/languages');
        if (error.value) {
            console.error(error.value);
            return;
        }
        languages.value = data.value as Language[];
    }

        // GET a language by its ID
    async function fetchLanguageById(id: number) {
        if (!currentLanguage.value || currentLanguage.value.id !== id) {
            const cachedLanguage = languages.value.find(k => k.id === id);
            if (cachedLanguage) {
                currentLanguage.value = cachedLanguage;
            } else {
                const { data, error } = await useFetch(`/api/languages/${id}`);
                if (error.value) {
                    console.error('Unable to fetch the language: ', error.value);
                    return;
                }
                currentLanguage.value = data.value as Language;
            }
        }
    }

        // POST Create new language
    async function createLanguage(payload: Partial<Language>) {
        const { data, error } = await useFetch('/api/languages', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Unable to create the language: ', error.value);
            return;
        }
        const newLanguage = data.value as Language;
        languages.value.push(newLanguage);
        return newLanguage;
    }

        // PUT Update existing language
    async function updateLanguage(payload: Partial<Language>, id: number) {
        const { data, error } = await useFetch(`/api/languages/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Unable to update the language :', error.value);
            return;
        }
        const updatedLanguage = data.value as Language;
        const index = languages.value.findIndex(k => k.id === id);
        if (index !== -1) languages.value[index] = updatedLanguage;
        if (currentLanguage.value?.id === id) currentLanguage.value = updatedLanguage;
        return updatedLanguage;
    }

        // DELETE language
    async function deleteLanguage(id: number) {
        const { error } = await useFetch(`/api/languages/${id}`, { method: 'DELETE' })
        if (error.value) {
            console.error('Unable to delete the language: ', error.value);
            return;
        }
        languages.value = languages.value.filter(k => k.id !== id)
        if (currentLanguage.value?.id === id) currentLanguage.value = null;
    }

        // Clear current language
    function clearCurrentLanguage() {
        currentLanguage.value = null;
    }

    return {
        languages,
        currentLanguage,
        isLoaded,
        fetchLanguages,
        fetchLanguageById,
        createLanguage,
        updateLanguage,
        deleteLanguage,
        clearCurrentLanguage
    }
})
