export const usePublisher = () => {
    function fetchPublishers() {
        return $fetch<PublisherDTO[]>('/api/publishers');
    }

    function fetchPublisherById(id: number) {
        return $fetch<PublisherDTO>(`/api/publishers/${id}`);
    }

    function createPublisher(payload: Partial<PublisherDTO>) {
        return $fetch<PublisherDTO>('/api/publishers', {
            method: 'POST',
            body: payload
        });
    }

    function updatePublisher(id: number, payload: Partial<PublisherDTO>) {
        return $fetch<PublisherDTO>(`/api/publishers/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deletePublisher(id: number) {
        return $fetch<void>(`/api/publishers/${id}`, {
            method: 'DELETE'
        });
    }

    return {
        fetchPublishers,
        fetchPublisherById,
        createPublisher,
        updatePublisher,
        deletePublisher
    };
};
