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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControllerHome;
import fr.paris.lutece.plugins.accesscontrol.service.AccessControlServiceProvider;
import fr.paris.lutece.plugins.accesscontrol.service.IAccessControllerType;
import fr.paris.lutece.plugins.accesscontrol.service.IPersistentAccessControllerType;
import fr.paris.lutece.plugins.accesscontrol.util.BoolCondition;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;
import fr.paris.lutece.portal.service.accesscontrol.IAccessControlServiceProvider;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.url.UrlItem;

@Controller( xpageName = AccessControlXPage.XPAGE_NAME, pageTitleI18nKey = AccessControlXPage.MESSAGE_PAGE_TITLE, pagePathI18nKey = AccessControlXPage.MESSAGE_PATH )
public class AccessControlXPage extends MVCApplication
{
    private static final long serialVersionUID = -459438276022465973L;

    public static final String XPAGE_NAME = "accesscontrol";
    private static final String URL_PORTAL = "Portal.jsp";

    // Messages
    protected static final String MESSAGE_PAGE_TITLE = "accesscontrol.xpage.form.view.pageTitle";
    protected static final String MESSAGE_PATH = "accesscontrol.xpage.form.view.pagePathLabel";

    // Views
    public static final String VIEW_CONTROLLER = "controllerView";

    // Actions
    private static final String ACTION_VALIDATE_CONTROLLER = "doValidateController";
    private static final String ACTION_RETURN_CONTROLLER = "doReturnController";

    // Parameters
    public static final String PARAMETER_INIT = "init";
    public static final String PARAMETER_ID_ACCESS_CONTROL = "id_accesscontrol";
    public static final String PARAMETER_RESOURCE_ID = "id_resource";
    public static final String PARAMETER_RESOURCE_TYPE = "type_resource";

    // Marks
    private static final String MARK_CONTROLLER_HTML = "controller_html";
    private static final String MARK_FIRST_CONTROLLER = "first_controller";

    // Templates
    private static final String TEMPLATE_CONTROLLER_VIEW = "skin/plugins/accesscontrol/controller_view.html";

    private int _nCurrentControllerOrder;
    private Map<Integer, String> _accessControlResult;
    private List<AccessController> _controllerList;
    private AccessController _currentController;
    private AccessControl _accessControl;

    private int _idResource;
    private String _resourceType;

    private IAccessControlServiceProvider _service = SpringContextService.getBean( AccessControlServiceProvider.BEAN_NAME );

    private void init( HttpServletRequest request )
    {
        int idAccessControl = NumberUtils.toInt( request.getParameter( PARAMETER_ID_ACCESS_CONTROL ), -1 );
        _idResource = NumberUtils.toInt( request.getParameter( PARAMETER_RESOURCE_ID ), -1 );
        _resourceType = request.getParameter( PARAMETER_RESOURCE_TYPE );
        _nCurrentControllerOrder = 1;
        _accessControlResult = new HashMap<>( );
        _accessControl = AccessControlHome.findByPrimaryKey( idAccessControl );
        _controllerList = AccessControllerHome.getAccessControllersListByAccessControlId( idAccessControl );
    }

    /**
     * Return the default XPage with the list of all available Form
     * 
     * @param request
     *            The HttpServletRequest
     * @return the list of all available forms
     * @throws SiteMessageException
     */
    @View( value = VIEW_CONTROLLER )
    public XPage getControllerView( HttpServletRequest request ) throws SiteMessageException
    {
        if ( BooleanUtils.toBoolean( request.getParameter( PARAMETER_INIT ) ) )
        {
            init( request );
        }

        if ( CollectionUtils.isEmpty( _controllerList ) )
        {
            return null;
        }

        if ( _nCurrentControllerOrder > _controllerList.size( ) )
        {
            return validateAccessControlAndRedirect( request );
        }

        Locale locale = getLocale( request );

        _currentController = _controllerList.stream( ).filter( c -> c.getOrder( ) == _nCurrentControllerOrder ).findFirst( ).orElse( null );
        if ( _currentController == null )
        {
            return validateAccessControlAndRedirect( request );
        }
        IAccessControllerType currentControllerType = SpringContextService.getBean( _currentController.getType( ) );

        Map<String, Object> model = getModel( );
        model.put( MARK_CONTROLLER_HTML, currentControllerType.getControllerForm( request, locale, _currentController ) );
        model.put( MARK_FIRST_CONTROLLER, _nCurrentControllerOrder == 1 );

        XPage xPage = getXPage( TEMPLATE_CONTROLLER_VIEW, locale, model );
        xPage.setTitle( I18nService.getLocalizedString( MESSAGE_PAGE_TITLE, locale ) );
        xPage.setPathLabel( I18nService.getLocalizedString( MESSAGE_PATH, locale ) );

        return xPage;
    }

    private XPage validateAccessControlAndRedirect( HttpServletRequest request ) throws SiteMessageException
    {
        boolean result = true;
        for ( int i = 0; i < _controllerList.size( ); i++ )
        {
            int order = i + 1;
            boolean valid = _accessControlResult.get( order ) == null;

            AccessController controller = _controllerList.stream( ).filter( c -> c.getOrder( ) == order ).findFirst( ).orElse( null );

            if ( controller != null )
            {
                BoolCondition condition = BoolCondition.valueOf( controller.getBoolCond( ) );

                if ( condition == BoolCondition.AND )
                {
                    result = result && valid;
                }
                else
                    if ( condition == BoolCondition.OR )
                    {
                        result = result || valid;
                    }
            }
        }

        if ( result && _accessControlResult.size() == _controllerList.size() )
        {
            AccessControlSessionData sessionData = _service.getSessionDataForResource( request, _idResource, _resourceType );
            sessionData.setAccessControlResult( true );
            request.getSession( ).setAttribute( sessionData.getSessionKey( ), sessionData );

            return redirect( request, URL_PORTAL + "?" + sessionData.getReturnQueryString( ) );
        }

        UrlItem url = null;
        if ( StringUtils.isEmpty( _accessControl.getReturnUrl( ) ) )
        {
            url = new UrlItem( URL_PORTAL );
        }
        else
        {
            url = new UrlItem( _accessControl.getReturnUrl( ) );
        }
        Object [ ] messageArgs = new Object [ ] {
                _accessControlResult.values( ).stream( ).filter( Objects::nonNull ).collect( Collectors.joining( " - " ) )
        };
        SiteMessageService.setMessage( request, "accesscontrol.xpage.accesscontrol.sitemessage.error", messageArgs,
                "accesscontrol.xpage.accesscontrol.sitemessage.title", url.getUrl( ), null, SiteMessage.TYPE_ERROR );
        return null;
    }

    @Action( value = ACTION_VALIDATE_CONTROLLER )
    public XPage doValidateController( HttpServletRequest request ) throws UserNotSignedException
    {
        IAccessControllerType currentControllerType = SpringContextService.getBean( _currentController.getType( ) );
        String validationResult = currentControllerType.validate( request, _currentController );

        if ( currentControllerType instanceof IPersistentAccessControllerType && validationResult == null )
        {
            AccessControlSessionData sessionData = _service.getSessionDataForResource( request, _idResource, _resourceType );
            ( (IPersistentAccessControllerType) currentControllerType ).persistDataToSession( sessionData, request, getLocale( request ),
                    _currentController.getId( ) );
        }
        _accessControlResult.put( _currentController.getOrder( ), validationResult );
        _nCurrentControllerOrder++;
        return redirectView( request, VIEW_CONTROLLER );
    }

    @Action( value = ACTION_RETURN_CONTROLLER )
    public XPage doReturnController( HttpServletRequest request )
    {
        _nCurrentControllerOrder--;
        return redirectView( request, VIEW_CONTROLLER );
    }
}
