<script setup lang="ts">
const props = defineProps<{
  photopoem: PhotoPoem
}>();
</script>

<template>
  <Card class="hover:shadow-md">
    <template #header>
      <div class="flex justify-center p-3">
        <div v-if="photopoem.images.length > 0">
          <Avatar
              :image="`/api/uploads/${photopoem.images[0].filename}`"
              shape="square"
              size="xlarge"
              oncontextmenu="return false;"
          />
        </div>
        <div v-else>
          <Avatar
              icon="pi pi-book"
              shape="square"
              size="xlarge"
          />
        </div>
      </div>
    </template>
    <template #title>
      <NuxtLink :to="`/photopoems/${photopoem.id}`">
        <p class="text-center text-sm font-bold outfit-headline text-[#063D79]">
          {{ photopoem.title }}
        </p>
      </NuxtLink>
    </template>
  </Card>
</template>

<style scoped>

</style>