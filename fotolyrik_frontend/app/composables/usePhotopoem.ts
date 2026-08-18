export const usePhotopoem = () => {
    async function fetchPhotopoems() {
        return $fetch<PhotoPoemDTO[]>('/api/photopoems');
    }

    async function fetchPhotopoemById(id: number) {
        return $fetch<PhotoPoemDTO>(`/api/photopoems/${id}`);
    }

    async function fetchPhotopoemHighlight() {
        return $fetch<PhotoPoemDTO>('/api/photopoems/highlight');
    }

    async function filterPhotopoems(params: Record<string, any>) {
        return $fetch<PhotoPoemDTO[]>('/api/photopoems/filter', {
            query: params
        });
    }

    async function createPhotopoem(payload: Partial<PhotoPoemDTO>) {
        return $fetch<PhotoPoemDTO>('/api/photopoems', {
            method: 'POST',
            body: payload
        });
    }

    async function updatePhotopoem(id: number, payload: Partial<PhotoPoemDTO>) {
        return $fetch<PhotoPoemDTO>(`/api/photopoems/${id}`, {
            method: 'PUT',
            body: payload
        });
    }

    async function deletePhotopoem(id: number) {
        return $fetch<void>(`/api/photopoems/${id}`, {
            method: 'DELETE'
        });
    }

    return {
        fetchPhotopoems,
        fetchPhotopoemById,
        fetchPhotopoemHighlight,
        filterPhotopoems,
        createPhotopoem,
        updatePhotopoem,
        deletePhotopoem
    };
};
