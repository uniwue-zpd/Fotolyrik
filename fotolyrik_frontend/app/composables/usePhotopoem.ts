import type {Page, PhotopoemPageable} from "~/utils/types";

export const usePhotopoem = () => {
    function fetchAll() {
        return $fetch<PhotoPoemDTO[]>('/api/photopoems');
    }

    function fetchById(id: number) {
        return $fetch<PhotoPoemDTO>(`/api/photopoems/${id}`);
    }

    function fetchHighlight() {
        return $fetch<PhotoPoemDTO>('/api/photopoems/highlight');
    }

    function filter(params: Record<string, any>) {
        return $fetch<PhotoPoemDTO[]>('/api/photopoems/filter', {
            query: params
        });
    }

    function create(payload: Partial<PhotoPoemDTO>) {
        return $fetch<PhotoPoemDTO>('/api/photopoems', {
            method: 'POST',
            body: payload
        });
    }

    function update(id: number, payload: Partial<PhotoPoemDTO>) {
        return $fetch<PhotoPoemDTO>(`/api/photopoems/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    function deleteById(id: number) {
        return $fetch<void>(`/api/photopoems/${id}`, {
            method: 'DELETE'
        });
    }

    function fetchPaginated(params: PhotopoemPageable) {
        return $fetch<Page<PhotoPoemDTO>>('/api/photopoems', { query: params });
    }

    function getAll(){
        return useAsyncData('photopoem-list', fetchAll);
    }

    function getById(id: number){
        return useAsyncData( `photopoem-${id}`, () => fetchById(id) );
    }

    function getHighlight(){
        return useAsyncData('photopoem-highlight', fetchHighlight)
    }

    function getAllFiltered(params: any){
        if (Object.keys(params).length !== 1) console.error('provide only one filter')
        const [key, value] = Object.entries(params)[0]!;
        return useAsyncData(`photopoem-${key}-${value}`, ()=>filter(params))
    }

    return {
        fetchAll,
        fetchById,
        fetchHighlight,
        filter,
        create,
        update,
        deleteById,
        fetchPaginated,
        getAll,
        getById,
        getHighlight,
        getAllFiltered
    };
};
