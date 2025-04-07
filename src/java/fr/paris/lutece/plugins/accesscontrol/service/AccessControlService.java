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

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlFilter;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControllerHome;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.util.ReferenceList;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;

@ApplicationScoped
public class AccessControlService implements IAccessControlService
{
    private List<IAccessControllerType> _accessControllerTypeList;

    @PostConstruct
    private void initAccessControlService( )
    {
    	_accessControllerTypeList = CDI.current( ).select( IAccessControllerType.class ).stream( ).toList( );
    }
    
    @Override
    public ReferenceList createAccessControllerReferenceList( Locale locale )
    {
        ReferenceList list = new ReferenceList( );
        for ( IAccessControllerType controller : _accessControllerTypeList )
        {
            list.addItem( controller.getBeanName( ), controller.getTitle( locale ) );
        }
        return list;
    }

    @Override
    public void deleteAccessController( int idControlType )
    {
        AccessController accessController = AccessControllerHome.findByPrimaryKey( idControlType );
        if ( accessController != null )
        {
            IAccessControllerType controller = CDI.current( ).select( IAccessControllerType.class ).select( NamedLiteral.of( accessController.getType( ) ) ).get( );
            if ( controller instanceof IPersistentAccessControllerType )
            {
                ( (IPersistentAccessControllerType) controller ).doDeleteDataHandlerConfig( accessController.getId( ) );
            }

            if ( controller.hasConfig( ) )
            {
                controller.deleteConfig( idControlType );
            }
            AccessControllerHome.remove( idControlType );
        }
    }

    @Override
    public List<AccessControl> getListAccessControlsByFilter( AccessControlFilter filter )
    {
        return AccessControlHome.getAccessControllersListByFilter( filter );
    }

    @Override
    public ReferenceList getAccessControlsEnabled( User user, Locale locale )
    {
        AccessControlFilter filter = new AccessControlFilter( );
        filter.setIsEnabled( AccessControlFilter.FILTER_TRUE );
        List<AccessControl> list = getListAccessControlsByFilter( filter );

        ReferenceList referenceList = new ReferenceList( );
        referenceList.addItem( -1, "-" );
        for ( AccessControl accessControl : AdminWorkgroupService.getAuthorizedCollection( list, user ) )
        {
            referenceList.addItem( accessControl.getId( ), accessControl.getName( ) );
        }
        return referenceList;
    }

    @Override
    public void applyPersistentData( AccessControlSessionData sessionData, Object destination )
    {
        Map<Integer, Serializable> persistentData = sessionData.getPersistentData( );
        for ( Integer id : persistentData.keySet( ) )
        {
            AccessController controller = AccessControllerHome.findByPrimaryKey( id );
            IAccessControllerType controllerType = CDI.current( ).select( IAccessControllerType.class ).select( NamedLiteral.of( controller.getType( ) ) ).get( );
            if ( controllerType instanceof IPersistentAccessControllerType )
            {
                IPersistentAccessControllerType persistentControllerType = (IPersistentAccessControllerType) controllerType;
                persistentControllerType.applyPersistentData( controller, sessionData, destination );
            }
        }
    }
}
