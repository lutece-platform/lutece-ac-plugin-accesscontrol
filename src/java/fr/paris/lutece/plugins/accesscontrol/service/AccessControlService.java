package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

/**
 * Implementation of {@link IAccessControlService}
 *
 */
public class AccessControlService implements IAccessControlService
{
    public static final String BEAN_NAME = "accesscontrol.accessControlService";
    
    @Override
    public ReferenceList createAccessControllerReferenceList( Locale locale )
    {
        ReferenceList list = new ReferenceList( );
        for ( IAccessControllerType controller : SpringContextService.getBeansOfType( IAccessControllerType.class ) )
        {
            list.addItem( controller.getBeanName( ), controller.getTitle( locale ) );
        }
        return list;
    }

}
