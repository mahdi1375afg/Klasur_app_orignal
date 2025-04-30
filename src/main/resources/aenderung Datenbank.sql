alter table aufgabe drop column aufgabetype
create type aufgabetype as enum('geschlossene aufgabe','offene aufgabe');

alter table geschlosseneantwort add colum rank int