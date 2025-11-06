export async function useRefreshStoreData() {
    const copyright_status_store = useCopyrightStatusStore();
    const file_store = useFileStore();
    const keyword_store = useKeywordStore();
    const language_store = useLanguageStore();
    const person_store = usePersonStore();
    const photopoem_store = usePhotopoemStore();
    const place_store = usePlaceStore();
    const pubmedium_store = usePubMediumStore();

    try {
        await Promise.all([
            copyright_status_store.refreshCopyrighStatusesData(),
            file_store.refreshFilesData(),
            keyword_store.refreshKeywordsData(),
            language_store.refreshLanguagesData(),
            person_store.refreshPersonsData(),
            photopoem_store.refreshPhotopoemsData(),
            place_store.refreshPlacesData(),
            pubmedium_store.refreshPubMediaData()
        ]);
        console.log('All stores were refreshed successfully');
    } catch (err) {
        console.error('Unable to refresh stores');
    }
}
