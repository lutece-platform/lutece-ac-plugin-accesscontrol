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
package fr.paris.lutece.plugins.accesscontrol.business.config;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

public class AgeAccessControllerConfigDAOTest extends LuteceTestCase
{
    private static final int ID = 1;
    private static final String COMMENT_1 = "comment1";
    private static final String COMMENT_2 = "comment2";
    private static final String MSG_1 = "msg1";
    private static final String MSG_2 = "msg2";
    private static final int AGE_MIN_1 = 3;
    private static final int AGE_MIN_2 = 4;
    private static final int AGE_MAX_1 = 13;
    private static final int AGE_MAX_2 = 14;

    public void testDao( )
    {
        IAccessControllerConfigDAO<AgeAccessControllerConfig> dao = SpringContextService.getBean( AgeAccessControllerConfigDAO.BEAN_NAME );

        AgeAccessControllerConfig config = new AgeAccessControllerConfig( );
        config.setIdAccessController( ID );
        config.setComment( COMMENT_1 );
        config.setErrorMessage( MSG_1 );
        config.setAgeMin( AGE_MIN_1 );
        config.setAgeMax( AGE_MAX_1 );
        dao.insert( config );

        AgeAccessControllerConfig loaded = dao.load( ID );
        assertEquals( config.getComment( ), loaded.getComment( ) );
        assertEquals( config.getErrorMessage( ), loaded.getErrorMessage( ) );
        assertEquals( config.getAgeMin( ), loaded.getAgeMin( ) );
        assertEquals( config.getAgeMax( ), loaded.getAgeMax( ) );

        config.setComment( COMMENT_2 );
        config.setErrorMessage( MSG_2 );
        config.setAgeMin( AGE_MIN_2 );
        config.setAgeMax( AGE_MAX_2 );
        dao.store( config );
        loaded = dao.load( ID );
        assertEquals( config.getComment( ), loaded.getComment( ) );
        assertEquals( config.getErrorMessage( ), loaded.getErrorMessage( ) );
        assertEquals( config.getAgeMin( ), loaded.getAgeMin( ) );
        assertEquals( config.getAgeMax( ), loaded.getAgeMax( ) );

        dao.delete( ID );
        loaded = dao.load( ID );
        assertNull( loaded );
    }
}
