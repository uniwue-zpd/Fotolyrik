import {useFiles} from "~/composables/useFiles";
export async function useRefreshStoreData() {
    const pub_rhythm_store = usePubRhythmStore();

    try {
        await Promise.all([
            pub_rhythm_store.refreshPubRhythmsData()
        ]);
        console.log('All stores were refreshed successfully');
    } catch (err) {
        console.error('Unable to refresh stores');
    }
}
