CREATE TABLE polaznici (
  kor_ime varchar(10) NOT NULL DEFAULT '',
  ime varchar(25) NOT NULL DEFAULT '',
  prezime varchar(25) NOT NULL DEFAULT '',
  lozinka varchar(20) NOT NULL DEFAULT '',
  email_adresa varchar(40) NOT NULL DEFAULT '',
  vrsta int NOT NULL DEFAULT 1,
  datum_kreiranja timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  datum_promjene timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (kor_ime)
);

CREATE TABLE grupe (
  gr_ime varchar(10) NOT NULL DEFAULT '',
  naziv varchar(25) NOT NULL DEFAULT '',
  PRIMARY KEY (gr_ime)
);

CREATE TABLE polaznici_grupe (
  kor_ime varchar(10) NOT NULL DEFAULT '',
  gr_ime varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (kor_ime,gr_ime),
  CONSTRAINT polaznici_grupe_FK1 FOREIGN KEY (kor_ime) REFERENCES polaznici (kor_ime)
);
 