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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerDataHome;
import fr.paris.lutece.plugins.accesscontrol.business.config.IAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfigDAO;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;

/**
 * Abstract {@link IAccessControllerType} for UserCodeAccessControllerType & LuteceUserCodeAccessControllerType
 */
public abstract class AbstractUserCodeAccessControllerType implements IAccessControllerType
{
    @Inject
    @Named( UserCodeAccessControllerConfigDAO.BEAN_NAME )
    private IAccessControllerConfigDAO<UserCodeAccessControllerConfig> _dao;

    private static final String MARK_CONFIG = "config";
    private static final String TEMPLATE_CONFIG = "/admin/plugins/accesscontrol/config/user_code_controller_config.html";
    private static final String PARAMETER_COMMENT = "comment";
    private static final String PARAMETER_ERROR_MESSAGE = "error_message";
    private static final String PARAMETER_USER_CODE = "code";

    @Override
    public boolean hasConfig( )
    {
        return true;
    }

    @Override
    public void deleteConfig( int idController )
    {
        _dao.delete( idController );
    }

    @Override
    public String getControllerConfigForm( HttpServletRequest request, Locale locale, AccessController controller )
    {
        UserCodeAccessControllerConfig config = _dao.load( controller.getId( ) );
        if ( config == null )
        {
            config = new UserCodeAccessControllerConfig( );
            config.setIdAccessController( controller.getId( ) );
            _dao.insert( config );
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, config );

        return AppTemplateService.getTemplate( TEMPLATE_CONFIG, locale, model ).getHtml( );
    }

    @Override
    public void saveControllerConfig( HttpServletRequest request, Locale locale, AccessController controller )
    {
        UserCodeAccessControllerConfig config = _dao.load( controller.getId( ) );
        config.setComment( request.getParameter( PARAMETER_COMMENT ) );
        config.setErrorMessage( request.getParameter( PARAMETER_ERROR_MESSAGE ) );
        _dao.store( config );
    }

    @Override
    public String getControllerForm( HttpServletRequest request, Locale locale, AccessController controller )
    {
        UserCodeAccessControllerConfig config = _dao.load( controller.getId( ) );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, config );

        return AppTemplateService.getTemplate( getTemplateController( ), locale, model ).getHtml( );
    }

    @Override
    public String validate( HttpServletRequest request, AccessController controller ) throws UserNotSignedException
    {
        UserCodeAccessControllerConfig config = _dao.load( controller.getId( ) );

        String userId = getUserId( request );
        String code = request.getParameter( PARAMETER_USER_CODE );

        if ( StringUtils.isEmpty( userId ) || StringUtils.isEmpty( code ) )
        {
            return config.getErrorMessage( );
        }
        if ( !UserCodeControllerDataHome.checkUserCodeValid( userId, code, controller.getIdAccesscontrol( ) ) )
        {
            return config.getErrorMessage( );
        }
        return null;
    }

    protected abstract String getUserId( HttpServletRequest request ) throws UserNotSignedException;

    protected abstract String getTemplateController( );
}
