/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.accesscontrol.web;

import java.lang.reflect.Field;
import java.sql.Date;

import org.jboss.weld.proxy.WeldClientProxy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.spi.CDI;

/**
 * Two access controls opened in two tabs of the same session
 */
public class AccessControlJspBeanTwoTabsTest extends LuteceTestCase
{
    private AccessControl _enabled;
    private AccessControl _disabled;

    @BeforeEach
    public void createAccessControls( )
    {
        _enabled = create( "tab one", true, new Date( System.currentTimeMillis( ) ) );
        _disabled = create( "tab two", false, Date.valueOf( "2020-01-01" ) );
    }

    @AfterEach
    public void removeAccessControls( )
    {
        AccessControlHome.remove( _enabled.getId( ) );
        AccessControlHome.remove( _disabled.getId( ) );
    }

    /**
     * Tab 1 opens the enabled access control, tab 2 the disabled one, tab 1 saves a new description: only the description changes
     */
    @Test
    public void testModifyKeepsItsOwnState( ) throws Exception
    {
        AccessControlJspBean bean = bean( request( ) );
        MockHttpServletRequest tab1 = request( );
        tab1.addParameter( "id", String.valueOf( _enabled.getId( ) ) );
        bean.getModifyAccessControl( tab1 );
        MockHttpServletRequest tab2 = request( );
        tab2.addParameter( "id", String.valueOf( _disabled.getId( ) ) );
        bean.getModifyAccessControl( tab2 );

        MockHttpServletRequest save = request( );
        save.addParameter( "id", String.valueOf( _enabled.getId( ) ) );
        save.addParameter( "name", _enabled.getName( ) );
        save.addParameter( "description", "changed in tab one" );
        save.addParameter( "workgroup", "all" );
        AccessControl stored = bean.getPostedAccessControl( save );

        assertEquals( "changed in tab one", stored.getDescription( ) );
        assertTrue( stored.isEnabled( ) );
        assertEquals( _enabled.getCreationDate( ).toString( ), stored.getCreationDate( ).toString( ) );
        assertEquals( _enabled.getId( ), stored.getId( ) );
    }

    private static MockHttpServletRequest request( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), AbstractManageAccessControlJspBean.RIGHT_MANAGEACCESSCONTROL );
        return request;
    }

    private static AccessControlJspBean bean( MockHttpServletRequest request ) throws Exception
    {
        AccessControlJspBean bean = CDI.current( ).select( AccessControlJspBean.class ).get( );
        bean.init( request, AbstractManageAccessControlJspBean.RIGHT_MANAGEACCESSCONTROL );
        Field field = MVCAdminJspBean.class.getDeclaredField( "_response" );
        field.setAccessible( true );
        field.set( ( (WeldClientProxy) bean ).getMetadata( ).getContextualInstance( ), new MockHttpServletResponse( ) );
        return bean;
    }

    private static AccessControl create( String strName, boolean bEnabled, Date date )
    {
        AccessControl accessControl = new AccessControl( );
        accessControl.setName( "AccessControlJspBeanTwoTabsTest " + strName );
        accessControl.setDescription( "" );
        accessControl.setWorkgroup( "all" );
        accessControl.setEnabled( bEnabled );
        accessControl.setCreationDate( date );
        return AccessControlHome.create( accessControl );
    }
}
