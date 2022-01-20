package fr.paris.lutece.plugins.accesscontrol.business.config;

/**
 * Config for CommentAccessControllerType
 */
public class TosAccessControllerConfig implements IAccessControllerConfig
{

    private int _nIdAccessController;
    private String _strComment;
    private String _strErrorMessage;
    
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

}
