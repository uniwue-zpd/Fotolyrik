import {useFiles} from "~/composables/useFiles";
export default defineNuxtPlugin(async (nuxtApp) => {
    const pub_media_store = usePubMediumStore();
    const pub_rhythm_store = usePubRhythmStore();

    await pub_media_store.fetchPubMedia();
    await pub_rhythm_store.fetchPubRhythms();
});
