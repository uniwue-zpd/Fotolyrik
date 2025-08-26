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

    async function getFiles() {
        loadingDown.value = true;
        errorDown.value = null;

        try {
            const response = await apiClient.get<File[]>("/files/all");
            files.value = response.data;

        } catch (err: any) {
            errorDown.value = err.message || 'Failed to fetch files';
            console.error('Error fetching files:', err);
        } finally {
            loadingDown.value = false;
        }
    }

    async function removeFile(file: File) {
        try {
            apiClient.delete(`/files/${file.id}`);
            files.value = files.value.filter(f => f.id !== file.id);
        } catch (err: any) {
            console.error('Failed to delete file:', err);
            errorDown.value = err.message || 'Failed to delete file';
        }
    }

    async function uploadFiles(fileList: FileList) {
        progressUp.value = 0
        loadingUp.value = true
        errorUp.value = null

        const formData = new FormData();
        Array.from(fileList).forEach(file => {
            formData.append('file', file);
        });

        try {
            const response = await apiClient.post<File[]>('/files', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                onUploadProgress: (progressEvent) => {
                    if (progressEvent.lengthComputable) {
                        progressUp.value = Math.round((progressEvent.loaded * 100) / progressEvent.total!);
                    }
                }
            });
            files.value.push(...response.data);
        } catch (err: any) {
            errorUp.value = err.message || 'Failed to upload files';
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

    return {
        files,
        loadingDown,
        errorDown,
        progressUp,
        loadingUp,
        errorUp,

        getFiles,
        removeFile,
        uploadFiles,
        getImagePreview
    }
})
