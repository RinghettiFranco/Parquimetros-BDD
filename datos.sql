#	DCIC - Base de Datos - 2020
#	Ringhetti Franco y Talmon Federico
#	Proyecto 2 - Ejecricio 1
#	Archivo batch para la inserción de datos en la base de datos "parquimetros" 

USE parquimetros;

#Carga de conductores
INSERT INTO Conductores(dni,registro,nombre,apellido,direccion,telefono) 
	VALUES (23458498,90929698,"Michael Jeffrey","Jordan","Avenida Chicago 23","154669098");
INSERT INTO Conductores(dni,registro,nombre,apellido,direccion,telefono) 
	VALUES (23633116,11121316,"LeBron","James","Avenida Lakers 23","154361116");
INSERT INTO Conductores(dni,registro,nombre,apellido,direccion,telefono) 
	VALUES (30141617,14151618,"Wardell Stephen","Curry","San Francisco St. 30","154141516");
INSERT INTO Conductores(dni,registro,nombre,apellido,direccion,telefono) 
	VALUES (35071617,15161735,"Kevin","Durant","Brooklyn Bvd. 7","154357123");

#Carga de automoviles	
INSERT INTO Automoviles(patente, marca, modelo, color, dni)
	VALUES ("GOA723","Rolls Royce","Wraith","Negro",23458498);
INSERT INTO Automoviles(patente, marca, modelo, color, dni)
	VALUES ("MJJ023","Ferrari","LaFerrari","Rojo",23458498);
INSERT INTO Automoviles(patente, marca, modelo, color, dni)
	VALUES ("KIN623","Ferrari","Enzo","Rojo",23633116);
INSERT INTO Automoviles(patente, marca, modelo, color, dni)
	VALUES ("LBJ301","Audi","R8","Negro",23633116);
INSERT INTO Automoviles(patente, marca, modelo, color, dni)
	VALUES ("LAL023","Bugatti","Veyron","Azul",23633116);
INSERT INTO Automoviles(patente, marca, modelo, color, dni)
	VALUES ("CLE231","Lamborghini","Diablo","Amarillo",23633116);
INSERT INTO Automoviles(patente, marca, modelo, color, dni)
	VALUES ("WSC303","Rolls Royce","Wraith","Blanco",30141617);
INSERT INTO Automoviles(patente, marca, modelo, color, dni)
	VALUES ("GSW467","Ferrari","LaFerrari","Amarillo",30141617);
INSERT INTO Automoviles(patente, marca, modelo, color, dni)
	VALUES ("KDT735","Hummer","H4","Negro",35071617);

#Carga de tipos de tarjetas	
INSERT INTO Tipos_tarjeta(tipo, descuento)
	VALUES ("Campeon",0.50);
INSERT INTO Tipos_tarjeta(tipo, descuento)
	VALUES ("MVP",0.20);
INSERT INTO Tipos_tarjeta(tipo, descuento)
	VALUES ("GOAT",0.75);

#Carga de tarjetas	
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (100.00,"MVP","GOA723");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (200.00,"GOAT","GOA723");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (400.00,"Campeon","MJJ023");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (312.27,"Campeon","KIN623");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (236.06,"MVP","KIN623");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (123.45,"GOAT","KIN623");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (420.69,"Campeon","LBJ301");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (233.00,"GOAT","LAL023");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (200.30,"MVP","CLE231");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (302.06,"MVP","WSC303");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (900.30,"Campeon","GSW467");
INSERT INTO Tarjetas(saldo, tipo, patente)
	VALUES (030.25,"Campeon","KDT735");

#Carga de inspectores	
INSERT INTO Inspectores(legajo, dni, nombre, apellido, password)
	VALUES (1234,11111111,"Greg","Popovich",md5('GOAT-COACH'));
INSERT INTO Inspectores(legajo, dni, nombre, apellido, password)
	VALUES (2345,12345678,"Pat","Riley",md5('ShowTime'));
INSERT INTO Inspectores(legajo, dni, nombre, apellido, password)
	VALUES (3456,87654321,"Phil","Jackson",md5('ZenMaster'));	

#Carga de ubicaciones
INSERT INTO Ubicaciones(calle, altura, tarifa)
	VALUES ("Celtics Bvd.",16,52.34);
INSERT INTO Ubicaciones(calle, altura, tarifa)
	VALUES ("Avenida Lakers",2010,24.08);	
INSERT INTO Ubicaciones(calle, altura, tarifa)
	VALUES ("Avenida Chicago",1998,45.23);
INSERT INTO Ubicaciones(calle, altura, tarifa)
	VALUES ("San Antonio St.",2014,21.00);

#Carga de parquimetros
INSERT INTO Parquimetros(id_parq, numero, calle, altura)
	VALUES (1,1,"Celtics Bvd.",16);
INSERT INTO Parquimetros(id_parq, numero, calle, altura)
	VALUES (2,2,"Celtics Bvd.",16);
INSERT INTO Parquimetros(id_parq, numero, calle, altura)
	VALUES (3,3,"Celtics Bvd.",16);
INSERT INTO Parquimetros(id_parq, numero, calle, altura)
	VALUES (4,1,"Avenida Lakers",2010);
INSERT INTO Parquimetros(id_parq, numero, calle, altura)
	VALUES (5,1,"Avenida Chicago",1998);
INSERT INTO Parquimetros(id_parq, numero, calle, altura)
	VALUES (6,2,"Avenida Chicago",1998);
INSERT INTO Parquimetros(id_parq, numero, calle, altura)
	VALUES (7,1,"San Antonio St.",2014);

#Carga de estacionamientos
INSERT INTO	Estacionamientos(id_tarjeta, id_parq, fecha_ent, hora_ent,fecha_sal, hora_sal)
	VALUES (1,6,"2006-02-01","16:54:23","2006-02-01","17:32:05");
INSERT INTO	Estacionamientos(id_tarjeta, id_parq, fecha_ent, hora_ent,fecha_sal, hora_sal)
	VALUES (3,4,"2009-11-23","10:02:45","2009-11-23","11:26:12");
INSERT INTO	Estacionamientos(id_tarjeta, id_parq, fecha_ent, hora_ent,fecha_sal, hora_sal)
	VALUES (8,1,"2016-06-15","22:12:31","2016-06-15","00:12:21");
INSERT INTO	Estacionamientos(id_tarjeta, id_parq, fecha_ent, hora_ent,fecha_sal, hora_sal)
	VALUES (3,5,"2020-10-08","10:23:15",NULL,NULL);
INSERT INTO	Estacionamientos(id_tarjeta, id_parq, fecha_ent, hora_ent,fecha_sal, hora_sal)
	VALUES (6,2,"2020-10-08","09:12:14",NULL,NULL);

#Carga de accesos	
INSERT INTO Accede(legajo, id_parq, fecha, hora)
	VALUES (1234,2,"2009-11-23","10:02:45");
INSERT INTO Accede(legajo, id_parq, fecha, hora)
	VALUES (3456,6,"2020-10-08","09:12:14");

#Carga de asociados	
INSERT INTO Asociado_con(legajo, calle, altura, dia, turno)
	VALUES (1234,"San Antonio St.",2014,'ma','T');
INSERT INTO Asociado_con(legajo, calle, altura, dia, turno)
	VALUES (2345,"Avenida Lakers",2010,'ju','M');
INSERT INTO Asociado_con(legajo, calle, altura, dia, turno)
	VALUES (3456,"Avenida Chicago",1998,'vi','T');

#Carga de multas	
INSERT INTO Multa(fecha, hora, patente, id_asociado_con)
	VALUES ("2020-10-08","12:15:40","KIN623",1);