<script setup lang="ts">
import { ref, computed } from 'vue';
const colorMode = useColorMode();

const states = [
    {icon: 'pi-moon', value: 'dark' },
  {icon: 'pi-sun', value: 'light'},
  {icon: 'pi-desktop', value: 'system'},
];
// Initialize index based on current preference so the icon is right on page load
const currentIndex = ref(states.findIndex(s => s.value === colorMode.preference) || 0);
const currentIcon = computed(() => `pi ${states[currentIndex.value].icon}`);
const toggle = () => {
  currentIndex.value = (currentIndex.value + 1) % states.length;

  colorMode.preference =states[currentIndex.value].value;
};
</script>

<template>
    <Button type="button" :icon="currentIcon" rounded-sm aria-label="User" variant="link" class="text-white" @click="toggle"/>
</template>
