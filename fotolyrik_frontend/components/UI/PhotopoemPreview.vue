<script setup lang="ts">
const file_store = useFileStore();

const props = defineProps<{
  photopoem: PhotoPoemDTO
}>();
</script>

<template>
  <div class="rounded-md shadow-md hover:shadow-lg transition-shadow duration-300 p-2 h-full bg-[#F1F2F2]">
    <NuxtLink :to="`/photopoems/${photopoem.id}`" class="flex flex-col gap-2">
      <div class="flex justify-center">
        <div v-if="photopoem.images.length > 0">
          <Avatar
              :image="file_store.getImagePreview(`/api/uploads/${photopoem.images[0].filename}`)"
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
      <p class="text-center text-sm font-bold outfit-headline text-[#063D79]">
        <span v-if="photopoem.title">
          {{ photopoem.title }}
        </span>
        <span v-else>{{ photopoem.altTitle }}</span>
      </p>
    </NuxtLink>
  </div>
</template>

<style scoped>

</style>
