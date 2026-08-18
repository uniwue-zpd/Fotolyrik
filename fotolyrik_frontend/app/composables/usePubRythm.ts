export const usePubRhythm = () => {
    async function fetchPubRhythms() {
        return $fetch<PubRhythmDTO[]>('/api/publication_rhythms');
    }

    async function fetchPubRhythmById(id: number) {
        return $fetch<PubRhythmDTO>(`/api/publication_rhythms/${id}`);
    }

    async function createPubRhythm(payload: Partial<PubRhythmDTO>) {
        return $fetch<PubRhythmDTO>('/api/publication_rhythms', {
            method: 'POST',
            body: payload
        });
    }

    async function updatePubRhythm(id: number, payload: Partial<PubRhythmDTO>) {
        return $fetch<PubRhythmDTO>(`/api/publication_rhythms/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deletePubRhythm(id: number) {
        return $fetch<void>(`/api/publication_rhythms/${id}`, {
            method: 'DELETE'
        });
    }

    return {
        fetchPubRhythms,
        fetchPubRhythmById,
        createPubRhythm,
        updatePubRhythm,
        deletePubRhythm
    };
};