export const usePubMedium = () => {
    function fetchAll() {
        return $fetch<PubMediumDTO[]>('/api/publication_media');
    }

    function fetchById(id: number) {
        return $fetch<PubMediumDTO>(`/api/publication_media/${id}`);
    }

    function fetchPubMediumNeighborsById(id: number) {
        return $fetch<IDSliceDTO>(`/api/publication_media/${id}/neighbor`);
    }

    function filter(params: Record<string, any>) {
        return $fetch<PubMediumDTO[]>('/api/publication_media/filter', {
            query: params
        });
    }

    function create(payload: Partial<PubMediumDTO>) {
        return $fetch<PubMediumDTO>('/api/publication_media', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<PubMediumDTO>) {
        return $fetch<PubMediumDTO>(`/api/publication_media/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/publication_media/${id}`, {
            method: 'DELETE'
        });
    }

    function fetchMetricsById(id: number) {
        return $fetch<PubMediumMetricsDTO>(`/api/publication_media/${id}/stats/metrics`);
    }

    function getAll(){
        return useAsyncData('pubMedium-list', fetchAll);
    }
    function getById(id: number){
        return useAsyncData( `pubMedium-${id}`, () => fetchById(id) );
    }
    function getAllFiltered(params: any){
        if (Object.keys(params).length !== 1) console.error('provide only one filter')
        const [key, value] = Object.entries(params)[0]!;
        return useAsyncData(`pubMedium-${key}-${value}`, ()=>filter(params));
    }
    function getNeighborsById(id: number) {
        return useAsyncData( `pubMedium-${id}-neighbor`, () => fetchPubMediumNeighborsById(id));
    }
    function getMetricsById(id:number){
        return useAsyncData(`pubMedium-metrics-${id}`, ()=> fetchMetricsById(id));
    }

    return {
        fetchAll,
        fetchById,
        filter,
        create,
        update,
        deleteById,
        fetchMetricsById,
        getAll,
        getById,
        getAllFiltered,
        getNeighborsById,
        getMetricsById,
    };
};
