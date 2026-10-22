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

import java.sql.Date;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControllerHome;
import fr.paris.lutece.plugins.accesscontrol.service.IAccessControlService;
import fr.paris.lutece.plugins.accesscontrol.service.IAccessControllerType;
import fr.paris.lutece.plugins.accesscontrol.service.rbac.AccessControlResourceIdService;
import fr.paris.lutece.plugins.accesscontrol.util.BoolCondition;
import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage AccessControl features ( manage, create, modify, remove )
 */
@SessionScoped
@Named
@Controller( controllerJsp = "ManageAccessControls.jsp", controllerPath = "jsp/admin/plugins/accesscontrol/", right = "ACCESSCONTROL_MANAGEMENT", securityTokenEnabled = true )
public class AccessControlJspBean extends AbstractManageAccessControlJspBean
{
    private static final long serialVersionUID = 128971112958212947L;

    public static final String RIGHT_MANAGE_ACCESS_CONTROL = "ACCESSCONTROL_MANAGEMENT";

    // Templates
    private static final String TEMPLATE_MANAGE_ACCESSCONTROLS = "/admin/plugins/accesscontrol/manage_accesscontrols.html";
    private static final String TEMPLATE_CREATE_ACCESSCONTROL = "/admin/plugins/accesscontrol/create_accesscontrol.html";
    private static final String TEMPLATE_MODIFY_ACCESSCONTROL = "/admin/plugins/accesscontrol/modify_accesscontrol.html";
    private static final String TEMPLATE_MODIFY_CONTROLLER = "/admin/plugins/accesscontrol/modify_controller.html";

    // Parameters
    private static final String PARAMETER_ID_ACCESSCONTROL = "id";
    private static final String PARAMETER_ID_CONTROLLER = "id_controller";
    private static final String PARAMETER_ORDER = "new_order";
    private static final String PARAMETER_BOOL_CONDITON = "boolCond";
    private static final String PARAMETER_CONTROLLER_TYPE = "controller_type";
    private static final String PARAMETER_ACTION = "apply";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ACCESSCONTROLS = "accesscontrol.manage_accesscontrols.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ACCESSCONTROL = "accesscontrol.modify_accesscontrol.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_ACCESSCONTROL = "accesscontrol.create_accesscontrol.pageTitle";
    private static final String PROPERTY_MODIFY_CONTROLLER_PAGE_TITLE = "accesscontrol.modify_controller.page_title";

    // Markers
    private static final String MARK_ACCESSCONTROL_LIST = "accesscontrol_list";
    private static final String MARK_ACCESSCONTROL = "accesscontrol";
    private static final String MARK_DEFAULT_VALUE_WORKGROUP_KEY = "workgroup_key_default_value";
    private static final String MARK_USER_WORKGROUP_REF_LIST = "user_workgroup_list";
    private static final String MARK_CONTROLLER_TYPE_LIST = "controller_type_list";
    private static final String MARK_CONTROLLER_LIST = "controller_list";
    private static final String MARK_CONDITION_LIST = "condition_list";
    private static final String MARK_CONTROLLER_CONFIG = "controller_config";
    private static final String JSP_MANAGE_ACCESSCONTROLS = "jsp/admin/plugins/accesscontrol/ManageAccessControls.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_ACCESSCONTROL = "accesscontrol.message.confirmRemoveAccessControl";
    private static final String MESSAGE_CONFIRM_REMOVE_ACCESSCONTROLLER = "accesscontrol.message.confirmRemoveAccessController";
    private static final String MESSAGE_CONFIRM_ENABLE_ACCESSCONTROL = "accesscontrol.message.confirmEnableAccessControl";
    private static final String MESSAGE_CONFIRM_DISABLE_ACCESSCONTROL = "accesscontrol.message.confirmDisableAccessControl";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "accesscontrol.model.entity.accesscontrol.attribute.";

    // Views
    private static final String VIEW_MANAGE_ACCESSCONTROLS = "manageAccessControls";
    private static final String VIEW_CREATE_ACCESSCONTROL = "createAccessControl";
    private static final String VIEW_MODIFY_ACCESSCONTROL = "modifyAccessControl";
    private static final String VIEW_MODIFY_CONFIG_CONTROLLER = "modifyConfigController";
    private static final String VIEW_CONFIRM_REMOVE_ACCESSCONTROL = "confirmRemoveAccessControl";
    private static final String VIEW_CONFIRM_REMOVE_ACCESSCONTROLLER = "confirmRemoveAccessController";
    private static final String VIEW_CONFIRM_ENABLE_ACCESSCONTROL = "confirmEnableAccessControl";
    private static final String VIEW_CONFIRM_DISABLE_ACCESSCONTROL = "confirmDisableAccessControl";

    // Actions
    private static final String ACTION_CREATE_ACCESSCONTROL = "createAccessControl";
    private static final String ACTION_MODIFY_ACCESSCONTROL = "modifyAccessControl";
    private static final String ACTION_REMOVE_ACCESSCONTROL = "removeAccessControl";
    private static final String ACTION_ENABLE_ACCESSCONTROL = "enableAccessControl";
    private static final String ACTION_DISABLE_ACCESSCONTROL = "disableAccessControl";
    private static final String ACTION_CREATE_CONTROLLER = "createController";
    private static final String ACTION_CHANGE_CONDITON = "changeCondition";
    private static final String ACTION_CHANGE_ORDER = "changeOrder";
    private static final String ACTION_REMOVE_ACCESSCONTROLLER = "removeAccessController";
    private static final String ACTION_MODIFY_CONFIG_CONTROLLER = "modifyConfigController";
    private static final String ACTION_CANCEL_MODIFY_CONFIG_CONTROLLER = "cancelModifyConfigController";

    // RBAC permission required by each view and action, on the access control it concerns
    private static final Map<String, String> MAP_PERMISSIONS = new HashMap<>( );

    static
    {
        MAP_PERMISSIONS.put( VIEW_CREATE_ACCESSCONTROL, AccessControlResourceIdService.PERMISSION_CREATE );
        MAP_PERMISSIONS.put( ACTION_CREATE_ACCESSCONTROL, AccessControlResourceIdService.PERMISSION_CREATE );
        for ( String strKey : new String [ ] {
                VIEW_MODIFY_ACCESSCONTROL, ACTION_MODIFY_ACCESSCONTROL, VIEW_MODIFY_CONFIG_CONTROLLER, ACTION_MODIFY_CONFIG_CONTROLLER,
                ACTION_CANCEL_MODIFY_CONFIG_CONTROLLER, VIEW_CONFIRM_REMOVE_ACCESSCONTROLLER, ACTION_REMOVE_ACCESSCONTROLLER, ACTION_CREATE_CONTROLLER,
                ACTION_CHANGE_CONDITON, ACTION_CHANGE_ORDER
        } )
        {
            MAP_PERMISSIONS.put( strKey, AccessControlResourceIdService.PERMISSION_MODIFY );
        }
        MAP_PERMISSIONS.put( VIEW_CONFIRM_REMOVE_ACCESSCONTROL, AccessControlResourceIdService.PERMISSION_DELETE );
        MAP_PERMISSIONS.put( ACTION_REMOVE_ACCESSCONTROL, AccessControlResourceIdService.PERMISSION_DELETE );
        for ( String strKey : new String [ ] {
                VIEW_CONFIRM_ENABLE_ACCESSCONTROL, VIEW_CONFIRM_DISABLE_ACCESSCONTROL, ACTION_ENABLE_ACCESSCONTROL, ACTION_DISABLE_ACCESSCONTROL
        } )
        {
            MAP_PERMISSIONS.put( strKey, AccessControlResourceIdService.PERMISSION_ENABLE );
        }
    }

    // Infos
    private static final String INFO_ACCESSCONTROL_CREATED = "accesscontrol.info.accesscontrol.created";
    private static final String INFO_ACCESSCONTROL_UPDATED = "accesscontrol.info.accesscontrol.updated";
    private static final String INFO_ACCESSCONTROL_REMOVED = "accesscontrol.info.accesscontrol.removed";

    @Inject
    private IAccessControlService _accessControlService;

    // Session variable to store working values
    private AccessControl _accesControl;

    /**
     * {@inheritDoc} The RBAC permission of the requested view or action is checked on the access control it concerns: the list only displays the authorized
     * actions, a request built by hand must be refused too
     */
    @Override
    public String processController( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException
    {
        checkAccessControlPermission( request );

        return super.processController( request, response );
    }

    /**
     * Checks that the user has the RBAC permission required by the requested view and action
     *
     * @param request
     *            The Http request
     * @throws AccessDeniedException
     *             if the user is not authorized
     */
    private void checkAccessControlPermission( HttpServletRequest request ) throws AccessDeniedException
    {
        for ( String strKey : new String [ ] {
                MVCUtils.getView( request ), MVCUtils.getAction( request )
        } )
        {
            String strPermission = ( strKey != null ) ? MAP_PERMISSIONS.get( strKey ) : null;

            if ( strPermission != null )
            {
                String strIdResource = AccessControlResourceIdService.PERMISSION_CREATE.equals( strPermission ) ? RBAC.WILDCARD_RESOURCES_ID
                        : String.valueOf( getIdAccessControl( request ) );

                if ( !RBACService.isAuthorized( AccessControl.RESOURCE_TYPE, strIdResource, strPermission, (User) AdminUserService.getAdminUser( request ) ) )
                {
                    throw new AccessDeniedException( "Permission " + strPermission + " denied on access control " + strIdResource );
                }
            }
        }
    }

    /**
     * Gets the access control concerned by the request: the access control parameter, else the access control of the controller parameter, else the
     * access control being modified
     *
     * @param request
     *            The Http request
     * @return the id of the access control, -1 if unknown
     */
    private int getIdAccessControl( HttpServletRequest request )
    {
        int nIdAccessControl = NumberUtils.toInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ), -1 );

        if ( nIdAccessControl == -1 && request.getParameter( PARAMETER_ID_CONTROLLER ) != null )
        {
            AccessController controller = AccessControllerHome.findByPrimaryKey( NumberUtils.toInt( request.getParameter( PARAMETER_ID_CONTROLLER ), -1 ) );
            nIdAccessControl = ( controller != null ) ? controller.getIdAccesscontrol( ) : -1;
        }

        if ( nIdAccessControl == -1 && _accesControl != null )
        {
            nIdAccessControl = _accesControl.getId( );
        }

        return nIdAccessControl;
    }

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_ACCESSCONTROLS, defaultView = true )
    public String getManageAccessControls( HttpServletRequest request )
    {
        _accesControl = null;
        List<AccessControl> listAccessControls = AccessControlHome.getAccessControlsList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_ACCESSCONTROL_LIST, listAccessControls, JSP_MANAGE_ACCESSCONTROLS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_ACCESSCONTROLS, TEMPLATE_MANAGE_ACCESSCONTROLS, model );
    }

    /**
     * Returns the form to create a accesscontrol
     *
     * @param request
     *            The Http request
     * @return the html code of the accesscontrol form
     */
    @View( VIEW_CREATE_ACCESSCONTROL )
    public String getCreateAccessControl( HttpServletRequest request )
    {
        _accesControl = ( _accesControl != null ) ? _accesControl : new AccessControl( );
        AdminUser adminUser = getUser( );
        Locale locale = getLocale( );

        Map<String, Object> model = getModel( );
        model.put( MARK_ACCESSCONTROL, _accesControl );
        model.put( MARK_USER_WORKGROUP_REF_LIST, AdminWorkgroupService.getUserWorkgroups( adminUser, locale ) );
        model.put( MARK_DEFAULT_VALUE_WORKGROUP_KEY, AdminWorkgroupService.ALL_GROUPS );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_ACCESSCONTROL, TEMPLATE_CREATE_ACCESSCONTROL, model );
    }

    /**
     * Process the data capture form of a new accesscontrol
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_ACCESSCONTROL )
    public String doCreateAccessControl( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _accesControl, request, getLocale( ) );

        // Check constraints
        if ( !validateBean( _accesControl, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_ACCESSCONTROL );
        }

        _accesControl.setCreationDate( new Date( System.currentTimeMillis( ) ) );
        AccessControlHome.create( _accesControl );
        addInfo( INFO_ACCESSCONTROL_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }

    /**
     * Manages the removal form of a accesscontrol whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @View( value = VIEW_CONFIRM_REMOVE_ACCESSCONTROL, securityTokenAction = ACTION_REMOVE_ACCESSCONTROL )
    public String getConfirmRemoveAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_ACCESSCONTROL ) );
        url.addParameter( PARAMETER_ID_ACCESSCONTROL, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ACCESSCONTROL, null, null, url.getUrl( ), null,
                AdminMessage.TYPE_CONFIRMATION, null, JSP_MANAGE_ACCESSCONTROLS );

        return redirect( request, strMessageUrl );
    }

    /**
     * Manages the removal form of a accesscontroller whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @View( value = VIEW_CONFIRM_REMOVE_ACCESSCONTROLLER, securityTokenAction = ACTION_REMOVE_ACCESSCONTROLLER )
    public String getConfirmRemoveAccessController( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTROLLER ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_ACCESSCONTROLLER ) );
        url.addParameter( PARAMETER_ID_CONTROLLER, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ACCESSCONTROLLER, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Manages the modification form of a accesscontroller whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @View( VIEW_MODIFY_CONFIG_CONTROLLER )
    public String getModifyConfigController( HttpServletRequest request ) throws AccessDeniedException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTROLLER ) );

        AccessController controller = AccessControllerHome.findByPrimaryKey( nId );
        if ( controller == null )
        {
            throw new AccessDeniedException( "AccessController not found for ID " + nId );
        }

        IAccessControllerType controllerType = CDI.current( ).select( IAccessControllerType.class ).select( NamedLiteral.of( controller.getType( ) ) ).get( );

        if ( controllerType == null )
        {
            throw new AccessDeniedException( "Unknown controller type " + controller.getType( ) );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_CONTROLLER_CONFIG, controllerType.getControllerConfigForm( request, getLocale( ), controller ) );

        setPageTitleProperty( PROPERTY_MODIFY_CONTROLLER_PAGE_TITLE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_CONTROLLER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Do the modification form of a accesscontroller whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( value = ACTION_MODIFY_CONFIG_CONTROLLER )
    public String doModifyConfigController( HttpServletRequest request ) throws AccessDeniedException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTROLLER ) );

        AccessController controller = AccessControllerHome.findByPrimaryKey( nId );
        if ( controller == null )
        {
            throw new AccessDeniedException( "AccessController not found for ID " + nId );
        }
       
        IAccessControllerType controllerType = CDI.current( ).select( IAccessControllerType.class ).select( NamedLiteral.of( controller.getType( ) ) ).get( );

            
        if ( controllerType == null )
        {
            throw new AccessDeniedException( "Unknown controller type " + controller.getType( ) );
        }

        controllerType.saveControllerConfig( request, getLocale( ), controller );
        String action = request.getParameter( PARAMETER_ACTION );
        if ( StringUtils.isNotBlank( action ) )
        {
            return redirect( request, VIEW_MODIFY_CONFIG_CONTROLLER, PARAMETER_ID_CONTROLLER, nId );
        }
        return redirect( request, VIEW_MODIFY_ACCESSCONTROL, PARAMETER_ID_ACCESSCONTROL, controller.getIdAccesscontrol( ) );
    }

    /**
     * Cancel the modification form of a accesscontroller whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( value = ACTION_CANCEL_MODIFY_CONFIG_CONTROLLER, securityTokenDisabled = true )
    public String cancelModifyConfigController( HttpServletRequest request ) throws AccessDeniedException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTROLLER ) );

        AccessController controller = AccessControllerHome.findByPrimaryKey( nId );
        if ( controller == null )
        {
            throw new AccessDeniedException( "AccessController not found for ID " + nId );
        }
    	return redirect( request, VIEW_MODIFY_ACCESSCONTROL, PARAMETER_ID_ACCESSCONTROL, controller.getIdAccesscontrol( ) );
    }
    
    /**
     * Handles the removal form of a accesscontrol
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage accesscontrols
     */
    @Action( ACTION_REMOVE_ACCESSCONTROL )
    public String doRemoveAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );

        List<AccessController> controllerList = AccessControllerHome.getAccessControllersListByAccessControlId( nId );
        for ( AccessController controller : controllerList )
        {
            _accessControlService.deleteAccessController( controller.getId( ) );
        }
        AccessControlHome.remove( nId );
        addInfo( INFO_ACCESSCONTROL_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }

    /**
     * Handles the removal form of a accesscontroller
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage accesscontrols
     */
    @Action( ACTION_REMOVE_ACCESSCONTROLLER )
    public String doRemoveAccessController( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTROLLER ) );

        AccessController controllerToDelete = AccessControllerHome.findByPrimaryKey( nId );
        int idAccessControl = controllerToDelete.getIdAccesscontrol( );

        _accessControlService.deleteAccessController( nId );
        List<AccessController> controllerList = AccessControllerHome.getAccessControllersListByAccessControlId( idAccessControl );
        int newOrder = 1;
        for ( AccessController controller : controllerList )
        {
            controller.setOrder( newOrder++ );
            AccessControllerHome.update( controller );
        }
        addInfo( INFO_ACCESSCONTROL_REMOVED, getLocale( ) );
        return redirect( request, VIEW_MODIFY_ACCESSCONTROL, PARAMETER_ID_ACCESSCONTROL, idAccessControl );
    }

    /**
     * Returns the form to update info about a accesscontrol
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_ACCESSCONTROL )
    public String getModifyAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );

        AdminUser adminUser = getUser( );
        Locale locale = getLocale( );

        if ( _accesControl == null || ( _accesControl.getId( ) != nId ) )
        {
            _accesControl = AccessControlHome.findByPrimaryKey( nId );
        }

        List<AccessController> listController = AccessControllerHome.getAccessControllersListByAccessControlId( nId );
        listController.forEach( ac -> ac.setTitle( locale ) );

        ReferenceList conditionList = new ReferenceList( );
        for ( BoolCondition bc : BoolCondition.values( ) )
        {
            conditionList.addItem( bc.name( ), bc.getLabel( locale ) );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_ACCESSCONTROL, _accesControl );
        model.put( MARK_CONDITION_LIST, conditionList );
        model.put( MARK_USER_WORKGROUP_REF_LIST, AdminWorkgroupService.getUserWorkgroups( adminUser, locale ) );
        model.put( MARK_CONTROLLER_LIST, listController );
        model.put( MARK_CONTROLLER_TYPE_LIST, _accessControlService.createAccessControllerReferenceList( locale ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_ACCESSCONTROL, TEMPLATE_MODIFY_ACCESSCONTROL, model );
    }

    /**
     * Process the change form of a accesscontrol
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_ACCESSCONTROL )
    public String doModifyAccessControl( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _accesControl, request, getLocale( ) );

        // Check constraints
        if ( !validateBean( _accesControl, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_ACCESSCONTROL, PARAMETER_ID_ACCESSCONTROL, _accesControl.getId( ) );
        }

        AccessControlHome.update( _accesControl );
        addInfo( INFO_ACCESSCONTROL_UPDATED, getLocale( ) );
        
        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }

    /**
     * Asks the confirmation of the activation of the accesscontrol whose identifier is in the http request; the confirmation posts the activation
     *
     * @param request
     *            The Http request
     * @return the url of the confirmation message
     */
    @View( value = VIEW_CONFIRM_ENABLE_ACCESSCONTROL, securityTokenAction = ACTION_ENABLE_ACCESSCONTROL )
    public String getConfirmEnableAccessControl( HttpServletRequest request )
    {
        return redirect( request, getConfirmationUrl( request, ACTION_ENABLE_ACCESSCONTROL, MESSAGE_CONFIRM_ENABLE_ACCESSCONTROL ) );
    }

    /**
     * Asks the confirmation of the deactivation of the accesscontrol whose identifier is in the http request; the confirmation posts the
     * deactivation
     *
     * @param request
     *            The Http request
     * @return the url of the confirmation message
     */
    @View( value = VIEW_CONFIRM_DISABLE_ACCESSCONTROL, securityTokenAction = ACTION_DISABLE_ACCESSCONTROL )
    public String getConfirmDisableAccessControl( HttpServletRequest request )
    {
        return redirect( request, getConfirmationUrl( request, ACTION_DISABLE_ACCESSCONTROL, MESSAGE_CONFIRM_DISABLE_ACCESSCONTROL ) );
    }

    /**
     * Builds the url of the confirmation message whose button posts the given action on the accesscontrol whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @param strAction
     *            The action posted by the confirmation
     * @param strMessageKey
     *            The key of the confirmation message
     * @return the url of the confirmation message
     */
    private String getConfirmationUrl( HttpServletRequest request, String strAction, String strMessageKey )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );
        UrlItem url = new UrlItem( getActionUrl( strAction ) );
        url.addParameter( PARAMETER_ID_ACCESSCONTROL, nId );

        return AdminMessageService.getMessageUrl( request, strMessageKey, null, null, url.getUrl( ), null, AdminMessage.TYPE_CONFIRMATION, null,
                JSP_MANAGE_ACCESSCONTROLS );
    }

    /**
     * Enables the accesscontrol
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_ENABLE_ACCESSCONTROL )
    public String doEnableAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );

        AccessControl accessControl = AccessControlHome.findByPrimaryKey( nId );
        if ( accessControl != null )
        {
            accessControl.setEnabled( true );
            AccessControlHome.update( accessControl );
        }
        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }

    /**
     * Disables the accesscontrol
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_DISABLE_ACCESSCONTROL )
    public String doDisableAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );

        AccessControl accessControl = AccessControlHome.findByPrimaryKey( nId );
        if ( accessControl != null )
        {
            accessControl.setEnabled( false );
            AccessControlHome.update( accessControl );
        }
        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }

    /**
     * Create a new {@link AccessController}
     * 
     * @param request
     * @return
     */
    @Action( value = ACTION_CREATE_CONTROLLER, securityTokenDisabled = true )
    public String doCreateController( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );

        AccessControl accessControl = AccessControlHome.findByPrimaryKey( nId );
        if ( accessControl != null )
        {
            AccessController controller = new AccessController( );
            controller.setIdAccesscontrol( nId );
            controller.setType( request.getParameter( PARAMETER_CONTROLLER_TYPE ) );
            controller.setBoolCond( BoolCondition.AND.name( ) );

            List<AccessController> listController = AccessControllerHome.getAccessControllersListByAccessControlId( nId );

            int maxOrder = listController.stream( ).max( Comparator.comparingInt( AccessController::getOrder ) ).map( AccessController::getOrder ).orElse( 0 );
            controller.setOrder( maxOrder + 1 );
            AccessControllerHome.create( controller );

        }

        return redirect( request, VIEW_MODIFY_ACCESSCONTROL, PARAMETER_ID_ACCESSCONTROL, nId );
    }

    /**
     * Change the condition of the controller
     * 
     * @param request
     * @return
     */
    @Action( value = ACTION_CHANGE_CONDITON, securityTokenDisabled = true  )
    public String doChangeControllerCondition( HttpServletRequest request )
    {
        int nIdController = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTROLLER ) );
        AccessController controller = AccessControllerHome.findByPrimaryKey( nIdController );
        if ( controller != null )
        {
            controller.setBoolCond( request.getParameter( PARAMETER_BOOL_CONDITON ) );
            AccessControllerHome.update( controller );
        }

        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );
        return redirect( request, VIEW_MODIFY_ACCESSCONTROL, PARAMETER_ID_ACCESSCONTROL, nId );
    }

    /**
     * Change the order of the controller
     * 
     * @param request
     * @return
     */
    @Action( value = ACTION_CHANGE_ORDER, securityTokenDisabled = true )
    public String doChangeControllerOrder( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );
        int nIdController = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTROLLER ) );
        AccessController controllerToChange = AccessControllerHome.findByPrimaryKey( nIdController );
        if ( controllerToChange == null )
        {
            return redirect( request, VIEW_MODIFY_ACCESSCONTROL, PARAMETER_ID_ACCESSCONTROL, nId );
        }

        int nOrderToSet = Integer.parseInt( request.getParameter( PARAMETER_ORDER ) );
        List<AccessController> controllerList = AccessControllerHome.getAccessControllersListByAccessControlId( nId );

        // order goes up
        if ( nOrderToSet < controllerToChange.getOrder( ) )
        {
            controllerList = controllerList.stream( ).filter( ac -> ac.getOrder( ) >= nOrderToSet && ac.getOrder( ) < controllerToChange.getOrder( ) )
                    .collect( Collectors.toList( ) );
            for ( AccessController controller : controllerList )
            {
                if ( controller.getOrder( ) < controllerToChange.getOrder( ) )
                {
                    controller.setOrder( controller.getOrder( ) + 1 );
                    AccessControllerHome.update( controller );
                }
            }
        }
        // order goes down
        else
            if ( nOrderToSet > controllerToChange.getOrder( ) )
            {
                controllerList = controllerList.stream( ).filter( ac -> ac.getOrder( ) < nOrderToSet && ac.getOrder( ) > controllerToChange.getOrder( ) )
                        .collect( Collectors.toList( ) );
                for ( AccessController controller : controllerList )
                {
                    if ( controller.getOrder( ) > controllerToChange.getOrder( ) )
                    {
                        controller.setOrder( controller.getOrder( ) - 1 );
                        AccessControllerHome.update( controller );
                    }
                }
            }
        controllerToChange.setOrder( nOrderToSet );
        AccessControllerHome.update( controllerToChange );

        return redirect( request, VIEW_MODIFY_ACCESSCONTROL, PARAMETER_ID_ACCESSCONTROL, nId );
    }
}
