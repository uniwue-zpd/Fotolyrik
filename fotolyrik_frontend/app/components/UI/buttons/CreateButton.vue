<script setup lang="ts">
defineProps<{ to: string }>();
</script>

<template>
  <div class="p-2 border border-solid rounded-md hover:shadow-md">
    <NuxtLink :to="to" class="flex items-center">
      <i class="pi pi-pen-to-square mr-2" />
      <div class="text-primary roboto-plain flex flex-row gap-1">
        Neu <div class="max-sm:hidden">anlegen</div>
      </div>
    </NuxtLink>
  </div>
</template>
