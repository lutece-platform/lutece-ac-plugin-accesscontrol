package fr.paris.lutece.plugins.accesscontrol.business;

import java.sql.Date;

public class UserCodeControllerData
{
    private String _strUser;
    private String _strCode;
    private int _nIdAccessControl;
    private Date _validityDate;

    /**
     * @return the strUser
     */
    public String getUser( )
    {
        return _strUser;
    }
    /**
     * @param strUser the strUser to set
     */
    public void setUser( String strUser )
    {
        _strUser = strUser;
    }
    /**
     * @return the strCode
     */
    public String getCode( )
    {
        return _strCode;
    }
    /**
     * @param strCode the strCode to set
     */
    public void setCode( String strCode )
    {
        _strCode = strCode;
    }
    /**
     * @return the nIdAccessControl
     */
    public int getIdAccessControl( )
    {
        return _nIdAccessControl;
    }
    /**
     * @param nIdAccessControl the nIdAccessControl to set
     */
    public void setIdAccessControl( int nIdAccessControl )
    {
        _nIdAccessControl = nIdAccessControl;
    }
    /**
     * @return the validityDate
     */
    public Date getValidityDate( )
    {
        return _validityDate;
    }
    /**
     * @param validityDate the validityDate to set
     */
    public void setValidityDate( Date validityDate )
    {
        _validityDate = validityDate;
    }
    
}
