package fr.paris.lutece.plugins.accesscontrol.business;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * Implements {@link IControllerUserCodeDataDAO} 
 */
public class ControllerUserCodeDataDAO implements IControllerUserCodeDataDAO
{
    private static final String SQL_QUERY_SELECT_ALL = "SELECT userId, code, id_access_control, date_validity FROM accesscontrol_controller_user_code_data ";
    private static final String SQL_QUERY_SELECT_BY_USER = SQL_QUERY_SELECT_ALL + "WHERE userId = ? ";
    private static final String SQL_QUERY_SELECT_BY_USER_AND_CODE = SQL_QUERY_SELECT_ALL + "WHERE userId = ? AND CODE = ? AND id_access_control = ? AND ( date_validity IS NULL OR date_validity > ? )";
    private static final String SQL_QUERY_INSERT = "INSERT INTO accesscontrol_controller_user_code_data ( userId, code, id_access_control, date_validity ) VALUES ( ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM accesscontrol_controller_user_code_data WHERE userId = ? AND id_access_control = ? ";
    private static final String SQL_QUERY_DELETE_BU_ACCESS_CONTROL = "DELETE FROM accesscontrol_controller_user_code_data WHERE id_access_control = ? ";
    private static final String SQL_QUERY_SELECT_INVALID = SQL_QUERY_SELECT_ALL + " WHERE date_validity IS NOT NULL AND  date_validity < ? ";

    @Override
    public void insert( ControllerUserCodeData userCode, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex , userCode.getUser( ) );
            daoUtil.setString( ++nIndex , userCode.getCode( ) );
            daoUtil.setInt( ++nIndex , userCode.getIdAccessControl( ) );
            daoUtil.setDate( ++nIndex , userCode.getValidityDate( ) );
            
            daoUtil.executeUpdate( );
        }
    }
    
    @Override
    public void delete( String strUser, int idAccessControl, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex , strUser );
            daoUtil.setInt( ++nIndex , idAccessControl );
            daoUtil.executeUpdate( );
        }
        
    }
    
    @Override
    public void deleteByAccessControl( int idAccessControl, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BU_ACCESS_CONTROL, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setInt( ++nIndex , idAccessControl );
            daoUtil.executeUpdate( );
        }
    }
    
    @Override
    public ControllerUserCodeData load( String strUser, Plugin plugin )
    {
        ControllerUserCodeData data = null;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_USER, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex , strUser );
            
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                data = dataToObject( daoUtil );
            }
        }
        
        return data;
    }
    
    @Override
    public List<ControllerUserCodeData> loadAll( Plugin plugin )
    {
        List<ControllerUserCodeData> list = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                list.add( dataToObject( daoUtil ) );
            }
        }
        return list;
    }
    
    @Override
    public boolean checkUserCode( String strUser, String strCode, int idAccessControl, Plugin plugin )
    {
        ControllerUserCodeData data = null;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_USER_AND_CODE, plugin ) )
        {
            int nIndex = 0;
            daoUtil.setString( ++nIndex , strUser );
            daoUtil.setString( ++nIndex , strCode );
            daoUtil.setInt( ++nIndex , idAccessControl );
            daoUtil.setDate( ++nIndex , new Date( System.currentTimeMillis( ) ) );
            
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                data = dataToObject( daoUtil );
            }
        }
        
        return data != null;
    }
    
    private ControllerUserCodeData dataToObject( DAOUtil daoUtil )
    {
        int nIndex = 0;
        ControllerUserCodeData data = new ControllerUserCodeData( );
        data.setUser( daoUtil.getString( ++nIndex ) );
        data.setCode( daoUtil.getString( ++nIndex ) );
        data.setIdAccessControl( daoUtil.getInt( ++nIndex ) );
        data.setValidityDate( daoUtil.getDate( ++nIndex ) );
        
        return data;
    }

    @Override
    public List<ControllerUserCodeData> loadDateInvalid( Plugin plugin )
    {
        List<ControllerUserCodeData> list = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_INVALID, plugin ) )
        {
            daoUtil.setDate( 1, new Date( System.currentTimeMillis( ) ) );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                list.add( dataToObject( daoUtil ) );
            }
        }
        return list;
    }
}
