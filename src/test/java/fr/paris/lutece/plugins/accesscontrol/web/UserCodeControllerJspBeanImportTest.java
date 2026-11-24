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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerData;

/**
 * Lines of the CSV import of the user codes
 */
public class UserCodeControllerJspBeanImportTest
{
    @Test
    public void testValidLine( )
    {
        UserCodeControllerData data = UserCodeControllerJspBean.parseLine( "u1;C1;2030-01-01", 7 );
        assertEquals( "u1", data.getUser( ) );
        assertEquals( "C1", data.getCode( ) );
        assertEquals( java.sql.Date.valueOf( "2030-01-01" ), data.getValidityDate( ) );
        assertEquals( 7, data.getIdAccessControl( ) );
    }

    /**
     * A header line, a non ISO date or a missing field is refused instead of failing
     */
    @Test
    public void testInvalidLines( )
    {
        assertNull( UserCodeControllerJspBean.parseLine( "utilisateur;code;date", 7 ) );
        assertNull( UserCodeControllerJspBean.parseLine( "u2;C2;01/01/2030", 7 ) );
        assertNull( UserCodeControllerJspBean.parseLine( "u3;C3", 7 ) );
        assertNull( UserCodeControllerJspBean.parseLine( ";C4;2030-01-01", 7 ) );
    }

    /**
     * A user or a code longer than its column is a wrong line: refused before anything is written
     */
    @Test
    public void testParseLineTooLong( )
    {
        String strLong = "a".repeat( 101 );
        assertNull( UserCodeControllerJspBean.parseLine( strLong + ";C1;2030-01-01", 7 ) );
        assertNull( UserCodeControllerJspBean.parseLine( "u1;" + strLong + ";2030-01-01", 7 ) );
        assertNotNull( UserCodeControllerJspBean.parseLine( "a".repeat( 100 ) + ";C1;2030-01-01", 7 ) );
    }
}
