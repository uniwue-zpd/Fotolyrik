export const usePlace = () => {
    async function fetchPlaces() {
        return $fetch<PlaceDTO[]>('/api/places');
    }

    async function fetchPlaceById(id: number) {
        return $fetch<PlaceDTO>(`/api/places/${id}`);
    }

    async function getContributionPlaces(personId: number) {
        return $fetch<PlaceDTO[]>(`/api/persons/${personId}/stats/contribution_places`);
    }

    async function createPlace(payload: Partial<PlaceDTO>) {
        return $fetch<PlaceDTO>('/api/places', {
            method: 'POST',
            body: payload
        });
    }

    async function updatePlace(id: number, payload: Partial<PlaceDTO>) {
        return $fetch<PlaceDTO>(`/api/places/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deletePlace(id: number) {
        return $fetch<void>(`/api/places/${id}`, {
            method: 'DELETE'
        });
    }

    async function fetchPlaceMetrics(id: number) {
        return $fetch<PlaceMetricsDTO>(`/api/places/${id}/stats/metrics`);
    }

    return {
        fetchPlaces,
        fetchPlaceById,
        getContributionPlaces,
        createPlace,
        updatePlace,
        deletePlace,
        fetchPlaceMetrics
    };
};