<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold text-[#063D79] outfit-headline">Dateien</h1>
    </template>
    <template #content>
      <DataTable
        :value="fileStore.files"
        v-model:filters="filter"
        removableSort
        paginator
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20, 50]"
        :loading="fileStore.loadingDown"
        :totalRecords="25"
      >
        <template #header>
          <div class="flex flex-row justify-between items-center">
            <div class="p-2 border-[1px] border-solid rounded-md hover:shadow-md flex items-center cursor-pointer" @click="uploadVisible = true">
              <i class="pi pi-upload mr-2"/>
              <div class="text-[#063D79] roboto-plain">Dateien hinzufügen</div>
            </div>
            <IconField>
              <InputIcon>
                <i class="pi pi-search"/>
              </InputIcon>
              <InputText
                v-model="filter['global'].value"
                type="text"
                placeholder="Dateisuche"
              />
            </IconField>
          </div>
        </template>
        <Column header="Vorschau" headerClass="w-[75px]">
          <template #body="slotProps">
            <div class="relative w-[75px] h-[75px] rounded-full overflow-hidden object-center">
              <Image
                alt="Vorschau"
                loading="lazy"
                preview
                @contextmenu.prevent
                class="w-full h-full"
              >
                <template #previewicon>
                  <i class="pi pi-search"></i>
                </template>
                <template #image>
                  <img 
                    v-if="!imageErrors[slotProps.data.path]" 
                    @error="handleImageError(slotProps.data.path)" 
                    :src="fileStore.getImagePreview(slotProps.data.path)" 
                    alt="MiniPreview" 
                    class="w-full h-full object-cover"
                    @contextmenu.prevent
                  />
                  <div v-else class="flex items-center justify-center w-full h-full">
                    <i class="pi pi-image text-gray-400 text-xl"></i>
                  </div>
                </template>
                <template #original>
                  <img 
                    v-if="!imageErrors[slotProps.data.path]" 
                    @error="handleImageError(slotProps.data.path)" 
                    :src="fileStore.getImagePreview(slotProps.data.path)" 
                    alt="MaxPreview"
                    class="max-w-screen-md max-h-screen object-contain"
                    @contextmenu.prevent
                  />
                  <div v-else class="flex items-center justify-center w-full h-full">
                    <i class="pi pi-image text-gray-400 text-9xl"></i>
                  </div>
                </template>
              </Image>
            </div>
          </template>
        </Column>
        <Column field="filename" header="Datei" sortable headerClass="w-[60%]"></Column>
        <Column field="created_date" header="Erstellt am" sortable>
          <template #body="slotProps">
              {{ timestampToDate(slotProps.data.created_date) }}
          </template>
        </Column>
        <Column class="w-24 !text-end">
          <template #body="{ data }">
            <Button icon="pi pi-trash" rounded severity="danger" variant="text" @click="fileStore.removeFile(data)"/>
          </template>
        </Column>
      </DataTable>
    </template>
  </Card>
  <Dialog v-model:visible="uploadVisible" modal header="Dateien hinzufügen" :draggable="false" class="w-[40dvw] min-w-[30rem]">
    <FormsUploadForm/>
  </Dialog>
</template>

<script lang="ts" setup>
import { onMounted } from 'vue';
import { useFileStore } from '~/stores/FileStore';
import { FilterMatchMode } from "@primevue/core";

const fileStore = useFileStore();

const imageErrors = ref<Record<string, boolean>>({});
const uploadVisible = ref<boolean>(false);

const filter = ref({ global: {value: null, matchMode: FilterMatchMode.CONTAINS}});
const options: Intl.DateTimeFormatOptions = {
  weekday: "long",
  year: "numeric",
  month: "2-digit",
  day: "2-digit"
};

onMounted(() => {
  fileStore.getFiles();
});

const timestampToDate = (timestamp: string) => {
  return new Date(timestamp).toLocaleDateString("de-DE", options);
};

const handleImageError = (path: string) => {
  imageErrors.value[path] = true;
};
</script>

<style>
.p-image-rotate-left-button,
.p-image-rotate-right-button,
.p-image-zoom-in-button,
.p-image-zoom-out-button {
    display: none !important;
}
</style>