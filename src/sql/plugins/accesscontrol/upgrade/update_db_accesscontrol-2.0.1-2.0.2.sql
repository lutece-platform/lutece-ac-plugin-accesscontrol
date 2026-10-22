-- liquibase formatted sql
-- changeset accesscontrol:update_db_accesscontrol-2.0.1-2.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- LUT-34233 : the access controls are managed through RBAC (resource type ACCESCONTROL_ACCESCONTROL), now checked by the server (LUT-34234):
-- the role accesscontrol_manager and the super_admin role of the core (held by admin) get every permission on every access control.
-- Same rows as the changeset init_core_accesscontrol.sql-rev2 of a fresh install.
DELETE FROM core_admin_role WHERE role_key = 'accesscontrol_manager';
INSERT INTO core_admin_role (role_key,role_description) VALUES ('accesscontrol_manager','ACCESSCONTROL - Gestion des contrôles d''accès');
DELETE FROM core_user_role WHERE role_key = 'accesscontrol_manager' AND id_user = 1;
INSERT INTO core_user_role (role_key,id_user) VALUES ('accesscontrol_manager',1);
DELETE FROM core_admin_role_resource WHERE role_key IN ('accesscontrol_manager','super_admin') AND resource_type = 'ACCESCONTROL_ACCESCONTROL';
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('accesscontrol_manager','ACCESCONTROL_ACCESCONTROL','*','*');
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('super_admin','ACCESCONTROL_ACCESCONTROL','*','*');
