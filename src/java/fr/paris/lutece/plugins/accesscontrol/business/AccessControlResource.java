package fr.paris.lutece.plugins.accesscontrol.business;

public class AccessControlResource
{
    private int _nIdResource;
    private String _strResourceType;
    private int _nIdAccessControl;
    
    /**
     * @return the nIdResource
     */
    public int getIdResource( )
    {
        return _nIdResource;
    }
    /**
     * @param nIdResource the nIdResource to set
     */
    public void setIdResource( int nIdResource )
    {
        _nIdResource = nIdResource;
    }
    /**
     * @return the strResourceType
     */
    public String getResourceType( )
    {
        return _strResourceType;
    }
    /**
     * @param strResourceType the strResourceType to set
     */
    public void setResourceType( String strResourceType )
    {
        _strResourceType = strResourceType;
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
}
