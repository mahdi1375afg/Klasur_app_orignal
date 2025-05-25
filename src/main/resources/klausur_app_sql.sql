 CREATE TABLE IF NOT EXISTS benutzer (
 id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 benutzername varchar(55) NOT NULL,
 passwort varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS modul (
 id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 name varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS klausur (
 id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 name VARCHAR(255) NOT NULL,
 datum DATE NOT NULL,
 gesamtpunkte DECIMAL(5,1) NOT NULL,
 benutzer_id INT NOT NULL,
 FOREIGN KEY (benutzer_id) REFERENCES benutzer(id)
);

CREATE TYPE IF NOT EXISTS taxonomie_stufe AS ENUM (
 'Erinnern',
 'Verstehen',
 'Anwenden',
 'Analysieren',
 'Bewerten',
 'Erschaffen'
);

CREATE TABLE IF NOT EXISTS aufgabe (
 id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 name VARCHAR(255) NOT NULL,
 aufgabentext TEXT NOT NULL,
 zeit INTERVAL MINUTE NOT NULL,
 format VARCHAR(20) NOT NULL,
 punkte DECIMAL(4,1) NOT NULL,
 taxonomie taxonomie_stufe,
 benutzer_id INT NOT NULL,
 FOREIGN KEY (benutzer_id) REFERENCES benutzer(id)
);

CREATE TABLE IF NOT EXISTS offene_aufgabe (
 musterloesung TEXT NOT NULL,
 aufgabe_id INT PRIMARY KEY,
 FOREIGN KEY (aufgabe_id) REFERENCES aufgabe(id)
);

CREATE TYPE IF NOT EXISTS aufgaben_typ AS ENUM (
 'Single-Choice',
 'Multiple-Choice',
 'Wahr/Falsch',
 'Lückentext',
 'Zuordnung',
 'Ranking'
);

CREATE TABLE IF NOT EXISTS geschlossene_aufgabe (
 typ aufgaben_typ NOT NULL,
 aufgabe_id INT PRIMARY KEY,
 FOREIGN KEY (aufgabe_id) REFERENCES aufgabe(id)
);

CREATE TABLE IF NOT EXISTS antwortmoeglichkeit_geschlossen (
 id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 antworttext TEXT NOT NULL,
 ist_korrekt BOOLEAN NOT NULL,
 geschlossene_aufgabe_id INT NOT NULL,
 FOREIGN KEY (geschlossene_aufgabe_id) REFERENCES geschlossene_aufgabe(aufgabe_id)
);

CREATE TABLE IF NOT EXISTS aufgaben_klausur (
 PRIMARY KEY (aufgabe_id, klausur_id),
 aufgabe_id INT NOT NULL,
 klausur_id INT NOT NULL,
 FOREIGN KEY (aufgabe_id) REFERENCES aufgabe(id),
 FOREIGN KEY (klausur_id) REFERENCES klausur(id)
);

CREATE TABLE IF NOT EXISTS aufgaben_modul (
 PRIMARY KEY (aufgabe_id, modul_id),
 aufgabe_id INT NOT NULL,
 modul_id INT NOT NULL,
 FOREIGN KEY (aufgabe_id) REFERENCES aufgabe(id),
 FOREIGN KEY (modul_id) REFERENCES modul(id)
);

CREATE TABLE IF NOT EXISTS antwortMehrParts_geschlossen (
 id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 antworttext TEXT NOT NULL,
 antworttext2 TEXT NOT NULL,
 geschlossene_aufgabe_id INT NOT NULL,
 FOREIGN KEY (geschlossene_aufgabe_id) REFERENCES geschlossene_aufgabe(aufgabe_id)
);

CREATE TABLE IF NOT EXISTS antwortRanking_geschlossen (
 id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 rank INT,
 antworttext TEXT NOT NULL,
 geschlossene_aufgabe_id INT NOT NULL,
 FOREIGN KEY (geschlossene_aufgabe_id) REFERENCES geschlossene_aufgabe(aufgabe_id)
);