<script lang="ts" setup>
import { FilterMatchMode } from "@primevue/core";
import {useFiles} from "~/composables/useFiles";

const toast = useToast();
const fileApi = useFiles();

const imageErrors = ref<Record<string, boolean>>({});
const previewURLs = ref<Record<number, string>>({});

const filter = ref({ global: {value: null, matchMode: FilterMatchMode.CONTAINS}});
const options: Intl.DateTimeFormatOptions = {
  weekday: "long",
  year: "numeric",
  month: "2-digit",
  day: "2-digit"
};

const { data: files, status, refresh, error } = await useAsyncData(
    'files-list',
    () => fileApi.fetchFiles(),
    {default: ()=>[]}
);

watch(
    files,
    async (fileList) => {
      if (!fileList) return;
      await Promise.all(
          fileList.map(async (file: FileDTO) => {
            if (!previewURLs.value[file.id]) {
              const url = await fileApi.getImageContent(file.id);
              if (url) previewURLs.value[file.id] = url;
            }
          })
      );
    },
    { immediate: true, deep: true }
);

const onFileSelect = async (e: any) => {
  try {
    if (e.files && e.files.length > 0) {
      await fileApi.uploadFiles(e.files);
      await refresh();
      toast.add({severity: "success", summary: "Erfolg", detail: "Erfolgreich hinzugefügt", life: 3000});
    }
  } catch (error) {
    console.log(error);
    toast.add({severity: "error", summary: "Fehler", detail: "Ein Fehler ist aufgetreten", life: 3000});
  }
}

const timestampToDate = (timestamp: string) => {
  return new Date(timestamp).toLocaleDateString("de-DE", options);
};

const handleImageError = (path: string) => {
  imageErrors.value[path] = true;
};

const deleteFile = async (id:number) =>{
  await fileApi.removeFile(id);
  await refresh();
}
</script>

<template>
  <Card>
    <template #title>
      <h1 class="text-3xl font-bold text-primary outfit-headline">Dateien</h1>
    </template>
    <template #content>
      <DataTable
        :value="files"
        v-model:filters="filter"
        removableSort
        paginator
        :rows="10"
        :rowsPerPageOptions="[5, 10, 20, 50]"
        :loading="status==='pending'"
        :totalRecords="25"
      >
        <template #header>
          <div class="flex flex-row justify-between items-center">
            <FileUpload
                mode="basic"
                name="upload[]"
                accept=".png,.jpg,.jpeg,image/png,image/jpeg"
                chooseLabel="Dateien hinzufügen"
                invalidFileSizeMessage="Dateien sind zu groß. Maximal 20MB"
                :maxFileSize="20000000"
                :customUpload="true"
                :multiple="true"
                @select="onFileSelect"
                auto
                :pt="{ pcChooseButton: { root: { class: 'p-button-secondary p-button-outlined' } } }"
            />
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
                    v-if="previewURLs[slotProps.data.id] && !imageErrors[slotProps.data.path]"
                    @error="handleImageError(slotProps.data.path)"
                    :src="previewURLs[slotProps.data.id]"
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
                    :src="previewURLs[slotProps.data.id]"
                    alt="MaxPreview"
                    class="max-w-(--breakpoint-md) max-h-screen object-contain"
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
        <Column field="filename" header="Datei" headerClass="w-[60%]" :sortable="true"/>
        <Column field="created_date" header="Erstellt am" :sortable="true">
          <template #body="slotProps">
              {{ timestampToDate(slotProps.data.createdDate) }}
          </template>
        </Column>
        <Column class="w-24 text-end!">
          <template #body="{ data }">
            <Button icon="pi pi-trash" rounded-sm severity="danger" variant="text" @click="deleteFile(data.id)"/>
          </template>
        </Column>
      </DataTable>
    </template>
  </Card>
</template>



<style>
.p-image-rotate-left-button,
.p-image-rotate-right-button,
.p-image-zoom-in-button,
.p-image-zoom-out-button {
    display: none !important;
}
</style>
