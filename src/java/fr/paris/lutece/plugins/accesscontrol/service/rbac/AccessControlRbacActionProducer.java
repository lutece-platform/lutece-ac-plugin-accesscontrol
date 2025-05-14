package fr.paris.lutece.plugins.accesscontrol.service.rbac;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class AccessControlRbacActionProducer
{

	@Produces
	@ApplicationScoped
	@Named( "accesscontrol.rbacActionModify" )
	public AccessControlRbacAction produceAccessControlRbacActionModify(
			@ConfigProperty( name = "accesscontrol.rbacActionModify.url"  ) String url,
			@ConfigProperty( name = "accesscontrol.rbacActionModify.nameKey"  ) String nameKey,			
			@ConfigProperty( name = "accesscontrol.rbacActionModify.iconUrl"  ) String iconUrl,
			@ConfigProperty( name = "accesscontrol.rbacActionModify.permission"  ) String permission,
			@ConfigProperty( name = "accesscontrol.rbacActionModify.enabled"  ) boolean enabled )
	{
		return new AccessControlRbacAction( url, nameKey, iconUrl, permission, enabled );
	}
	
	@Produces
	@ApplicationScoped
	@Named( "accesscontrol.rbacActionDelete" )
	public AccessControlRbacAction produceAccessControlRbacActionDelete(
			@ConfigProperty( name = "accesscontrol.rbacActionDelete.url"  ) String url,
			@ConfigProperty( name = "accesscontrol.rbacActionDelete.nameKey"  ) String nameKey,			
			@ConfigProperty( name = "accesscontrol.rbacActionDelete.iconUrl"  ) String iconUrl,
			@ConfigProperty( name = "accesscontrol.rbacActionDelete.permission"  ) String permission,
			@ConfigProperty( name = "accesscontrol.rbacActionDelete.enabled"  ) boolean enabled )
	{
		return new AccessControlRbacAction( url, nameKey, iconUrl, permission, enabled );
	}
	
	@Produces
	@ApplicationScoped
	@Named( "accesscontrol.rbacActionEnable" )
	public AccessControlRbacAction produceAccessControlRbacActionEnable(
			@ConfigProperty( name = "accesscontrol.rbacActionEnable.url"  ) String url,
			@ConfigProperty( name = "accesscontrol.rbacActionEnable.nameKey"  ) String nameKey,			
			@ConfigProperty( name = "accesscontrol.rbacActionEnable.iconUrl"  ) String iconUrl,
			@ConfigProperty( name = "accesscontrol.rbacActionEnable.permission"  ) String permission,
			@ConfigProperty( name = "accesscontrol.rbacActionEnable.enabled"  ) boolean enabled )
	{
		return new AccessControlRbacAction( url, nameKey, iconUrl, permission, enabled );
	}
	
	@Produces
	@ApplicationScoped
	@Named( "accesscontrol.rbacActionDisable" )
	public AccessControlRbacAction produceAccessControlRbacActionDisable(
			@ConfigProperty( name = "accesscontrol.rbacActionDisable.url"  ) String url,
			@ConfigProperty( name = "accesscontrol.rbacActionDisable.nameKey"  ) String nameKey,			
			@ConfigProperty( name = "accesscontrol.rbacActionDisable.iconUrl"  ) String iconUrl,
			@ConfigProperty( name = "accesscontrol.rbacActionDisable.permission"  ) String permission,
			@ConfigProperty( name = "accesscontrol.rbacActionDisable.enabled"  ) boolean enabled )
	{
		return new AccessControlRbacAction( url, nameKey, iconUrl, permission, enabled );
	}
}