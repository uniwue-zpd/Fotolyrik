//logout.get.ts
export default defineEventHandler(async (event) => {
    const session = await getUserSession(event);
    const refreshToken = session.secure?.refreshToken;

    const config = useRuntimeConfig();
    const { serverUrl, realm, clientId } = config.oauth.keycloak;

    await clearUserSession(event);

    const baseKeycloakUrl = serverUrl.replace(/\/$/, "");
    const logoutUrl = `${baseKeycloakUrl}/realms/${realm}/protocol/openid-connect/logout`;

    if (refreshToken) {
        try {
            const body = new URLSearchParams();
            body.append('client_id', clientId);
            if (config.oauth.keycloak.clientSecret) {
                body.append('client_secret', config.oauth.keycloak.clientSecret);
            }
            body.append('refresh_token', refreshToken);

            const internalBase = (config.oauth.keycloak.serverUrlInternal || serverUrl).replace(/\/$/, "");
            await $fetch(`${internalBase}/realms/${realm}/protocol/openid-connect/logout`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body
            });
        } catch (error) {
            console.error('Keycloak Backchannel-Logout fehlgeschlagen:', error);
        }
    }
    const host = getRequestHost(event);
    const protocol = getRequestProtocol(event);
    const currentOrigin = `${protocol}://${host}`;
    const postLogoutRedirectUri = encodeURIComponent(`${currentOrigin}/`);
    const browserLogoutUrl = `${logoutUrl}?client_id=${clientId}&post_logout_redirect_uri=${postLogoutRedirectUri}`;

    return sendRedirect(event, browserLogoutUrl);
});
