package fr.paris.lutece.plugins.accesscontrol.service;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControllerHome;
import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerData;
import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerDataHome;
import fr.paris.lutece.plugins.accesscontrol.business.config.IAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfigDAO;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

public class UserCodeAccessControllerTypeTest extends LuteceTestCase
{
    private static final int ID_ACCESS_CONTROL = 1;
    private static final String USER = "user";
    private static final String CODE_OK = "codeOK";
    private static final String CODE_KO = "codeKO";
    private static final String ERROR_MSG = "Error Message";
    
    private IAccessControllerConfigDAO<UserCodeAccessControllerConfig> _dao = new UserCodeAccessControllerConfigDAO( );
    
    public void testValidate( ) throws UserNotSignedException
    {
        AccessController accessController = new AccessController( );
        accessController.setIdAccesscontrol( ID_ACCESS_CONTROL );
        AccessControllerHome.create( accessController );
        
        UserCodeAccessControllerConfig config = new UserCodeAccessControllerConfig( );
        config.setIdAccessController( accessController.getId( ) );
        config.setErrorMessage( ERROR_MSG );
        _dao.insert( config );
        
        
        UserCodeControllerData data = new UserCodeControllerData( );
        data.setIdAccessControl( ID_ACCESS_CONTROL );
        data.setUser( USER );
        data.setCode( CODE_OK );
        data.setValidityDate( Date.valueOf( LocalDate.now( ).plusDays( 1 ) ) );
        
        UserCodeControllerDataHome.create( data );

        UserCodeAccessControllerType controller = SpringContextService.getBean( UserCodeAccessControllerType.BEAN_NAME );
        
        MockHttpServletRequest requestKO = new MockHttpServletRequest( );
        requestKO.addParameter( "code", CODE_KO );
        requestKO.addParameter( "userId", USER );
        
        MockHttpServletRequest requestOK = new MockHttpServletRequest( );
        requestOK.addParameter( "code", CODE_OK );
        requestOK.addParameter( "userId", USER );
        
        assertNull( controller.validate( requestOK, accessController ) );
        assertEquals( ERROR_MSG, controller.validate( requestKO, accessController ) );
        
        controller.deleteConfig( accessController.getId( ) );
        UserCodeControllerDataHome.remove( USER, ID_ACCESS_CONTROL );
        AccessControllerHome.remove( accessController.getId( ) );
    }
}
