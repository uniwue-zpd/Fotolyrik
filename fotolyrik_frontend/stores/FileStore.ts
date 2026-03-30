import { defineStore } from "pinia";
import { ref } from 'vue';
import apiClient from "~/service/api";
import type { File } from "~/utils/types"

export const useFileStore = defineStore("files", () => {
    const files = ref<File[]>([])

    const loadingDown = ref(false)
    const errorDown = ref<string | null>(null)

    const progressUp = ref(0)
    const loadingUp = ref(false)
    const errorUp = ref<string | null>(null)

    const fileContents = ref<Map<number, string>>(new Map());

    async function fetchFiles() {
        loadingDown.value = true;
        errorDown.value = null;

        const { data, error } = await useFetch("/api/files/all");
        if (error.value) {
            console.error("Unable to fetch files: ", error.value);
            errorDown.value = error.value.message || "Failed to fetch files";
            return;
        }
        files.value = data.value as File[];
        loadingDown.value = false;
    }

    async function refreshFilesData() {
        try {
            const data = await $fetch('/api/files/all');
            files.value = data as File[];
        } catch (err) {
            console.error('Unable to refetch the data', err);
        }
    }

    async function removeFile(file: File) {
        try {
            await $fetch(`/api/files/${ file.id }`, { method: 'DELETE' });
            files.value = files.value.filter(f => f.id !== file.id);
        } catch (err: any) {
            console.error('Failed to delete file: ', err);
            errorDown.value = err?.message || 'Failed to delete file';
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
            const response = await $fetch<File[]>('/api/files', {
                method: 'POST',
                body: formData
            });
            files.value.push(...response);
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
        files,
        loadingDown,
        errorDown,
        progressUp,
        loadingUp,
        errorUp,
        fetchFiles,
        refreshFilesData,
        removeFile,
        uploadFiles,
        getImagePreview,
        getImageContent
    }
})
