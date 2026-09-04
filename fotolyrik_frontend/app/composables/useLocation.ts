export const useLocation = () => {
    function fetchAll() {
        return $fetch<LocationDTO[]>('/api/locations');
    }

    function fetchById(id: number) {
        return $fetch<LocationDTO>(`/api/locations/${id}`);
    }

    function filter(params: Record<string, any>) {
        return $fetch<LocationDTO[]>('/api/locations/filter', {
            query: params
        });
    }

    function create(payload: Partial<LocationDTO>) {
        return $fetch<LocationDTO>('/api/locations', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<LocationDTO>) {
        return $fetch<LocationDTO>(`/api/locations/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/locations/${id}`, {
            method: 'DELETE'
        });
    }

    function getAll(){
        return useAsyncData('location-list', fetchAll);
    }
    function getById(id: number){
        return useAsyncData( `location-${id}`, () => fetchById(id) );
    }

    return {
        fetchAll,
        fetchById,
        filter,
        create,
        update,
        deleteById,
        getAll,
        getById,
    };
};
