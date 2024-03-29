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

import java.sql.Date;
import java.util.List;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * This is the business class test for the object AccessControl
 */
public class AccessControlBusinessTest extends LuteceTestCase
{
    private static final String NAME1 = "Name1";
    private static final String NAME2 = "Name2";
    private static final String DESCRIPTION1 = "Description1";
    private static final String DESCRIPTION2 = "Description2";
    private static final Date CREATIONDATE1 = new Date( 1000000l );
    private static final Date CREATIONDATE2 = new Date( 2000000l );
    private static final boolean ISENABLED1 = true;
    private static final boolean ISENABLED2 = false;
    private static final String WORKGROUPKEY1 = "WorkgroupKey1";
    private static final String WORKGROUPKEY2 = "WorkgroupKey2";

    /**
     * test AccessControl
     */
    public void testBusiness( )
    {
        // Initialize an object
        AccessControl accessControl = new AccessControl( );
        accessControl.setName( NAME1 );
        accessControl.setDescription( DESCRIPTION1 );
        accessControl.setCreationDate( CREATIONDATE1 );
        accessControl.setEnabled( ISENABLED1 );
        accessControl.setWorkgroup( WORKGROUPKEY1 );

        // Create test
        AccessControlHome.create( accessControl );
        AccessControl accessControlStored = AccessControlHome.findByPrimaryKey( accessControl.getId( ) );
        assertEquals( accessControlStored.getName( ), accessControl.getName( ) );
        assertEquals( accessControlStored.getDescription( ), accessControl.getDescription( ) );
        assertEquals( accessControlStored.getCreationDate( ).toString( ), accessControl.getCreationDate( ).toString( ) );
        assertEquals( accessControlStored.isEnabled( ), accessControl.isEnabled( ) );
        assertEquals( accessControlStored.getWorkgroup( ), accessControl.getWorkgroup( ) );

        AccessControlFilter filter = new AccessControlFilter( );
        filter.setIsEnabled( AccessControlFilter.FILTER_TRUE );
        List<AccessControl> resultList = AccessControlHome.getAccessControllersListByFilter( filter );
        assertEquals( 1, resultList.size( ) );

        // Update test
        accessControl.setName( NAME2 );
        accessControl.setDescription( DESCRIPTION2 );
        accessControl.setCreationDate( CREATIONDATE2 );
        accessControl.setEnabled( ISENABLED2 );
        accessControl.setWorkgroup( WORKGROUPKEY2 );
        AccessControlHome.update( accessControl );
        accessControlStored = AccessControlHome.findByPrimaryKey( accessControl.getId( ) );
        assertEquals( accessControlStored.getName( ), accessControl.getName( ) );
        assertEquals( accessControlStored.getDescription( ), accessControl.getDescription( ) );
        assertEquals( accessControlStored.getCreationDate( ).toString( ), accessControl.getCreationDate( ).toString( ) );
        assertEquals( accessControlStored.isEnabled( ), accessControl.isEnabled( ) );
        assertEquals( accessControlStored.getWorkgroup( ), accessControl.getWorkgroup( ) );

        // List test
        AccessControlHome.getAccessControlsList( );

        // Delete test
        AccessControlHome.remove( accessControl.getId( ) );
        accessControlStored = AccessControlHome.findByPrimaryKey( accessControl.getId( ) );
        assertNull( accessControlStored );

    }

}
