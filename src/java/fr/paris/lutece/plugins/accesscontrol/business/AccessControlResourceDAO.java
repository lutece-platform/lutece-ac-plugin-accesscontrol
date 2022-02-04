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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for AccessControlResource objects
 */
public class AccessControlResourceDAO implements IAccessControlResourceDAO
{
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_resource_accesscontrol ( id_resource, resource_type, id_access_control ) VALUES (?,?,?) ";
    private static final String SQL_QUERY_DELETE_BY_RESOURCE = "DELETE FROM accesscontrol_resource_accesscontrol WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_DELETE_BY_ACCESSCONTROL = "DELETE FROM accesscontrol_resource_accesscontrol WHERE id_access_control = ? ";
    private static final String SQL_QUERY_FIND_BY_RESOURCE = "SELECT id_access_control FROM accesscontrol_resource_accesscontrol WHERE id_resource = ? AND resource_type = ? ";

    @Override
    public void insert( AccessControlResource accessControlResource, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, accessControlResource.getIdResource( ) );
            daoUtil.setString( ++nIndex, accessControlResource.getResourceType( ) );
            daoUtil.setInt( ++nIndex, accessControlResource.getIdAccessControl( ) );

            daoUtil.executeUpdate( );
        }

    }

    @Override
    public void deleteByResource( int nIdResource, String strResourceType, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_RESOURCE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdResource );
            daoUtil.setString( ++nIndex, strResourceType );
            daoUtil.executeUpdate( );
        }

    }

    @Override
    public void deleteByAccessControl( int nIdAccessControl, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ACCESSCONTROL, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdAccessControl );
            daoUtil.executeUpdate( );
        }

    }

    @Override
    public int findByResource( int nIdResource, String strResourceType, Plugin plugin )
    {
        int id = -1;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_RESOURCE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdResource );
            daoUtil.setString( ++nIndex, strResourceType );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                id = daoUtil.getInt( 1 );
            }
        }
        return id;
    }

}
