package org.example;

import org.example.domain.User;
import org.example.domain.UserAccount;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    private UserAccount konto;

    @BeforeEach
    public void setup() throws SQLException {
        konto = new UserAccount();
        konto.register("TestUser","123456");
        UserAccount.aktuellerBenutzer = null;
    }

    @Test
    public void testAbmeldenKeinUser() {
        String result = konto.logout();
        assertEquals("Fehler beim abmelden! Kein Nutzer angemeldet!", result);
    }

    @Test
    public void testAbmeldenMitUser() {
        UserAccount.aktuellerBenutzer = new User(1,"User","1234");  // Minimal-Stub, einfach neues Objekt
        String result = konto.logout();
        assertEquals("Erfolgreich", result);
        assertNull(UserAccount.aktuellerBenutzer);
    }


    @Test
    public void testAbmeldenWennUserNull() {
        UserAccount.aktuellerBenutzer = null;
        assertEquals("Fehler beim abmelden! Kein Nutzer angemeldet!", konto.logout());
    }

    @Test
    public void testAnmeldenWennUserNichtVerhanden() throws SQLException {
        int result = konto.login("Nix","1234");
        assertEquals(2, result);
    }

    @Test
    public void testAnmeldenPasswortFalsch() throws SQLException {
        int result = konto.login("TestUser","Falsch");
        assertEquals(1, result);
    }


    @Test
    public void testRegisterVorhanden() throws SQLException {
        boolean result = konto.register("TestUser","123456");
        assertEquals(false, result);
    }

}
