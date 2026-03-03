export default defineNuxtPlugin(async (nuxtApp) => {
    const keyword_store = useKeywordStore();
    const person_store = usePersonStore();
    const photopoem_store = usePhotopoemStore();
    const place_store = usePlaceStore();
    const pub_media_store = usePubMediumStore();
    const file_store = useFileStore();
    const language_store = useLanguageStore();
    const copyright_status_store = useCopyrightStatusStore();
    const publisher_store = usePublisherStore();
    const pub_rhythm_store = usePubRhythmStore();

    await keyword_store.fetchKeywords();
    await person_store.fetchPersons();
    await photopoem_store.fetchPhotopoems();
    await photopoem_store.fetchPhotopoemHighlight();
    await place_store.fetchPlaces();
    await pub_media_store.fetchPubMedia();
    await file_store.fetchFiles();
    await language_store.fetchLanguages();
    await copyright_status_store.fetchCopyrightStatuses();
    await publisher_store.fetchPublishers();
    await pub_rhythm_store.fetchPubRhythms();
});
