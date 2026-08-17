import { ref } from 'vue';
import apiClient from "~/service/api";
import type { FileDTO } from "~/utils/types";

const fileContents = ref<Map<number, string>>(new Map());

const progressUp = ref(0);
const loadingUp = ref(false);
const errorUp = ref<string | null>(null);

export function useFiles() {
    const { data: files, pending: loadingDown, error: errorDown, refresh: refreshFilesData } = useFetch<FileDTO[]>("/api/files/all", {
        deep: true
    });

    async function removeFile(file: FileDTO) {
        try {
            await $fetch(`/api/files/${file.id}`, { method: 'DELETE' });
            if (files.value) {
                files.value = files.value.filter(f => f.id !== file.id);
            }
        } catch (err: any) {
            console.error('Failed to delete file: ', err);
        }
    }

    async function uploadFiles(fileList: FileList) {
        progressUp.value = 0;
        loadingUp.value = true;
        errorUp.value = null;
        const formData = new FormData();
        Array.from(fileList).forEach(file => {
            formData.append('file', file);
        });
        try {
            const response = await $fetch<FileDTO[]>('/api/files', {
                method: 'POST',
                body: formData
            });

            if (files.value) {
                files.value.push(...response);
            }
            progressUp.value = 100;
        } catch (err: any) {
            errorUp.value = err?.message || 'Failed to upload files';
            console.error('Error uploading files:', err);
        } finally {
            loadingUp.value = false;
        }
    }

    function getImagePreview(path: string) {
        if (!path) return '';
        const filename = path.split(/[\\/]/).pop() || '';
        return `${apiClient.defaults.baseURL || ''}/uploads/${encodeURIComponent(filename)}`;
    }

    async function getImageContent(id: number): Promise<string | null> {
        if (fileContents.value.has(id)) return fileContents.value.get(id)!;
        try {
            const response = await $fetch(`/api/files/${id}/content`, {
                method: 'GET',
                responseType: 'blob'
            });
            const url = URL.createObjectURL(response as Blob);
            fileContents.value.set(id, url);
            return url;
        } catch (err) {
            console.error('Failed to fetch image content: ', err);
            return null;
        }
    }

    return {
        files: files as Ref<FileDTO[]>,
        loadingDown,
        errorDown,
        progressUp,
        loadingUp,
        errorUp,
        fetchFiles: refreshFilesData,
        refreshFilesData,
        removeFile,
        uploadFiles,
        getImagePreview,
        getImageContent
    };
}