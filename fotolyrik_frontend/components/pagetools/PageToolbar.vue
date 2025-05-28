<script setup lang="ts">
import { ref } from 'vue';

const props = defineProps<{
  page_url: string;
  api_url: string;
}>();

const toast = useToast();
const items = ref([
  {
    label: 'Edit',
    icon: 'pi pi-file-edit',
    command: () => {
      toast.add({ severity: 'info', summary: 'Änderung', detail: 'Der Eintrag wurde erfolgreich geändert', life: 3000 });
    }
  },
  {
    label: 'Delete',
    icon: 'pi pi-trash',
    command: () => {
      toast.add({ severity: 'info', summary: 'Löschvorgang', detail: 'Der Eintrag wurde erfolgreich gelöscht', life: 3000 });
    }
  },
  {
    label: 'Share',
    icon: 'pi pi-share-alt',
    command: () => {
      navigator.clipboard.writeText(props.page_url);
      toast.add({ severity: 'info', summary: 'Hinweis', detail: 'Link erfolgreich kopiert!', life: 3000 });
    }
  },
  {
    label: 'API',
    icon: 'pi pi-code',
    command: () => {
      window.open(props.api_url, '_blank');
    }
  }
]);
</script>

<template>
  <SpeedDial
      :model="items"
      direction="left"
      class="pagetoolbar"
  >
    <template #button="{ toggleCallback }">
      <Button icon="pi pi-bars" @click="toggleCallback" style="color: black"/>
    </template>
    <template #item="{ item, toggleCallback }">
      <div class="px-2 cursor-pointer" @click="toggleCallback">
        <span :class="item.icon"/>
      </div>
    </template>
  </SpeedDial>
</template>

<style>
.pagetoolbar > .p-button {
  background: none;
  border: none;
}

.pagetoolbar > .p-button:hover {
  background: none;
  border: none;
}
</style>
