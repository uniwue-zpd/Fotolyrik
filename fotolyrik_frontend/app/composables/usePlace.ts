export const usePlace = () => {
    function fetchAll() {
        return $fetch<PlaceDTO[]>('/api/places');
    }

    function fetchById(id: number) {
        return $fetch<PlaceDTO>(`/api/places/${id}`);
    }

    function getContributionPlacesById(personId: number) {
        return $fetch<PlaceDTO[]>(`/api/persons/${personId}/stats/contribution_places`);
    }

    function create(payload: Partial<PlaceDTO>) {
        return $fetch<PlaceDTO>('/api/places', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<PlaceDTO>) {
        return $fetch<PlaceDTO>(`/api/places/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/places/${id}`, {
            method: 'DELETE'
        });
    }

    function fetchMetricsById(id: number) {
        return $fetch<PlaceMetricsDTO>(`/api/places/${id}/stats/metrics`);
    }
    function getAll(){
        return useAsyncData('place-list', fetchAll);
    }
    function getById(id: number){
        return useAsyncData( `place-${id}`, () => fetchById(id) );
    }
    function getMetricsById(id: number){
        return useAsyncData( `place-metrics-${id}`, () => fetchMetricsById(id) );
    }

    return {
        fetchAll,
        fetchById,
        getContributionPlacesById,
        create,
        update,
        deleteById,
        fetchMetricsById,
        getAll,
        getById,
        getMetricsById,
    };
};
