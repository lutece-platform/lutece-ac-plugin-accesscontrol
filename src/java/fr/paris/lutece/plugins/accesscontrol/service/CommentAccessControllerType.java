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

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.config.CommentAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.CommentAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.IAccessControllerConfigDAO;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;

public class CommentAccessControllerType implements IAccessControllerType
{
    @Inject
    @Named( CommentAccessControllerConfigDAO.BEAN_NAME )
    private IAccessControllerConfigDAO<CommentAccessControllerConfig> _dao;

    public static final String BEAN_NAME = "accesscontrol.commentAccessControllerType";
    private static final String TITLE_KEY = "accesscontrol.controller.commentAccessController.name";

    private static final String PARAMETER_COMMENT = "comment";
    private static final String TEMPLATE_CONFIG = "/admin/plugins/accesscontrol/config/comment_controller_config.html";
    private static final String TEMPLATE_CONTROLLER = "skin/plugins/accesscontrol/controller/comment_controller_template.html";
    private static final String MARK_CONFIG = "config";

    @Override
    public String getBeanName( )
    {
        return BEAN_NAME;
    }

    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( TITLE_KEY, locale );
    }

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
        CommentAccessControllerConfig config = _dao.load( controller.getId( ) );
        if ( config == null )
        {
            config = new CommentAccessControllerConfig( );
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
        CommentAccessControllerConfig config = _dao.load( controller.getId( ) );
        config.setComment( request.getParameter( PARAMETER_COMMENT ) );
        _dao.store( config );
    }

    @Override
    public String getControllerForm( HttpServletRequest request, Locale locale, AccessController controller )
    {
        CommentAccessControllerConfig config = _dao.load( controller.getId( ) );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, config );

        return AppTemplateService.getTemplate( TEMPLATE_CONTROLLER, locale, model ).getHtml( );
    }
}
