/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.accesscontrol.business;

import java.io.Serializable;
import java.util.Locale;

import fr.paris.lutece.plugins.accesscontrol.service.IAccessControllerType;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This is the business class for the object AccessController
 */
public class AccessController implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;
    private String _strType;
    private int _nOrder;
    private int _nIdAccesscontrol;
    private String _strBoolCond;

    private transient String _title;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Type
     * 
     * @return The Type
     */
    public String getType( )
    {
        return _strType;
    }

    /**
     * Sets the Type
     * 
     * @param strType
     *            The Type
     */
    public void setType( String strType )
    {
        _strType = strType;
    }

    /**
     * Returns the Order
     * 
     * @return The Order
     */
    public int getOrder( )
    {
        return _nOrder;
    }

    /**
     * Sets the Order
     * 
     * @param nOrder
     *            The Order
     */
    public void setOrder( int nOrder )
    {
        _nOrder = nOrder;
    }

    /**
     * Returns the IdAccesscontrol
     * 
     * @return The IdAccesscontrol
     */
    public int getIdAccesscontrol( )
    {
        return _nIdAccesscontrol;
    }

    /**
     * Sets the IdAccesscontrol
     * 
     * @param nIdAccesscontrol
     *            The IdAccesscontrol
     */
    public void setIdAccesscontrol( int nIdAccesscontrol )
    {
        _nIdAccesscontrol = nIdAccesscontrol;
    }

    /**
     * Returns the BoolCond
     * 
     * @return The BoolCond
     */
    public String getBoolCond( )
    {
        return _strBoolCond;
    }

    /**
     * Sets the BoolCond
     * 
     * @param strBoolCond
     *            The BoolCond
     */
    public void setBoolCond( String strBoolCond )
    {
        _strBoolCond = strBoolCond;
    }

    /**
     * set the title of the control
     * 
     * @param locale
     * @return
     */
    public void setTitle( Locale locale )
    {
        _title = "";
        IAccessControllerType controller = SpringContextService.getBean( _strType );
        if ( controller != null )
        {
            _title = controller.getTitle( locale );
        }
    }

    /**
     * get the title of the control
     * 
     * @return
     */
    public String getTitle( )
    {
        return _title;
    }
    
    /**
     * checks if this controller has a config
     * @return
     */
    public boolean isConfig( )
    {
        IAccessControllerType controller = SpringContextService.getBean( _strType );
        return controller != null && controller.hasConfig( );
    }
}
