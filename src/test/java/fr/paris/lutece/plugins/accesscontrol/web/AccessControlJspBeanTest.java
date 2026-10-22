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
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;

/**
 * RBAC permissions of the access controls, checked by the server whatever the links displayed
 */
public class AccessControlJspBeanTest extends LuteceTestCase
{
    @Inject
    private AccessControlJspBean _bean;
    private AccessControl _accessControl;

    /**
     * Creates the access control used by the tests
     */
    @BeforeEach
    protected void createAccessControl( )
    {
        _accessControl = new AccessControl( );
        _accessControl.setName( "AccessControlJspBeanTest" );
        _accessControl.setDescription( "" );
        _accessControl.setWorkgroup( "all" );
        _accessControl.setCreationDate( new Date( System.currentTimeMillis( ) ) );
        AccessControlHome.create( _accessControl );
    }

    /**
     * Removes the access control used by the tests
     */
    @AfterEach
    protected void removeAccessControl( )
    {
        AccessControlHome.remove( _accessControl.getId( ) );
    }

    /**
     * An administrator who has the right of the feature but no RBAC permission cannot create, modify, enable or remove an access control, even with a
     * request built by hand
     */
    @Test
    public void testViewsAndActionsRefusedWithoutPermission( )
    {
        String strId = String.valueOf( _accessControl.getId( ) );
        String [ ] [ ] requests = {
                {
                        "view", "createAccessControl", null
                }, {
                        "view", "modifyAccessControl", strId
                }, {
                        "action", "removeAccessControl", strId
                }, {
                        "action", "enableAccessControl", strId
                }, {
                        "action", "disableAccessControl", strId
                }, {
                        "view", "confirmRemoveAccessControl", strId
                }
        };

        for ( String [ ] parameters : requests )
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.setMethod( "POST" );
            AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), AbstractManageAccessControlJspBean.RIGHT_MANAGEACCESSCONTROL );
            request.addParameter( parameters [0], parameters [1] );
            if ( parameters [2] != null )
            {
                request.addParameter( "id", parameters [2] );
            }

            assertThrows( AccessDeniedException.class, ( ) -> _bean.processController( request, new MockHttpServletResponse( ) ), parameters [1] );
        }
    }
}
