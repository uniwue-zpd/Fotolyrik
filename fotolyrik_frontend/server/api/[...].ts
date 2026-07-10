import { joinURL } from "ufo";
import { getAccessToken } from "#server/utils/auth";
import { isProtectedGetRoute } from "~/utils/helpers";

export default defineEventHandler(async (event) => {
    const proxyUrl = useRuntimeConfig().apiBaseUrl;
    const path = event.path.replace(/^\/api/, '');
    const target = joinURL(proxyUrl, path);

    // Public GET requests
    if (event.method === "GET" && !isProtectedGetRoute(path)) {
        return proxyRequest(event, target);
    }

    // All other requests (protected GETs + POST/PUT/PATCH/DELETE)
    const accessToken = await getAccessToken(event);

    return proxyRequest(event, target, {
        headers: {
            Authorization: `Bearer ${ accessToken }`,
        },
    });
});
