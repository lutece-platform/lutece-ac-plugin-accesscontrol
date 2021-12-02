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
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.accesscontrol.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for AccessControl objects
 */
public final class AccessControlDAO implements IAccessControlDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_access_control, name, description, creation_date, is_enabled, workgroup_key FROM accesscontrol_accesscontrol WHERE id_access_control = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_accesscontrol ( name, description, creation_date, is_enabled, workgroup_key ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_accesscontrol WHERE id_access_control = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE accesscontrol_accesscontrol SET id_access_control = ?, name = ?, description = ?, creation_date = ?, is_enabled = ?, workgroup_key = ? WHERE id_access_control = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_access_control, name, description, creation_date, is_enabled, workgroup_key FROM accesscontrol_accesscontrol";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_access_control FROM accesscontrol_accesscontrol";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( AccessControl accessControl, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, accessControl.getName( ) );
            daoUtil.setString( nIndex++, accessControl.getDescription( ) );
            daoUtil.setDate( nIndex++, accessControl.getCreationDate( ) );
            daoUtil.setBoolean( nIndex++, accessControl.isEnabled( ) );
            daoUtil.setString( nIndex++, accessControl.getWorkgroupKey( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                accessControl.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AccessControl load( int nKey, Plugin plugin )
    {
        AccessControl accessControl = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                accessControl = new AccessControl( );
                int nIndex = 1;

                accessControl.setId( daoUtil.getInt( nIndex++ ) );
                accessControl.setName( daoUtil.getString( nIndex++ ) );
                accessControl.setDescription( daoUtil.getString( nIndex++ ) );
                accessControl.setCreationDate( daoUtil.getDate( nIndex++ ) );
                accessControl.setEnabled( daoUtil.getBoolean( nIndex++ ) );
                accessControl.setWorkgroupKey( daoUtil.getString( nIndex ) );
            }
        }
        return accessControl;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( AccessControl accessControl, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;

            daoUtil.setInt( nIndex++, accessControl.getId( ) );
            daoUtil.setString( nIndex++, accessControl.getName( ) );
            daoUtil.setString( nIndex++, accessControl.getDescription( ) );
            daoUtil.setDate( nIndex++, accessControl.getCreationDate( ) );
            daoUtil.setBoolean( nIndex++, accessControl.isEnabled( ) );
            daoUtil.setString( nIndex++, accessControl.getWorkgroupKey( ) );
            daoUtil.setInt( nIndex, accessControl.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<AccessControl> selectAccessControlsList( Plugin plugin )
    {
        List<AccessControl> accessControlList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                AccessControl accessControl = new AccessControl( );
                int nIndex = 1;

                accessControl.setId( daoUtil.getInt( nIndex++ ) );
                accessControl.setName( daoUtil.getString( nIndex++ ) );
                accessControl.setDescription( daoUtil.getString( nIndex++ ) );
                accessControl.setCreationDate( daoUtil.getDate( nIndex++ ) );
                accessControl.setEnabled( daoUtil.getBoolean( nIndex++ ) );
                accessControl.setWorkgroupKey( daoUtil.getString( nIndex ) );

                accessControlList.add( accessControl );
            }
        }
        return accessControlList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdAccessControlsList( Plugin plugin )
    {
        List<Integer> accessControlList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                accessControlList.add( daoUtil.getInt( 1 ) );
            }
        }
        return accessControlList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectAccessControlsReferenceList( Plugin plugin )
    {
        ReferenceList accessControlList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                accessControlList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }
        }
        return accessControlList;
    }
}
