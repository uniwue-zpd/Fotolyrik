export default defineNuxtPlugin(async (nuxtApp) => {
    const photopoem_store = usePhotopoemStore();
    const person_store = usePersonStore();

    await photopoem_store.fetchPhotopoems();
    await person_store.fetchPersons();
});