package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

/**
 * Interface for access controllers
 */
public interface IAccessControllerType
{

    /**
     * Gets The bean name of the controllerType
     * @return
     */
    String getBeanName( );
    
    /**
     * Gets The display title of the controllerType
     * @param locale
     * @return
     */
    String getTitle( Locale locale );
    
    /**
     * Indicates if the controllerType needs a config
     * @return
     */
    default boolean hasConfig( )
    {
        return false;
    }
    
    /**
     * Delete the config of the controllerType
     */
    default void deleteConfig( int idControlType )
    {
    }
}
