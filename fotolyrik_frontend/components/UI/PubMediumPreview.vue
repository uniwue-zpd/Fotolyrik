<script setup lang="ts">
const props = defineProps<{
  pubmedium: PubMediumDTO
}>();
</script>

<template>
  <div class="rounded-md shadow-md hover:shadow-lg transition-shadow duration-300 p-2 h-full bg-[#F1F2F2]">
    <NuxtLink :to="`/publication_media/${pubmedium.id}`" class="flex flex-col gap-2">
      <div class="flex justify-center">
        <Avatar
            icon="pi pi-book"
            shape="square"
            size="xlarge"
        />
      </div>
      <p class="text-center text-sm font-bold outfit-headline text-[#063D79]">
        {{ pubmedium.title }}
      </p>
    </NuxtLink>
  </div>
</template>

<style scoped>

</style>
