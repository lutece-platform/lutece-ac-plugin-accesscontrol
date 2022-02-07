package fr.paris.lutece.plugins.accesscontrol.service.rbac;

import java.util.Locale;

import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import fr.paris.lutece.plugins.accesscontrol.util.AccessControlUtils;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

public class AccessControlResourceIdService extends ResourceIdService
{
    public static final String PERMISSION_CREATE = "CREATE";
    public static final String PERMISSION_DELETE = "DELETE";
    public static final String PERMISSION_MODIFY = "MODIFY";
    public static final String PERMISSION_ENABLE = "ENABLE";

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "accesscontrol.permission.label.resourceType";
    private static final String PROPERTY_LABEL_CREATE = "accesscontrol.permission.label.create";
    private static final String PROPERTY_LABEL_DELETE = "accesscontrol.permission.label.delete";
    private static final String PROPERTY_LABEL_MODIFY = "accesscontrol.permission.label.modify";
    private static final String PROPERTY_LABEL_ENABLE = "accesscontrol.permission.label.enable";
    
    /** Creates a new instance of DocumentTypeResourceIdService */
    public AccessControlResourceIdService( )
    {
        super( );
        setPluginName( AccessControlUtils.PLUGIN_NAME );
    }
    
    @Override
    public void register( )
    {
        ResourceType resourceType = new ResourceType( );
        resourceType.setResourceIdServiceClass( AccessControlResourceIdService.class.getName( ) );
        resourceType.setPluginName( AccessControlUtils.PLUGIN_NAME );
        resourceType.setResourceTypeKey( AccessControl.RESOURCE_TYPE );
        resourceType.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission permission = new Permission( );
        permission.setPermissionKey( PERMISSION_CREATE );
        permission.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        resourceType.registerPermission( permission );

        permission = new Permission( );
        permission.setPermissionKey( PERMISSION_MODIFY );
        permission.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        resourceType.registerPermission( permission );

        permission = new Permission( );
        permission.setPermissionKey( PERMISSION_DELETE );
        permission.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        resourceType.registerPermission( permission );
        
        permission = new Permission( );
        permission.setPermissionKey( PERMISSION_ENABLE );
        permission.setPermissionTitleKey( PROPERTY_LABEL_ENABLE );
        resourceType.registerPermission( permission );

        ResourceTypeManager.registerResourceType( resourceType );
    }
    
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        return AccessControlHome.getAccessControlsReferenceList( );
    }
    
    @Override
    public String getTitle( String strId, Locale locale )
    {
        int nIdAccessControl = -1;

        try
        {
            nIdAccessControl = Integer.parseInt( strId );
        }
        catch( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }
        
        AccessControl accessControl = AccessControlHome.findByPrimaryKey( nIdAccessControl );
        return accessControl.getName( );
    }
}
