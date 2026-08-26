export const usePlace = () => {
    function fetchPlaces() {
        return $fetch<PlaceDTO[]>('/api/places');
    }

    function fetchPlaceById(id: number) {
        return $fetch<PlaceDTO>(`/api/places/${id}`);
    }

    function getContributionPlaces(personId: number) {
        return $fetch<PlaceDTO[]>(`/api/persons/${personId}/stats/contribution_places`);
    }

    function createPlace(payload: Partial<PlaceDTO>) {
        return $fetch<PlaceDTO>('/api/places', {
            method: 'POST',
            body: payload
        });
    }

    function updatePlace(id: number, payload: Partial<PlaceDTO>) {
        return $fetch<PlaceDTO>(`/api/places/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deletePlace(id: number) {
        return $fetch<void>(`/api/places/${id}`, {
            method: 'DELETE'
        });
    }

    function fetchPlaceMetrics(id: number) {
        return $fetch<PlaceMetricsDTO>(`/api/places/${id}/stats/metrics`);
    }
    function usePlaceList(){
        return useAsyncData('place-list', fetchPlaces);
    }
    function usePlaceId(id: number){
        return useAsyncData( `place-${id}`, () => fetchPlaceById(id) );
    }
    function usePlaceMetricsId(id: number){
        return useAsyncData( `place-metrics-${id}`, () => fetchPlaceMetrics(id) );
    }

    return {
        fetchPlaces,
        fetchPlaceById,
        getContributionPlaces,
        createPlace,
        updatePlace,
        deletePlace,
        fetchPlaceMetrics,
        usePlaceList,
        usePlaceId,
        usePlaceMetricsId,
    };
};
