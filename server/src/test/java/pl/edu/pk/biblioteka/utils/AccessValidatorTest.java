package pl.edu.pk.biblioteka.utils;

import org.junit.Test;
import pl.edu.pk.biblioteka.data.Permissions;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccessValidatorTest {

    @Test
    public void isNotBlocked() {
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("permissionId")).thenReturn(Permissions.BLOCKED_ACCOUNT);

        assertFalse(AccessValidator.isNotBlocked(httpSession));
    }

    @Test
    public void isLibrarian() {
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("permissionId")).thenReturn(Permissions.LIBRARIAN_ACCESS);

        assertTrue(AccessValidator.isLibrarian(httpSession));
    }

    @Test
    public void isReader() {
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("permissionId")).thenReturn(Permissions.COMMON_READER);

        assertTrue(AccessValidator.isReader(httpSession));
    }
}