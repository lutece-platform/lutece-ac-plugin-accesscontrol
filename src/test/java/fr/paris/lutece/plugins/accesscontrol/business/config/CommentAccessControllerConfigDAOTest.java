package fr.paris.lutece.plugins.accesscontrol.business.config;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

public class CommentAccessControllerConfigDAOTest extends LuteceTestCase
{
    private static final int ID = 1;
    private static final String COMMENT_1 = "comment1";
    private static final String COMMENT_2 = "comment2";
    
    public void testDao( )
    {
        IAccessControllerConfigDAO<CommentAccessControllerConfig> dao = SpringContextService.getBean( CommentAccessControllerConfigDAO.BEAN_NAME );
        
        CommentAccessControllerConfig config = new CommentAccessControllerConfig( );
        config.setIdAccessController( ID );
        config.setComment( COMMENT_1 );
        dao.insert( config );
        
        CommentAccessControllerConfig loaded = dao.load( ID );
        assertEquals( COMMENT_1, loaded.getComment( ) );
        
        config.setComment( COMMENT_2 );
        dao.store( config );
        loaded = dao.load( ID );
        assertEquals( COMMENT_2, loaded.getComment( ) );
        
        dao.delete( ID );
        loaded = dao.load( ID );
        assertNull( loaded );
    }
}
