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

    function usePublisherList(){
        return useAsyncData('publisher-list', fetchPublishers);
    }
    function usePublisherId(id: number){
        return useAsyncData( `publisher-${id}`, () => fetchPublisherById(id) );
    }

    return {
        fetchPublishers,
        fetchPublisherById,
        createPublisher,
        updatePublisher,
        deletePublisher,
        usePublisherList,
        usePublisherId,
    };
};
