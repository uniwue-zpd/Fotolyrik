<script setup lang="ts">
const photopoemStore = usePhotopoemStore();
const { data: photopoems, pending: isLoading, error: hasError } = useAsyncData(`photopoems-paginated`, () => photopoemStore.fetchPhotopoemsPaginated({}));

useHead(() => ({
  title: 'Fotogedichte - Sammlung'
}));
</script>

<template>
</template>

<style scoped>

</style>
