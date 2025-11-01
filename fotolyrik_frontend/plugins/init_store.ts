export default defineNuxtPlugin(async (nuxtApp) => {
    const keyword_store = useKeywordStore();
    const person_store = usePersonStore();
    const photopoem_store = usePhotopoemStore();
    const place_store = usePlaceStore();
    const pub_media_store = usePubMediumStore();

    await keyword_store.fetchKeywords();
    await person_store.fetchPersons();
    await photopoem_store.fetchPhotopoems();
    await place_store.fetchPlaces();
    await pub_media_store.fetchPubMedia();
});