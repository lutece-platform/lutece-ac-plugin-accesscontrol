package fr.paris.lutece.plugins.accesscontrol.business;

import fr.paris.lutece.plugins.accesscontrol.util.AccessControlUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for AccessControlResource objects
 */
public final class AccessControlResourceHome
{
    private static IAccessControlResourceDAO _dao = SpringContextService.getBean( "accesscontrol.accessControlResourceDAO" );
    private static Plugin _plugin = AccessControlUtils.getPlugin( );
    
    /**
     * Private constructor - this class need not be instantiated
     */
    private AccessControlResourceHome( )
    {
    }
    
    /**
     * Create an instance of the accessControlResource class
     * 
     * @param accessControlResource
     *            The instance of the AccessControlResource which contains the informations to store
     */
    public static void create( AccessControlResource accessControlResource )
    {
        _dao.insert( accessControlResource, _plugin );
    }
    
    /**
     * Delete a record from the table
     * 
     * @param nIdResource
     *            The identifier of the resource
     * @param strResourceType
     *             The type of the resource         
     */
    public static void removeByResource( int nIdResource, String strResourceType )
    {
        _dao.deleteByResource( nIdResource, strResourceType, _plugin );
    }
    
    /**
     * Delete a record from the table
     * 
     * @param nIdResource
     *            The identifier of the access control
     */
    public static void removeByAccessControl( int nIdAccessControl )
    {
        _dao.deleteByAccessControl( nIdAccessControl, _plugin );
    }
    
    /**
     * load a record from the table
     * 
     * @param nIdResource
     *            The identifier of the resource
     * @param strResourceType
     *             The type of the resource         
     */
    public static int findByResource( int nIdResource, String strResourceType )
    {
        return _dao.findByResource( nIdResource, strResourceType, _plugin );
    }
}
