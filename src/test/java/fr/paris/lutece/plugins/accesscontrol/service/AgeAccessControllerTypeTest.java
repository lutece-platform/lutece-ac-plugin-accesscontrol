package fr.paris.lutece.plugins.accesscontrol.service;

import java.time.LocalDate;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.config.AgeAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.AgeAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.IAccessControllerConfigDAO;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

public class AgeAccessControllerTypeTest extends LuteceTestCase
{
    private static final String ERROR_MSG = "Error Message";
    private IAccessControllerConfigDAO<AgeAccessControllerConfig> _dao = new AgeAccessControllerConfigDAO( );
    
    public void testValidate( )
    {
        AgeAccessControllerConfig config = new AgeAccessControllerConfig( );
        config.setAgeMin( 10 );
        config.setAgeMax( 20 );
        config.setErrorMessage( ERROR_MSG );
        _dao.insert( config );
        
        AccessController accessController = new AccessController( );
        accessController.setId( config.getIdAccessController( ) );

        AgeAccessControllerType controller = SpringContextService.getBean( AgeAccessControllerType.BEAN_NAME );
        
        LocalDate dateOK = LocalDate.now( ).minusYears( 15 );
        MockHttpServletRequest requestOK = new MockHttpServletRequest( );
        requestOK.addParameter( "birth_date", dateOK.toString( ) );
        
        LocalDate dateKO1 = LocalDate.now( ).minusYears( 9 );
        MockHttpServletRequest requestKO1 = new MockHttpServletRequest( );
        requestOK.addParameter( "birth_date", dateKO1.toString( ) );
        
        LocalDate dateKO2 = LocalDate.now( ).minusYears( 21 );
        MockHttpServletRequest requestKO2 = new MockHttpServletRequest( );
        requestOK.addParameter( "birth_date", dateKO2.toString( ) );
        
        assertNull( controller.validate( requestOK, accessController ) );
        assertEquals( ERROR_MSG, controller.validate( requestKO1, accessController ) );
        assertEquals( ERROR_MSG, controller.validate( requestKO2, accessController ) );
        
        controller.deleteConfig( accessController.getId( ) );
    }
    
}
