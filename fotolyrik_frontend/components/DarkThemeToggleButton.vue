<script setup lang="ts">
import { ref, computed } from 'vue';
const colorMode = useColorMode();

const states = {
  dark : {icon: 'pi-moon', next: 'light'},
  light: {icon: 'pi-sun', next: 'system'},
  system:{icon: 'pi-desktop', next: 'dark'},
} as const;
const currentIndex = ref("system" as keyof typeof states);

until(() => colorMode.unknown).toBe(false).then(() => {
  currentIndex.value = colorMode.preference as keyof typeof states;
});
const currentIcon = computed(() => `pi ${states[currentIndex.value].icon}`);
const toggle = () => {
  currentIndex.value = states[currentIndex.value].next;

  colorMode.preference = currentIndex.value;
};
onMounted(()=>{

});
</script>

<template>
    <Button type="button" :icon="currentIcon" rounded-sm aria-label="User" variant="link" class="text-white" @click="toggle"/>
</template>
