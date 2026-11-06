<script setup lang="ts">
const { headerLink, flexDir = "col", showPlaceholder = false } = defineProps<{
    headerLink: string,
    flexDir?: "col" | "row",
    showPlaceholder?: boolean
}>();

const contentCard = ref<HTMLElement | null>(null);
const cardHeight = ref(0);
const placeholderVisible = ref(false);

onMounted(() => {
  // Keep the placeholder at the initial card height to avoid layout shifts.
  cardHeight.value = contentCard.value?.getBoundingClientRect().height ?? 0;
  watch(() => showPlaceholder, (loading, _, onCleanup) => {
    if (!loading) {
      placeholderVisible.value = false;
      return;
    }

    // Keep the current content visible for fast requests to avoid a placeholder flash.
    const timer = setTimeout(() => {
      placeholderVisible.value = true;
    }, 200);
    // Cancel the delay if loading finishes or the component is unmounted.
    onCleanup(() => clearTimeout(timer));
  }, { immediate: true });
});

</script>

<template>
    <div v-if="!placeholderVisible" ref="contentCard" class="border-2 border-primary rounded-md p-2 shadow-md">
        <div :class="`flex flex-${flexDir} *:whitespace-nowrap gap-1 ${flexDir === 'row' ? 'items-center' : ''}`">
            <slot name="preHeader" />
            <NuxtLink :to="headerLink" class="relative truncate w-fit max-w-full text-lg group outfit-headline font-semibold text-primary">
                <slot name="header" />
                <span
                    class="absolute bottom-0 left-0 h-px w-0 bg-current transition-all duration-300 group-hover:w-full" />
            </NuxtLink>
            <slot name="subContent" />
        </div>
    </div>
    <div v-else class="border-2 border-primary rounded-md p-2 shadow-md animate-pulse" :style="`height: ${cardHeight}px;`" />
</template>