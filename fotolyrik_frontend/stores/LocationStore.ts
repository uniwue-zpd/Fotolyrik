import { defineStore } from "pinia";
import { ref, computed } from "vue";

export const useLocationStore = defineStore('location', () => {
    // State
    const locations = ref<LocationDTO[]>([] as LocationDTO[]);
    const current_location = ref<LocationDTO | null>(null);

    // Getters
    const isLoaded = computed(() => locations.value.length > 0);

    // Actions

    // GET all locations
    async function fetchLocations() {
        if (!isLoaded.value) {
            const { data, error } = await useFetch('/api/locations');
            if (error.value) {
                console.error('Error fetching locations:', error.value);
                return;
            }
            locations.value = data.value as LocationDTO[];
        }
    }

    // GET refetch locations
    async function refreshLocationData() {
        try {
            const data = await $fetch('/api/locations');
            locations.value = data as LocationDTO[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

    // GET location by ID
    async function fetchLocationById(id: number) {
        if (!current_location.value || current_location.value.id !== id) {
            const cachedLocation = locations.value.find(l => l.id === id);
            if (cachedLocation) {
                current_location.value = cachedLocation;
            } else {
                const { data, error } = await useFetch(`/api/locations/${id}`);
                if (error.value) {
                    console.error(`Error fetching location with id ${id}:`, error.value);
                    return;
                }
                current_location.value = data.value as LocationDTO;
            }
        }
    }

    // GET locations by filter params
    async function filterLocations(params: Record<string, any>): Promise<LocationDTO[]> {
        try {
            const data = await $fetch('/api/locations/filter', { query: params });
            return data as LocationDTO[];
        } catch (err) {
            console.error('Error fetching locations by params:', err);
            return [];
        }
    }

    // POST Create new location
    async function createLocation(payload: Partial<LocationDTO>) {
        const { data, error } = await useFetch('/api/locations', {
            method: 'POST',
            body: payload
        });
        if (error.value) {
            console.error('Error creating location:', error.value);
            return;
        }
        const newLocation = data.value as LocationDTO;
        locations.value.push(newLocation);
        return newLocation;
    }

    // PUT Update existing location
    async function updateLocation(payload: Partial<LocationDTO>, id: number) {
        try {
            const updatedLocation = await $fetch<LocationDTO>(`/api/locations/${id}`, {
                method: 'PUT',
                body: payload
            });
            const index = locations.value.findIndex(l => l.id === id);
            if (index !== -1) locations.value[index] = updatedLocation;
            if (current_location.value?.id === id) current_location.value = updatedLocation;
            return updatedLocation;
        } catch (err) {
            console.error('Error updating location:', err);
            return;
        }
    }

    // DELETE existing location
    async function deleteLocation(id: number) {
        const { error } = await useFetch(`/api/locations/${id}`, { method: 'DELETE' });
        if (error.value) {
            console.error('Error deleting location:', error.value);
            return;
        }
        locations.value = locations.value.filter(l => l.id !== id);
        if (current_location.value?.id === id) current_location.value = null;
    }

    // Navigation left
    function previousLocation() {
        const currentIndex = locations.value.findIndex(l => l.id === current_location.value?.id);
        if (currentIndex !== -1 && currentIndex > 0) {
            return locations.value[currentIndex - 1];
        } else {
            return null;
        }
    }

    // Navigation right
    function nextLocation() {
        const currentIndex = locations.value.findIndex(l => l.id === current_location.value?.id);
        if (currentIndex !== -1 && currentIndex < locations.value.length - 1) {
            return locations.value[currentIndex + 1];
        } else {
            return null;
        }
    }

    // Clear current location
    function clearLocation() {
        current_location.value = null;
    }

    return {
        locations,
        current_location,
        isLoaded,
        fetchLocations,
        refreshLocationData,
        fetchLocationById,
        filterLocations,
        createLocation,
        updateLocation,
        deleteLocation,
        previousLocation,
        nextLocation,
        clearLocation,
    }
});