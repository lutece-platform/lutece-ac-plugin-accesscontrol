package fr.paris.lutece.plugins.accesscontrol.business;

import fr.paris.lutece.test.LuteceTestCase;

public class AccessControlResourceBusinessTest extends LuteceTestCase
{
    private static final int ID_RESOURCE = 42;
    private static final String RESOURCE_TYPE = "type";
    private static final int ID_ACCESS_CONTROL = 22;
    
    /**
     * test AccessControlResource
     */
    public void testBusiness( )
    {
        AccessControlResource accessControlResource = new AccessControlResource( );
        accessControlResource.setIdResource( ID_RESOURCE );
        accessControlResource.setResourceType( RESOURCE_TYPE );
        accessControlResource.setIdAccessControl( ID_ACCESS_CONTROL );
        
        AccessControlResourceHome.create( accessControlResource );
        
        int idAccessControl = AccessControlResourceHome.findByResource( ID_RESOURCE, RESOURCE_TYPE );
        assertEquals( ID_ACCESS_CONTROL, idAccessControl );
        
        AccessControlResourceHome.removeByResource( ID_RESOURCE, RESOURCE_TYPE );
        idAccessControl = AccessControlResourceHome.findByResource( ID_RESOURCE, RESOURCE_TYPE );
        assertEquals( -1, idAccessControl );
        
        AccessControlResourceHome.create( accessControlResource );
        
        idAccessControl = AccessControlResourceHome.findByResource( ID_RESOURCE, RESOURCE_TYPE );
        assertEquals( ID_ACCESS_CONTROL, idAccessControl );
        
        AccessControlResourceHome.removeByAccessControl( ID_ACCESS_CONTROL );
        idAccessControl = AccessControlResourceHome.findByResource( ID_RESOURCE, RESOURCE_TYPE );
        assertEquals( -1, idAccessControl );
    }
}
