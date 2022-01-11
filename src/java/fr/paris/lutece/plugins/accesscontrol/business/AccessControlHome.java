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

import java.util.List;

import fr.paris.lutece.portal.business.accesscontrol.AccessControl;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

/**
 * This class provides instances management methods (create, find, ...) for AccessControl objects
 */
public final class AccessControlHome
{
    // Static variable pointed at the DAO instance
    private static IAccessControlDAO _dao = SpringContextService.getBean( "accesscontrol.accessControlDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "accesscontrol" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AccessControlHome( )
    {
    }

    /**
     * Create an instance of the accessControl class
     * 
     * @param accessControl
     *            The instance of the AccessControl which contains the informations to store
     * @return The instance of accessControl which has been created with its primary key.
     */
    public static AccessControl create( AccessControl accessControl )
    {
        _dao.insert( accessControl, _plugin );

        return accessControl;
    }

    /**
     * Update of the accessControl which is specified in parameter
     * 
     * @param accessControl
     *            The instance of the AccessControl which contains the data to store
     * @return The instance of the accessControl which has been updated
     */
    public static AccessControl update( AccessControl accessControl )
    {
        _dao.store( accessControl, _plugin );

        return accessControl;
    }

    /**
     * Remove the accessControl whose identifier is specified in parameter
     * 
     * @param nKey
     *            The accessControl Id
     */
    public static void remove( int nKey )
    {
        AccessControlResourceHome.removeByAccessControl( nKey );
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a accessControl whose identifier is specified in parameter
     * 
     * @param nKey
     *            The accessControl primary key
     * @return an instance of AccessControl
     */
    public static AccessControl findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the accessControl objects and returns them as a list
     * 
     * @return the list which contains the data of all the accessControl objects
     */
    public static List<AccessControl> getAccessControlsList( )
    {
        return _dao.selectAccessControlsList( _plugin );
    }

    /**
     * Load the id of all the accessControl objects and returns them as a list
     * 
     * @return the list which contains the id of all the accessControl objects
     */
    public static List<Integer> getIdAccessControlsList( )
    {
        return _dao.selectIdAccessControlsList( _plugin );
    }

    /**
     * Load the data of all the accessControl objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the accessControl objects
     */
    public static ReferenceList getAccessControlsReferenceList( )
    {
        return _dao.selectAccessControlsReferenceList( _plugin );
    }
}
