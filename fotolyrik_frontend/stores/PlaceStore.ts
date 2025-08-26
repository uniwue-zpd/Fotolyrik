import { defineStore } from 'pinia';
import { ref } from 'vue';
import apiClient from "~/service/api";
import type { Place } from "~/utils/types";

export const usePlaceStore = defineStore('place', () => {
    // State
    const places = ref<Place[]>([] as Place[]);
    const current_place = ref<Place | null>(null);

    // Getters
    const isLoaded = computed(() => places.value.length > 0);

    // Actions
        // Fetch all places
    async function fetchPlaces() {
        if (!isLoaded.value) {
            try {
                const response = await apiClient.get<Place[]>('/places');
                places.value = response.data;
            } catch (error) {
                console.log('Error fetching places:', error);
            }
        }
    }

        // Fetch place by ID
    async function fetchPlaceById(id: number) {
        if (!current_place.value || current_place.value.id !== id) {
            const cached_place = places.value.find(p => p.id === id);
            if (cached_place) {
                current_place.value = cached_place;
            } else {
                try {
                    const response = await apiClient.get<Place>(`/places/${id}`);
                    current_place.value = response.data;
                } catch (error) {
                    console.log('Error fetching place by ID:', error);
                }
            }
        }
    }

        // Create new place
    async function createPlace(payload: Partial<Place>) {
        try {
            const response = await apiClient.post('/places', payload);
            places.value.push(response.data);
            return response.data;
        } catch (error) {
            console.log('Error creating place:', error);
            throw error;
        }
    }

        // Update existing place
    async function updatePlace(payload: Partial<Place>, id: number) {
        try {
            const response = await apiClient.put(`/places/${id}`, payload);
            const index = places.value.findIndex(p => p.id === id);
            if (index !== -1) {
                places.value[index] = response.data;
            }
            if (current_place.value?.id === id) {
                current_place.value = response.data;
            }
            return response.data;
        } catch (error) {
            console.log('Error updating place:', error);
            throw error;
        }
    }

        // DELETE existing place
    async function deletePlace(id: number) {
        try {
            await apiClient.delete(`/places/${id}`);
            places.value = places.value.filter(p => p.id !== id);
            if (current_place.value?.id === id) {
                current_place.value = null;
            }
        } catch (error) {
            console.log('Error deleting place:', error);
            throw error;
        }
    }

        // Navigation left
    function previousPlace() {
        const current_index = places.value.findIndex(p => p.id === current_place.value?.id);
        if (current_index !== -1 && current_index) {
            return places.value[current_index - 1] as Place;
        } else {
            return null;
        }
    }

        // Navigation right
    function nextPlace() {
        const current_index = places.value.findIndex(p => p.id === current_place.value?.id);
        if (current_index !== -1 && current_index < places.value.length - 1) {
            return places.value[current_index + 1] as Place;
        } else {
            return null;
        }
    }

        // Clear current place
    function clearPlace() {
        current_place.value = null;
    }

    return {
        places,
        current_place,
        fetchPlaces,
        fetchPlaceById,
        createPlace,
        updatePlace,
        deletePlace,
        previousPlace,
        nextPlace,
        clearPlace
    }
})
