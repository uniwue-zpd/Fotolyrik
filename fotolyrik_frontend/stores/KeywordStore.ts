import { defineStore } from 'pinia';
import { ref } from 'vue';
import apiClient from '~/service/api';
import type { Keyword } from '~/utils/types';

export const useKeywordStore = defineStore('keyword', () => {
    // State
    const keywords = ref<Keyword[]>([] as Keyword[]);
    const currentKeyword = ref<Keyword | null>(null);

    // Getters
    const isLoaded = computed(() => keywords.value.length > 0);

    // Actions
        // Fetch all keywords
    async function fetchKeywords() {
        if (!isLoaded.value) {
            try {
                const response = await apiClient.get<Keyword[]>('/keywords');
                keywords.value = response.data;
            } catch (error) {
                console.log('Error fetching keywords:', error);
            }
        }
    }

        // Fetch keyword by ID
    async function fetchKeywordById(id: number) {
        if (!currentKeyword.value || currentKeyword.value.id !== id) {
            const cachedKeyword = keywords.value.find(k => k.id === id);
            if (cachedKeyword) {
                currentKeyword.value = cachedKeyword;
            } else {
                try {
                    const response = await apiClient.get<Keyword>(`/keywords/${id}`);
                    currentKeyword.value = response.data;
                } catch (error) {
                    console.log('Error fetching keyword by ID:', error);
                }
            }
        }
    }

        // Create new keyword
    async function createKeyword(payload: Partial<Keyword>) {
        try {
            const response = await apiClient.post('/keywords', payload);
            keywords.value.push(response.data);
            return response.data;
        } catch (error) {
            console.log('Error creating keyword:', error);
            throw error;
        }
    }

        // Update existing keyword
    async function updateKeyword(payload: Partial<Keyword>, id: number) {
        try {
            const response = await apiClient.put(`/keywords/${id}`, payload);
            const index = keywords.value.findIndex(k => k.id === id);
            if (index !== -1) {
                keywords.value[index] = response.data;
            }
            if (currentKeyword.value && currentKeyword.value.id === id) {
                currentKeyword.value = response.data;
            }
            return response.data;
        } catch (error) {
            console.log('Error updating keyword:', error);
            throw error;
        }
    }

        // Delete keyword
    async function deleteKeyword(id: number) {
        try {
            await apiClient.delete(`/keywords/${id}`);
            keywords.value = keywords.value.filter(k => k.id !== id);
            if (currentKeyword.value?.id === id) {
                currentKeyword.value = null;
            }
        } catch (error) {
            console.log('Error deleting keyword:', error);
            throw error;
        }
    }

        // Clear current keyword
    function clearCurrentKeyword() {
        currentKeyword.value = null;
    }

    return {
        keywords,
        currentKeyword,
        isLoaded,
        fetchKeywords,
        fetchKeywordById,
        createKeyword,
        updateKeyword,
        deleteKeyword,
        clearCurrentKeyword
    }
})
