package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

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
    void persistData( AccessControlSessionData sessionData, HttpServletRequest request, Locale locale, int idController );
    
    IPersistentDataHandler getIPersistentDataHandler( int configId );
}
