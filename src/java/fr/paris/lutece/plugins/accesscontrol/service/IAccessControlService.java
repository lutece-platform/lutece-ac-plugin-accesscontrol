package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.util.ReferenceList;

/**
 * Service for {@link AccessControl}
 */
public interface IAccessControlService
{

    /**
     * create a {@link ReferenceList} containing available {@link IAccessControllerType}
     * @param locale
     * @return
     */
    ReferenceList createAccessControllerReferenceList ( Locale locale );
    
    /**
     * Delete the AccessController
     * @param idControlType
     */
    void deleteAccessController( int idControlType );
}
