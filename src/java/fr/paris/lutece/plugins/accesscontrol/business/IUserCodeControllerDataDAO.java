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

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * IControllerUserCodeDataDAO Interface
 */
public interface IUserCodeControllerDataDAO
{

    /**
     * Insert a new record in the table.
     * 
     * @param userCode
     *            instance of the ControllerUserCodeData object to insert
     * @param plugin
     *            the Plugin
     */
    void insert( UserCodeControllerData userCode, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param strUser
     * @param idAccessControl
     * @param plugin
     *            the Plugin
     */
    void delete( String strUser, int idAccessControl, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param idAccessControl
     * @param plugin
     *            the Plugin
     */
    void deleteByAccessControl( int idAccessControl, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param strUser
     *            The identifier of the ControllerUserCodeData
     * @param plugin
     *            the Plugin
     * @return The instance of the ControllerUserCodeData
     */
    UserCodeControllerData load( String strUser, Plugin plugin );

    /**
     * Load the data from the table
     * 
     * @param plugin
     *            the Plugin
     * @return A list of instance of the ControllerUserCodeData
     */
    List<UserCodeControllerData> loadAll( Plugin plugin );

    /**
     * Load the data from the table
     * 
     * @param plugin
     *            the Plugin
     * @return A list of instance of the ControllerUserCodeData
     */
    List<UserCodeControllerData> loadDateInvalid( Plugin plugin );

    /**
     * Checks if the pair user/code exists
     * 
     * @param strUser
     * @param strCode
     * @param idAccessControl
     * @param plugin
     * @return
     */
    boolean checkUserCode( String strUser, String strCode, int idAccessControl, Plugin plugin );

}
