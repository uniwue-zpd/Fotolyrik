import {useFiles} from "~/composables/useFiles";
export default defineNuxtPlugin(async (nuxtApp) => {
    const pub_rhythm_store = usePubRhythmStore();

    await pub_rhythm_store.fetchPubRhythms();
});
