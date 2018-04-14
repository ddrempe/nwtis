INSERT INTO polaznici (kor_ime,ime,prezime,lozinka,email_adresa,vrsta,datum_kreiranja,datum_promjene) VALUES 
 ('admin','Iva','Zec','123456','admin@foi.hr',0,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('george','George','Harrison','123456','george@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('ivo','Ivo','Zec','123456','ivo@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('janica','Janica','Kostelic','123456','janica@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('janis','Janis','Joplin','123456','janis@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('jdean','James','Dean','123456','jdean@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('jlo','Jennifer','Lopez','123456','jlo@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('jmoore','Joe','Moore','123456','jmoore@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('john','John','Lennon','123456','john@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('mato','Mato','Kos','123456','mato@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('paul','Paul','McCartney','123456','paul@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('pero','Pero','Kos','123456','pero@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('pgreen','Peter','Green','123456','pgreen@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('ringo','Ringo','Star','123456','ringo@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00'),
 ('tjones','Tom','Jones','123456','tjones@foi.hr',1,'2010-03-20 10:54:45','2012-03-14 00:00:00');
 
 INSERT INTO grupe (gr_ime,naziv) VALUES 
 ('admin','Administratori'),
 ('manager','Manageri'),
 ('nwtis','NWTiS');
 
INSERT INTO polaznici_grupe (kor_ime,gr_ime) VALUES 
 ('admin','admin'),
 ('mato','manager'),
 ('pero','admin'),
 ('pero','nwtis'); 
 