<script setup lang="ts">
import type { DropdownMenuItem } from '@nuxt/ui';

type EntityType = 'person' | 'place' | 'photopoem' | 'pub_medium' | 'keyword' | 'location';

const props = defineProps<{
  id: number;
  entity_type: EntityType;
  page_url: string;
}>();

const router = useRouter();
const toast = useToast();

const deleters: Record<EntityType, (id: number) => Promise<void>> = {
  person: usePerson().deleteById,
  place: usePlace().deleteById,
  photopoem: usePhotopoem().deleteById,
  pub_medium: usePubMedium().deleteById,
  keyword: useKeyword().deleteById,
  location: useLocation().deleteById,
};

function copyLink() {
  navigator.clipboard.writeText(window.location.origin + props.page_url);
  toast.add({ color: 'info', title: 'Hinweis', description: 'Link erfolgreich kopiert!', duration: 3000 });
}

const items = computed<DropdownMenuItem[][]>(() => [
  [
    { label: 'Bearbeiten', icon: 'i-lucide-square-pen', to: `${props.page_url}/edit` },
    { label: 'Link kopieren', icon: 'i-lucide-link', onSelect: copyLink },
    { label: 'API', icon: 'i-lucide-code', onSelect: () => window.open(`/api${props.page_url}`, '_blank') },
  ],
  [
    { label: 'Löschen', icon: 'i-lucide-trash-2', color: 'error', onSelect: () => { deleteOpen.value = true; } },
  ],
]);

const deleteOpen = ref(false);
const deleting = ref(false);
let deleted = false;

async function deleteEntry() {
  deleting.value = true;
  try {
    await deleters[props.entity_type](props.id);
    deleted = true;
    deleteOpen.value = false;
    // Refresh data after deletion to avoid displaying deleted entry
    await refreshNuxtData();
    toast.add({ color: 'success', title: 'Gelöscht', description: 'Eintrag erfolgreich gelöscht', duration: 3000 });
    router.push(props.page_url.substring(0, props.page_url.lastIndexOf('/')));
  } catch (error) {
    toast.add({ color: 'error', title: 'Fehler', description: 'Eintrag konnte nicht gelöscht werden', duration: 3000 });
  } finally {
    deleting.value = false;
  }
}

watch(deleteOpen, (open) => {
  if (open) {
    deleted = false;
  } else if (!deleted) {
    toast.add({ color: 'info', title: 'Abgebrochen', description: 'Löschvorgang abgebrochen', duration: 3000 });
  }
});
</script>

<template>
  <UDropdownMenu
    :items="items"
    :modal="false"
    :content="{ side: 'left', align: 'start', sideOffset: 6, collisionPadding: 8 }"
    :ui="{ content: 'min-w-48', item: 'cursor-pointer' }"
  >
    <UButton
      icon="i-lucide-ellipsis-vertical"
      color="neutral"
      variant="ghost"
      aria-label="Aktionen"
      class="rounded-full data-[state=open]:bg-elevated"
    />
  </UDropdownMenu>

  <UModal
    v-model:open="deleteOpen"
    title="Löschvorgang bestätigen"
    description="Möchten Sie diesen Eintrag wirklich löschen?"
    :dismissible="!deleting"
  >
    <template #footer>
      <div class="flex justify-end gap-2 w-full">
        <UButton label="Nein" color="neutral" variant="outline" :disabled="deleting" @click="deleteOpen = false" />
        <UButton label="Ja, löschen" color="error" icon="i-lucide-trash-2" :loading="deleting" @click="deleteEntry" />
      </div>
    </template>
  </UModal>
</template>
