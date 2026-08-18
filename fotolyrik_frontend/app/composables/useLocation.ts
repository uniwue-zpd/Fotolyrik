export const useLocation = () => {
    async function fetchLocations() {
        return $fetch<LocationDTO[]>('/api/locations');
    }

    async function fetchLocationById(id: number) {
        return $fetch<LocationDTO>(`/api/locations/${id}`);
    }

    async function filterLocations(params: Record<string, any>) {
        return $fetch<LocationDTO[]>('/api/locations/filter', {
            query: params
        });
    }

    async function createLocation(payload: Partial<LocationDTO>) {
        return $fetch<LocationDTO>('/api/locations', {
            method: 'POST',
            body: payload
        });
    }

    async function updateLocation(id: number, payload: Partial<LocationDTO>) {
        return $fetch<LocationDTO>(`/api/locations/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deleteLocation(id: number) {
        return $fetch<void>(`/api/locations/${id}`, {
            method: 'DELETE'
        });
    }

    return {
        fetchLocations,
        fetchLocationById,
        filterLocations,
        createLocation,
        updateLocation,
        deleteLocation
    };
};