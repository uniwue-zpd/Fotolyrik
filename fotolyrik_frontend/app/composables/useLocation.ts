export const useLocation = () => {
    function fetchLocations() {
        return $fetch<LocationDTO[]>('/api/locations');
    }

    function fetchLocationById(id: number) {
        return $fetch<LocationDTO>(`/api/locations/${id}`);
    }

    function filterLocations(params: Record<string, any>) {
        return $fetch<LocationDTO[]>('/api/locations/filter', {
            query: params
        });
    }

    function createLocation(payload: Partial<LocationDTO>) {
        return $fetch<LocationDTO>('/api/locations', {
            method: 'POST',
            body: payload
        });
    }

    function updateLocation(id: number, payload: Partial<LocationDTO>) {
        return $fetch<LocationDTO>(`/api/locations/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteLocation(id: number) {
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
