export const usePubRhythm = () => {
    function fetchPubRhythms() {
        return $fetch<PubRhythmDTO[]>('/api/publication_rhythms');
    }

    function fetchPubRhythmById(id: number) {
        return $fetch<PubRhythmDTO>(`/api/publication_rhythms/${id}`);
    }

    function createPubRhythm(payload: Partial<PubRhythmDTO>) {
        return $fetch<PubRhythmDTO>('/api/publication_rhythms', {
            method: 'POST',
            body: payload
        });
    }

    function updatePubRhythm(id: number, payload: Partial<PubRhythmDTO>) {
        return $fetch<PubRhythmDTO>(`/api/publication_rhythms/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deletePubRhythm(id: number) {
        return $fetch<void>(`/api/publication_rhythms/${id}`, {
            method: 'DELETE'
        });
    }

    function usePubRhythmList(){
        return useAsyncData('pubRhythm-list', fetchPubRhythms);
    }

    return {
        fetchPubRhythms,
        fetchPubRhythmById,
        createPubRhythm,
        updatePubRhythm,
        deletePubRhythm,
        usePubRhythmList,
    };
};
