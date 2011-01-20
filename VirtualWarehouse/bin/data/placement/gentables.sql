CREATE DATABASE vwburr;

CREATE TABLE MODEL(
	id int not null AUTO_INCREMENT,
	typeid varchar(20) not null,
	name varchar(30) not null,
	fileName varchar(40) not null,
	folderName varchar(50) not null,
	format varchar(10) not null,
	translationX int not null,
	translationY int not null,
	translationZ int not null,
	scale int,
	rotationX int not null,
	rotationY int not null,
	rotationZ int not null,
	rotationW int,
	PRIMARY KEY(id))
	ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS WAREHOUSE(
	warehouseid int not null AUTO_INCREMENT,
	id int,
	primary key(warehouseid),
	foreign key(id) references MODEL(id)
	on delete cascade
	on update cascade)
	ENGINE = InnoDB;
	
CREATE TABLE IF NOT EXISTS RACK(
	rackid int not null,
	rackaisle varchar(3) not null,
	binNumber1 varchar(5) not null,
	binNumber2 varchar(5) not null,
	id int not null,
	primary key(rackid),
	foreign key(id) references MODEL(id)
	on delete cascade
	on update cascade)
	ENGINE = InnoDB;
	
CREATE TABLE SHELF(
	shelfid int not null,
	height int,
	rackid int not null,
	primary key(shelfid),
	foreign key(rackid) references RACK(rackid)
	on delete cascade
	on update cascade)
	ENGINE = InnoDB;
	
CREATE TABLE IF NOT EXISTS PALLET(
	positionX int not null,
	positionY int not null,
	textureName varchar(30) not null,
	textureLocation varchar(50) not null,
	shelfid int not null,
	foreign key (shelfid) references SHELF(shelfid)
	on delete cascade
	on update cascade,
	primary key(shelfid, positionX, positionY))
	ENGINE = InnoDB;
	