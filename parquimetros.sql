#	DCIC - Base de Datos - 2020
#	Ringhetti Franco y Talmon Federico
#	Proyecto 2 - Ejecricio 1
#	Archivo batch para la creación de la base de datos "parquimetros" 

#Creación de la base de datos parquímetros
CREATE DATABASE parquimetros;

#Selección de la base de datos parquímetros
USE parquimetros;

#-----------------------------------------------------------------------------------------------------------------------
#	Creacion de las tablas correspondientes a entidades 
#-----------------------------------------------------------------------------------------------------------------------

#Tabla correspondiente a Conductores(dni, nombre, apellido, direccion, telefono, registro)
CREATE TABLE Conductores (
	dni INT UNSIGNED NOT NULL,
	registro INT UNSIGNED NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	apellido VARCHAR(20) NOT NULL,
	direccion VARCHAR(40) NOT NULL,
	telefono VARCHAR(20),
	
	CONSTRAINT pk_conductores PRIMARY KEY (dni)
)ENGINE=InnoDB;

#Tabla correspondiente a Automoviles(patente, marca, modelo, color, dni)
CREATE TABLE Automoviles (
	patente VARCHAR(6) NOT NULL,
	marca VARCHAR(15) NOT NULL,
	modelo VARCHAR(15) NOT NULL,
	color VARCHAR(20) NOT NULL,
	dni INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_automoviles PRIMARY KEY (patente),
	
	CONSTRAINT fk_automoviles_dni FOREIGN KEY (dni) REFERENCES Conductores (dni)
    ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

#Tabla correspondiente a Tipos_tarjeta(tipo, descuento)
CREATE TABLE Tipos_tarjeta (
	tipo VARCHAR(20) NOT NULL,
	descuento DECIMAL(3,2) UNSIGNED NOT NULL,
	
	CONSTRAINT pk_tipos_tarjeta PRIMARY KEY (tipo),
	CONSTRAINT chk_descuento CHECK (descuento<1 AND descuento >=0)
)ENGINE=InnoDB;

#Tabla correspondiente a Tarjetas(id tarjeta, saldo, tipo, patente)
CREATE TABLE Tarjetas (
	id_tarjeta INT UNSIGNED NOT NULL AUTO_INCREMENT,
	saldo DECIMAL(5,2) NOT NULL,
	tipo VARCHAR(20) NOT NULL,
	patente VARCHAR(6) NOT NULL,
	
	CONSTRAINT pk_tarjetas PRIMARY KEY (id_tarjeta),
	
	CONSTRAINT fk_tarjetas_tipo FOREIGN KEY (tipo) REFERENCES Tipos_tarjeta (tipo)
    ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT fk_tarjetas_patente FOREIGN KEY (patente) REFERENCES Automoviles (patente)
	ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

#Tabla correspondiente a Inspectores(legajo, dni, nombre, apellido, password)
CREATE TABLE Inspectores (
	legajo INT UNSIGNED NOT NULL,
	dni INT UNSIGNED NOT NULL,
	nombre VARCHAR(20) NOT NULL,
	apellido VARCHAR(20) NOT NULL,
	password VARCHAR(32) NOT NULL,
	
	CONSTRAINT pk_inspectores PRIMARY KEY (legajo)
)ENGINE=InnoDB;

#Tabla correspondiente a Ubicaciones(calle, altura, tarifa)
CREATE TABLE Ubicaciones (
	calle VARCHAR(40) NOT NULL,
	altura INT UNSIGNED NOT NULL,
	tarifa DECIMAL(5,2) UNSIGNED NOT NULL,
	
	CONSTRAINT pk_ubicaciones PRIMARY KEY (calle,altura)
)ENGINE=InnoDB;

#Tabla correspondiente a Parquimetros(id parq, numero, calle, altura)
CREATE TABLE Parquimetros (
	id_parq INT UNSIGNED NOT NULL,
	numero INTEGER UNSIGNED NOT NULL,
	calle VARCHAR(40) NOT NULL,
	altura INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_parquimetros PRIMARY KEY (id_parq),
	CONSTRAINT fk_parquimetros_calle_altura FOREIGN KEY (calle,altura) REFERENCES Ubicaciones (calle,altura)
	ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

#-----------------------------------------------------------------------------------------------------------------------
#	Creación de las tablas correspondientes a relaciones
#-----------------------------------------------------------------------------------------------------------------------

#Tabla correspondiente a Estacionamientos(id tarjeta, id parq, fecha ent, hora ent,fecha sal, hora sal)
CREATE TABLE Estacionamientos (
	id_tarjeta INT UNSIGNED NOT NULL,
	id_parq INT UNSIGNED NOT NULL,
	fecha_ent DATE NOT NULL,
	hora_ent TIME NOT NULL,
	fecha_sal DATE,
	hora_sal TIME,
	
	CONSTRAINT pk_estacionamientos PRIMARY KEY (id_parq,fecha_ent,hora_ent),
	
	CONSTRAINT fk_estacionamientos_id_tarjeta FOREIGN KEY (id_tarjeta) REFERENCES Tarjetas (id_tarjeta)
    ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT fk_estacionamientos_id_parq FOREIGN KEY (id_parq) REFERENCES Parquimetros (id_parq) 
    ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

#Tabla correspondiente a Accede(legajo, id parq, fecha, hora)
CREATE TABLE Accede (
	legajo INT UNSIGNED NOT NULL,
	id_parq INT UNSIGNED NOT NULL,
	fecha DATE NOT NULL,
	hora TIME NOT NULL,
	
	CONSTRAINT pk_accede PRIMARY KEY (id_parq,fecha,hora),
	
	CONSTRAINT fk_accede_legajo FOREIGN KEY (legajo) REFERENCES Inspectores (legajo)
	ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT fk_accede_parq FOREIGN KEY (id_parq) REFERENCES Parquimetros (id_parq) 
    ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

#Tabla correspondiente a Asociado_con(id asociado con, legajo, calle, altura, dia, turno)
CREATE TABLE Asociado_con (
	id_asociado_con INT UNSIGNED NOT NULL AUTO_INCREMENT,
	legajo INT UNSIGNED NOT NULL,
	calle VARCHAR(40) NOT NULL,
	altura INT UNSIGNED NOT NULL,
	dia ENUM('do','lu','ma','mi','ju','vi','sa') NOT NULL,
	turno ENUM('M','T') NOT NULL,
	
	CONSTRAINT pk_asociado_con PRIMARY KEY (id_asociado_con),
	
	CONSTRAINT fk_asociado_con_legajo FOREIGN KEY (legajo) REFERENCES Inspectores (legajo)
    ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT fk_asociado_con_altura FOREIGN KEY (calle,altura) REFERENCES Ubicaciones (calle,altura)
    ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

#Tabla correspondiente a Multa(numero, fecha, hora, patente, id asociado con)
CREATE TABLE Multa (
	numero INT UNSIGNED NOT NULL AUTO_INCREMENT,
	fecha DATE NOT NULL,
	hora TIME NOT NULL,
	patente VARCHAR(6) NOT NULL,
	id_asociado_con INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_multa PRIMARY KEY (numero),
	
	CONSTRAINT fk_multa_patente FOREIGN KEY (patente) REFERENCES Automoviles (patente) 
    ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT fk_multa_id_asociado FOREIGN KEY (id_asociado_con) REFERENCES Asociado_con (id_asociado_con)
    ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

#-----------------------------------------------------------------------------------------------------------------------
#	Creación de la vista estacionados para ocultar la estructura de la bdd al inspector
#-----------------------------------------------------------------------------------------------------------------------

CREATE VIEW estacionados AS
SELECT p.calle, p.altura, t.patente
FROM (Parquimetros p NATURAL JOIN Estacionamientos e NATURAL JOIN Tarjetas t)
WHERE (e.fecha_sal IS NULL) AND (e.hora_sal IS NULL);

#-----------------------------------------------------------------------------------------------------------------------
#	Creación de los usuarios y asignación de sus permisos
#-----------------------------------------------------------------------------------------------------------------------

#Creacion del usuario admin encargado de administrar la base de datos.
#Posee acceso total sobre las tablas. 
#Solo puede ingresar desde la maquina donde se encuentra el servidor.
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON parquimetros.* TO 'admin'@'localhost' WITH GRANT OPTION;

#Creacion del usuario venta encargado del permiso de acceso a la aplicacion de venta de tarjetas.
#Debe tener privilegios minimos para cargar una tarjeta nueva con un saldo inicial y de cualquier tipo.
CREATE USER 'venta'@'localhost' IDENTIFIED BY 'venta';
GRANT SELECT ON parquimetros.Tipos_tarjeta TO 'venta'@'localhost';
GRANT INSERT ON parquimetros.Tarjetas TO 'venta'@'localhost';

#Creacion del usuario inspector encargado a permitir el acceso de las unidades personales de los inspectores.
#Debe tener privilegios minimos para:
#	+Validar número de legajo y password de un inspector.
#	+Dado un parquímetro obtener patentes de los automóviles con un estacionamiento abierto.
#	+Cargar multas.
#	+Registrar accesos a parquímetros.
CREATE USER 'inspector'@'localhost' IDENTIFIED BY 'inspector';
GRANT SELECT ON parquimetros.Inspectores TO 'inspector'@'localhost';
GRANT SELECT ON parquimetros.Parquimetros TO 'inspector'@'localhost';
GRANT SELECT ON parquimetros.estacionados TO 'inspector'@'localhost';
GRANT SELECT ON parquimetros.Asociado_con TO 'inspector'@'localhost';
GRANT SELECT ON parquimetros.Automoviles TO 'inspector'@'localhost';
GRANT SELECT ON parquimetros.Multa TO 'inspector'@'localhost';
GRANT INSERT ON parquimetros.Multa TO 'inspector'@'localhost';
GRANT INSERT ON parquimetros.Accede TO 'inspector'@'localhost';



