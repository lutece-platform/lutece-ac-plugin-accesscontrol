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
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * Validity date of a user code: the code is valid until that date included, as the purge daemon keeps it that day
 */
public class UserCodeValidityTest extends LuteceTestCase
{
    private static final int ID_ACCESS_CONTROL = 987003;

    @AfterEach
    public void removeCodes( )
    {
        UserCodeControllerDataHome.removeByAccessControl( ID_ACCESS_CONTROL );
    }

    @Test
    public void testCodeValidUntilTodayIsAccepted( )
    {
        create( "today", Date.valueOf( LocalDate.now( ) ) );

        assertTrue( UserCodeControllerDataHome.checkUserCodeValid( "today", "C1", ID_ACCESS_CONTROL ) );
    }

    @Test
    public void testCodeValidUntilYesterdayIsRefused( )
    {
        create( "yesterday", Date.valueOf( LocalDate.now( ).minusDays( 1 ) ) );

        assertFalse( UserCodeControllerDataHome.checkUserCodeValid( "yesterday", "C1", ID_ACCESS_CONTROL ) );
    }

    private static void create( String strUser, Date date )
    {
        UserCodeControllerData data = new UserCodeControllerData( );
        data.setUser( strUser );
        data.setCode( "C1" );
        data.setIdAccessControl( ID_ACCESS_CONTROL );
        data.setValidityDate( date );
        UserCodeControllerDataHome.create( data );
    }
}
