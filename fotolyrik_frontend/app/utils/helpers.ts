import {PROTECTED_GET_ROUTES} from "~/utils/types";

export function isProtectedGetRoute(path: string) {
    return PROTECTED_GET_ROUTES.some((pattern) => pattern.test(path));
}
