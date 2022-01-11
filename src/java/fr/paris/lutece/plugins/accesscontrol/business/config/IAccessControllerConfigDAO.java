package fr.paris.lutece.plugins.accesscontrol.business.config;

/**
 *  Global Dao interface for {@link IAccessControllerConfig}
 */
public interface IAccessControllerConfigDAO<C extends IAccessControllerConfig>
{

    /**
     * Insert a new AccessController config
     * 
     * @param config
     *            the AccessController config
     */
    void insert( C config );

    /**
     * Update a AccessController config
     * 
     * @param config
     *            the AccessController config
     */
    void store( C config );

    /**
     * Load a AccessController config
     * 
     * @param nIdAccessController
     *            the AccessController id
     * @return the AccessController config
     */
    C load( int nIdAccessController );

    /**
     * Delete the AccessController config
     * 
     * @param nIdAccessController
     *            the id of the AccessController
     */
    void delete( int nIdAccessController );
}
