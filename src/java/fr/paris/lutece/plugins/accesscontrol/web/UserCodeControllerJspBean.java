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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerData;
import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerDataHome;
import fr.paris.lutece.plugins.accesscontrol.service.IAccessControlService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.upload.MultipartItem;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;

/**
 * This class provides the user interface to manage ControllerUserCode features ( manage, create, modify, remove )
 */
@RequestScoped
@Named
@Controller( controllerJsp = "ManageControllerUserCode.jsp", controllerPath = "jsp/admin/plugins/accesscontrol/", right = "USERCODES_MANAGEMENT", securityTokenEnabled = true )
public class UserCodeControllerJspBean extends AbstractManageAccessControlJspBean
{

    private static final long serialVersionUID = 5752966056141055327L;

    public static final String RIGHT_MANAGE_USER_CODES = "USERCODES_MANAGEMENT";

    // Messages
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ACCESSCONTROLS = "accesscontrol.manage_usercodes.pageTitle";

    private static final String MESSAGE_ERROR_NO_FILE = "accesscontrol.manage_usercode.error.noFile";
    private static final String MESSAGE_ERROR_INVALID_LINE = "accesscontrol.manage_usercode.error.invalidLine";
    private static final String MESSAGE_ERROR_NO_ACCESS_CONTROL = "accesscontrol.manage_usercode.error.noAccessControl";
    private static final String MESSAGE_INFO_IMPORTED = "accesscontrol.manage_usercode.info.imported";

    // View
    private static final String VIEW_MANAGE_USER_CODE = "manageUserCodeView";

    // Actions
    private static final String ACTION_CANCEL_IMPORT = "cancelImport";
    private static final String ACTION_DO_IMPORT = "doImport";

    // Marks
    private static final String MARK_ACCESS_CONTROL_LIST = "access_control_list";

    private static final String PARAMETER_ACCESS_CONTROL = "access_control";
    private static final String PARAMETER_FILE = "csv_file";
    private static final int MAX_LENGTH_USER_CODE = 100;

    // Templates
    private static final String TEMPLATE_MANAGE_USER_CODE = "/admin/plugins/accesscontrol/manage_usercodes.html";

    @Inject
    private IAccessControlService _accessControlService;

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

    @Action( value = ACTION_CANCEL_IMPORT, securityTokenDisabled = true )
    public String doCancelImport( HttpServletRequest request )
    {
        return redirectView( request, VIEW_MANAGE_USER_CODE );
    }

    // The token of the page is issued under the name of its view: the import posted from it is checked under that name
    @Action( value = ACTION_DO_IMPORT, securityTokenAction = VIEW_MANAGE_USER_CODE )
    public String doImport( HttpServletRequest request )
    {
        int accessControlId = NumberUtils.toInt( request.getParameter( PARAMETER_ACCESS_CONTROL ), -1 );
        if ( !isAccessControlOffered( accessControlId ) )
        {
            addError( MESSAGE_ERROR_NO_ACCESS_CONTROL, getLocale( ) );
            return redirectView( request, VIEW_MANAGE_USER_CODE );
        }
        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartItem fileItem = multipartRequest.getFile( PARAMETER_FILE );
            if ( fileItem == null || fileItem.getSize( ) == 0 )
            {
                addError( MESSAGE_ERROR_NO_FILE, getLocale( ) );
                return redirectView( request, VIEW_MANAGE_USER_CODE );
            }

            // Every line is checked before any is written: a file with a wrong line imports nothing
            List<UserCodeControllerData> listData = new ArrayList<>( );
            try ( Scanner scanner = new Scanner( fileItem.getInputStream( ) ) )
            {
                int nLine = 0;
                while ( scanner.hasNextLine( ) )
                {
                    String strLine = scanner.nextLine( );
                    nLine++;
                    if ( StringUtils.isBlank( strLine ) )
                    {
                        continue;
                    }
                    UserCodeControllerData data = parseLine( strLine, accessControlId );
                    if ( data == null )
                    {
                        addError( I18nService.getLocalizedString( MESSAGE_ERROR_INVALID_LINE, new Object [ ] {
                                String.valueOf( nLine )
                        }, getLocale( ) ) );
                        return redirectView( request, VIEW_MANAGE_USER_CODE );
                    }
                    listData.add( data );
                }
            }
            catch( IOException e )
            {
                AppLogService.error( "Error reading file", e );
                addError( MESSAGE_ERROR_NO_FILE, getLocale( ) );
                return redirectView( request, VIEW_MANAGE_USER_CODE );
            }

            for ( UserCodeControllerData data : listData )
            {
                UserCodeControllerDataHome.remove( data.getUser( ), data.getIdAccessControl( ) );
                UserCodeControllerDataHome.create( data );
            }
            addInfo( I18nService.getLocalizedString( MESSAGE_INFO_IMPORTED, new Object [ ] {
                    String.valueOf( listData.size( ) )
            }, getLocale( ) ) );
        }

        return redirectView( request, VIEW_MANAGE_USER_CODE );
    }

    /**
     * Whether the access control is one of those the page offers: enabled and in a workgroup of the user. The empty choice "-" of the
     * list (-1) is not one, nor an id posted by hand that the user is not allowed to see.
     * 
     * @param accessControlId
     *            the id of the access control
     * @return true if the user codes may be imported into it
     */
    private boolean isAccessControlOffered( int accessControlId )
    {
        if ( accessControlId <= 0 )
        {
            return false;
        }
        String strId = String.valueOf( accessControlId );
        return _accessControlService.getAccessControlsEnabled( getUser( ), getLocale( ) ).stream( ).anyMatch( item -> strId.equals( item.getCode( ) ) );
    }

    /**
     * Reads a line user;code;yyyy-MM-dd of the imported file
     * 
     * @param strLine
     *            the line
     * @param accessControlId
     *            the id of the access control
     * @return the data, or null if the line is not in the expected format
     */
    static UserCodeControllerData parseLine( String strLine, int accessControlId )
    {
        String [ ] strFields = strLine.split( ";" );
        if ( strFields.length < 3 || StringUtils.isAnyBlank( strFields [0], strFields [1], strFields [2] )
                || strFields [0].trim( ).length( ) > MAX_LENGTH_USER_CODE || strFields [1].trim( ).length( ) > MAX_LENGTH_USER_CODE )
        {
            // columns userId and code are varchar(100): a longer value is a wrong line, not an SQL error in the middle of the import
            return null;
        }
        UserCodeControllerData data = new UserCodeControllerData( );
        data.setIdAccessControl( accessControlId );
        data.setUser( strFields [0].trim( ) );
        data.setCode( strFields [1].trim( ) );
        try
        {
            data.setValidityDate( Date.valueOf( LocalDate.parse( strFields [2].trim( ) ) ) );
        }
        catch( DateTimeParseException e )
        {
            return null;
        }
        return data;
    }
}
