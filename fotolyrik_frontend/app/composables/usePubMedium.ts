export const usePubMedium = () => {
    function fetchPubMedia() {
        return $fetch<PubMediumDTO[]>('/api/publication_media');
    }

    function fetchPubMediumById(id: number) {
        return $fetch<PubMediumDTO>(`/api/publication_media/${id}`);
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

    return {
        fetchPubMedia,
        fetchPubMediumById,
        filterPubMedia,
        createPubMedium,
        updatePubMedium,
        deletePubMedium,
        fetchPubMediumMetrics
    };
};
