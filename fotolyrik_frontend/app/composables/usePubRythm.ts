export const usePubRhythm = () => {
    function fetchAll() {
        return $fetch<PubRhythmDTO[]>('/api/publication_rhythms');
    }

    function fetchById(id: number) {
        return $fetch<PubRhythmDTO>(`/api/publication_rhythms/${id}`);
    }

    function create(payload: Partial<PubRhythmDTO>) {
        return $fetch<PubRhythmDTO>('/api/publication_rhythms', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<PubRhythmDTO>) {
        return $fetch<PubRhythmDTO>(`/api/publication_rhythms/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/publication_rhythms/${id}`, {
            method: 'DELETE'
        });
    }

    function getAll(){
        return useAsyncData('pubRhythm-list', fetchAll);
    }

    return {
        fetchAll,
        fetchById,
        create,
        update,
        deleteById,
        getAll,
    };
};
