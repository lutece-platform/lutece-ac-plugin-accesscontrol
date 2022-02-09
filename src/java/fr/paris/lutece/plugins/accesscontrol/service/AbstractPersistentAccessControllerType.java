package fr.paris.lutece.plugins.accesscontrol.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.accesscontrol.business.config.AbstractControllerConfig;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

public abstract class AbstractPersistentAccessControllerType implements IPersistentAccessControllerType
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
        if ( StringUtils.isNotEmpty( config.getDataHandler( ) ) )
        {
            IPersistentDataHandler dataHandler = SpringContextService.getBean( config.getDataHandler( ) );
            model.put( MARK_DATA_HANDLER_CONFIG_TEMPLATE, dataHandler.getDataHandlerConfigForm( locale, config.getIdAccessController( ) ) );
        }
        model.put( MARK_PERSISTENT_DATA_HANDLER_LIST, list );
    }
    
    protected void saveDataHandlerConfig( HttpServletRequest request, AbstractControllerConfig config )
    {
        String action = request.getParameter( PARAMETER_ACTION );
        IPersistentDataHandler oldDataHandler = getDataHandler( config.getDataHandler( ) );
        if ( action != null )
        {
            config.setDataHandler( request.getParameter( PARAMETER_DATA_HANDLER ) );
        }
        IPersistentDataHandler dataHandler = getDataHandler( config.getDataHandler( ) );
        if ( dataHandler != null )
        {
            dataHandler.doSaveConfig( request, config.getIdAccessController( ) );
        }
        else if ( oldDataHandler != null )
        {
            oldDataHandler.doDeleteConfig( config.getIdAccessController( ) );
        }
    }
    
    private IPersistentDataHandler getDataHandler( String dataHandler )
    {
        if ( StringUtils.isEmpty( dataHandler ) )
        {
            return null;
        }
        return SpringContextService.getBean( dataHandler );
    }
}
