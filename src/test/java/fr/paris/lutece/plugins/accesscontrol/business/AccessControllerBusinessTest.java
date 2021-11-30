/*
 * Copyright (c) 2002-2021, City of Paris
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
 *"
 * License 1.0
 */

package fr.paris.lutece.plugins.accesscontrol.business;

import fr.paris.lutece.test.LuteceTestCase;


/**
 * This is the business class test for the object AccessController
 */
public class AccessControllerBusinessTest extends LuteceTestCase
{
    private static final String TYPE1 = "Type1";
    private static final String TYPE2 = "Type2";
    private static final int ORDER1 = 1;
    private static final int ORDER2 = 2;
    private static final int IDACCESSCONTROL1 = 1;
    private static final int IDACCESSCONTROL2 = 2;
    private static final String BOOLCOND1 = "BoolCond1";
    private static final String BOOLCOND2 = "BoolCond2";

	/**
	* test AccessController
	*/
    public void testBusiness(  )
    {
        // Initialize an object
        AccessController accessController = new AccessController();
        accessController.setType( TYPE1 );
        accessController.setOrder( ORDER1 );
        accessController.setIdAccesscontrol( IDACCESSCONTROL1 );
        accessController.setBoolCond( BOOLCOND1 );

        // Create test
        AccessControllerHome.create( accessController );
        AccessController accessControllerStored = AccessControllerHome.findByPrimaryKey( accessController.getId( ) );
        assertEquals( accessControllerStored.getType() , accessController.getType( ) );
        assertEquals( accessControllerStored.getOrder() , accessController.getOrder( ) );
        assertEquals( accessControllerStored.getIdAccesscontrol() , accessController.getIdAccesscontrol( ) );
        assertEquals( accessControllerStored.getBoolCond() , accessController.getBoolCond( ) );

        // Update test
        accessController.setType( TYPE2 );
        accessController.setOrder( ORDER2 );
        accessController.setIdAccesscontrol( IDACCESSCONTROL2 );
        accessController.setBoolCond( BOOLCOND2 );
        AccessControllerHome.update( accessController );
        accessControllerStored = AccessControllerHome.findByPrimaryKey( accessController.getId( ) );
        assertEquals( accessControllerStored.getType() , accessController.getType( ) );
        assertEquals( accessControllerStored.getOrder() , accessController.getOrder( ) );
        assertEquals( accessControllerStored.getIdAccesscontrol() , accessController.getIdAccesscontrol( ) );
        assertEquals( accessControllerStored.getBoolCond() , accessController.getBoolCond( ) );

        // List test
        AccessControllerHome.getAccessControllersList( );

        // Delete test
        AccessControllerHome.remove( accessController.getId( ) );
        accessControllerStored = AccessControllerHome.findByPrimaryKey( accessController.getId( ) );
        assertNull( accessControllerStored );
        
    }
    
    
     

}