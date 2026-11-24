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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Combination of the results of the controllers of an access control
 */
public class AccessControlXPageConditionTest extends LuteceTestCase
{
    /**
     * A single controller in OR (it became the first one after a removal or a reorder) is refused when it fails
     */
    @Test
    public void testFirstControllerInOrIsNotIgnored( )
    {
        Map<Integer, String> mapResult = new HashMap<>( );
        mapResult.put( 1, "ERR-AGE" );

        assertFalse( AccessControlXPage.isPassed( List.of( controller( 1, "OR" ) ), mapResult ) );
    }

    /**
     * Age (OR, first) then ToS (AND): the age fails, the ToS passes: refused
     */
    @Test
    public void testFirstInOrThenAnd( )
    {
        Map<Integer, String> mapResult = new HashMap<>( );
        mapResult.put( 1, "ERR-AGE" );
        mapResult.put( 2, null );

        assertFalse( AccessControlXPage.isPassed( List.of( controller( 1, "OR" ), controller( 2, "AND" ) ), mapResult ) );
    }

    /**
     * Controls: OR between two controllers still accepts one of them, AND still needs both
     */
    @Test
    public void testOrAndAnd( )
    {
        Map<Integer, String> mapResult = new HashMap<>( );
        mapResult.put( 1, "ERR" );
        mapResult.put( 2, null );

        assertTrue( AccessControlXPage.isPassed( List.of( controller( 1, "AND" ), controller( 2, "OR" ) ), mapResult ) );
        assertFalse( AccessControlXPage.isPassed( List.of( controller( 1, "AND" ), controller( 2, "AND" ) ), mapResult ) );
    }

    private static AccessController controller( int nOrder, String strCondition )
    {
        AccessController controller = new AccessController( );
        controller.setOrder( nOrder );
        controller.setBoolCond( strCondition );
        return controller;
    }
}
