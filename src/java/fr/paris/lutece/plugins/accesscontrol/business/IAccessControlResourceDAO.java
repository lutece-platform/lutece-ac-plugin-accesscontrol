package fr.paris.lutece.plugins.accesscontrol.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * IAccessControlResourceDAO Interface
 */
public interface IAccessControlResourceDAO
{

    /**
     * Insert a new record in the table.
     * 
     * @param accessControlResource
     *            instance of the AccessControlResource object to insert
     * @param plugin
     *            the Plugin
     */
    void insert( AccessControlResource accessControlResource, Plugin plugin );
    
    /**
     * Delete a record from the table
     * 
     * @param nIdResource
     *            The identifier of the resource
     * @param strResourceType
     *             The type of the resource         
     * @param plugin
     *            the Plugin
     */
    void deleteByResource( int nIdResource, String strResourceType, Plugin plugin );
    
    /**
     * Delete a record from the table
     * 
     * @param nIdResource
     *            The identifier of the access control
     * @param plugin
     *            the Plugin
     */
    void deleteByAccessControl( int nIdAccessControl , Plugin plugin );
    
    /**
     * load a record from the table
     * 
     * @param nIdResource
     *            The identifier of the resource
     * @param strResourceType
     *             The type of the resource         
     * @param plugin
     *            the Plugin
     */
    int findByResource( int nIdResource, String strResourceType, Plugin plugin );
}
