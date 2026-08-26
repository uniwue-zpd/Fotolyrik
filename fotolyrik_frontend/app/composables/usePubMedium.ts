export const usePubMedium = () => {
    function fetchPubMedia() {
        return $fetch<PubMediumDTO[]>('/api/publication_media');
    }

    function fetchPubMediumById(id: number) {
        return $fetch<PubMediumDTO>(`/api/publication_media/${id}`);
    }

    function fetchPubMediumNeighborsById(id: number) {
        return $fetch<IDSliceDTO>(`/api/publication_media/${id}/neighbor`);
    }

    function filterPubMedia(params: Record<string, any>) {
        return $fetch<PubMediumDTO[]>('/api/publication_media/filter', {
            query: params
        });
    }

    function createPubMedium(payload: Partial<PubMediumDTO>) {
        return $fetch<PubMediumDTO>('/api/publication_media', {
            method: 'POST',
            body: payload
        });
    }

    function updatePubMedium(id: number, payload: Partial<PubMediumDTO>) {
        return $fetch<PubMediumDTO>(`/api/publication_media/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deletePubMedium(id: number) {
        return $fetch<void>(`/api/publication_media/${id}`, {
            method: 'DELETE'
        });
    }

    function fetchPubMediumMetrics(id: number) {
        return $fetch<PubMediumMetricsDTO>(`/api/publication_media/${id}/stats/metrics`);
    }

    function usePubMediumList(){
        return useAsyncData('pubMedium-list', fetchPubMedia);
    }
    function usePubMediumId(id: number){
        return useAsyncData( `pubMedium-${id}`, () => fetchPubMediumById(id) );
    }
    function useFilteredPubMedium(params: any){
        if (Object.keys(params).length !== 1) console.error('provide only one filter')
        const [key, value] = Object.entries(params)[0]!;
        return useAsyncData(`pubMedium-${key}-${value}`, ()=>filterPubMedia(params));
    }
    function usePubMediumNeighbors(id: number) {
        return useAsyncData( `pubMedium-${id}-neighbor`, () => fetchPubMediumNeighborsById(id));
    }
    function usePubMediumMetics(id:number){
        return useAsyncData(`pubMedium-metrics-${id}`, ()=> fetchPubMediumMetrics(id));
    }

    return {
        fetchPubMedia,
        fetchPubMediumById,
        filterPubMedia,
        createPubMedium,
        updatePubMedium,
        deletePubMedium,
        fetchPubMediumMetrics,
        usePubMediumList,
        usePubMediumId,
        useFilteredPubMedium,
        usePubMediumNeighbors,
        usePubMediumMetics,
    };
};
