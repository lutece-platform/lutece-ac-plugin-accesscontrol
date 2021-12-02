package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

import fr.paris.lutece.portal.service.i18n.I18nService;

public class MockTempAccessControllerType implements IAccessControllerType
{
    private static final String BEAN_NAME = "accesscontrol.mockTempAccessControllerType";
    private static final String TITLE_KEY = "accesscontrol.controller.mockTempAccessController.name";
    
    
    @Override
    public String getBeanName( )
    {
        return BEAN_NAME;
    }
    
    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( TITLE_KEY, locale );
    }
}
