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
package fr.paris.lutece.plugins.accesscontrol.web;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerData;
import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerDataHome;
import fr.paris.lutece.plugins.accesscontrol.service.AccessControlService;
import fr.paris.lutece.plugins.accesscontrol.service.IAccessControlService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;

/**
 * This class provides the user interface to manage ControllerUserCode features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageControllerUserCode.jsp", controllerPath = "jsp/admin/plugins/accesscontrol/", right = "USERCODES_MANAGEMENT" )

public class UserCodeControllerJspBean extends AbstractManageAccessControlJspBean
{

    private static final long serialVersionUID = 5752966056141055327L;

    public static final String RIGHT_MANAGE_USER_CODES = "USERCODES_MANAGEMENT";

    // Messages
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ACCESSCONTROLS = "accesscontrol.manage_usercodes.pageTitle";

    // View
    private static final String VIEW_MANAGE_USER_CODE = "manageUserCodeView";

    // Actions
    private static final String ACTION_CANCEL_IMPORT = "cancelImport";
    private static final String ACTION_DO_IMPORT = "doImport";

    // Marks
    private static final String MARK_ACCESS_CONTROL_LIST = "access_control_list";

    private static final String PARAMETER_ACCESS_CONTROL = "access_control";
    private static final String PARAMETER_FILE = "csv_file";

    // Templates
    private static final String TEMPLATE_MANAGE_USER_CODE = "/admin/plugins/accesscontrol/manage_usercodes.html";

    private IAccessControlService _accessControlService = SpringContextService.getBean( AccessControlService.BEAN_NAME );

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_USER_CODE, defaultView = true )
    public String getManageUserCode( HttpServletRequest request )
    {
        AdminUser adminUser = getUser( );

        Map<String, Object> model = getModel( );
        model.put( MARK_ACCESS_CONTROL_LIST, _accessControlService.getAccessControlsEnabled( adminUser, getLocale( ) ) );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_ACCESSCONTROLS, TEMPLATE_MANAGE_USER_CODE, model );
    }

    @Action( ACTION_CANCEL_IMPORT )
    public String doCancelImport( HttpServletRequest request )
    {
        return redirectView( request, VIEW_MANAGE_USER_CODE );
    }

    @Action( ACTION_DO_IMPORT )
    public String doImport( HttpServletRequest request )
    {
        int accessControlId = NumberUtils.toInt( request.getParameter( PARAMETER_ACCESS_CONTROL ), -1 );
        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );
            try ( Scanner scanner = new Scanner( fileItem.getInputStream( ) ) )
            {
                while ( scanner.hasNextLine( ) )
                {
                    String strLine = scanner.nextLine( );
                    String [ ] strFields = strLine.split( ";" );

                    UserCodeControllerData data = new UserCodeControllerData( );
                    data.setIdAccessControl( accessControlId );
                    data.setUser( strFields [0] );
                    data.setCode( strFields [1] );
                    data.setValidityDate( Date.valueOf( LocalDate.parse( strFields [2] ) ) );

                    UserCodeControllerDataHome.remove( data.getUser( ), data.getIdAccessControl( ) );
                    UserCodeControllerDataHome.create( data );
                }
            }
            catch( IOException e )
            {
                AppLogService.error( "Error reading file" );
            }
        }

        return redirectView( request, VIEW_MANAGE_USER_CODE );
    }
}
