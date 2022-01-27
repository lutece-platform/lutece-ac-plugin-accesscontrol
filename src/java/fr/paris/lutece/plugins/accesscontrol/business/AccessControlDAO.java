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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for AccessControl objects
 */
public final class AccessControlDAO implements IAccessControlDAO
{
    // Constants
    private static final String SQL_QUERY_SELECTALL = "SELECT id_access_control, name, description, creation_date, is_enabled, workgroup_key, return_url FROM accesscontrol_accesscontrol ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECTALL + " WHERE id_access_control = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_accesscontrol ( name, description, creation_date, is_enabled, workgroup_key, return_url ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_accesscontrol WHERE id_access_control = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE accesscontrol_accesscontrol SET id_access_control = ?, name = ?, description = ?, creation_date = ?, is_enabled = ?, workgroup_key = ?, return_url = ? WHERE id_access_control = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_access_control FROM accesscontrol_accesscontrol";
    private static final String SQL_FILTER_IS_ENABLED = " is_enabled = ? ";
    private static final String SQL_FILTER_WORKGROUP = " workgroup_key = ? ";
    private static final String SQL_FILTRE_NAME = " name = ? ";
    private static final String SQL_ORDER_BY_DATE_CREATION = " ORDER BY creation_date DESC ";
    private static final String CONSTANT_WHERE = " WHERE ";
    private static final String CONSTANT_AND = " AND ";
    
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
            daoUtil.setString( nIndex++, accessControl.getWorkgroup( ) );
            daoUtil.setString( nIndex++, accessControl.getReturnUrl( ) );

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
                accessControl = dataToObject( daoUtil );
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
            daoUtil.setString( nIndex++, accessControl.getWorkgroup( ) );
            daoUtil.setString( nIndex++, accessControl.getReturnUrl( ) );
            
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
                AccessControl accessControl = dataToObject( daoUtil );
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
    
    @Override
    public List<AccessControl> selectAccessControlByFilter( AccessControlFilter filter )
    {
        List<AccessControl> listAccessControl = new ArrayList<>( );
        List<String> listStrFilter = new ArrayList<>( );

        if ( filter.containsIsEnabled( ) )
        {
            listStrFilter.add( SQL_FILTER_IS_ENABLED );
        }

        if ( filter.containsWorkgroupCriteria( ) )
        {
            listStrFilter.add( SQL_FILTER_WORKGROUP );
        }

        if ( filter.containsName( ) )
        {
            listStrFilter.add( SQL_FILTRE_NAME );
        }
        String strSQL = buildRequestWithFilter( SQL_QUERY_SELECTALL, listStrFilter, SQL_ORDER_BY_DATE_CREATION );
        try ( DAOUtil daoUtil = new DAOUtil( strSQL, PluginService.getPlugin( "accesscontrol" ) ) )
        {
            int nPos = 0;
            if ( filter.containsIsEnabled( ) )
            {
                daoUtil.setInt( ++nPos, filter.getIsEnabled( ) );
            }

            if ( filter.containsWorkgroupCriteria( ) )
            {
                daoUtil.setString( ++nPos, filter.getWorkgroup( ) );
            }

            if ( filter.containsName( ) )
            {
                daoUtil.setString( ++nPos, filter.getName( ) );
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                AccessControl accessControl = dataToObject( daoUtil );
                listAccessControl.add( accessControl );
            }
        }

        return listAccessControl;
    }
    
    /**
     * Builds a query with filters placed in parameters
     * 
     * @param strSelect
     *            the select of the query
     * @param listStrFilter
     *            the list of filter to add in the query
     * @param strOrder
     *            the order by of the query
     * @return a query
     */
    private static String buildRequestWithFilter( String strSelect, List<String> listStrFilter, String strOrder )
    {
        StringBuilder strBuilder = new StringBuilder( );
        strBuilder.append( strSelect );

        int nCount = 0;

        for ( String strFilter : listStrFilter )
        {
            if ( ++nCount == 1 )
            {
                strBuilder.append( CONSTANT_WHERE );
            }

            strBuilder.append( strFilter );

            if ( nCount != listStrFilter.size( ) )
            {
                strBuilder.append( CONSTANT_AND );
            }
        }

        if ( strOrder != null )
        {
            strBuilder.append( strOrder );
        }

        return strBuilder.toString( );
    }
    
    private AccessControl dataToObject( DAOUtil daoUtil )
    {
        int nIndex = 0;
        AccessControl accessControl = new AccessControl( );
        accessControl.setId( daoUtil.getInt( ++nIndex ) );
        accessControl.setName( daoUtil.getString( ++nIndex ) );
        accessControl.setDescription( daoUtil.getString( ++nIndex ) );
        accessControl.setCreationDate( daoUtil.getDate( ++nIndex ) );
        accessControl.setEnabled( daoUtil.getBoolean( ++nIndex ) );
        accessControl.setWorkgroup( daoUtil.getString( ++nIndex ) );
        accessControl.setReturnUrl( daoUtil.getString( ++nIndex ) );
        
        return accessControl;
    }
}
