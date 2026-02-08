<script setup lang="ts">
import { ref, computed } from 'vue';

const states = [
    {icon: 'pi-moon', value: 'dark' },
  {icon: 'pi-sun', value: 'light'},
  {icon: 'pi-desktop', value: 'system'},
];
const currentIndex = ref(0);
const currentIcon = computed(() => `pi ${states[currentIndex.value].icon}`);
const toggle = () => {
  currentIndex.value = (currentIndex.value + 1) % states.length;
};
</script>

<template>
    <Button type="button" :icon="currentIcon" rounded-sm aria-label="User" variant="link" class="text-white" @click="toggle"/>
</template>
