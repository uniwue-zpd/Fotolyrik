<script setup lang="ts">
import { ref, computed } from 'vue';
const colorMode = useColorMode();

const states = {
  dark : {icon: 'pi-moon', next: 'light'},
  light: {icon: 'pi-sun', next: 'dark'},
} as const;
const currentIndex = ref("light" as keyof typeof states);
defineProps<{
  inPopup?: boolean
}>()

until(() => colorMode.unknown).toBe(false).then(() => {
  currentIndex.value = colorMode.value as keyof typeof states;
});
const currentIcon = computed(() => `pi ${states[currentIndex.value].icon}`);
const toggle = () => {
  currentIndex.value = states[currentIndex.value].next;

  colorMode.preference = currentIndex.value;
};
</script>

<template>

  <div v-if="inPopup"  class="flex items-center gap-2 cursor-pointer" @click="toggle">
    <i :class="currentIcon" aria-hidden="true"></i>
    <span class="outfit-headline">Farbschema</span>

  </div>
  <i v-else :class="currentIcon +' text-white'" @click="toggle"></i>
</template>
