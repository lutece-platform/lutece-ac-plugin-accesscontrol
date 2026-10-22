-- liquibase formatted sql
-- changeset accesscontrol:init_core_accesscontrol.sql
-- preconditions onFail:MARK_RAN onError:WARN

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'ACCESSCONTROL_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('ACCESSCONTROL_MANAGEMENT','accesscontrol.adminFeature.ManageAccessControl.name',1,'jsp/admin/plugins/accesscontrol/ManageAccessControls.jsp','accesscontrol.adminFeature.ManageAccessControl.description',0,'accesscontrol',NULL,'ti ti-lock-access',NULL,4);

DELETE FROM core_admin_right WHERE id_right = 'USERCODES_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('USERCODES_MANAGEMENT','accesscontrol.adminFeature.ManageUserCodes.name',1,'jsp/admin/plugins/accesscontrol/ManageControllerUserCode.jsp','accesscontrol.adminFeature.ManageUserCodes.description',0,'accesscontrol',NULL,'ti ti-user-scan',NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'ACCESSCONTROL_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('ACCESSCONTROL_MANAGEMENT',1);

DELETE FROM core_user_right WHERE id_right = 'USERCODES_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('USERCODES_MANAGEMENT',1);

-- changeset accesscontrol:init_core_accesscontrol.sql-rev2
-- preconditions onFail:MARK_RAN onError:WARN
-- The access controls are managed through RBAC (resource type ACCESCONTROL_ACCESCONTROL): the role accesscontrol_manager and the super_admin role of
-- the core (held by admin) get every permission on every access control, otherwise no administrator can create, modify, enable or remove one
DELETE FROM core_admin_role WHERE role_key = 'accesscontrol_manager';
INSERT INTO core_admin_role (role_key,role_description) VALUES ('accesscontrol_manager','ACCESSCONTROL - Gestion des contrôles d''accès');
DELETE FROM core_user_role WHERE role_key = 'accesscontrol_manager' AND id_user = 1;
INSERT INTO core_user_role (role_key,id_user) VALUES ('accesscontrol_manager',1);
DELETE FROM core_admin_role_resource WHERE role_key IN ('accesscontrol_manager','super_admin') AND resource_type = 'ACCESCONTROL_ACCESCONTROL';
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('accesscontrol_manager','ACCESCONTROL_ACCESCONTROL','*','*');
INSERT INTO core_admin_role_resource (role_key,resource_type,resource_id,permission) VALUES ('super_admin','ACCESCONTROL_ACCESCONTROL','*','*');
