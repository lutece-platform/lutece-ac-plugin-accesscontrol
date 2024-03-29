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
package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.portal.service.security.UserNotSignedException;

/**
 * Interface for access controllers
 */
public interface IAccessControllerType
{

    /**
     * Gets The bean name of the controllerType
     * 
     * @return
     */
    String getBeanName( );

    /**
     * Gets The display title of the controllerType
     * 
     * @param locale
     * @return
     */
    String getTitle( Locale locale );

    /**
     * Indicates if the controllerType needs a config
     * 
     * @return
     */
    default boolean hasConfig( )
    {
        return false;
    }

    /**
     * Delete the config of the controller
     */
    default void deleteConfig( int idController )
    {
    }

    /**
     * Get the html form for the config of the controller.
     * 
     * @param idController
     * @return
     */
    default String getControllerConfigForm( HttpServletRequest request, Locale locale, AccessController controller )
    {
        return "";
    }

    /**
     * Save the config of the controller.
     * 
     * @param idController
     * @return
     */
    default void saveControllerConfig( HttpServletRequest request, Locale locale, AccessController controller )
    {
    }

    /**
     * Validate the controller
     * 
     * @param request
     * @return null if OK, an error message otherwise
     */
    default String validate( HttpServletRequest request, AccessController controller ) throws UserNotSignedException
    {
        return null;
    }

    /**
     * Get the html form for the config of the controller.
     * 
     * @param idController
     * @return
     */
    String getControllerForm( HttpServletRequest request, Locale locale, AccessController controller );
}
