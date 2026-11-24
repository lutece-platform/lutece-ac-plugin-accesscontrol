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
package fr.paris.lutece.plugins.accesscontrol.service;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.config.AgeAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.AgeAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.TosAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.TosAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfigDAO;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;

/**
 * A controller whose configuration has no error message (configuration page opened, never saved) still refuses
 */
public class AccessControllerRefusalTest extends LuteceTestCase
{
    @Test
    public void testTosWithoutErrorMessageRefuses( )
    {
        TosAccessControllerConfigDAO dao = new TosAccessControllerConfigDAO( );
        TosAccessControllerConfig config = new TosAccessControllerConfig( );
        dao.insert( config );
        try
        {
            assertNotNull( type( TosAccessControllerType.class, TosAccessControllerType.BEAN_NAME ).validate( new MockHttpServletRequest( ),
                    controller( config.getIdAccessController( ) ) ) );
        }
        finally
        {
            dao.delete( config.getIdAccessController( ) );
        }
    }

    @Test
    public void testUserCodeWithoutErrorMessageRefuses( ) throws Exception
    {
        UserCodeAccessControllerConfigDAO dao = new UserCodeAccessControllerConfigDAO( );
        UserCodeAccessControllerConfig config = new UserCodeAccessControllerConfig( );
        dao.insert( config );
        try
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( "userId", "nobody" );
            request.addParameter( "code", "000" );
            assertNotNull( type( UserCodeAccessControllerType.class, UserCodeAccessControllerType.BEAN_NAME ).validate( request,
                    controller( config.getIdAccessController( ) ) ) );
        }
        finally
        {
            dao.delete( config.getIdAccessController( ) );
        }
    }

    @Test
    public void testAgeWithoutErrorMessageRefuses( )
    {
        AgeAccessControllerConfigDAO dao = new AgeAccessControllerConfigDAO( );
        AgeAccessControllerConfig config = new AgeAccessControllerConfig( );
        config.setAgeMin( 18 );
        dao.insert( config );
        try
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( "birth_date", "2015-01-01" );
            assertNotNull( type( AgeAccessControllerType.class, AgeAccessControllerType.BEAN_NAME ).validate( request,
                    controller( config.getIdAccessController( ) ) ) );
        }
        finally
        {
            dao.delete( config.getIdAccessController( ) );
        }
    }

    private static AccessController controller( int nId )
    {
        AccessController controller = new AccessController( );
        controller.setId( nId );
        return controller;
    }

    private static <T extends IAccessControllerType> T type( Class<T> clazz, String strName )
    {
        return CDI.current( ).select( clazz ).select( NamedLiteral.of( strName ) ).get( );
    }
}
