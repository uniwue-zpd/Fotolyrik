<script setup lang="ts">
import { ref } from 'vue';
import { useConfirm } from 'primevue/useconfirm';
import apiClient from '~/service/api';

const props = defineProps<{
  page_url: string;
}>();

const confirm = useConfirm();
const router = useRouter();
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
      confirm.require({
        message: 'Möchten Sie diesen Eintrag wirklich löschen?',
        header: 'Löschvorgang bestätigen',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Ja',
        rejectLabel: 'Nein',
        accept: async () => {
          try {
            await apiClient.delete(props.page_url);
            toast.add({ severity: 'success', summary: 'Gelöscht', detail: 'Eintrag erfolgreich gelöscht', life: 3000 });
            router.push(props.page_url.substring(0, props.page_url.lastIndexOf('/')));
          } catch (error) {
            toast.add({ severity: 'error', summary: 'Fehler', detail: 'Eintrag konnte nicht gelöscht werden', life: 3000 });
          }
        },
        reject: () => {
          toast.add({ severity: 'info', summary: 'Abgebrochen', detail: 'Löschvorgang abgebrochen', life: 3000 });
        }
      });
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
      window.open(`/api${props.page_url}`, '_blank');
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
  <ConfirmDialog class="confirm_deletion"/>
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

.confirm_deletion .p-button {
  background: none;
  border: none;
  color: black
}

.confirm_deletion .p-button:hover {
  background: none;
  border: none;
  color: black;
}
</style>
