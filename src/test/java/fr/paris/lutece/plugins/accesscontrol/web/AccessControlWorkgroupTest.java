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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.enterprise.inject.spi.CDI;

/**
 * The access controls of a workgroup the user does not belong to are refused by the back office screens
 */
public class AccessControlWorkgroupTest extends LuteceTestCase
{
    private AccessControl _otherWorkgroup;
    private AccessControl _allWorkgroups;

    @BeforeEach
    public void createAccessControls( )
    {
        _otherWorkgroup = create( "AccessControlWorkgroupTest other", "bh_wg_ac14_test" );
        _allWorkgroups = create( "AccessControlWorkgroupTest all", "all" );
    }

    @AfterEach
    public void removeAccessControls( )
    {
        AccessControlHome.remove( _otherWorkgroup.getId( ) );
        AccessControlHome.remove( _allWorkgroups.getId( ) );
    }

    @Test
    public void testOtherWorkgroupRefused( )
    {
        AccessControlJspBean bean = CDI.current( ).select( AccessControlJspBean.class ).get( );
        for ( String [ ] parameters : new String [ ] [ ] {
                {
                        "view", "modifyAccessControl"
                }, {
                        "action", "disableAccessControl"
                }, {
                        "view", "confirmRemoveAccessControl"
                }
        } )
        {
            MockHttpServletRequest request = request( parameters, _otherWorkgroup );
            assertThrows( AccessDeniedException.class, ( ) -> bean.checkAccessControlWorkgroup( request ), parameters [1] );
            MockHttpServletRequest control = request( parameters, _allWorkgroups );
            assertDoesNotThrow( ( ) -> bean.checkAccessControlWorkgroup( control ), parameters [1] );
        }
    }

    private static MockHttpServletRequest request( String [ ] parameters, AccessControl accessControl )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), AbstractManageAccessControlJspBean.RIGHT_MANAGEACCESSCONTROL );
        request.addParameter( parameters [0], parameters [1] );
        request.addParameter( "id", String.valueOf( accessControl.getId( ) ) );
        return request;
    }

    private static AccessControl create( String strName, String strWorkgroup )
    {
        AccessControl accessControl = new AccessControl( );
        accessControl.setName( strName );
        accessControl.setDescription( "" );
        accessControl.setWorkgroup( strWorkgroup );
        accessControl.setCreationDate( new Date( System.currentTimeMillis( ) ) );
        return AccessControlHome.create( accessControl );
    }
}
