package fr.paris.lutece.plugins.accesscontrol.service.daemon;

import java.util.List;

import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerData;
import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerDataHome;
import fr.paris.lutece.portal.service.daemon.Daemon;

/**
 * Daemon that deletes expired user codes
 */
public class UserCodeControllerDataDaemon extends Daemon
{

    @Override
    public void run( )
    {
        List<UserCodeControllerData> invalidList = UserCodeControllerDataHome.findAllInvalidDate( );
        for ( UserCodeControllerData data : invalidList )
        {
            UserCodeControllerDataHome.remove( data.getUser( ), data.getIdAccessControl( ) );
        }
        
    }

}
