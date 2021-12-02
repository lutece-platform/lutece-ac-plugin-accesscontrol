package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

/**
 * Interface for access controllers
 */
public interface IAccessControllerType
{

    /**
     * Gets The bean name of the controller
     * @return
     */
    String getBeanName( );
    
    /**
     * Gets The display title of the controller
     * @param locale
     * @return
     */
    String getTitle( Locale locale );
    
    /**
     * Indicates if the controller needs a config
     * @return
     */
    default boolean hasConfig( )
    {
        return false;
    }
}
