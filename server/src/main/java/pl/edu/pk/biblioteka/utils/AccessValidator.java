package pl.edu.pk.biblioteka.utils;

import pl.edu.pk.biblioteka.data.Permissions;

import javax.servlet.http.HttpSession;

/**
 * Klasa z metodami sprawdzającymi czy klient jest zalogowany i czy ma odpowienie uprawnienia
 */
public class AccessValidator {

    /**
     * Sprawdza czy konto klient nie jest zablokowane
     * @param session
     * @return
     */
    public static boolean isNotBlocked(HttpSession session) {
        Integer permissionId = getPermissionId(session);

        return permissionId != null && permissionId != Permissions.BLOCKED_ACCOUNT;
    }

    /**
     * Sprawdza czy klient jest bibliotekarzem
     * @param session
     * @return
     */
    public static boolean isLibrarian(HttpSession session) {
        Integer permissionId = getPermissionId(session);

        return permissionId != null && permissionId == Permissions.LIBRARIAN_ACCESS;
    }

    /**
     * Sprawdza czy klient jest czytelnikiem
     * @param session
     * @return
     */
    public static boolean isReader(HttpSession session) {
        Integer permissionId = getPermissionId(session);

        return permissionId != null && permissionId == Permissions.COMMON_READER;
    }

    /**
     * Klasa wyciągająca z sesji atrybut 'permissionId' (jeśli istnije)
     * @param session
     * @return id uprawnienia lub null jeśli nie ma atrybutu 'permissionId'
     */
    private static Integer getPermissionId(HttpSession session) {
        if (session != null) {
            Object permission = session.getAttribute("permissionId");
            if (permission != null) {
                if (permission instanceof Integer) {
                    return  (Integer) permission;
                }
            }
        }

        return null;
    }
}
