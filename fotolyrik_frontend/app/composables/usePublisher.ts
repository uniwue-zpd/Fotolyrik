export const usePublisher = () => {
    async function fetchPublishers() {
        return $fetch<PublisherDTO[]>('/api/publishers');
    }

    async function fetchPublisherById(id: number) {
        return $fetch<PublisherDTO>(`/api/publishers/${id}`);
    }

    async function createPublisher(payload: Partial<PublisherDTO>) {
        return $fetch<PublisherDTO>('/api/publishers', {
            method: 'POST',
            body: payload
        });
    }

    async function updatePublisher(id: number, payload: Partial<PublisherDTO>) {
        return $fetch<PublisherDTO>(`/api/publishers/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deletePublisher(id: number) {
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