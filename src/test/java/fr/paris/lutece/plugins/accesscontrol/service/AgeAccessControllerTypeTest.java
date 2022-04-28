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
