package fr.paris.lutece.plugins.accesscontrol.business.config;

/**
 * Abstract implementation of {@link IAccessControllerConfig}. <br />
 * Attributes:<br />
 * <ul>
 *  <li>Id Controller</li>
 *  <li>Comment to Display</li>
 *  <li>Error Message</li>
 * </ul>
 */
public abstract class AbstractControllerConfig implements IAccessControllerConfig
{
    private int _nIdAccessController;
    private String _strComment;
    private String _strErrorMessage;
    
    /**
     * @return the strErrorMessage
     */
    public String getErrorMessage( )
    {
        return _strErrorMessage;
    }

    /**
     * @param strErrorMessage the strErrorMessage to set
     */
    public void setErrorMessage( String strErrorMessage )
    {
        _strErrorMessage = strErrorMessage;
    }
    
    @Override
    public int getIdAccessController( )
    {
        return _nIdAccessController;
    }

    @Override
    public void setIdAccessController( int idAccessController )
    {
        _nIdAccessController = idAccessController;
    }

    /**
     * @return the strComment
     */
    public String getComment( )
    {
        return _strComment;
    }

    /**
     * @param strComment the strComment to set
     */
    public void setComment( String strComment )
    {
        _strComment = strComment;
    }
}
