export const usePubMedium = () => {
    async function fetchPubMedia() {
        return $fetch<PubMediumDTO[]>('/api/publication_media');
    }

    async function fetchPubMediumById(id: number) {
        return $fetch<PubMediumDTO>(`/api/publication_media/${id}`);
    }

    async function filterPubMedia(params: Record<string, any>) {
        return $fetch<PubMediumDTO[]>('/api/publication_media/filter', {
            query: params
        });
    }

    async function createPubMedium(payload: Partial<PubMediumDTO>) {
        return $fetch<PubMediumDTO>('/api/publication_media', {
            method: 'POST',
            body: payload
        });
    }

    async function updatePubMedium(id: number, payload: Partial<PubMediumDTO>) {
        return $fetch<PubMediumDTO>(`/api/publication_media/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deletePubMedium(id: number) {
        return $fetch<void>(`/api/publication_media/${id}`, {
            method: 'DELETE'
        });
    }

    async function fetchPubMediumMetrics(id: number) {
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