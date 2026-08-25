import {useFiles} from "~/composables/useFiles";
export default defineNuxtPlugin(async (nuxtApp) => {
    const place_store = usePlaceStore();
    const pub_media_store = usePubMediumStore();
    const publisher_store = usePublisherStore();
    const pub_rhythm_store = usePubRhythmStore();

    await place_store.fetchPlaces();
    await pub_media_store.fetchPubMedia();
    await publisher_store.fetchPublishers();
    await pub_rhythm_store.fetchPubRhythms();
});
