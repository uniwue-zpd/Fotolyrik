export const usePerson = () => {
    async function fetchPersons() {
        return $fetch<PersonDTO[]>('/api/persons');
    }

    async function fetchPersonById(id: number) {
        return $fetch<PersonDTO>(`/api/persons/${id}`);
    }

    async function createPerson(payload: Partial<PersonDTO>) {
        return $fetch<PersonDTO>('/api/persons', {
            method: 'POST',
            body: payload
        });
    }

    async function updatePerson(id: number, payload: Partial<PersonDTO>) {
        return $fetch<PersonDTO>(`/api/persons/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deletePerson(id: number) {
        return $fetch<void>(`/api/persons/${id}`, {
            method: 'DELETE'
        });
    }

    async function fetchAuthorThemes(id: number, limit?: number) {
        return $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats/themes`, {
            query: { limit }
        });
    }

    async function fetchAuthorImageMotifs(id: number, limit?: number) {
        return $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats/image-motifs`, {
            query: { limit }
        });
    }

    async function fetchPersonMetrics(id: number) {
        return $fetch<PersonMetricsDTO>(`/api/persons/${id}/stats/metrics`);
    }

    async function searchPeople(query: string) {
        return $fetch<PersonPreviewDTO[]>('/api/persons/search', {
            query: { query }
        });
    }

    return {
        fetchPersons,
        fetchPersonById,
        createPerson,
        updatePerson,
        deletePerson,
        fetchAuthorThemes,
        fetchAuthorImageMotifs,
        fetchPersonMetrics,
        searchPeople
    };
};