# Tic Empleo

Aplicación web para publicar ofertas de trabajo.

Thymeleaf: [https://www.thymeleaf.org/](https://www.thymeleaf.org/)

El listado de ofertas de trabajo es público y es accesible para cualquiera, pero para poder solicitar una oferta de empleo tiene que ser un usuario registrado.

Existen tres tipos de usuarios:

**Administrador** 

Tiene acceso a toda la aplicación. Listado de ofertas de empleo, categorías, solicitudes y usuarios.

1. Crear, editar o eliminar ofertas de empleo y categorías.

2. Eliminar solicitudes.

3. Eliminar o bloquear/desbloquear usuarios

**Supervisor**

Tiene acceso al listado de ofertas de empleo, categorías y solicitudes

1. Crear, editar o eliminar ofertas de empleo y categorías.

2. Eliminar solicitudes.

**Usuario**

Este usuario solo tiene acceso a la lista de ofertas de empleo, ver los detalles de las ofertas y solicitar ofertas de empleo.

## Requisitos

Es necesario tener instalado:

Cualquier IDE como [Eclipse](https://www.eclipse.org/downloads/) o [NetBeans](https://netbeans.org/)

MySQL [https://dev.mysql.com/downloads/](https://dev.mysql.com/downloads/)

Opcional un cliente gráfico para MySQL [MySQL Workbench](https://www.mysql.com/products/workbench/)

Lombok [https://knowdb.tech/como-configurar-lombok-en-eclipse/](https://knowdb.tech/como-configurar-lombok-en-eclipse/)

## Usuarios para probar la aplicación

**usuario:** administrador

**contraseña:** administrador

**usuario:** supervisor

**contraseña:** administrador

**usuario:** usuario

**contraseña:** usuario

## Crear la base de datos empleos

!["](/src/main/resources/static/images/base-de-datos.png)

```
-- Script para crear la base de datos empleos

DROP SCHEMA IF EXISTS empleos;
CREATE SCHEMA IF NOT EXISTS empleos;
USE empleos;

DROP TABLE IF EXISTS `categorias`;
CREATE TABLE `categorias` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `perfiles`;
CREATE TABLE `perfiles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `perfil` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(100) NOT NULL,
  `estatus` int(11) NOT NULL DEFAULT '1',
  `fecha_registro` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `vacantes`;
CREATE TABLE `vacantes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text NOT NULL,
  `fecha` date NOT NULL,
  `salario` double NOT NULL,
  `estatus` enum('Creada','Aprobada','Eliminada') NOT NULL,
  `destacada` int(11) NOT NULL,
  `imagen` varchar(250) NOT NULL,
  `detalles` text,
  `idCategoria` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_vacantes_categorias1_idx` (`idCategoria`),
  CONSTRAINT `fk_vacantes_categorias1` FOREIGN KEY (`idCategoria`) REFERENCES `Categorias` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `solicitudes`;
CREATE TABLE `solicitudes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `archivo` varchar(250) NOT NULL,
  `comentarios` text,
  `idVacante` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vacante_usuario_UNIQUE` (`idVacante`,`idUsuario`),
  KEY `fk_solicitudes_vacantes1_idx` (`idVacante`),
  KEY `fk_solicitudes_usuarios1_idx` (`idUsuario`),
  CONSTRAINT `fk_solicitudes_usuarios1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_solicitudes_vacantes1` FOREIGN KEY (`idVacante`) REFERENCES `vacantes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `perfiles_usuarios`;
CREATE TABLE `perfiles_usuarios` (
  `idUsuario` int(11) NOT NULL,
  `idPerfil` int(11) NOT NULL,
  UNIQUE KEY `usuario_perfil_UNIQUE` (`idUsuario`,`idPerfil`),
  KEY `fk_usuarios1_idx` (`idUsuario`),
  KEY `fk_perfiles1_idx` (`idPerfil`),
  CONSTRAINT `fk_usuarios1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_perfiles1` FOREIGN KEY (`idPerfil`) REFERENCES `perfiles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

## Crear tablas necesarias Spring Security

```
-- Crear tabla de usuarios
CREATE TABLE `users` (
	`username` varchar(50) NOT NULL,
	`password` varchar(50) NOT NULL,
	`enabled` tinyint(1) NOT NULL,
	PRIMARY KEY (`username`)
) ENGINE=InnoDB;

-- Crear tabla de roles
CREATE TABLE `authorities` (
	`username` varchar(50) NOT NULL,
	`authority` varchar(50) NOT NULL,
	UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
	CONSTRAINT `authorities_ibfk_1`
	FOREIGN KEY (`username`)
	REFERENCES `users` (`username`)
) ENGINE=InnoDB;

```

## Insertar datos

```
-- Insertar Categorías

INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (1,'Sistemas','Ofertas relacionadas con sistemas');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (2,'Desarrollo Web','Ofertas relacionadas con desarrollo web');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (3,'Desarrollo Back-End','Ofertas relacionadas con desarrollo back-end');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (4,'Desarrollo Front-End','Ofertas relacionadas con desarrollo front-end');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (5,'Desarrollo Full-Stack','Ofertas relacionadas con desarrollo full-stack');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (6,'MERN Stack','Ofertas relacionadas con desarrollo MERN stack');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (7,'MEAN Stack','Ofertas relacionadas con desarrollo MEAN stack');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (8,'Android/IOS','Ofertas relacionadas con desarrollo Android/IOS');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (9,'Big Data','Ofertas relacionadas con big data');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (10,'Negocio','Ofertas relacionadas con sistemas de gestión empresarial');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (11,'Diseño Gráfico','Ofertas relacionadas con diseño gráfico');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (12,'Redes','Ofertas relacionadas con diseño de redes, implementación y administración de redes');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (13,'Base de Datos','Ofertas relacionadas con diseño, integración y administración de bases de datos');
INSERT INTO `categorias` (`id`,`nombre`,`descripcion`) VALUES (14,'Blockchain/Bitcoin','Ofertas relacionadas con blockchain y bitcoin');

-- Insertar Vacantes

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`) 
VALUES (1,
'Administrador de Sistemas','Buscamos administrador/a de sistemas Linux/Windows','2021-02-20',22000,'Aprobada',1,'yuwvwl-reltan.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior ASIR</p>
<p><strong>Nivel de experiencia</strong></p>
<p>No requerida</p>
<p><strong>Tipo de empleo</strong><p>
<p>Jornada completa</p>
<p><strong>Conocimientos necesarios</strong></p>
<p>- Bases de datos SQL y Oracle</p>
<p>- Conocimientos de virtualización con VMWare </p>
<p>- Administración de sistemas Linux/Windows</p>
<p>- Aplicación de normativas de seguridad</p>
<p>- Conocimientos de automatización</p>
<p>- Configuración de switches y firewalls</p>',
1);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`) 
VALUES (
2,
'Desarrollador MEAN Stack','Buscamos desarrollador/a MEAN Stack','2021-02-18',24000,'Aprobada',1,'nxlfbz-quantum.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>No requerida</p>
<p><strong>Tipo de empleo</strong><p>
<p>Jornada completa</p>
<p><strong>Conocimientos necesarios</strong></p>
<p>- MongoDB</p>
<p>- Express</p>
<p>- Angular</p>
<p>- Node.js</p>',
7);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`) 
VALUES (
3,
'Desarrollador MERN Stack','Buscamos desarrollador/a MERN Stack','2021-02-20',24000,'Aprobada',1,'e09vgs-kata.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>No requerida</p>
<p><strong>Tipo de empleo</strong><p>
<p>Jornada completa</p>
<p><strong>Conocimientos necesarios</strong></p>
<p>- MongoDB</p>
<p>- Express</p>
<p>- React</p>
<p>- Next.js</p>
<p>- Node.js</p>',
6);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`) 
VALUES (
4,'Desarrollador Front-End','Buscamos programador/a con experiencia en desarrollo front-end','2021-01-29',30000,'Aprobada',1,'sk67ma-razvan.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>2 años de experiencia con JavaScript y Angular</p>
<p><strong>Tipo de empleo</strong><p>
<p>Jornada completa</p>
<p><strong>Conocimientos necesarios</strong></p>
<p>Angular</p>
<p>- JavaScript/TypeScript</p>
<p>- HTML5, CSS3, Sass</p>
<p>- HTML5, CSS3, Sass</p>',
4);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`) 
VALUES (
5,
'Desarrollador Back-End','Buscamos programador/a con experiencia en desarrollo back-end','2021-02-20',24000,'Aprobada',1,'3bey5s-titan.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>2 años de experiencia con JavaScript y Node.js</p>
<p><strong>Tipo de empleo</strong><p>
<p>Git</p>
<p>Node.js</p>
<p>Express</p>
<p>- JavaScript/TypeScript</p>',
3);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`)
VALUES (
6,
'Analista Programador Java J2EE','Buscamos analista programador/a Java','2021-02-19',30000,'Aprobada',1,'gaov0b-technet.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>4 años de experiencia como analista programador Java2EE.</p>
<p><strong><span>Conocimientos</span></strong></p>
<p>- Experiencia con Java en entornos web</p>
<p>- Spring Framework</p>
<p>- Spring MVC</p>
<p>- Spring Data JPA</p>
<p>Bases de datos SQL y NoSQL</p>
<p>Thymeleaf</p>
<p>RESTful web service</p>',
5);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`)
VALUES (
7,'Programador Java J2EE','Buscamos programador/a Java','2021-02-19',22000,'Aprobada',1,'2zhedj-karma.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>No requerida</p>
<p><strong><span>Conocimientos</span></strong></p>
<p>- Experiencia con Java en entornos web</p>
<p>- Spring Framework</p>
<p>- Spring MVC</p>
<p>- Spring Data JPA</p>
<p>Bases de datos SQL y NoSQL</p>
<p>Thymeleaf</p>
<p>RESTful web service</p>',
9);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`)
VALUES (
8,'Desarrollador Web','Buscamos programador/a con experiencia en desarrollo web','2021-01-29',28000,'Aprobada',1,'0xgqx0-veritas.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>2 años de experiencia como desarrollador web</p>
<p><strong><span>Conocimientos</span></strong></p>
<p>- PHP</p>
<p>- Symphony</p>
<p>- Laravel</p>
<p>- JavaScript/TypeSript</p>
<p>- Node.js</p>
<p>- Express</p>
<p>- Angular</p>
<p>- HTML5, CSS3</p>
<p>- Bootstrap</p>',
2);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`) 
VALUES (
9,'Desarrollador Full-Stack','Buscamos desarrollador/a full-stack','2021-02-20',30000,'Aprobada',1,'xp5bny-zen.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>2 años de experiencia como desarrollador full-stack</p>
<p><strong><span>Conocimientos</span></strong></p>
<p>- Git</p>
<p>- Java</p>
<p>- JavaScript/TypeScript</p>
<p>- Angular, React</p>
<p>- Node.js</p>
<p>- Spring Boot</p>
<p>- Spring MVC</p>
<p>- Spring Data JPA</p>
<p>- Bootstrap, HTML5, CSS3</p>
<p>- Bases de datos SQL y NoSQL (MySQL y MongoDB)</p>',
5);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`)
VALUES (
10,'Programador Android/IOS','Buscamos programador/a con experiencia en desarrollo Android/IOS','2021-02-19',28000,'Aprobada',1,'whn7h5-nako.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>2 años de experiencia como desarrollador Android/IOS</p>
<p><strong><span>Conocimientos</span></strong></p>
<p>- Java, Kotlin</p>
<p>- Test unitarios</p>
<p>- HTML5, CSS3</p>
<p>Aplicaciones móviles</p>',
8);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`)
VALUES (
11,'Arquitecto Big Data','Buscamos arquitecto/a Big Data con experiencia','2021-02-20',34000,'Aprobada',1,'nd3dfv-karma.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior DAM o DAW</p>
<p><strong>Nivel de experiencia</strong></p>
<p>4 años de experiencia como arquitecto Big Data</p>
<p><strong><span>Conocimientos</span></strong></p>
<p>- Git</p>
<p>- Python</p>
<p>- Scala</p>
<p>- Hadoop</p>
<p>- Spark</p>
<p>- HortonWorks</p>
<p>- Jira</p>
<p>- Bases de Datos SQL y NoSQL</p>
<p>- Oracle, MongoDB, Cassandra</p>',
9);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`) 
VALUES (
12,'Consultor Sap','Buscamos consultor Sap MM','2021-01-29',34000,'Aprobada',1,'ht10l9-arkan.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior</p>
<p><strong>Nivel de experiencia</strong></p>
<p>2 años de experiencia</p>
<p><strong><span>Conocimientos</span></strong></p>
<p>- Integración de sistemas SAP, ERP, modulos (MM, FICO, SD), S/4 Hana Cloud</p>',
10);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`) 
VALUES (
13,'Administrador de Redes','Buscamos administrador/a de redes para monitorizar y administrar servidores Linux','2021-02-22',23000,'Aprobada',1,'9ne0h5-konnen.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior</p>
<p><strong>Nivel de experiencia</strong></p>
<p>2 años de experiencia como administrador de Redes y sistemas Linux Red Hat</p>
<p><strong><span>Conocimientos</span></strong></p>
<p>- Sistemas Linux Red Hat, configuración, integración y administración de redes</p>
<p>- LAN, VLAN, seguridad, routing, DNS, DHCP, VPN, monitorización</p>',
12);

INSERT INTO `vacantes` (`id`,`nombre`,`descripcion`,`fecha`,`salario`,`estatus`,`destacada`,`imagen`,`detalles`,`idCategoria`)
VALUES (
14,'Administrador de Bases de Datos','Buscamos administrador de bases de datos SQL, NoSQL, MongoDB, Cassandra, Oracle, MySQL, PostgreSQL','2021-02-21',26000,'Aprobada',1,'fgwlv1-neotech.png',
'<p><strong>Estudios mínimos</strong></p>
<p>Ciclo Formativo Grado Superior</p>
<p><strong>Nivel de experiencia</strong></p>
<p>Experiencia como administrador de bases de datos</p>
<p><strong><span>Conocimientos</span></strong></p>
<p>- Experiencia en configuración, integración y administración de bases de datos</p>
<p>- Bases de Datos SQL y NoSQL, Oracle, MySQL, PostgreSQL, MongoDB, Cassandra</p>',
13);

-- Insertar Perfiles

INSERT INTO `perfiles` (`id`,`perfil`) VALUES (1,'Supervisor');
INSERT INTO `perfiles` (`id`,`perfil`) VALUES (2,'Administrador');
INSERT INTO `perfiles` (`id`,`perfil`) VALUES (3,'Usuario');

-- Insertar Usuarios

-- Insertar nuestros usuarios

INSERT INTO `usuarios` (`nombre`, `email`, `estatus`, `username`, `password`, `fecha_registro`) 
values ('Administrador', 'administrador@gmail.com', 1, 'administrador', '$2a$10$k7Un1BlwCegnU0SwdL8a1uUsSq7xPxd9nudZqfrnnCPN8UU8brrra', '2021-02-20');
INSERT INTO `usuarios` (`nombre`, `email`, `estatus`, `username`, `password`, `fecha_registro`) 
values ('Supervisor', 'supervisor@gmail.com', 1, 'supervisor', '$2a$10$Fj7UT9zRkADh7VEQQcJaKOvUnloWmmk4aI.ntpwAN6QxYpTHPbPP2', '2021-02-20');
INSERT INTO `usuarios` (`nombre`, `email`, `estatus`, `username`, `password`, `fecha_registro`) 
values ('Usuario', 'usuario@gmail.com', 1, 'usuario', '$2a$10$mxAnT412v0TB0hNM4lCNCOX7kITKi7wTUktXFRRvC6mbRbOOExcNi', '2021-02-20');

-- Insertar/asignar roles a nuestros usuarios.
INSERT INTO `perfiles_usuarios` VALUES (1,2);
INSERT INTO `perfiles_usuarios` VALUES (2,1);
INSERT INTO `perfiles_usuarios` VALUES (3,3);

```

## Subida de archivos

Lo mejor en el diseño de aplicaciones Web es no guardar los archivos en un carpeta relativa a la aplicación. Es mejor crear una carpeta fuera del contexto de la aplicación web y tener una mejor administración de estos recursos.  

En este caso los archivos se guardan en una carpeta relativa a la aplicación y Eclipse no reconoce automáticamente si se han creado archivos o carpetas dentro del proyecto que no hayan sido creadas por el IDE.

Para solucionar este problema y refrescar los directorios automáticamente hay que hacer la siguiente configuración:

1. Accede al menú: Window/Preferences

2. En el árbol de elementos que aparece a la izquierda, accede a General/Workspace

3. Ahí encontrarás la casilla **Refresh using native hooks or polling** que aparece desactivada por defecto. Actívala.

!["](/src/main/resources/static/images/refrescar-directorios-eclipse.png)

## Licencia

__GNU GENERAL PUBLIC LICENSE__, si desea saber más, visite el fichero LICENSE