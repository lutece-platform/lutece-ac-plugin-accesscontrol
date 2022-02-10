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

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.config.AgeAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.AgeAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.IAccessControllerConfigDAO;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;

public class AgeAccessControllerType extends AbstractPersistentAccessControllerType<AgeAccessControllerConfig> implements IAccessControllerType
{
    @Inject
    @Named( AgeAccessControllerConfigDAO.BEAN_NAME )
    private IAccessControllerConfigDAO<AgeAccessControllerConfig> _dao;

    public static final String BEAN_NAME = "accesscontrol.ageAccessControllerType";
    private static final String TITLE_KEY = "accesscontrol.controller.ageAccessController.name";

    private static final String TEMPLATE_CONFIG = "/admin/plugins/accesscontrol/config/age_controller_config.html";
    private static final String TEMPLATE_CONTROLLER = "skin/plugins/accesscontrol/controller/age_controller_template.html";

    private static final String MARK_CONFIG = "config";

    protected static final String PARAMETER_DATE = "birth_date";
    private static final String PARAMETER_COMMENT = "comment";
    private static final String PARAMETER_ERROR_MESSAGE = "error_message";
    private static final String PARAMETER_AGE_MIN = "ageMin";
    private static final String PARAMETER_AGE_MAX = "ageMax";
    
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
        AgeAccessControllerConfig config = _dao.load( controller.getId( ) );
        if ( config == null )
        {
            config = new AgeAccessControllerConfig( );
            config.setIdAccessController( controller.getId( ) );
            _dao.insert( config );
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, config );
        addPersistentDataToModel( locale, config, model );

        return AppTemplateService.getTemplate( TEMPLATE_CONFIG, locale, model ).getHtml( );
    }

    @Override
    public String getControllerForm( HttpServletRequest request, Locale locale, AccessController controller )
    {
        AgeAccessControllerConfig config = _dao.load( controller.getId( ) );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, config );

        return AppTemplateService.getTemplate( TEMPLATE_CONTROLLER, locale, model ).getHtml( );
    }

    @Override
    public void saveControllerConfig( HttpServletRequest request, Locale locale, AccessController controller )
    {
        AgeAccessControllerConfig config = _dao.load( controller.getId( ) );
        config.setComment( request.getParameter( PARAMETER_COMMENT ) );
        config.setErrorMessage( request.getParameter( PARAMETER_ERROR_MESSAGE ) );
        config.setAgeMin( NumberUtils.toInt( request.getParameter( PARAMETER_AGE_MIN ), 0 ) );
        config.setAgeMax( NumberUtils.toInt( request.getParameter( PARAMETER_AGE_MAX ), 0 ) );
        
        saveDataHandlerConfig( request, config );
        _dao.store( config );
    }

    @Override
    public String validate( HttpServletRequest request, AccessController controller )
    {
        AgeAccessControllerConfig config = _dao.load( controller.getId( ) );
        String strDate = request.getParameter( PARAMETER_DATE );
        if ( strDate == null )
        {
            return config.getErrorMessage( );
        }

        LocalDate birthDate = LocalDate.parse( strDate );

        int currentAge = Period.between( birthDate, LocalDate.now( ) ).getYears( );

        if ( config.getAgeMin( ) > 0 && currentAge <= config.getAgeMin( ) )
        {
            return config.getErrorMessage( );
        }

        if ( config.getAgeMax( ) > 0 && currentAge >= config.getAgeMax( ) )
        {
            return config.getErrorMessage( );
        }

        return null;
    }
    
    @Override
    public void persistDataToSession( AccessControlSessionData sessionData, HttpServletRequest request, Locale locale, int idController )
    {
        String strDate = request.getParameter( PARAMETER_DATE );
        LocalDate date = LocalDate.parse( strDate );
        sessionData.addPersistentParam( idController, String.valueOf( Date.valueOf( date ).getTime( ) ) );
    }
    
    @Override
    protected AgeAccessControllerConfig loadConfig( int idConfig )
    {
        return _dao.load( idConfig );
    }
}
