export const usePhotopoem = () => {
    function fetchPhotopoems() {
        return $fetch<PhotoPoemDTO[]>('/api/photopoems');
    }

    function fetchPhotopoemById(id: number) {
        return $fetch<PhotoPoemDTO>(`/api/photopoems/${id}`);
    }

    function fetchPhotopoemHighlight() {
        return $fetch<PhotoPoemDTO>('/api/photopoems/highlight');
    }

    function filterPhotopoems(params: Record<string, any>) {
        return $fetch<PhotoPoemDTO[]>('/api/photopoems/filter', {
            query: params
        });
    }

    function createPhotopoem(payload: Partial<PhotoPoemDTO>) {
        return $fetch<PhotoPoemDTO>('/api/photopoems', {
            method: 'POST',
            body: payload
        });
    }

    function updatePhotopoem(id: number, payload: Partial<PhotoPoemDTO>) {
        return $fetch<PhotoPoemDTO>(`/api/photopoems/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deletePhotopoem(id: number) {
        return $fetch<void>(`/api/photopoems/${id}`, {
            method: 'DELETE'
        });
    }
    function usePhotopoemList(){
        return useAsyncData('photopoem-list', fetchPhotopoems);
    }
    function usePhotopoemId(id: number){
        return useAsyncData( `photopoem-${id}`, () => fetchPhotopoemById(id) );
    }
    function usePhotopoemHightlight(){
        return useAsyncData('photopoem-highlight', fetchPhotopoemHighlight)
    }

    return {
        fetchPhotopoems,
        fetchPhotopoemById,
        fetchPhotopoemHighlight,
        filterPhotopoems,
        createPhotopoem,
        updatePhotopoem,
        deletePhotopoem,
        usePhotopoemList,
        usePhotopoemId,
        usePhotopoemHightlight,
    };
};
