package fr.paris.lutece.plugins.accesscontrol.service;

/**
 * Access Controller type that persists data in the AccessControlSessionData
 *
 */
public interface IPersistentAccessControllerType
{
    IPersistentDataHandler getIPersistentDataHandler( int configId );
}
