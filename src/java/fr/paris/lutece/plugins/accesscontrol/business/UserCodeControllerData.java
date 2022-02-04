/*
 * Copyright (c) 2002-2022, City of Paris
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
     * @param strUser
     *            the strUser to set
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
     * @param strCode
     *            the strCode to set
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
     * @param nIdAccessControl
     *            the nIdAccessControl to set
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
     * @param validityDate
     *            the validityDate to set
     */
    public void setValidityDate( Date validityDate )
    {
        _validityDate = validityDate;
    }

}
