package fr.paris.lutece.plugins.accesscontrol.business.config;

import fr.paris.lutece.plugins.accesscontrol.util.AccessControlUtils;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Dao for {@link CommentAccessControllerConfig}
 */
public class CommentAccessControllerConfigDAO implements IAccessControllerConfigDAO<CommentAccessControllerConfig>
{
    public static final String BEAN_NAME = "accesscontrol.commentAccessControllerConfigDAO";
    
    private static final String SQL_QUERY_SELECT = "SELECT id_access_control, comment FROM accesscontrol_controller_comment_config WHERE id_access_control = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_controller_comment_config (id_access_control, comment) VALUES (?,?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE accesscontrol_controller_comment_config SET comment = ? WHERE id_access_control = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_controller_comment_config WHERE id_access_control = ? ";
    
    @Override
    public void insert( CommentAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, AccessControlUtils.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, config.getIdAccessController( ) );
            daoUtil.setString( 2, config.getComment( ) );
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public void store( CommentAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, AccessControlUtils.getPlugin( ) ) )
        {
            daoUtil.setString( 1, config.getComment( ) );
            daoUtil.setInt( 2, config.getIdAccessController( ) );
            daoUtil.executeUpdate( );
        }
        
    }

    @Override
    public CommentAccessControllerConfig load( int nIdAccessController )
    {
        CommentAccessControllerConfig config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, AccessControlUtils.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdAccessController );
            daoUtil.executeQuery( );
            
            if ( daoUtil.next( ) )
            {
                config = new CommentAccessControllerConfig( );
                config.setIdAccessController( daoUtil.getInt( 1 ) );
                config.setComment( daoUtil.getString( 2 ) );
            }
        }
        return config;
    }

    @Override
    public void delete( int nIdAccessController )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, AccessControlUtils.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdAccessController );
            daoUtil.executeUpdate( );
        }
    }
}
