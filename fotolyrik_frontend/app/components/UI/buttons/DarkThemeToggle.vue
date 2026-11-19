<script setup lang="ts">
defineProps<{
  inPopup?: boolean
}>()

const colorMode = useColorMode();

const states = {
  dark : { icon: 'i-lucide-moon', next: 'light' },
  light: { icon: 'i-lucide-sun', next: 'dark' },
} as const;

const currentIndex = ref("light" as keyof typeof states);

until(() => colorMode.unknown).toBe(false).then(() => {
  currentIndex.value = colorMode.value as keyof typeof states;
});

const currentIcon = computed(() => states[currentIndex.value].icon);

const toggle = () => {
  currentIndex.value = states[currentIndex.value].next;
  colorMode.preference = currentIndex.value;
};
</script>

<template>
  <div v-if="inPopup"  class="flex items-center gap-2 cursor-pointer" @click="toggle">
    <UIcon :name="currentIcon" aria-hidden="true" />
    <span class="outfit-headline">Farbschema</span>
  </div>
  <UButton
    v-else
    :icon="currentIcon"
    variant="link"
    aria-label="Farbschema wechseln"
    class="text-white hover:text-white cursor-pointer"
    @click="toggle"
  />
</template>
