<script setup lang="ts">
import type { BreadcrumbItem } from '@nuxt/ui'
import { computed } from 'vue'
import { useRoute } from '#app'

const route = useRoute()

const items = computed<BreadcrumbItem[]>(() => {
  const pathSegments = route.path.split('/').filter(Boolean)

  const crumbs: BreadcrumbItem[] = [
    { label: 'Homepage', to: '/' }
  ]

  pathSegments.forEach((segment, index) => {
    const to = '/' + pathSegments.slice(0, index + 1).join('/')
    crumbs.push({
      label: segment.charAt(0).toUpperCase() + segment.slice(1),
      to
    })
  })

  return crumbs
})
</script>

<template>
  <UBreadcrumb :items="items">
    <template #separator>
      <span class="mx-2 text-muted">/</span>
    </template>
  </UBreadcrumb>
</template>


<style scoped></style>
