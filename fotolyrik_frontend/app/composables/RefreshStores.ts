import {useFiles} from "~/composables/useFiles";
export async function useRefreshStoreData() {
    const copyright_status_store = useCopyrightStatusStore();
    const use_files = useFiles();
    const keyword_store = useKeywordStore();
    const language_store = useLanguageStore();
    const person_store = usePersonStore();
    const photopoem_store = usePhotopoemStore();
    const place_store = usePlaceStore();
    const pubmedium_store = usePubMediumStore();
    const publisher_store = usePublisherStore();
    const pub_rhythm_store = usePubRhythmStore();

    try {
        await Promise.all([
            copyright_status_store.refreshCopyrighStatusesData(),
            use_files.refreshFilesData(),
            keyword_store.refreshKeywordsData(),
            language_store.refreshLanguagesData(),
            person_store.refreshPersonsData(),
            photopoem_store.refreshPhotopoemsData(),
            place_store.refreshPlacesData(),
            pubmedium_store.refreshPubMediaData(),
            publisher_store.refreshPublishersData(),
            pub_rhythm_store.refreshPubRhythmsData()
        ]);
        console.log('All stores were refreshed successfully');
    } catch (err) {
        console.error('Unable to refresh stores');
    }
}
