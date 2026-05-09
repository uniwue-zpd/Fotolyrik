import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { PersonFullDTO } from "~/utils/types";

export const usePersonStore = defineStore('person', () => {
    // State
    const persons = ref<PersonFullDTO[]>([] as PersonFullDTO[]);
    const currentPerson = ref<PersonFullDTO | null>(null);

    // Getters
    const isLoaded = computed(() => persons.value.length > 0);

    // Actions
        // Fetch all persons
    async function fetchPersons() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/persons');
            if (error.value) {
                console.error('Error fetching persons:', error.value);
                return;
            }
            persons.value = data.value as PersonFullDTO[];
        }
    }

        // GET refetch persons
    async function refreshPersonsData() {
        try {
            const data = await $fetch('/api/persons');
            persons.value = data as PersonFullDTO[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

        // GET refetch persons by ID
    async function refreshPersonsDataById(id: number) {
        try {
            const data = await $fetch(`/api/persons/${id}`);
            const index = persons.value.findIndex(p => p.id === id);
            persons.value[index] = data as PersonFullDTO;
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
                const { data, error } = await useFetch(`/api/persons/${id}`);
                if (error.value) {
                    console.error(`Error fetching person with id ${id}:`, error.value);
                    return;
                }
                currentPerson.value = data.value as PersonFullDTO;
            }
        }
    }

        // Create new person
    async function createPerson(payload: Partial<PersonFullDTO>) {
        const { data, error } = await useFetch('/api/persons', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating person:', error.value);
            return;
        }
        const newPerson = data.value as PersonFullDTO;
        persons.value.push(newPerson);
        return newPerson;
    }

        // Update existing person
    async function updatePerson(payload: Partial<PersonFullDTO>, id: number) {
        const { data, error } = await useFetch(`/api/persons/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Error updating person:', error.value);
            return;
        }
        const updatedPerson = data.value as PersonFullDTO;
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
            return persons.value[currentIndex - 1] as PersonFullDTO;
        } else {
            return null;
        }
    }

        // Navigation right
    function nextPerson() {
        const currentIndex = persons.value.findIndex(p => p.id === currentPerson.value?.id);
        if (currentIndex !== -1 && currentIndex < persons.value.length - 1) {
            return persons.value[currentIndex + 1] as PersonFullDTO;
        } else {
            return null;
        }
    }

        // Clear current person
    function clearPerson() {
        currentPerson.value = null;
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
        clearPerson
    }
});

