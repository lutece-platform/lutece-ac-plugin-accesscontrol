package fr.paris.lutece.plugins.accesscontrol.business.config;

/**
 * Config for AgeAccessControllerType
 */
public class AgeAccessControllerConfig extends AbstractControllerConfig
{
    private int _nAgeMin;
    private int _nAgeMax;
    
    /**
     * @return the nAgeMin
     */
    public int getAgeMin( )
    {
        return _nAgeMin;
    }
    /**
     * @param nAgeMin the nAgeMin to set
     */
    public void setAgeMin( int nAgeMin )
    {
        _nAgeMin = nAgeMin;
    }
    /**
     * @return the nAgeMax
     */
    public int getAgeMax( )
    {
        return _nAgeMax;
    }
    /**
     * @param nAgeMax the nAgeMax to set
     */
    public void setAgeMax( int nAgeMax )
    {
        _nAgeMax = nAgeMax;
    }
}
