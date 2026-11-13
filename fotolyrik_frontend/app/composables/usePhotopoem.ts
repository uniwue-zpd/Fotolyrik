import type {Page, PhotopoemPageable} from "~/utils/types";

export const usePhotopoem = () => {
    function fetchAll() {
        return $fetch<PhotoPoemDTO[]>('/api/photopoems/all');
    }

    function fetchById(id: number) {
        return $fetch<PhotoPoemDTO>(`/api/photopoems/${id}`);
    }

    function fetchHighlight() {
        return $fetch<PhotoPoemDTO>('/api/photopoems/highlight');
    }

    /** Removes undefined, null and empty-string values so they are not sent as query parameters. */
    function cleanQuery(params: Record<string, any>) {
        return Object.fromEntries(
            Object.entries(params).filter(([, value]) => value !== undefined && value !== null && value !== '')
        );
    }

    /** Flat view: unpaginated list of lightweight previews. At least one filter is required. */
    function filter(params: Record<string, any>) {
        return $fetch<PhotoPoemPreviewDTO[]>('/api/photopoems', {
            query: { ...cleanQuery(params), view: 'flat' }
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

    /** Paginated view: page of full photopoem DTOs. All filters are optional. */
    function fetchPaginated(params: PhotopoemPageable) {
        return $fetch<Page<PhotoPoemDTO>>('/api/photopoems', {
            query: { ...cleanQuery(params), view: 'paginated' }
        });
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
