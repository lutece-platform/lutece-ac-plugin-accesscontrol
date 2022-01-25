package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControllerHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

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
    
    @Override
    public void deleteAccessController( int idControlType )
    {
        AccessController accessController = AccessControllerHome.findByPrimaryKey( idControlType );
        if ( accessController != null )
        {
            IAccessControllerType controller = SpringContextService.getBean( accessController.getType( ) );
            if ( controller.hasConfig( ) )
            {
                controller.deleteConfig( idControlType );
            }
            AccessControllerHome.remove( idControlType );
        }
    }
}
