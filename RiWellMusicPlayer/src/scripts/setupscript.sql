CREATE TABLE Benutzer(
	ID SERIAL,
	Vorname VARCHAR(100) NOT NULL,
	Nachname VARCHAR(100) NULL,
	Benutzer VARCHAR(100) NULL UNIQUE,
	EMail VARCHAR(255) NULL,
	Passwort VARCHAR(32) NULL,	
	PRIMARY KEY(ID)
);

CREATE TABLE Musikdaten (
	ID SERIAL,
	Titel VARCHAR(100) NULL,
	Benutzer_ID INTEGER NOT NULL REFERENCES Benutzer(ID),
	Timestamp date NOT NULL,
	data bytea,
	PRIMARY KEY(ID)
);

CREATE TABLE Album (
	ID SERIAL,
	Albumname VARCHAR(100) NULL,
	PRIMARY KEY(ID)
);

CREATE TABLE Genre (
	ID SERIAL,
	Genre VARCHAR(100) NULL,
	PRIMARY KEY(ID)
);

CREATE TABLE Interpret (
	ID SERIAL,
	Interpret VARCHAR(100) NULL,
	PRIMARY KEY(ID)
);


CREATE TABLE benutzer_benutzer (
	ID_Benutzer1 INTEGER REFERENCES Benutzer(ID),
	ID_Benutzer2 INTEGER REFERENCES Benutzer(ID)
);

CREATE TABLE Musikdaten_Interpret (
	ID_Musik INTEGER REFERENCES Musikdaten(ID),
	ID_Interpret INTEGER REFERENCES Interpret(ID)
);

CREATE TABLE Musikdaten_Album (
	ID_Musik INTEGER REFERENCES Musikdaten(ID),
	ID_Album INTEGER REFERENCES Album(ID)
);

CREATE TABLE Musikdaten_Genre (
	ID_Musik INTEGER REFERENCES Musikdaten(ID),
	ID_Genre INTEGER REFERENCES Genre(ID)
);
	
CREATE VIEW AlleTitel AS
 SELECT md.id,md.benutzer_id,md.titel,g.genre,a.albumname,i.interpret FROM musikdaten md
 INNER JOIN musikdaten_album ma ON md.id = ma.id_musik
 INNER JOIN musikdaten_genre mg ON md.id = mg.id_musik
 INNER JOIN musikdaten_interpret mi ON md.id = mi.id_musik
 INNER JOIN genre g ON g.id = mg.id_genre
 INNER JOIN interpret i ON mi.id_interpret = i.id
 INNER JOIN album a ON ma.id_album = a.id;

CREATE VIEW score AS SELECT benutzer, COUNT(*) as count FROM musikdaten JOIN BENUTZER ON benutzer_id = benutzer.id GROUP BY BENUTZER;