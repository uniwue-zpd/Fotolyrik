import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Person } from "~/utils/types";

export const usePersonStore = defineStore('person', () => {
    // State
    const persons = ref<Person[]>([] as Person[]);
    const currentPerson = ref<Person | null>(null);

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
            persons.value = data.value as Person[];
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
                currentPerson.value = data.value as Person;
            }
        }
    }

        // Create new person
    async function createPerson(payload: Partial<Person>) {
        const { data, error } = await useFetch('/api/persons', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating person:', error.value);
            return;
        }
        const newPerson = data.value as Person;
        persons.value.push(newPerson);
        return newPerson;
    }

        // Update existing person
    async function updatePerson(payload: Partial<Person>, id: number) {
        const { data, error } = await useFetch(`/api/persons/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Error updating person:', error.value);
            return;
        }
        const updatedPerson = data.value as Person;
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
            return persons.value[currentIndex - 1] as Person;
        } else {
            return null;
        }
    }

        // Navigation right
    function nextPerson() {
        const currentIndex = persons.value.findIndex(p => p.id === currentPerson.value?.id);
        if (currentIndex !== -1 && currentIndex < persons.value.length - 1) {
            return persons.value[currentIndex + 1] as Person;
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
        fetchPersonById,
        createPerson,
        updatePerson,
        deletePerson,
        previousPerson,
        nextPerson,
        clearPerson
    }
});

