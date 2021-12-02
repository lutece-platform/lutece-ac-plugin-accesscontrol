package fr.paris.lutece.plugins.accesscontrol.util;

import java.util.Locale;

import fr.paris.lutece.portal.service.i18n.I18nService;

public enum BoolCondition
{
    AND("accesscontrol.modify_accesscontrol.condition.and"),
    OR("accesscontrol.modify_accesscontrol.condition.or");
    
    private String _labelKey;
    
    private BoolCondition( String labelKey )
    {
        _labelKey = labelKey;
    }
    
    public String getLabel( Locale locale )
    {
        return I18nService.getLocalizedString( _labelKey, locale );
    }
}
