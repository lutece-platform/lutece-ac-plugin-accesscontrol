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
 * This class provides Data Access methods for AccessController objects
 */
public final class AccessControllerDAO implements IAccessControllerDAO
{
    // Constants
    private static final String SQL_QUERY_SELECTALL = "SELECT id_access_controller, type, display_order, id_access_control, bool_cond FROM accesscontrol_accesscontroller ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECTALL + " WHERE id_access_controller = ?";
    private static final String SQL_QUERY_SELECT_BY_ACCESS_CONTROL = SQL_QUERY_SELECTALL + " WHERE id_access_control = ? ORDER BY display_order ASC ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_accesscontroller ( type, display_order, id_access_control, bool_cond ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_accesscontroller WHERE id_access_controller = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE accesscontrol_accesscontroller SET id_access_controller = ?, type = ?, display_order = ?, id_access_control = ?, bool_cond = ? WHERE id_access_controller = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_access_controller FROM accesscontrol_accesscontroller";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( AccessController accessController, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , accessController.getType( ) );
            daoUtil.setInt( nIndex++ , accessController.getOrder( ) );
            daoUtil.setInt( nIndex++ , accessController.getIdAccesscontrol( ) );
            daoUtil.setString( nIndex++ , accessController.getBoolCond( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                accessController.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AccessController load( int nKey, Plugin plugin )
    {
        AccessController accessController = null;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	        
	        if ( daoUtil.next( ) )
	        {
	            accessController  = dataToObject( daoUtil );
	        }
        }
        return accessController;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( AccessController accessController, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
	        int nIndex = 1;
	        
	        daoUtil.setInt( nIndex++ , accessController.getId( ) );
	        daoUtil.setString( nIndex++ , accessController.getType( ) );
	        daoUtil.setInt( nIndex++ , accessController.getOrder( ) );
	        daoUtil.setInt( nIndex++ , accessController.getIdAccesscontrol( ) );
	        daoUtil.setString( nIndex++ , accessController.getBoolCond( ) );
	        daoUtil.setInt( nIndex , accessController.getId( ) );
	
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<AccessController> selectAccessControllersList( Plugin plugin )
    {
        List<AccessController> accessControllerList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            AccessController accessController = dataToObject( daoUtil );
	            accessControllerList.add( accessController );
	        }
        }
        return accessControllerList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdAccessControllersList( Plugin plugin )
    {
        List<Integer> accessControllerList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            accessControllerList.add( daoUtil.getInt( 1 ) );
	        }
        }
        return accessControllerList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectAccessControllersReferenceList( Plugin plugin )
    {
        ReferenceList accessControllerList = new ReferenceList();
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            accessControllerList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
	        }
    	}
        return accessControllerList;
    }
    
    @Override
    public List<AccessController> selectAccessControllersListByAccessControl( int idAccessControl, Plugin plugin )
    {
        List<AccessController> accessControllerList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ACCESS_CONTROL, plugin ) )
        {
            daoUtil.setInt( 1 , idAccessControl );
            daoUtil.executeQuery( );
            
            while ( daoUtil.next( ) )
            {
                AccessController accessController = dataToObject( daoUtil );
                accessControllerList.add( accessController );
            }
        }
        return accessControllerList;
    }
    
    private AccessController dataToObject( DAOUtil daoUtil )
    {
        AccessController accessController = new AccessController();
        int nIndex = 0;
        
        accessController.setId( daoUtil.getInt( ++nIndex ) );
        accessController.setType( daoUtil.getString( ++nIndex ) );            
        accessController.setOrder( daoUtil.getInt( ++nIndex ) );            
        accessController.setIdAccesscontrol( daoUtil.getInt( ++nIndex ) );            
        accessController.setBoolCond( daoUtil.getString( ++nIndex ) );
        
        return accessController;
    }
}
