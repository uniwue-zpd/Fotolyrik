import type { FileDTO } from "~/utils/types"

export const useFiles = ()=> {

    async function fetchFiles(): Promise<FileDTO[]> {
        return await $fetch<FileDTO[]>("/api/files/all");
    }

    async function removeFile(id: number):Promise<void> {
        return $fetch(`/api/files/${id}`, {method: 'DELETE'});
    }

    async function uploadFiles(fileList: FileList | File[]): Promise<FileDTO[]>{
        const formData = new FormData();
        Array.from(fileList).forEach((file) => {
            formData.append("file", file);
        });

        return $fetch<FileDTO[]>("/api/files", {
            method: "POST",
            body: formData
        });
    }

    function getImagePreview(path: string): string {
        if (!path) return '';
        const config = useRuntimeConfig();
        const filename = path.split(/[\\/]/).pop() || '';
        const baseURL = config.public.apiBase || '';
        return `${baseURL}/uploads/${encodeURIComponent(filename)}`;
    }

    async function getImageContent(id: number): Promise<string | null> {
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

    return {
        fetchFiles,
        removeFile,
        uploadFiles,
        getImagePreview,
        getImageContent
    }
}
