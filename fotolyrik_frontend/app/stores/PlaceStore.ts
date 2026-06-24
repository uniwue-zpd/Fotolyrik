import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { PlaceDTO } from "~/utils/types";

export const usePlaceStore = defineStore('place', () => {
    // State
    const places = ref<PlaceDTO[]>([] as PlaceDTO[]);
    const current_place = ref<PlaceDTO | null>(null);

    // Getters
    const isLoaded = computed(() => places.value.length > 0);

    // Actions
        // GET Fetch all places
    async function fetchPlaces() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/places', {
                deep: true
            });
            if (error.value) {
                console.error('Error fetching places:', error.value);
                return;
            }
            places.value = data.value as PlaceDTO[];
        }
    }

        // GET Refetch all places
    async function refreshPlacesData() {
        try {
            const data = await $fetch('/api/places');
            places.value = data as PlaceDTO[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

        // GET Fetch place by ID
    async function fetchPlaceById(id: number) {
        if (!current_place.value || current_place.value.id !== id) {
            const cachedPlace = places.value.find(p => p.id === id);
            if (cachedPlace) {
                current_place.value = cachedPlace;
            } else {
                const { data, error } = await useFetch(`/api/places/${id}`, {
                    deep: true
                });
                if (error.value) {
                    console.error(`Error fetching place with id ${id}:`, error.value);
                    return;
                }
                current_place.value = data.value as PlaceDTO;
            }
        }
    }

    // GET contribution places
    async function getContributionPlaces(personId: number): Promise<PlaceDTO[]> {
        try {
            return await $fetch<PlaceDTO[]>(`/api/persons/${personId}/stats/contribution_places`);
        } catch (error) {
            console.error('Getting contribution places:', error);
            return [];
        }
    }
        // POST Create new place
    async function createPlace(payload: Partial<PlaceDTO>) {
        const { data, error } = await useFetch('/api/places', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating place:', error.value);
            return;
        }
        const newPlace = data.value as PlaceDTO;
        places.value.push(newPlace);
        return newPlace;
    }

        // PUT Update existing place
    async function updatePlace(payload: Partial<PlaceDTO>, id: number) {
        const { data, error } = await useFetch(`/api/places/${id}`, {
            method: 'PUT',
            body: payload
        });
        if (error.value) {
            console.error('Error updating place:', error.value);
            return;
        }
        const updatedPlace = data.value as PlaceDTO;
        const index = places.value.findIndex(p => p.id === id);
        if (index !== -1) places.value[index] = updatedPlace;
        if (current_place.value?.id === id) current_place.value = updatedPlace;
        return updatedPlace;
    }

        // DELETE existing place
    async function deletePlace(id: number) {
        const { error } = await useFetch(`/api/places/${id}`, { method: 'DELETE' })
        if (error.value) {
            console.error('Error deleting place:', error.value);
            return;
        }
        places.value = places.value.filter(p => p.id !== id);
        if (current_place.value?.id === id) current_place.value = null;
    }

        // Navigation left
    function previousPlace() {
        const current_index = places.value.findIndex(p => p.id === current_place.value?.id);
        if (current_index !== -1 && current_index) {
            return places.value[current_index - 1] as PlaceDTO;
        } else {
            return null;
        }
    }

        // Navigation right
    function nextPlace() {
        const current_index = places.value.findIndex(p => p.id === current_place.value?.id);
        if (current_index !== -1 && current_index < places.value.length - 1) {
            return places.value[current_index + 1] as PlaceDTO;
        } else {
            return null;
        }
    }

    async function fetchPlaceMetrics(id: number): Promise<PlaceMetricsDTO> {
        return await $fetch<PlaceMetricsDTO>(`/api/places/${id}/stats/metrics`);
    }
        // Clear current place
    function clearPlace() {
        current_place.value = null;
    }

    return {
        places,
        current_place,
        fetchPlaces,
        refreshPlacesData,
        fetchPlaceById,
        getContributionPlaces,
        createPlace,
        updatePlace,
        deletePlace,
        previousPlace,
        nextPlace,
        fetchPlaceMetrics,
        clearPlace
    }
})
