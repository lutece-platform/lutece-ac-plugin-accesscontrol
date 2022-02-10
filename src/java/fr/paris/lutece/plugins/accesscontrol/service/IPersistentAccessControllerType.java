package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;

/**
 * Access Controller type that persists data in the AccessControlSessionData
 *
 */
public interface IPersistentAccessControllerType
{

    /**
     * Persist the data in the AccessControlSessionData
     * @param request
     * @param locale
     * @param controller
     */
    void persistDataToSession( AccessControlSessionData sessionData, HttpServletRequest request, Locale locale, int idController );
    
    /**
     * Apply the session data to the destination object
     * @param controller
     * @param sessionData
     * @param destination
     */
    void applyPersistentData( AccessController controller, AccessControlSessionData sessionData, Object destination ); 
    
    /**
     * Delete the config of the dataHandler
     * @param idConfig
     */
    void doDeleteDataHandlerConfig( int idConfig );
}
