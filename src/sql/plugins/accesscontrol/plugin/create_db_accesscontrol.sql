--
-- Structure for table accesscontrol_accesscontrol
--
DROP TABLE IF EXISTS accesscontrol_accesscontrol;
CREATE TABLE accesscontrol_accesscontrol (
	id_access_control int AUTO_INCREMENT,
	name varchar(255) default '',
	description long varchar,
	creation_date date DEFAULT CURRENT_TIMESTAMP NOT NULL,
	is_enabled SMALLINT DEFAULT 0 NOT NULL,
	workgroup_key varchar(255) default '',
	PRIMARY KEY (id_access_control)
);

--
-- Structure for table accesscontrol_accesscontroller
--
DROP TABLE IF EXISTS accesscontrol_accesscontroller;
CREATE TABLE accesscontrol_accesscontroller (
	id_access_controller int AUTO_INCREMENT,
	type varchar(255),
	id_access_control int,
	bool_cond varchar(50),
	display_order int,
	PRIMARY KEY (id_access_controller)
);
CREATE INDEX index_ac_accontroller_id ON accesscontrol_accesscontroller ( id_access_control );

--
-- Structure for table accesscontrol_resource_accesscontrol
--
DROP TABLE IF EXISTS accesscontrol_resource_accesscontrol;
CREATE TABLE accesscontrol_resource_accesscontrol (
	id_resource int,
	resource_type varchar(255),
	id_access_control int,
	PRIMARY KEY( id_resource, resource_type, id_access_control )
);
CREATE INDEX index_ac_accontroller_resource ON accesscontrol_resource_accesscontrol ( id_resource, resource_type );

--
-- Structure for table accesscontrol_controller_comment_config
--
DROP TABLE IF EXISTS accesscontrol_controller_comment_config;
CREATE TABLE accesscontrol_controller_comment_config (
	id_access_control int,
	comment long varchar,
	PRIMARY KEY( id_access_control )
);
