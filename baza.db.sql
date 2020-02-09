BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "projekat" (
	"id"	INTEGER,
	"naziv"	TEXT,
	"vodja"	INTEGER,
	"opis"	TEXT,
	PRIMARY KEY("id"),
	FOREIGN KEY("vodja") REFERENCES "clan"("id")
);
CREATE TABLE IF NOT EXISTS "clan" (
	"id"	INTEGER,
	"ime"	TEXT,
	"prezime"	TEXT,
	"datum_rodjenja"	TEXT,
	"adresa_stanovanja"	TEXT,
	"grad"	TEXT,
	"drzava"	TEXT,
	"drzavljanstvo"	TEXT,
	"predsjednik"	INTEGER,
	"skupstina"	INTEGER,
	PRIMARY KEY("id")
);
COMMIT;
