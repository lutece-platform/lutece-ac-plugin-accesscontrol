package fr.paris.lutece.plugins.accesscontrol.web;

import java.util.Map;

import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

/**
 * This class provides the user interface to manage ControllerUserCode features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageControllerUserCode.jsp", controllerPath = "jsp/admin/plugins/accesscontrol/", right = "USERCODES_MANAGEMENT" )

public class ControllerUserCodeJspBean extends AbstractManageAccessControlJspBean
{

    private static final long serialVersionUID = 5752966056141055327L;

    public static final String RIGHT_MANAGE_USER_CODES = "USERCODES_MANAGEMENT";
    
    // Messages
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ACCESSCONTROLS = "accesscontrol.manage_usercodes.pageTitle";
    
    // View
    private static final String VIEW_MANAGE_USER_CODE = "manageUserCodeView";
    
    // Templates
    private static final String TEMPLATE_MANAGE_USER_CODE = "/admin/plugins/accesscontrol/manage_usercodes.html";
    
    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_USER_CODE, defaultView = true )
    public String getManageUserCode( )
    {
        Map<String, Object> model = getModel( );
        
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_ACCESSCONTROLS, TEMPLATE_MANAGE_USER_CODE, model );
    }
}
