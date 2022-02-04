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

import java.util.List;

import fr.paris.lutece.plugins.accesscontrol.util.AccessControlUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public final class UserCodeControllerDataHome
{

    private static IUserCodeControllerDataDAO _dao = SpringContextService.getBean( "accesscontrol.userCodeControllerDataDAO" );
    private static Plugin _plugin = AccessControlUtils.getPlugin( );

    /**
     * Private constructor - this class need not be instantiated
     */
    private UserCodeControllerDataHome( )
    {
    }

    /**
     * Create an instance of the controllerUserCodeData class
     * 
     * @param controllerUserCodeData
     *            The instance of the ControllerUserCodeData which contains the informations to store
     * @return The instance of controllerUserCodeData which has been created with its primary key.
     */
    public static UserCodeControllerData create( UserCodeControllerData controllerUserCodeData )
    {
        _dao.insert( controllerUserCodeData, _plugin );
        return controllerUserCodeData;
    }

    /**
     * Remove the controllerUserCodeData whose identifier is specified in parameter
     * 
     * @param strUser
     *            The controllerUserCodeData user
     */
    public static void remove( String strUser, int idAccessControl )
    {
        _dao.delete( strUser, idAccessControl, _plugin );
    }

    /**
     * Remove the controllerUserCodeData whose identifier is specified in parameter
     * 
     * @param strUser
     *            The controllerUserCodeData user
     */
    public static void removeByAccessControl( int idAccessControl )
    {
        _dao.deleteByAccessControl( idAccessControl, _plugin );
    }

    /**
     * Returns an instance of a controllerUserCodeData whose identifier is specified in parameter
     * 
     * @param nKey
     *            The controllerUserCodeData primary key
     * @return an instance of ControllerUserCodeData
     */
    public static UserCodeControllerData findByPrimaryKey( String strUser )
    {
        return _dao.load( strUser, _plugin );
    }

    /**
     * Checks if the pair user/code is valid
     * 
     * @param strUser
     * @param strCode
     * @param idAccessControl
     * @return
     */
    public static boolean checkUserCodeValid( String strUser, String strCode, int idAccessControl )
    {
        return _dao.checkUserCode( strUser, strCode, idAccessControl, _plugin );
    }

    /**
     * Returns a list of instance of aontrollerUserCodeData
     * 
     * @return an instance of ControllerUserCodeData
     */
    public static List<UserCodeControllerData> findAll( )
    {
        return _dao.loadAll( _plugin );
    }

    /**
     * Returns a list of instance of aontrollerUserCodeData
     * 
     * @return an instance of ControllerUserCodeData
     */
    public static List<UserCodeControllerData> findAllInvalidDate( )
    {
        return _dao.loadDateInvalid( _plugin );
    }
}
