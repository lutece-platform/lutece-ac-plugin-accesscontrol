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

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.config.AbstractControllerConfig;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

public abstract class AbstractPersistentAccessControllerType<C extends AbstractControllerConfig> implements IPersistentAccessControllerType
{
    private static final String MARK_PERSISTENT_DATA_HANDLER_LIST = "data_handler_list";
    private static final String MARK_DATA_HANDLER_CONFIG_TEMPLATE = "data_handler_config";

    private static final String PARAMETER_DATA_HANDLER = "data_handler";
    private static final String PARAMETER_ACTION = "apply";

    protected void addPersistentDataToModel( Locale locale, AbstractControllerConfig config, Map<String, Object> model )
    {
        List<IPersistentDataHandler> dataHandlerList = SpringContextService.getBeansOfType( IPersistentDataHandler.class );
        ReferenceList list = new ReferenceList( );
        list.addItem( "", "-" );
        for ( IPersistentDataHandler dataHandler : dataHandlerList )
        {
            list.addItem( dataHandler.getBeanName( ), dataHandler.getHandlerName( locale ) );
        }
        IPersistentDataHandler dataHandler = config.getPersistentDataHandler( );
        if ( dataHandler != null )
        {
            model.put( MARK_DATA_HANDLER_CONFIG_TEMPLATE, dataHandler.getDataHandlerConfigForm( locale, config.getIdAccessController( ) ) );
        }
        if ( CollectionUtils.isNotEmpty( list ) && list.size( ) > 1 )
        {
            model.put( MARK_PERSISTENT_DATA_HANDLER_LIST, list );
        }
    }

    @Override
    public void applyPersistentData( AccessController controller, AccessControlSessionData sessionData, Object destination )
    {
        IPersistentDataHandler dataHandler = getIPersistentDataHandler( controller.getId( ) );
        if ( dataHandler != null )
        {
            Serializable data = sessionData.getPersistentData( ).get( controller.getId( ) );
            dataHandler.handlePersistentData( controller, sessionData, data, destination );
        }

    }

    @Override
    public void doDeleteDataHandlerConfig( int idConfig )
    {
        IPersistentDataHandler dataHandler = getIPersistentDataHandler( idConfig );
        if ( dataHandler != null )
        {
            dataHandler.doDeleteConfig( idConfig );
        }
    }

    protected void saveDataHandlerConfig( HttpServletRequest request, AbstractControllerConfig config )
    {
        String action = request.getParameter( PARAMETER_ACTION );
        IPersistentDataHandler oldDataHandler = config.getPersistentDataHandler( );
        if ( action != null )
        {
            config.setDataHandler( request.getParameter( PARAMETER_DATA_HANDLER ) );
        }
        IPersistentDataHandler dataHandler = config.getPersistentDataHandler( );
        if ( dataHandler != null )
        {
            dataHandler.doSaveConfig( request, config.getIdAccessController( ) );
        }
        else
            if ( oldDataHandler != null )
            {
                oldDataHandler.doDeleteConfig( config.getIdAccessController( ) );
            }
    }

    protected IPersistentDataHandler getIPersistentDataHandler( int idConfig )
    {
        C config = loadConfig( idConfig );
        if ( config == null )
        {
            return null;
        }
        return config.getPersistentDataHandler( );
    }

    protected abstract C loadConfig( int idConfig );
}
