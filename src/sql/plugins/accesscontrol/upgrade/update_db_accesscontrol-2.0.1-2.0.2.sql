-- liquibase formatted sql
-- changeset accesscontrol:update_db_accesscontrol-2.0.1-2.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- LUT-34233 : the access controls are managed through RBAC (resource type ACCESCONTROL_ACCESCONTROL), now checked by the server (LUT-34234):
-- the role accesscontrol_manager gets every permission on every access control.
-- An upgrade gives no right to anybody (same policy as forms LUT-33778): on an existing site, an administrator assigns the role
-- accesscontrol_manager to the users who manage the access controls, otherwise nobody can manage them any more. A fresh install gives
-- the role to admin and the permissions to super_admin (changeset init_core_accesscontrol.sql-rev2).
DELETE FROM core_admin_role WHERE role_key = 'accesscontrol_manager';
INSERT INTO core_admin_role (role_key,role_description) VALUES ('accesscontrol_manager','ACCESSCONTROL - Gestion des contrôles d''accès');
DELETE FROM core_admin_role_resource WHERE role_key = 'accesscontrol_manager' AND resource_type = 'ACCESCONTROL_ACCESCONTROL';
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('accesscontrol_manager','ACCESCONTROL_ACCESCONTROL','*','*');
