<script setup lang="ts">
const { headerLink, flexDir = "col", showPlaceholder = false } = defineProps<{
    headerLink: string,
    flexDir?: "col" | "row",
    showPlaceholder?: boolean
}>();

const contentCard = ref<HTMLElement | null>(null);
const cardHeight = ref(0);

onMounted(() => {
  cardHeight.value = contentCard.value?.getBoundingClientRect().height ?? 0;
});

</script>

<template>
    <div v-if="!showPlaceholder" ref="contentCard" class="border-2 border-primary rounded-md p-2 shadow-md">
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