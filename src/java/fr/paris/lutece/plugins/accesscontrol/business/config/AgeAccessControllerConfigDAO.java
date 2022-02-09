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
 * Dao for {@link CommentAccessControllerConfig}
 */
public class AgeAccessControllerConfigDAO implements IAccessControllerConfigDAO<AgeAccessControllerConfig>
{
    public static final String BEAN_NAME = "accesscontrol.ageAccessControllerConfigDAO";

    private static final String SQL_QUERY_SELECT = "SELECT id_access_controller, comment, error_message, age_min, age_max, data_handler FROM accesscontrol_controller_age_config WHERE id_access_controller = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_controller_age_config (id_access_controller, comment, error_message, age_min, age_max, data_handler ) VALUES (?,?,?,?,?,?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE accesscontrol_controller_age_config SET comment = ?, error_message = ?, age_min = ?, age_max = ?, data_handler = ? WHERE id_access_controller = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_controller_age_config WHERE id_access_controller = ? ";

    @Override
    public void insert( AgeAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, AccessControlUtils.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, config.getIdAccessController( ) );
            daoUtil.setString( ++nIndex, config.getComment( ) );
            daoUtil.setString( ++nIndex, config.getErrorMessage( ) );
            daoUtil.setInt( ++nIndex, config.getAgeMin( ) );
            daoUtil.setInt( ++nIndex, config.getAgeMax( ) );
            daoUtil.setString( ++nIndex, config.getDataHandler( ) );
            
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public void store( AgeAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, AccessControlUtils.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex, config.getComment( ) );
            daoUtil.setString( ++nIndex, config.getErrorMessage( ) );
            daoUtil.setInt( ++nIndex, config.getAgeMin( ) );
            daoUtil.setInt( ++nIndex, config.getAgeMax( ) );
            daoUtil.setString( ++nIndex, config.getDataHandler( ) );

            daoUtil.setInt( ++nIndex, config.getIdAccessController( ) );
            daoUtil.executeUpdate( );
        }

    }

    @Override
    public AgeAccessControllerConfig load( int nIdAccessController )
    {
        AgeAccessControllerConfig config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, AccessControlUtils.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdAccessController );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                int nIndex = 0;
                config = new AgeAccessControllerConfig( );
                config.setIdAccessController( daoUtil.getInt( ++nIndex ) );
                config.setComment( daoUtil.getString( ++nIndex ) );
                config.setErrorMessage( daoUtil.getString( ++nIndex ) );
                config.setAgeMin( daoUtil.getInt( ++nIndex ) );
                config.setAgeMax( daoUtil.getInt( ++nIndex ) );
                config.setDataHandler( daoUtil.getString( ++nIndex ) );
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
