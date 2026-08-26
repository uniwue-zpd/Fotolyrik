import {useFiles} from "~/composables/useFiles";
export async function useRefreshStoreData() {
    const pubmedium_store = usePubMediumStore();
    const publisher_store = usePublisherStore();
    const pub_rhythm_store = usePubRhythmStore();

    try {
        await Promise.all([
            pubmedium_store.refreshPubMediaData(),
            publisher_store.refreshPublishersData(),
            pub_rhythm_store.refreshPubRhythmsData()
        ]);
        console.log('All stores were refreshed successfully');
    } catch (err) {
        console.error('Unable to refresh stores');
    }
}
