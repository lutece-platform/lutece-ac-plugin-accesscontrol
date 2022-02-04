package fr.paris.lutece.plugins.accesscontrol.service;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.config.CommentAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.CommentAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.IAccessControllerConfigDAO;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

public class CommentAccessControllerTypeTest extends LuteceTestCase
{
    private static final String ERROR_MSG = "Error Message";
    private IAccessControllerConfigDAO<CommentAccessControllerConfig> _dao = new CommentAccessControllerConfigDAO( );
    
    public void testValidate( ) throws UserNotSignedException
    {
        CommentAccessControllerConfig config = new CommentAccessControllerConfig( );
        config.setErrorMessage( ERROR_MSG );
        _dao.insert( config );
        
        AccessController accessController = new AccessController( );
        accessController.setId( config.getIdAccessController( ) );

        CommentAccessControllerType controller = SpringContextService.getBean( CommentAccessControllerType.BEAN_NAME );
        
        MockHttpServletRequest requestOK = new MockHttpServletRequest( );
        
        assertNull( controller.validate( requestOK, accessController ) );
        
        controller.deleteConfig( accessController.getId( ) );
    }
    
}
