package fr.paris.lutece.plugins.accesscontrol.util;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

/**
 * Util class for plugin accessControl
 *
 */
public final class AccessControlUtils
{
    public static final String PLUGIN_NAME = "accesscontrol";
    
    private AccessControlUtils( )
    {
    }
    
    /**
     * Get the accessControl plugin
     * 
     * @return the accessControl plugin
     */
    public static Plugin getPlugin( )
    {
        return PluginService.getPlugin( PLUGIN_NAME );
    }
}
