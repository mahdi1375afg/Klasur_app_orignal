package org.example;

import org.example.domain.Nutzer;
import org.example.domain.benutzerKonto;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    private benutzerKonto konto;

    @BeforeEach
    public void setup() throws SQLException {
        konto = new benutzerKonto();
        konto.register("TestUser","123456");
        benutzerKonto.aktuellerBenutzer = null;
    }

    @Test
    public void testAbmeldenKeinUser() {
        String result = konto.abmelden();
        assertEquals("Fehler beim abmelden! Kein Nutzer angemeldet!", result);
    }

    @Test
    public void testAbmeldenMitUser() {
        benutzerKonto.aktuellerBenutzer = new Nutzer(1,"User","1234");  // Minimal-Stub, einfach neues Objekt
        String result = konto.abmelden();
        assertEquals("Erfolgreich", result);
        assertNull(benutzerKonto.aktuellerBenutzer);
    }


    @Test
    public void testAbmeldenWennUserNull() {
        benutzerKonto.aktuellerBenutzer = null;
        assertEquals("Fehler beim abmelden! Kein Nutzer angemeldet!", konto.abmelden());
    }

    @Test
    public void testAnmeldenWennUserNichtVerhanden() throws SQLException {
        int result = konto.anmelden("Nix","1234");
        assertEquals(2, result);
    }

    @Test
    public void testAnmeldenPasswortFalsch() throws SQLException {
        int result = konto.anmelden("TestUser","Falsch");
        assertEquals(1, result);
    }


    @Test
    public void testRegisterVorhanden() throws SQLException {
        boolean result = konto.register("TestUser","123456");
        assertEquals(false, result);
    }

}
