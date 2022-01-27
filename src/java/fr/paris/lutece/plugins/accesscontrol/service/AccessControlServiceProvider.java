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
package fr.paris.lutece.plugins.accesscontrol.service;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlResource;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlResourceHome;
import fr.paris.lutece.plugins.accesscontrol.web.AccessControlXPage;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;
import fr.paris.lutece.portal.service.accesscontrol.IAccessControlServiceProvider;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

/**
 * Implementation of {@link IAccessControlServiceProvider}
 *
 */
public class AccessControlServiceProvider implements IAccessControlServiceProvider
{
    public static final String BEAN_NAME = "accesscontrol.accessControlServiceProvider";
    
    private static final String URL_PORTAL = "Portal.jsp";
    
    @Inject
    private IAccessControlService _accessControlService;
    
    @Override
    public ReferenceList getAccessControlsEnabled( User user, Locale locale )
    {
        return _accessControlService.getAccessControlsEnabled( user, locale );
    }
    
    @Override
    public int findAccessControlForResource( int idResource, String resourceType )
    {
        return AccessControlResourceHome.findByResource( idResource, resourceType );
    }
    
    @Override
    public void createOrUpdateAccessControlResource( int idResource, String resourceType, int idAccessControl )
    {
        AccessControlResourceHome.removeByResource( idResource, resourceType );
        if ( idAccessControl != -1 )
        {
            AccessControlResource accessControlResource = new AccessControlResource( );
            accessControlResource.setIdResource( idResource );
            accessControlResource.setResourceType( resourceType );
            accessControlResource.setIdAccessControl( idAccessControl );
            AccessControlResourceHome.create( accessControlResource );
        }
    }
    
    @Override
    public XPage redirectToAccessControlXPage( HttpServletRequest request, int idResource, String resourceType, int idAccessControl )
    {
        UrlItem url = new UrlItem( URL_PORTAL );
        url.addParameter( MVCUtils.PARAMETER_PAGE, AccessControlXPage.XPAGE_NAME );
        url.addParameter( MVCUtils.PARAMETER_VIEW, AccessControlXPage.VIEW_CONTROLLER );
        url.addParameter( AccessControlXPage.PARAMETER_INIT, String.valueOf( true ) );
        url.addParameter( AccessControlXPage.PARAMETER_ID_ACCESS_CONTROL, idAccessControl );
        url.addParameter( AccessControlXPage.PARAMETER_RESOURCE_ID, idResource );
        url.addParameter( AccessControlXPage.PARAMETER_RESOURCE_TYPE, resourceType );
        
        AccessControlSessionData sessionData = new AccessControlSessionData( );
        sessionData.setReturnQueryString( request.getQueryString( ) );
        request.getSession( ).setAttribute( AccessControlSessionData.getSessionKey( idResource, resourceType ), sessionData );
        
        String strTarget = url.getUrl( );
        HttpServletResponse response = LocalVariables.getResponse( );
        try
        {
            MVCUtils.getLogger( ).debug( "Redirect :{}", strTarget );
            response.sendRedirect( strTarget );
        }
        catch( IOException e )
        {
            MVCUtils.getLogger( ).error( "Unable to redirect : {} : {}", strTarget, e.getMessage( ), e );
        }
        return new XPage( );
    }

    @Override
    public AccessControlSessionData getSessionDataForResource( HttpServletRequest request, int idResource, String resourceType )
    {
        Object objSessionData = request.getSession( ).getAttribute( AccessControlSessionData.getSessionKey( idResource, resourceType ) );
        if ( objSessionData == null )
        {
            return null;
        }
        return (AccessControlSessionData) objSessionData;
    }
    
    @Override
    public void deleteSessionDataForResource( HttpServletRequest request, int idResource, String resourceType )
    {
        request.getSession( ).removeAttribute( AccessControlSessionData.getSessionKey( idResource, resourceType ) );
        
    }
}
