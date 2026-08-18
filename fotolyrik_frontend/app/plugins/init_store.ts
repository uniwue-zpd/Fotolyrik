import {useFiles} from "~/composables/useFiles";
export default defineNuxtPlugin(async (nuxtApp) => {
    const person_store = usePersonStore();
    const photopoem_store = usePhotopoemStore();
    const place_store = usePlaceStore();
    const pub_media_store = usePubMediumStore();
    const publisher_store = usePublisherStore();
    const pub_rhythm_store = usePubRhythmStore();
    const location_store = useLocationStore();

    await person_store.fetchPersons();
    await photopoem_store.fetchPhotopoems();
    await photopoem_store.fetchPhotopoemHighlight();
    await place_store.fetchPlaces();
    await pub_media_store.fetchPubMedia();
    await publisher_store.fetchPublishers();
    await pub_rhythm_store.fetchPubRhythms();
    await location_store.fetchLocations();
});
