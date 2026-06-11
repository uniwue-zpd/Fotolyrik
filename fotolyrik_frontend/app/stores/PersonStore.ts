import {defineStore} from 'pinia';
import {ref} from 'vue';
import type {KeywordCountDTO, PersonDTO} from "~/utils/types";

export const usePersonStore = defineStore('person', () => {
    // State
    const persons = ref<PersonDTO[]>([] as PersonDTO[]);
    const currentPerson = ref<PersonDTO | null>(null);

    // Getters
    const isLoaded = computed(() => persons.value.length > 0);

    // Actions
        // Fetch all persons
    async function fetchPersons() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/persons', {
                deep: true
            });
            if (error.value) {
                console.error('Error fetching persons:', error.value);
                return;
            }
            persons.value = data.value as PersonDTO[];
        }
    }

        // GET refetch persons
    async function refreshPersonsData() {
        try {
            const data = await $fetch('/api/persons');
            persons.value = data as PersonDTO[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

        // GET refetch persons by ID
    async function refreshPersonsDataById(id: number) {
        try {
            const data = await $fetch(`/api/persons/${id}`);
            const index = persons.value.findIndex(p => p.id === id);
            persons.value[index] = data as PersonDTO;
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }
        // Fetch person by ID
    async function fetchPersonById(id: number) {
        if (!currentPerson.value || currentPerson.value.id !== id) {
            const cachedPerson = persons.value.find(p => p.id === id);
            if (cachedPerson) {
                currentPerson.value = cachedPerson;
            } else {
                const { data, error } = await useFetch(`/api/persons/${id}`, {
                    deep: true
                });
                if (error.value) {
                    console.error(`Error fetching person with id ${id}:`, error.value);
                    return;
                }
                currentPerson.value = data.value as PersonDTO;
            }
        }
    }

        // Create new person
    async function createPerson(payload: Partial<PersonDTO>) {
        const { data, error } = await useFetch('/api/persons', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating person:', error.value);
            return;
        }
        const newPerson = data.value as PersonDTO;
        persons.value.push(newPerson);
        return newPerson;
    }

        // Update existing person
    async function updatePerson(payload: Partial<PersonDTO>, id: number) {
        const { data, error } = await useFetch(`/api/persons/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Error updating person:', error.value);
            return;
        }
        const updatedPerson = data.value as PersonDTO;
        const index = persons.value.findIndex(p => p.id === id);
        if (index !== -1) persons.value[index] = updatedPerson;
        if (currentPerson.value?.id === id) currentPerson.value = updatedPerson;
        return updatedPerson;
    }

        // Delete existing person
    async function deletePerson(id: number) {
        const { error } = await useFetch(`/api/persons/${id}`, { method: 'DELETE' });
        if (error.value) {
            console.error('Error deleting person:', error.value);
            return;
        }
        persons.value = persons.value.filter(p => p.id !== id);
        if (currentPerson.value?.id === id) currentPerson.value = null;
    }

        // Navigation left
    function previousPerson() {
        const currentIndex = persons.value.findIndex(p => p.id === currentPerson.value?.id);
        if (currentIndex !== -1 && currentIndex) {
            return persons.value[currentIndex - 1] as PersonDTO;
        } else {
            return null;
        }
    }

        // Navigation right
    function nextPerson() {
        const currentIndex = persons.value.findIndex(p => p.id === currentPerson.value?.id);
        if (currentIndex !== -1 && currentIndex < persons.value.length - 1) {
            return persons.value[currentIndex + 1] as PersonDTO;
        } else {
            return null;
        }
    }

    async function fetchAuthorThemes(id: number, limit?: number): Promise<KeywordCountDTO[]> {
        return await $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats/themes`, {query: {limit}});
    }

    async function fetchAuthorImageMotifs(id: number, limit?: number): Promise<KeywordCountDTO[]> {
        return await $fetch<KeywordCountDTO[]>(`/api/persons/${id}/stats/image-motifs`, {query: {limit}});
    }

        // Clear current person
    function clearPerson() {
        currentPerson.value = null;
    }

    async function searchPeople(query: string): Promise<PersonPreviewDTO[]> {
        return await $fetch<PersonPreviewDTO[]>(`/api/persons/search`, { query: { query } });
    }

    return {
        persons,
        currentPerson,
        isLoaded,
        fetchPersons,
        refreshPersonsData,
        refreshPersonsDataById,
        fetchPersonById,
        createPerson,
        updatePerson,
        deletePerson,
        previousPerson,
        nextPerson,
        clearPerson,
        fetchAuthorThemes,
        fetchAuthorImageMotifs,
        searchPeople
    }
});

