<script setup lang="ts">
import {ref, watch} from 'vue';
import {useFiles} from "~/composables/useFiles";
const use_files = useFiles();

const props = defineProps<{
  photopoem: PhotoPoemDTO
}>();

const photopoem = props.photopoem;
const path = `/photopoems/${ photopoem.id }`;
const image_path = ref<string | null>(null);

async function loadFirstImage() {
  try {
    if (photopoem && photopoem.images.length > 0 && photopoem.images[0] !== undefined &&
        photopoem.imagesVisible === AccessLevel.PUBLIC) {
      image_path.value = await use_files.getImageContent(photopoem.images[0].id);
      return;
    }
  } catch (e) {
    console.error('Failed to load photopoem image content', e);
  }
  image_path.value = null;
}

watch(() => props.photopoem, loadFirstImage, { immediate: true, deep: true });
</script>

<template>
  <div class="rounded-md shadow-md hover:shadow-lg transition-shadow duration-300 p-2 h-full bg-gray-accent">
    <NuxtLink :to="path" class="flex flex-col gap-2">
      <div class="flex justify-center">
        <div v-if="image_path" class="rounded-md">
          <Avatar
              :image="image_path"
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
      <p class="text-center text-sm font-bold outfit-headline text-primary">
        <span v-if="photopoem.title" class="line-clamp-1" :title="photopoem.title">
          {{ photopoem.title }}
        </span>
        <span v-else class="line-clamp-1" :title="photopoem.altTitle ?? undefined">
          {{ photopoem.altTitle }}
        </span>
      </p>
    </NuxtLink>
  </div>
</template>

<style scoped>

</style>
