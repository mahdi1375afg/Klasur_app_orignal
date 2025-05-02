create type bloomLevel as enum('erinnern','verstehen','anwenden','analysieren','bewerten','erschaffen');
create type geschlosseneFrageType as enum('single Choice','multiple Choice','wahr oder falsch','luecken Text','Zuordnung,Ranking');
create type Bloom as enum('true', 'false');

create table modul(
id int generated always as identity primary key,
name varchar(255),
beschreibung text
);

create table klausur (
id int generated always as identity primary key,
name varchar(255),
beschreibung text,
modulId int,
dauer int,
erstellungsDatum date,
punkt int,
foreign key (modulId) references modul(id)
);

create table aufgabe (
id int generated always as identity primary key,
name varchar(255),
aufgabetext text,
zeit int,
bloomLevel bloomLevel not null,
type Bloom not null,
modulId int,
punkte int,
foreign key (modulId) references modul(id)
);

create table geschlosseneAufgabe(
id int generated always as identity primary key,
geschlosseneFrageType geschlosseneFrageType not null,
aufgabeId int,
foreign key (aufgabeId) references aufgabe (id)
);

create table antwort(
id int generated always as identity primary key,
Antwort text,
aufgabeId int,
foreign key (aufgabeId) references aufgabe (id)
);

create table geschlosseneAntwort(
id int generated always as identity primary key,
Antwort text,
isKorrekt bloom not null,
antwortId int not null,
foreign key (antwortId) references antwort(id)
);

create table aufgabeKlausur(
aufgabeId int not null,
klausurId int not null,
primary key(aufgabeId,klausurId),
foreign key (aufgabeId) references aufgabe(id),
foreign key (klausurId) references klausur(id)
);


create table nutzer(
id int generated always as identity primary key,
name varchar(255),
vorname varchar(255)
);

create table nutzerKonto(
id int generated always as identity primary key,
username varchar(255) not null,
password varchar(255) not null,
letzterAnmeldung date,
nutzerId int not null,
foreign key (nutzerId) references nutzer(id)
);

create table nutzerKontoAufgabe(
nutzerKontoId int not null,
aufgabeId int not null,
primary key (nutzerKontoId,aufgabeId),
foreign key (nutzerKontoId) references nutzerKonto(id),
foreign key (aufgabeId) references aufgabe(id)
);

create table nutzerKontoKlausur(
nutzerKontoId int not null,
klausurId int not null,
primary key (nutzerKontoId,klausurId),
foreign key (nutzerKontoId) references nutzerKonto(id),
foreign key (klausurId) references klausur(id)
)