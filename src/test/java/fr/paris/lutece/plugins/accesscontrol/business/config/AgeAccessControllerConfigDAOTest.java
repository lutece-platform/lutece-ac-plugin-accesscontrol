package fr.paris.lutece.plugins.accesscontrol.business.config;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

public class AgeAccessControllerConfigDAOTest extends LuteceTestCase
{
    private static final int ID = 1;
    private static final String COMMENT_1 = "comment1";
    private static final String COMMENT_2 = "comment2";
    private static final String MSG_1 = "msg1";
    private static final String MSG_2 = "msg2";
    private static final int AGE_MIN_1 = 3;
    private static final int AGE_MIN_2 = 4;
    private static final int AGE_MAX_1 = 13;
    private static final int AGE_MAX_2 = 14;
    
    public void testDao( )
    {
        IAccessControllerConfigDAO<AgeAccessControllerConfig> dao = SpringContextService.getBean( AgeAccessControllerConfigDAO.BEAN_NAME );
        
        AgeAccessControllerConfig config = new AgeAccessControllerConfig( );
        config.setIdAccessController( ID );
        config.setComment( COMMENT_1 );
        config.setErrorMessage( MSG_1 );
        config.setAgeMin( AGE_MIN_1 );
        config.setAgeMax( AGE_MAX_1 );
        dao.insert( config );
        
        AgeAccessControllerConfig loaded = dao.load( ID );
        assertEquals( config.getComment( ), loaded.getComment( ) );
        assertEquals( config.getErrorMessage( ), loaded.getErrorMessage( ));
        assertEquals( config.getAgeMin( ), loaded.getAgeMin( ));
        assertEquals( config.getAgeMax( ), loaded.getAgeMax( ));
        
        config.setComment( COMMENT_2 );
        config.setErrorMessage( MSG_2 );
        config.setAgeMin( AGE_MIN_2 );
        config.setAgeMax( AGE_MAX_2 );
        dao.store( config );
        loaded = dao.load( ID );
        assertEquals( config.getComment( ), loaded.getComment( ) );
        assertEquals( config.getErrorMessage( ), loaded.getErrorMessage( ));
        assertEquals( config.getAgeMin( ), loaded.getAgeMin( ));
        assertEquals( config.getAgeMax( ), loaded.getAgeMax( ));
        
        dao.delete( ID );
        loaded = dao.load( ID );
        assertNull( loaded );
    }
}
