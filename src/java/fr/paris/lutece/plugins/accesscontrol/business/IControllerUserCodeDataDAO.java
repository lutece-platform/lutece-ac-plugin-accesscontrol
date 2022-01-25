package fr.paris.lutece.plugins.accesscontrol.business;

import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * IControllerUserCodeDataDAO Interface
 */
public interface IControllerUserCodeDataDAO
{

    /**
     * Insert a new record in the table.
     * 
     * @param userCode
     *            instance of the ControllerUserCodeData object to insert
     * @param plugin
     *            the Plugin
     */
    void insert( ControllerUserCodeData userCode, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param strUser
     * @param idAccessControl    
     * @param plugin
     *            the Plugin
     */
    void delete( String strUser, int idAccessControl, Plugin plugin );
    
    /**
     * Delete a record from the table
     * 
     * @param idAccessControl
     * @param plugin
     *            the Plugin
     */
    void deleteByAccessControl( int idAccessControl, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param strUser
     *            The identifier of the ControllerUserCodeData
     * @param plugin
     *            the Plugin
     * @return The instance of the ControllerUserCodeData
     */
    ControllerUserCodeData load( String strUser, Plugin plugin );
    
    /**
     * Load the data from the table
     * 
     * @param plugin
     *            the Plugin
     * @return A list of instance of the ControllerUserCodeData
     */
    List<ControllerUserCodeData> loadAll( Plugin plugin );
    
    /**
     * Load the data from the table
     * 
     * @param plugin
     *            the Plugin
     * @return A list of instance of the ControllerUserCodeData
     */
    List<ControllerUserCodeData> loadDateInvalid( Plugin plugin );
    
    /**
     * Checks if the pair user/code exists
     * @param strUser
     * @param strCode
     * @param idAccessControl
     * @param plugin
     * @return
     */
    boolean checkUserCode( String strUser, String strCode, int idAccessControl, Plugin plugin );
    
}
