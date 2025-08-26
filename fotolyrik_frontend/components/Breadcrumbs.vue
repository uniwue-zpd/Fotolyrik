<script setup lang="ts">
import { ref } from 'vue';
import type { BreadcrumbItem } from '@nuxt/ui';
import { useRoute } from '#app';

const route = useRoute()

const items = ref<BreadcrumbItem[]>([
  {
    label: 'Home',
    icon: 'i-lucide-house'
  },
  {
    label: 'Components',
    icon: 'i-lucide-box',
    to: '/components'
  },
  {
    label: 'Breadcrumb',
    icon: 'i-lucide-link',
    to: '/components/breadcrumb'
  }
])
</script>

<template>
  <UBreadcrumb :items="items" />
</template>

<style scoped></style>