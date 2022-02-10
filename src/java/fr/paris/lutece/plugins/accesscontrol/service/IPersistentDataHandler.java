package fr.paris.lutece.plugins.accesscontrol.service;

import java.io.Serializable;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;

public interface IPersistentDataHandler
{
    
    void handlePersistentData( AccessController controller, AccessControlSessionData sessionData, Serializable data, Object destination );
    
    /**
     * Get the bean name of the handler
     * @return
     */
    String getBeanName( );
    
    /**
     * Get the handler name
     * @param locale
     * @return
     */
    String getHandlerName( Locale locale );
    
    /**
     * Get the html for the config of the handler
     * @param locale
     * @param idConfig
     * @return
     */
    String getDataHandlerConfigForm( Locale locale, int idConfig );
    
    /**
     * Save the config of the handler
     * @param request
     * @param idConfig
     */
    void doSaveConfig( HttpServletRequest request, int idConfig );
    
    /**
     * Delete the config of the handler
     * @param idConfig
     */
    void doDeleteConfig( int idConfig );
}
