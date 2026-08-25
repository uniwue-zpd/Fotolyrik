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
    function useFilteredPhotopoems(params: any){
        if (Object.keys(params).length !== 1) console.error('provide only one filter')
        const [key, value] = Object.entries(params)[0]!;
        return useAsyncData(`photopoem-${key}-${value}`, ()=>filterPhotopoems(params))
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
        useFilteredPhotopoems
    };
};
