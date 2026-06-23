export default defineOAuthKeycloakEventHandler({
    async onSuccess(event, { user, tokens }) {
        console.log(tokens)
        await setUserSession(event, {
            user: {
                id: user.id,
                email: user.email,
                name: user.name,
            },
            secure: {
                accessToken: tokens.access_token,
                refreshToken: tokens.refresh_token,
                accessTokenExpires: Date.now() + tokens.expires * 1000,
            }
        });
        return sendRedirect(event, '/');
    },
    onError(event, error) {
        console.error(error);
        return sendRedirect(event, '/');
    }
});
