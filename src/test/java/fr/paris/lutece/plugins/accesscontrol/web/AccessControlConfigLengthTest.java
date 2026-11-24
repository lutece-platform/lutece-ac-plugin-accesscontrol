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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.lang.reflect.Field;
import java.sql.Date;

import org.jboss.weld.proxy.WeldClientProxy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControllerHome;
import fr.paris.lutece.plugins.accesscontrol.business.config.TosAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.TosAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.service.TosAccessControllerType;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.spi.CDI;

/**
 * An error message longer than its column is refused with a message, not an SQL error
 */
public class AccessControlConfigLengthTest extends LuteceTestCase
{
    private AccessControl _accessControl;
    private AccessController _controller;

    @BeforeEach
    public void createController( )
    {
        _accessControl = new AccessControl( );
        _accessControl.setName( "AccessControlConfigLengthTest" );
        _accessControl.setDescription( "" );
        _accessControl.setWorkgroup( "all" );
        _accessControl.setCreationDate( new Date( System.currentTimeMillis( ) ) );
        AccessControlHome.create( _accessControl );
        _controller = new AccessController( );
        _controller.setIdAccesscontrol( _accessControl.getId( ) );
        _controller.setType( TosAccessControllerType.BEAN_NAME );
        _controller.setBoolCond( "AND" );
        _controller.setOrder( 1 );
        AccessControllerHome.create( _controller );
        TosAccessControllerConfig config = new TosAccessControllerConfig( );
        config.setIdAccessController( _controller.getId( ) );
        config.setErrorMessage( "short" );
        new TosAccessControllerConfigDAO( ).insert( config );
    }

    @AfterEach
    public void removeController( )
    {
        new TosAccessControllerConfigDAO( ).delete( _controller.getId( ) );
        AccessControllerHome.remove( _controller.getId( ) );
        AccessControlHome.remove( _accessControl.getId( ) );
    }

    @Test
    public void testErrorMessageTooLong( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), AbstractManageAccessControlJspBean.RIGHT_MANAGEACCESSCONTROL );
        request.addParameter( "id_controller", String.valueOf( _controller.getId( ) ) );
        request.addParameter( "comment", "<p>bh</p>" );
        request.addParameter( "error_message", "e".repeat( 120 ) );
        AccessControlJspBean bean = CDI.current( ).select( AccessControlJspBean.class ).get( );
        bean.init( request, AbstractManageAccessControlJspBean.RIGHT_MANAGEACCESSCONTROL );
        Field field = MVCAdminJspBean.class.getDeclaredField( "_response" );
        field.setAccessible( true );
        field.set( ( (WeldClientProxy) bean ).getMetadata( ).getContextualInstance( ), new MockHttpServletResponse( ) );

        assertDoesNotThrow( ( ) -> bean.doModifyConfigController( request ) );
        assertEquals( "short", new TosAccessControllerConfigDAO( ).load( _controller.getId( ) ).getErrorMessage( ) );
    }
}
