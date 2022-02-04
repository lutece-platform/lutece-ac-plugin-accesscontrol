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
import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Implements {@link IUserCodeControllerDataDAO}
 */
public class UserCodeControllerDataDAO implements IUserCodeControllerDataDAO
{
    private static final String SQL_QUERY_SELECT_ALL = "SELECT userId, code, id_access_control, date_validity FROM accesscontrol_controller_user_code_data ";
    private static final String SQL_QUERY_SELECT_BY_USER = SQL_QUERY_SELECT_ALL + "WHERE userId = ? ";
    private static final String SQL_QUERY_SELECT_BY_USER_AND_CODE = SQL_QUERY_SELECT_ALL
            + "WHERE userId = ? AND CODE = ? AND id_access_control = ? AND ( date_validity IS NULL OR date_validity > ? )";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_controller_user_code_data ( userId, code, id_access_control, date_validity ) VALUES ( ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_controller_user_code_data WHERE userId = ? AND id_access_control = ? ";
    private static final String SQL_QUERY_DELETE_BU_ACCESS_CONTROL = "DELETE FROM accesscontrol_controller_user_code_data WHERE id_access_control = ? ";
    private static final String SQL_QUERY_SELECT_INVALID = SQL_QUERY_SELECT_ALL + " WHERE date_validity IS NOT NULL AND  date_validity < ? ";

    @Override
    public void insert( UserCodeControllerData userCode, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex, userCode.getUser( ) );
            daoUtil.setString( ++nIndex, userCode.getCode( ) );
            daoUtil.setInt( ++nIndex, userCode.getIdAccessControl( ) );
            daoUtil.setDate( ++nIndex, userCode.getValidityDate( ) );

            daoUtil.executeUpdate( );
        }
    }

    @Override
    public void delete( String strUser, int idAccessControl, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex, strUser );
            daoUtil.setInt( ++nIndex, idAccessControl );
            daoUtil.executeUpdate( );
        }

    }

    @Override
    public void deleteByAccessControl( int idAccessControl, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BU_ACCESS_CONTROL, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, idAccessControl );
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public UserCodeControllerData load( String strUser, Plugin plugin )
    {
        UserCodeControllerData data = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_USER, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex, strUser );

            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                data = dataToObject( daoUtil );
            }
        }

        return data;
    }

    @Override
    public List<UserCodeControllerData> loadAll( Plugin plugin )
    {
        List<UserCodeControllerData> list = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                list.add( dataToObject( daoUtil ) );
            }
        }
        return list;
    }

    @Override
    public boolean checkUserCode( String strUser, String strCode, int idAccessControl, Plugin plugin )
    {
        UserCodeControllerData data = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_USER_AND_CODE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex, strUser );
            daoUtil.setString( ++nIndex, strCode );
            daoUtil.setInt( ++nIndex, idAccessControl );
            daoUtil.setDate( ++nIndex, new Date( System.currentTimeMillis( ) ) );

            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                data = dataToObject( daoUtil );
            }
        }

        return data != null;
    }

    private UserCodeControllerData dataToObject( DAOUtil daoUtil )
    {
        int nIndex = 0;
        UserCodeControllerData data = new UserCodeControllerData( );
        data.setUser( daoUtil.getString( ++nIndex ) );
        data.setCode( daoUtil.getString( ++nIndex ) );
        data.setIdAccessControl( daoUtil.getInt( ++nIndex ) );
        data.setValidityDate( daoUtil.getDate( ++nIndex ) );

        return data;
    }

    @Override
    public List<UserCodeControllerData> loadDateInvalid( Plugin plugin )
    {
        List<UserCodeControllerData> list = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_INVALID, plugin ) )
        {
            daoUtil.setDate( 1, new Date( System.currentTimeMillis( ) ) );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                list.add( dataToObject( daoUtil ) );
            }
        }
        return list;
    }
}
