import { defineStore } from 'pinia';
import { ref } from 'vue';
import apiClient from "~/service/api";
import type { Person } from "~/utils/types";

export const usePersonStore = defineStore('person', () => {
    // State
    const persons = ref<Person[]>([] as Person[]);
    const currentPerson = ref<Person | null>(null);

    // Getters
    const isLoaded = computed(() => persons.value.length > 0);

    // Actions
        // Fetch all persons
    async function fetchAllPersons() {
        if (!isLoaded.value) {
            try {
                const response = await apiClient.get<Person[]>('/persons');
                persons.value = response.data;
            } catch (error) {
                console.log('Error fetching persons:', error);
            }
        }
    }
        // Fetch person by ID
    async function fetchPersonById(id: number) {
        if (!currentPerson.value || currentPerson.value.id !== id) {
            const cachedPerson = persons.value.find(p => p.id === id);
            if (cachedPerson) {
                currentPerson.value = cachedPerson;
            } else {
                try {
                    const response = await apiClient.get<Person>(`/persons/${id}`);
                    currentPerson.value = response.data;
                } catch (error) {
                    console.log('Error fetching person by ID:', error);
                }
            }
        }
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

        // Create new person
    async function createPerson(payload: Partial<Person>) {
        try {
            const response = await apiClient.post('/persons', payload);
            persons.value.push(response.data);
            return response.data;
        } catch (error) {
            console.log('Error creating person:', error);
            throw error;
        }
    }

        // Update existing person
    async function updatePerson(payload: Partial<Person>, id: number) {
        try {
            const response = await apiClient.put(`/persons/${id}`, payload);
            const index = persons.value.findIndex(p => p.id === id);
            if (index !== -1) {
                persons.value[index] = response.data;
            }
            if (currentPerson.value?.id === id) {
                currentPerson.value = response.data;
            }
            return response.data;
        } catch (error) {
            console.log('Error updating person:', error);
            throw error;
        }
    }

    return {
        persons,
        currentPerson,
        isLoaded,
        fetchAllPersons,
        fetchPersonById,
        previousPerson,
        nextPerson,
        clearPerson,
        createPerson,
        updatePerson
    }
});

