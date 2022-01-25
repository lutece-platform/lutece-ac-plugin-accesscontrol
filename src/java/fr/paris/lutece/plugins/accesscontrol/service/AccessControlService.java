package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControllerHome;
import fr.paris.lutece.plugins.accesscontrol.business.IAccessControlDAO;
import fr.paris.lutece.portal.business.accesscontrol.AccessControl;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlFilter;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.util.ReferenceList;

public class AccessControlService implements IAccessControlService
{
    public static final String BEAN_NAME = "accesscontrol.accessControlService";
    
    @Inject
    private IAccessControlDAO _accessControlDAO;
    
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
    
    @Override
    public List<AccessControl> getListAccessControlsByFilter( AccessControlFilter filter )
    {
        return _accessControlDAO.selectAccessControlByFilter( filter );
    }
    
    @Override
    public ReferenceList getAccessControlsEnabled( User user, Locale locale )
    {
        AccessControlFilter filter = new AccessControlFilter( );
        filter.setIsEnabled( AccessControlFilter.FILTER_TRUE );
        List<AccessControl> list = getListAccessControlsByFilter( filter );
        
        ReferenceList referenceList = new ReferenceList( );
        referenceList.addItem( -1, "-" );
        for ( AccessControl accessControl : AdminWorkgroupService.getAuthorizedCollection( list, user ) )
        {
            referenceList.addItem( accessControl.getId( ), accessControl.getName( ) );
        }
        return referenceList;
    }
}
