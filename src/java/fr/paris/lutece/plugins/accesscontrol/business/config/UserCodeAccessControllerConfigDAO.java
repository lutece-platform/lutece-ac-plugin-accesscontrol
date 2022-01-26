package fr.paris.lutece.plugins.accesscontrol.business.config;

import fr.paris.lutece.plugins.accesscontrol.util.AccessControlUtils;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Dao for {@link UserCodeAccessControllerConfig}
 */
public class UserCodeAccessControllerConfigDAO implements IAccessControllerConfigDAO<UserCodeAccessControllerConfig>
{
    public static final String BEAN_NAME = "accesscontrol.userCodeAccessControllerConfigDAO";
    
    private static final String SQL_QUERY_SELECT = "SELECT id_access_controller, comment, error_message FROM accesscontrol_controller_user_code_config WHERE id_access_controller = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_controller_user_code_config (id_access_controller, comment, error_message) VALUES (?,?,?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE accesscontrol_controller_user_code_config SET comment = ?, error_message = ? WHERE id_access_controller = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_controller_user_code_config WHERE id_access_controller = ? ";
    
    @Override
    public void insert( UserCodeAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, AccessControlUtils.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, config.getIdAccessController( ) );
            daoUtil.setString( ++nIndex, config.getComment( ) );
            daoUtil.setString( ++nIndex, config.getErrorMessage( ) );
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public void store( UserCodeAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, AccessControlUtils.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex, config.getComment( ) );
            daoUtil.setString( ++nIndex, config.getErrorMessage( ) );
            
            daoUtil.setInt( ++nIndex, config.getIdAccessController( ) );
            daoUtil.executeUpdate( );
        }
        
    }

    @Override
    public UserCodeAccessControllerConfig load( int nIdAccessController )
    {
        UserCodeAccessControllerConfig config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, AccessControlUtils.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdAccessController );
            daoUtil.executeQuery( );
            
            if ( daoUtil.next( ) )
            {
                config = new UserCodeAccessControllerConfig( );
                config.setIdAccessController( daoUtil.getInt( 1 ) );
                config.setComment( daoUtil.getString( 2 ) );
                config.setErrorMessage( daoUtil.getString( 3 ) );
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
