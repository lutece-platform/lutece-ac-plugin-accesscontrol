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

import fr.paris.lutece.plugins.accesscontrol.util.AccessControlUtils;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Dao for {@link UserCodeAccessControllerConfig}
 */
public class UserCodeAccessControllerConfigDAO implements IAccessControllerConfigDAO<UserCodeAccessControllerConfig>
{
    public static final String BEAN_NAME = "accesscontrol.userCodeAccessControllerConfigDAO";

    private static final String SQL_QUERY_SELECT = "SELECT id_access_controller, comment, error_message FROM accesscontrol_controller_user_code_config WHERE id_access_controller = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_controller_user_code_config (id_access_controller, comment, error_message) VALUES (?,?,?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE accesscontrol_controller_user_code_config SET comment = ?, error_message = ? WHERE id_access_controller = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_controller_user_code_config WHERE id_access_controller = ? ";

    @Override
    public void insert( UserCodeAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, AccessControlUtils.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, config.getIdAccessController( ) );
            daoUtil.setString( ++nIndex, config.getComment( ) );
            daoUtil.setString( ++nIndex, config.getErrorMessage( ) );
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public void store( UserCodeAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, AccessControlUtils.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex, config.getComment( ) );
            daoUtil.setString( ++nIndex, config.getErrorMessage( ) );

            daoUtil.setInt( ++nIndex, config.getIdAccessController( ) );
            daoUtil.executeUpdate( );
        }

    }

    @Override
    public UserCodeAccessControllerConfig load( int nIdAccessController )
    {
        UserCodeAccessControllerConfig config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, AccessControlUtils.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdAccessController );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                config = new UserCodeAccessControllerConfig( );
                config.setIdAccessController( daoUtil.getInt( 1 ) );
                config.setComment( daoUtil.getString( 2 ) );
                config.setErrorMessage( daoUtil.getString( 3 ) );
            }
        }
        return config;
    }

    @Override
    public void delete( int nIdAccessController )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, AccessControlUtils.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdAccessController );
            daoUtil.executeUpdate( );
        }
    }
}
