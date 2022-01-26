package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.accesscontrol.business.AccessController;
import fr.paris.lutece.plugins.accesscontrol.business.UserCodeControllerDataHome;
import fr.paris.lutece.plugins.accesscontrol.business.config.IAccessControllerConfigDAO;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfig;
import fr.paris.lutece.plugins.accesscontrol.business.config.UserCodeAccessControllerConfigDAO;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;

/**
 * Abstract {@link IAccessControllerType} for UserCodeAccessControllerType & LuteceUserCodeAccessControllerType
 */
public abstract class AbstractUserCodeAccessControllerType implements IAccessControllerType
{
    @Inject
    @Named( UserCodeAccessControllerConfigDAO.BEAN_NAME )
    private IAccessControllerConfigDAO<UserCodeAccessControllerConfig> _dao;
    
    private static final String MARK_CONFIG = "config";
    private static final String TEMPLATE_CONFIG = "/admin/plugins/accesscontrol/config/user_code_controller_config.html";
    private static final String PARAMETER_COMMENT = "comment";
    private static final String PARAMETER_ERROR_MESSAGE = "error_message";
    private static final String PARAMETER_USER_CODE = "code";
    
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
        UserCodeAccessControllerConfig config = _dao.load( controller.getId( ) );
        if ( config == null )
        {
            config = new UserCodeAccessControllerConfig( );
            config.setIdAccessController( controller.getId( ) );
            _dao.insert( config );
        }
        
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, config );
        
        return AppTemplateService.getTemplate( TEMPLATE_CONFIG, locale, model ).getHtml( );
    }
    
    @Override
    public void saveControllerConfig( HttpServletRequest request, Locale locale, AccessController controller )
    {
        UserCodeAccessControllerConfig config = _dao.load( controller.getId( ) );
        config.setComment( request.getParameter( PARAMETER_COMMENT ) );
        config.setErrorMessage( request.getParameter( PARAMETER_ERROR_MESSAGE ) );
        _dao.store( config );
    }
    
    @Override
    public String getControllerForm( HttpServletRequest request, Locale locale, AccessController controller )
    {
        UserCodeAccessControllerConfig config = _dao.load( controller.getId( ) );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CONFIG, config );
        
        return AppTemplateService.getTemplate( getTemplateController( ), locale, model ).getHtml( );
    }
    
    @Override
    public String validate( HttpServletRequest request, AccessController controller ) throws UserNotSignedException
    {
        UserCodeAccessControllerConfig config = _dao.load( controller.getId( ) );
        
        String userId = getUserId( request );
        String code = request.getParameter( PARAMETER_USER_CODE );
        
        if ( StringUtils.isEmpty( userId ) || StringUtils.isEmpty( code ) )
        {
            return config.getErrorMessage( );
        }
        if ( !UserCodeControllerDataHome.checkUserCodeValid( userId, code, controller.getIdAccesscontrol( ) ) )
        {
            return config.getErrorMessage( );
        }
        return null;
    }
    
    protected abstract String getUserId( HttpServletRequest request ) throws UserNotSignedException;
    protected abstract String getTemplateController( );
}
