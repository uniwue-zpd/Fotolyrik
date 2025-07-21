<template>
  <div class="flex w-full">
    <FileUpload 
      name="upload[]" 
      accept="image/*"
      :multiple="true"
      :customUpload="true"
      :maxFileSize="20000000"
      @uploader="onFileUpload"
    >
      <template #header="{ chooseCallback, uploadCallback, clearCallback, files }">
        <div class="flex flex-wrap justify-between items-center flex-1 gap-4">
            <div class="flex gap-2">
                <Button @click="chooseCallback()" label="Auswählen" icon="pi pi-images" rounded outlined severity="secondary"></Button>
                <Button @click="uploadCallback" label="Hochladen" icon="pi pi-cloud-upload" rounded outlined severity="success" :disabled="!files || files.length === 0"></Button>
                <Button @click="clearCallback()" icon="pi pi-times" rounded outlined severity="danger" :disabled="!files || files.length === 0"></Button>
            </div>

        </div>
    </template>
      <template #empty>
        <div class="flex items-center justify-center flex-col">
          <i class="pi pi-cloud-upload !border-2 !rounded-full !p-8 mt-2 !text-4xl !text-muted-color" />
          <p class="mt-6 mb-0">Dateien hierher ziehen und loslassen, um sie hochzuladen.</p>
          <p class="text-gray-400">Maximale Dateigröße: 20MB</p>
        </div>
      </template>
      
      <div v-if="fileStore.loadingUp" class="mt-3">
        <ProgressBar :value="fileStore.progressUp" />
        <small>Uploading: {{ fileStore.progressUp }}%</small>
      </div>
    </FileUpload>
  </div>
</template>

<script lang="ts" setup>
import { useFileStore } from '~/stores/FileStore';

const fileStore = useFileStore();

const onFileUpload = async (event: any) => {
  try {
    if (event.files && event.files.length > 0) {
      await fileStore.uploadFiles(event.files);
    }
  } catch (error) {
    console.log(error);
  }
}
</script>

<style>
.p-fileupload {
  width: 100%;
}
</style>