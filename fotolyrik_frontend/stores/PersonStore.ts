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

    function clearPerson() {
        currentPerson.value = null;
    }

    return {
        persons,
        currentPerson,
        isLoaded,
        fetchAllPersons,
        fetchPersonById,
        clearPerson
    }
});

