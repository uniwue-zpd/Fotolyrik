export default defineNuxtPlugin(async (nuxtApp) => {
    const photopoem_store = usePhotopoemStore();

    await photopoem_store.fetchPhotopoems();
});