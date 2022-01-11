package fr.paris.lutece.plugins.accesscontrol.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for AccessControlResource objects
 */
public class AccessControlResourceDAO implements IAccessControlResourceDAO
{
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_resource_accesscontrol ( id_resource, resource_type, id_access_control ) VALUES (?,?,?) ";
    private static final String SQL_QUERY_DELETE_BY_RESOURCE = "DELETE FROM accesscontrol_resource_accesscontrol WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_DELETE_BY_ACCESSCONTROL = "DELETE FROM accesscontrol_resource_accesscontrol WHERE id_access_control = ? ";
    private static final String SQL_QUERY_FIND_BY_RESOURCE = "SELECT id_access_control FROM accesscontrol_resource_accesscontrol WHERE id_resource = ? AND resource_type = ? ";
    
    @Override
    public void insert( AccessControlResource accessControlResource, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, accessControlResource.getIdResource( ) );
            daoUtil.setString( ++nIndex, accessControlResource.getResourceType( ) );
            daoUtil.setInt( ++nIndex, accessControlResource.getIdAccessControl( ) );
            
            daoUtil.executeUpdate( );
        }
        
    }

    @Override
    public void deleteByResource( int nIdResource, String strResourceType, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_RESOURCE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdResource );
            daoUtil.setString( ++nIndex, strResourceType );
            daoUtil.executeUpdate( );
        }
        
    }

    @Override
    public void deleteByAccessControl( int nIdAccessControl, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ACCESSCONTROL, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdAccessControl );
            daoUtil.executeUpdate( );
        }
        
    }

    @Override
    public int findByResource( int nIdResource, String strResourceType, Plugin plugin )
    {
        int id = -1;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_RESOURCE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex, nIdResource );
            daoUtil.setString( ++nIndex, strResourceType );
            daoUtil.executeQuery( );
            
            if ( daoUtil.next( ) )
            {
                id = daoUtil.getInt( 1 );
            }
        }
        return id;
    }

}
