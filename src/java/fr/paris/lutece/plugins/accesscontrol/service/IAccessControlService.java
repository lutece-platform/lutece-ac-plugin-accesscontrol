package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.accesscontrol.AccessControl;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlFilter;
import fr.paris.lutece.util.ReferenceList;

public interface IAccessControlService
{

    /**
     * create a {@link ReferenceList} containing available {@link IAccessControllerType}
     * 
     * @param locale
     * @return
     */
    ReferenceList createAccessControllerReferenceList( Locale locale );
    
    /**
     * Delete the AccessController
     * 
     * @param idControlType
     */
    void deleteAccessController( int idControlType );
    
    /**
     * return a referencelist which contains a list enabled AccessControl.
     *
     * @param user
     *            the User
     * @param locale
     *            the locale
     * @return a referencelist which contains a list enabled AccessControl
     */
    ReferenceList getAccessControlsEnabled( User user, Locale locale );
    
    /**
     * return the AccessControl list by filter
     * 
     * @param filter
     *            the filter
     * @return the AccessControl list
     */
    List<AccessControl> getListAccessControlsByFilter( AccessControlFilter filter );
}
