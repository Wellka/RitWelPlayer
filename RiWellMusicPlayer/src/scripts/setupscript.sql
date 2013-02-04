
CREATE TABLE Benutzer (
	ID SERIAL,
	Vorname VARCHAR(100) NOT NULL,
	Nachname VARCHAR(100) NULL,
	Benutzer VARCHAR(100) NULL UNIQUE,
	Passwort VARCHAR(32) NULL,	
	PRIMARY KEY(ID)
);

CREATE TABLE Musikdaten (
	ID SERIAL,
	Titel VARCHAR(100) NULL,
	Benutzer_ID INTEGER NOT NULL REFERENCES Benutzer(ID),
	Timestamp date NOT NULL,
	TimeLength INTEGER,
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

CREATE TABLE Musikdaten_Gerne (
	ID_Musik INTEGER REFERENCES Musikdaten(ID),
	ID_Genre INTEGER REFERENCES Genre(ID)
);
