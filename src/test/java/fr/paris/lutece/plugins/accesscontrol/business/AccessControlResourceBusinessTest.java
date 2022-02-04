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
