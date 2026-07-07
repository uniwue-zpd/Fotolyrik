declare module '#auth-utils' {
    interface SecureSessionData {
        accessToken: string;
        refreshToken: string;
        accessTokenExpires: number;
    }

    interface User {
        name: string
        id:string
        email?: string
    }
}

export {};
