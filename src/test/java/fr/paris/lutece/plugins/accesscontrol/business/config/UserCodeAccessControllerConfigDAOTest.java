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

public class UserCodeAccessControllerConfigDAOTest extends LuteceTestCase
{
    private static final int ID = 1;
    private static final String COMMENT_1 = "comment1";
    private static final String COMMENT_2 = "comment2";
    private static final String MSG_1 = "msg1";
    private static final String MSG_2 = "msg2";

    public void testDao( )
    {
        IAccessControllerConfigDAO<UserCodeAccessControllerConfig> dao = SpringContextService.getBean( UserCodeAccessControllerConfigDAO.BEAN_NAME );

        UserCodeAccessControllerConfig config = new UserCodeAccessControllerConfig( );
        config.setIdAccessController( ID );
        config.setComment( COMMENT_1 );
        config.setErrorMessage( MSG_1 );
        dao.insert( config );

        UserCodeAccessControllerConfig loaded = dao.load( ID );
        assertEquals( COMMENT_1, loaded.getComment( ) );
        assertEquals( MSG_1, loaded.getErrorMessage( ) );

        config.setComment( COMMENT_2 );
        config.setErrorMessage( MSG_2 );
        dao.store( config );
        loaded = dao.load( ID );
        assertEquals( COMMENT_2, loaded.getComment( ) );
        assertEquals( MSG_2, loaded.getErrorMessage( ) );

        dao.delete( ID );
        loaded = dao.load( ID );
        assertNull( loaded );
    }
}
