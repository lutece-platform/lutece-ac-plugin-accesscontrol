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
package fr.paris.lutece.plugins.accesscontrol.service.rbac;

import java.util.Locale;

import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import fr.paris.lutece.plugins.accesscontrol.util.AccessControlUtils;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

public class AccessControlResourceIdService extends ResourceIdService
{
    public static final String PERMISSION_CREATE = "CREATE";
    public static final String PERMISSION_DELETE = "DELETE";
    public static final String PERMISSION_MODIFY = "MODIFY";
    public static final String PERMISSION_ENABLE = "ENABLE";

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "accesscontrol.permission.label.resourceType";
    private static final String PROPERTY_LABEL_CREATE = "accesscontrol.permission.label.create";
    private static final String PROPERTY_LABEL_DELETE = "accesscontrol.permission.label.delete";
    private static final String PROPERTY_LABEL_MODIFY = "accesscontrol.permission.label.modify";
    private static final String PROPERTY_LABEL_ENABLE = "accesscontrol.permission.label.enable";

    /** Creates a new instance of DocumentTypeResourceIdService */
    public AccessControlResourceIdService( )
    {
        super( );
        setPluginName( AccessControlUtils.PLUGIN_NAME );
    }

    @Override
    public void register( )
    {
        ResourceType resourceType = new ResourceType( );
        resourceType.setResourceIdServiceClass( AccessControlResourceIdService.class.getName( ) );
        resourceType.setPluginName( AccessControlUtils.PLUGIN_NAME );
        resourceType.setResourceTypeKey( AccessControl.RESOURCE_TYPE );
        resourceType.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission permission = new Permission( );
        permission.setPermissionKey( PERMISSION_CREATE );
        permission.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        resourceType.registerPermission( permission );

        permission = new Permission( );
        permission.setPermissionKey( PERMISSION_MODIFY );
        permission.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        resourceType.registerPermission( permission );

        permission = new Permission( );
        permission.setPermissionKey( PERMISSION_DELETE );
        permission.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        resourceType.registerPermission( permission );

        permission = new Permission( );
        permission.setPermissionKey( PERMISSION_ENABLE );
        permission.setPermissionTitleKey( PROPERTY_LABEL_ENABLE );
        resourceType.registerPermission( permission );

        ResourceTypeManager.registerResourceType( resourceType );
    }

    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        return AccessControlHome.getAccessControlsReferenceList( );
    }

    @Override
    public String getTitle( String strId, Locale locale )
    {
        int nIdAccessControl = -1;

        try
        {
            nIdAccessControl = Integer.parseInt( strId );
        }
        catch( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }

        AccessControl accessControl = AccessControlHome.findByPrimaryKey( nIdAccessControl );
        return accessControl.getName( );
    }
}
