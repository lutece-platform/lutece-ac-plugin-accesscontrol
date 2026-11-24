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

import java.io.IOException;
import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlResource;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlResourceHome;
import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControllerHome;
import fr.paris.lutece.plugins.accesscontrol.business.config.TosAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.TosAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.service.TosAccessControllerType;
import fr.paris.lutece.plugins.accesscontrol.service.UserCodeAccessControllerType;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpSession;

/**
 * The front office controllers are the ones of the access control linked to the resource, whatever the access control given by the request
 */
public class AccessControlXPageResourceTest extends LuteceTestCase
{
    private static final int ID_RESOURCE = 987001;
    private static final String RESOURCE_TYPE = "AC_XPAGE_TEST";

    private AccessControl _strict;
    private AccessControl _easy;
    private AccessController _code;
    private AccessController _tos;

    @BeforeEach
    public void createAccessControls( )
    {
        _strict = createAccessControl( "strict" );
        _code = createController( _strict, UserCodeAccessControllerType.BEAN_NAME );
        UserCodeAccessControllerConfig codeConfig = new UserCodeAccessControllerConfig( );
        codeConfig.setIdAccessController( _code.getId( ) );
        codeConfig.setErrorMessage( "ERR-CODE" );
        new UserCodeAccessControllerConfigDAO( ).insert( codeConfig );

        _easy = createAccessControl( "easy" );
        _tos = createController( _easy, TosAccessControllerType.BEAN_NAME );
        TosAccessControllerConfig tosConfig = new TosAccessControllerConfig( );
        tosConfig.setIdAccessController( _tos.getId( ) );
        tosConfig.setErrorMessage( "ERR-TOS" );
        new TosAccessControllerConfigDAO( ).insert( tosConfig );

        AccessControlResource resource = new AccessControlResource( );
        resource.setIdResource( ID_RESOURCE );
        resource.setResourceType( RESOURCE_TYPE );
        resource.setIdAccessControl( _strict.getId( ) );
        AccessControlResourceHome.create( resource );
    }

    @AfterEach
    public void removeAccessControls( )
    {
        AccessControlResourceHome.removeByResource( ID_RESOURCE, RESOURCE_TYPE );
        new UserCodeAccessControllerConfigDAO( ).delete( _code.getId( ) );
        new TosAccessControllerConfigDAO( ).delete( _tos.getId( ) );
        AccessControllerHome.remove( _code.getId( ) );
        AccessControllerHome.remove( _tos.getId( ) );
        AccessControlHome.remove( _strict.getId( ) );
        AccessControlHome.remove( _easy.getId( ) );
        LocalVariables.setLocal( null, null, null );
    }

    /**
     * The visitor sent to the access control of the resource gives the id of another access control and passes its controller: the resource
     * stays locked
     */
    @Test
    public void testOtherAccessControlDoesNotUnlockTheResource( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        HttpSession session = request.getSession( true );
        AccessControlSessionData sessionData = new AccessControlSessionData( ID_RESOURCE, RESOURCE_TYPE );
        session.setAttribute( sessionData.getSessionKey( ), sessionData );
        LocalVariables.setLocal( null, request, new RedirectResponse( ) );
        AccessControlXPage xpage = CDI.current( ).select( AccessControlXPage.class ).get( );

        request.addParameter( AccessControlXPage.PARAMETER_INIT, "true" );
        request.addParameter( AccessControlXPage.PARAMETER_ID_ACCESS_CONTROL, String.valueOf( _easy.getId( ) ) );
        request.addParameter( AccessControlXPage.PARAMETER_RESOURCE_ID, String.valueOf( ID_RESOURCE ) );
        request.addParameter( AccessControlXPage.PARAMETER_RESOURCE_TYPE, RESOURCE_TYPE );
        request.addParameter( "check", "1" );
        callIgnoringMessages( ( ) -> xpage.getControllerView( request ) );
        callIgnoringMessages( ( ) -> xpage.doValidateController( request ) );
        MockHttpServletRequest next = new MockHttpServletRequest( );
        shareSession( next, session );
        callIgnoringMessages( ( ) -> xpage.getControllerView( next ) );

        assertFalse( ( (AccessControlSessionData) session.getAttribute( sessionData.getSessionKey( ) ) ).isAccessControlResult( ) );
    }

    /**
     * Control: the access control of the resource is still played when the request gives it
     */
    @Test
    public void testOwnAccessControlIsPlayed( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        HttpSession session = request.getSession( true );
        AccessControlSessionData sessionData = new AccessControlSessionData( ID_RESOURCE, RESOURCE_TYPE );
        session.setAttribute( sessionData.getSessionKey( ), sessionData );
        LocalVariables.setLocal( null, request, new RedirectResponse( ) );
        AccessControlXPage xpage = CDI.current( ).select( AccessControlXPage.class ).get( );

        request.addParameter( AccessControlXPage.PARAMETER_INIT, "true" );
        request.addParameter( AccessControlXPage.PARAMETER_ID_ACCESS_CONTROL, String.valueOf( _strict.getId( ) ) );
        request.addParameter( AccessControlXPage.PARAMETER_RESOURCE_ID, String.valueOf( ID_RESOURCE ) );
        request.addParameter( AccessControlXPage.PARAMETER_RESOURCE_TYPE, RESOURCE_TYPE );
        Object page = callIgnoringMessages( ( ) -> xpage.getControllerView( request ) );

        assertNotNull( page );
    }

    private interface Call
    {
        Object call( ) throws Exception;
    }

    private static Object callIgnoringMessages( Call call )
    {
        try
        {
            return call.call( );
        }
        catch( Exception e )
        {
            // a site message (refusal) ends the flow
            return null;
        }
    }

    private static void shareSession( MockHttpServletRequest request, HttpSession session )
    {
        try
        {
            java.lang.reflect.Field field = MockHttpServletRequest.class.getDeclaredField( "session" );
            field.setAccessible( true );
            field.set( request, session );
        }
        catch( ReflectiveOperationException e )
        {
            throw new IllegalStateException( e );
        }
    }

    private static AccessControl createAccessControl( String strName )
    {
        AccessControl accessControl = new AccessControl( );
        accessControl.setName( "AccessControlXPageResourceTest " + strName );
        accessControl.setDescription( "" );
        accessControl.setWorkgroup( "all" );
        accessControl.setEnabled( true );
        accessControl.setCreationDate( new Date( System.currentTimeMillis( ) ) );
        return AccessControlHome.create( accessControl );
    }

    private static AccessController createController( AccessControl accessControl, String strType )
    {
        AccessController controller = new AccessController( );
        controller.setIdAccesscontrol( accessControl.getId( ) );
        controller.setType( strType );
        controller.setBoolCond( "AND" );
        controller.setOrder( 1 );
        return AccessControllerHome.create( controller );
    }

    /**
     * Response that ignores the redirects
     */
    private static class RedirectResponse extends MockHttpServletResponse
    {
        @Override
        public void sendRedirect( String strLocation ) throws IOException
        {
            // ignored
        }
    }
}
