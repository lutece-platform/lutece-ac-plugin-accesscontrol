package fr.paris.lutece.plugins.accesscontrol.service;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.config.IAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.TosAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.TosAccessControllerConfigDAO;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

public class TosAccessControllerTypeTest extends LuteceTestCase
{
    private static final String ERROR_MSG = "Error Message";
    private IAccessControllerConfigDAO<TosAccessControllerConfig> _dao = new TosAccessControllerConfigDAO( );
    
    public void testValidate( )
    {
        TosAccessControllerConfig config = new TosAccessControllerConfig( );
        config.setErrorMessage( ERROR_MSG );
        _dao.insert( config );
        
        AccessController accessController = new AccessController( );
        accessController.setId( config.getIdAccessController( ) );

        TosAccessControllerType controller = SpringContextService.getBean( TosAccessControllerType.BEAN_NAME );
        
        MockHttpServletRequest requestKO = new MockHttpServletRequest( );
        MockHttpServletRequest requestOK = new MockHttpServletRequest( );
        requestOK.addParameter( "check", "1" );
        
        assertNull( controller.validate( requestOK, accessController ) );
        assertEquals( ERROR_MSG, controller.validate( requestKO, accessController ) );
        
        controller.deleteConfig( accessController.getId( ) );
    }
    
}
