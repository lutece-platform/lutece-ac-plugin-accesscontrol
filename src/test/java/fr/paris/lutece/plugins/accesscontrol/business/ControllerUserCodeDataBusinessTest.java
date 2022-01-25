package fr.paris.lutece.plugins.accesscontrol.business;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import fr.paris.lutece.test.LuteceTestCase;

public class ControllerUserCodeDataBusinessTest extends LuteceTestCase
{
    private static final String USER_1 = "user1";
    private static final String USER_2 = "user2";
    private static final String CODE_1 = "code1";
    private static final String CODE_2 = "code2";
    private static final Date DATE_VALID = Date.valueOf( LocalDate.now( ).plusDays( 2 ) );
    private static final Date DATE_INVALID = Date.valueOf(LocalDate.now( ).minusDays( 2 ) );
    private static final int ID_ACCESS_CONTROL_1 = 1;
    private static final int ID_ACCESS_CONTROL_2 = 2;

    public void testCRUD( )
    {
        ControllerUserCodeData data = new ControllerUserCodeData( );
        data.setUser( USER_1 );
        data.setCode( CODE_1 );
        data.setIdAccessControl( ID_ACCESS_CONTROL_1 );
        data.setValidityDate( DATE_VALID );
        
        ControllerUserCodeDataHome.create( data );
        
        ControllerUserCodeData loaded = ControllerUserCodeDataHome.findByPrimaryKey( USER_1 );
        assertNotNull( loaded );
        assertEquals( data.getUser( ), loaded.getUser( ) );
        assertEquals( data.getCode( ), loaded.getCode( ) );
        
        List<ControllerUserCodeData> invalidList = ControllerUserCodeDataHome.findAllInvalidDate( );
        assertEquals( 0, invalidList.size( ) );
        
        assertTrue( ControllerUserCodeDataHome.checkUserCodeValid( USER_1, CODE_1, ID_ACCESS_CONTROL_1 ) );
        assertFalse( ControllerUserCodeDataHome.checkUserCodeValid( USER_1, CODE_1, ID_ACCESS_CONTROL_2 ) );
        assertFalse( ControllerUserCodeDataHome.checkUserCodeValid( USER_2, CODE_2, ID_ACCESS_CONTROL_1 ) );
        
        ControllerUserCodeDataHome.remove( USER_1, ID_ACCESS_CONTROL_1 );
        loaded = ControllerUserCodeDataHome.findByPrimaryKey( USER_1 );
        assertNull( loaded );
        
        data = new ControllerUserCodeData( );
        data.setUser( USER_1 );
        data.setCode( CODE_1 );
        data.setIdAccessControl( ID_ACCESS_CONTROL_1 );
        data.setValidityDate( DATE_INVALID );
        
        ControllerUserCodeDataHome.create( data );
        assertFalse( ControllerUserCodeDataHome.checkUserCodeValid( USER_1, CODE_1, ID_ACCESS_CONTROL_1 ) );
        
        invalidList = ControllerUserCodeDataHome.findAllInvalidDate( );
        assertEquals( 1, invalidList.size( ) );
        
        ControllerUserCodeDataHome.removeByAccessControl( ID_ACCESS_CONTROL_1 );
        loaded = ControllerUserCodeDataHome.findByPrimaryKey( USER_1 );
        assertNull( loaded );
    }
}

