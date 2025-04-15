create type bloomLevel as enum(
'errinern','verstehen','anwenden','anlysieren','bewerten','erschaffen'
);
create type Bloom as enum('true', 'false');
create table aufgabe (
id int generated always as identity primary key,
name varchar(255),
aufgabetext text,
zeit int,
bloomLevel bloomLevel not null,
type Bloom not null,
modulId int,
punkt int,
foreign key (modulId) references modul(id)
);

create type closedFrageType as enum('singel Choice','multiple Choice','wahr oder falsch','luecken Text','Zuordnung,Ranking')
create table closedFrage(
id int generated always as identity primary key,
closeFrageType closedFrageType not null,
aufgabeId int,
foreign key (aufgabeId) references aufgabe (id)
);

create table antwort(
id int generated always as identity primary key,
Antwort text,
aufgabeId int,
foreign key (aufgabeId) references aufgabe (id)
);

create table closeAntwort(
id int generated always as identity primary key,
Antwort text,
isKorrekt bloom not null,
rank int,
antwortId int not null,
foreign key (antwortId) references antwort(id)
);

create table aufgabeKlasur(
aufgabeId int not null,
klasurId int not null,
primary key(aufgabeId,klasurId),
foreign key (aufgabeId) references aufgabe(id),
foreign key (klasurId) references klasur(id)
);

create table klasur (
id int generated always as identity primary key,
name varchar(255),
beschreibung text,
modulId int,
dauer int,
erstellungsDatum date,
punkt int,
foreign key (modulId) references modul(id)
);

create table modul(
id int generated always as identity primary key,
name varchar(255),
beschreibung text
);

create table nutzer(
id int generated always as identity primary key,
name varchar(255),
age int,
email varchar(320)
);
create type status as enum('online','offline')
create table nutzerKonto(
id int generated always as identity primary key,
username varchar(255) not null,
password varchar(255) not null,
letzterAnmeldung date,
status status not null,
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

create table nutzerKontoKlasur(
nutzerKontoId int not null,
klasurId int not null,
primary key (nutzerKontoId,klasurId),
foreign key (nutzerKontoId) references nutzerKonto(id),
foreign key (klasurId) references klasur(id)
)