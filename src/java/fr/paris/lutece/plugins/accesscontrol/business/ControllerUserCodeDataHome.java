package fr.paris.lutece.plugins.accesscontrol.business;

import java.util.List;

import fr.paris.lutece.plugins.accesscontrol.util.AccessControlUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public final class ControllerUserCodeDataHome
{

    private static IControllerUserCodeDataDAO _dao = SpringContextService.getBean( "accesscontrol.controllerUserCodeDataDAO" );
    private static Plugin _plugin = AccessControlUtils.getPlugin( );
    
    /**
     * Private constructor - this class need not be instantiated
     */
    private ControllerUserCodeDataHome( )
    {
    }
    
    /**
     * Create an instance of the controllerUserCodeData class
     * 
     * @param controllerUserCodeData
     *            The instance of the ControllerUserCodeData which contains the informations to store
     * @return The instance of controllerUserCodeData which has been created with its primary key.
     */
    public static ControllerUserCodeData create( ControllerUserCodeData controllerUserCodeData )
    {
        _dao.insert( controllerUserCodeData, _plugin );
        return controllerUserCodeData;
    }

    /**
     * Remove the controllerUserCodeData whose identifier is specified in parameter
     * 
     * @param strUser
     *            The controllerUserCodeData user
     */
    public static void remove( String strUser, int idAccessControl )
    {
        _dao.delete( strUser, idAccessControl, _plugin );
    }
    
    /**
     * Remove the controllerUserCodeData whose identifier is specified in parameter
     * 
     * @param strUser
     *            The controllerUserCodeData user
     */
    public static void removeByAccessControl( int idAccessControl )
    {
        _dao.deleteByAccessControl( idAccessControl, _plugin );
    }

    /**
     * Returns an instance of a controllerUserCodeData whose identifier is specified in parameter
     * 
     * @param nKey
     *            The controllerUserCodeData primary key
     * @return an instance of ControllerUserCodeData
     */
    public static ControllerUserCodeData findByPrimaryKey( String strUser )
    {
        return _dao.load( strUser, _plugin );
    }
    
    /**
     * Checks if the pair user/code is valid
     * @param strUser
     * @param strCode
     * @param idAccessControl
     * @return
     */
    public static boolean checkUserCodeValid( String strUser, String strCode, int idAccessControl )
    {
        return _dao.checkUserCode( strUser, strCode, idAccessControl, _plugin );
    }
    
    /**
     * Returns a list of instance of aontrollerUserCodeData
     * 
     * @return an instance of ControllerUserCodeData
     */
    public static List<ControllerUserCodeData> findAll( )
    {
        return _dao.loadAll( _plugin );
    }
    
    /**
     * Returns a list of instance of aontrollerUserCodeData
     * 
     * @return an instance of ControllerUserCodeData
     */
    public static List<ControllerUserCodeData> findAllInvalidDate( )
    {
        return _dao.loadDateInvalid( _plugin );
    }
}
