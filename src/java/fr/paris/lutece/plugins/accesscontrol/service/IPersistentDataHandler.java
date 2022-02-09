package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

public interface IPersistentDataHandler
{
    String getBeanName( );
    
    String getHandlerName( Locale locale );
    
    String getDataHandlerConfigForm( Locale locale, int idConfig );
    
    void doSaveConfig( HttpServletRequest request, int idConfig );
    
    void doDeleteConfig( int idConfig );
}
