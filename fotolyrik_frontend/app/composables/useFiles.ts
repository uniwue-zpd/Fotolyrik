import type { FileDTO } from "~/utils/types"

export const useFiles = ()=> {

    function fetchAll(): Promise<FileDTO[]> {
        return $fetch<FileDTO[]>("/api/files/all");
    }

    function remove(id: number) {
        return $fetch(`/api/files/${id}`, {method: 'DELETE'});
    }

    function upload(fileList: FileList | File[]): Promise<FileDTO[]>{
        const formData = new FormData();
        Array.from(fileList).forEach((file) => {
            formData.append("file", file);
        });

        return $fetch<FileDTO[]>("/api/files", {
            method: "POST",
            body: formData
        });
    }

    function getPreview(path: string): string {
        if (!path) return '';
        const config = useRuntimeConfig();
        const filename = path.split(/[\\/]/).pop() || '';
        const baseURL = config.public.apiBase || '';
        return `${baseURL}/uploads/${encodeURIComponent(filename)}`;
    }

    async function getContent(id: number): Promise<string | null> {
        if (!import.meta.client) return null;
        try {
            const response = await $fetch(`/api/files/${id}/content`, {
                method: 'GET',
                responseType: 'blob'
            });

            return URL.createObjectURL(response as Blob);
        } catch (err) {
            console.error(`Failed to fetch image content for ID ${id}:`, err);
            return null;
        }
    }

    function getAll(){
        return useAsyncData('file-list', fetchAll);
    }

    return {
        fetchAll,
        remove,
        upload,
        getPreview,
        getContent,
        getAll
    }
}
