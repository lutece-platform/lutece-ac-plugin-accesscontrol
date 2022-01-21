package fr.paris.lutece.plugins.accesscontrol.business.config;

import fr.paris.lutece.plugins.accesscontrol.util.AccessControlUtils;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Dao for {@link CommentAccessControllerConfig}
 */
public class AgeAccessControllerConfigDAO implements IAccessControllerConfigDAO<AgeAccessControllerConfig>
{
    public static final String BEAN_NAME = "accesscontrol.ageAccessControllerConfigDAO";
    
    private static final String SQL_QUERY_SELECT = "SELECT id_access_controller, comment, error_message, age_min, age_max FROM accesscontrol_controller_age_config WHERE id_access_controller = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_controller_age_config (id_access_controller, comment, error_message, age_min, age_max ) VALUES (?,?,?,?,?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE accesscontrol_controller_age_config SET comment = ?, error_message = ?, age_min = ?, age_max = ? WHERE id_access_controller = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_controller_age_config WHERE id_access_controller = ? ";
    
    @Override
    public void insert( AgeAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, AccessControlUtils.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, config.getIdAccessController( ) );
            daoUtil.setString( ++nIndex, config.getComment( ) );
            daoUtil.setString( ++nIndex, config.getErrorMessage( ) );
            daoUtil.setInt( ++nIndex, config.getAgeMin( ) );
            daoUtil.setInt( ++nIndex, config.getAgeMax( ) );
            daoUtil.executeUpdate( );
        }
    }

    @Override
    public void store( AgeAccessControllerConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, AccessControlUtils.getPlugin( ) ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex, config.getComment( ) );
            daoUtil.setString( ++nIndex, config.getErrorMessage( ) );
            daoUtil.setInt( ++nIndex, config.getAgeMin( ) );
            daoUtil.setInt( ++nIndex, config.getAgeMax( ) );
            
            daoUtil.setInt( ++nIndex, config.getIdAccessController( ) );
            daoUtil.executeUpdate( );
        }
        
    }

    @Override
    public AgeAccessControllerConfig load( int nIdAccessController )
    {
        AgeAccessControllerConfig config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, AccessControlUtils.getPlugin( ) ) )
        {
            daoUtil.setInt( 1, nIdAccessController );
            daoUtil.executeQuery( );
            
            if ( daoUtil.next( ) )
            {
                int nIndex = 0;
                config = new AgeAccessControllerConfig( );
                config.setIdAccessController( daoUtil.getInt( ++nIndex ) );
                config.setComment( daoUtil.getString( ++nIndex ) );
                config.setErrorMessage( daoUtil.getString( ++nIndex ) );
                config.setAgeMin( daoUtil.getInt( ++nIndex ) );
                config.setAgeMax( daoUtil.getInt( ++nIndex ) );
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
