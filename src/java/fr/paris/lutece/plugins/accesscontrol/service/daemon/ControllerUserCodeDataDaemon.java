package fr.paris.lutece.plugins.accesscontrol.service.daemon;

import java.util.List;

import fr.paris.lutece.plugins.accesscontrol.business.ControllerUserCodeData;
import fr.paris.lutece.plugins.accesscontrol.business.ControllerUserCodeDataHome;
import fr.paris.lutece.portal.service.daemon.Daemon;

/**
 * Daemon that deletes expired user codes
 */
public class ControllerUserCodeDataDaemon extends Daemon
{

    @Override
    public void run( )
    {
        List<ControllerUserCodeData> invalidList = ControllerUserCodeDataHome.findAllInvalidDate( );
        for ( ControllerUserCodeData data : invalidList )
        {
            ControllerUserCodeDataHome.remove( data.getUser( ), data.getIdAccessControl( ) );
        }
        
    }

}
