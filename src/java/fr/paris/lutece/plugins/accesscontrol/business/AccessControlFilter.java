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

import org.apache.commons.lang3.StringUtils;

/**
 *
 * class AccessControlFilter
 *
 */
public class AccessControlFilter
{
    public static final String ALL_STRING = "all";
    public static final int ALL_INT = -1;
    public static final int FILTER_FALSE = 0;
    public static final int FILTER_TRUE = 1;
    private int _nIsEnabled = ALL_INT;
    private String _strWorkgroup = ALL_STRING;
    private String _strName = null;

    public String getName( )
    {
        return _strName;
    }

    public void setName( String name )
    {
        this._strName = name;
    }

    /**
     *
     * @return 1 if the access control return must be enabled 0 if the access control return must be disabled
     */
    public int getIsEnabled( )
    {
        return _nIsEnabled;
    }

    /**
     * Set 1 if the access control return must be enabled 0 if the access control return must be disabled
     * 
     * @param idState
     *            1 if the access control return must be enabled 0 if the access control return must be disabled
     */
    public void setIsEnabled( int idState )
    {
        _nIsEnabled = idState;
    }

    /**
     *
     * @return true if the filter contain access control state
     */
    public boolean containsIsEnabled( )
    {
        return ( _nIsEnabled != ALL_INT );
    }

    /**
     *
     * @return the workgroup of the access control
     */
    public String getWorkgroup( )
    {
        return _strWorkgroup;
    }

    /**
     * set the workgroup of the access control
     * 
     * @param workgroup
     *            the workgroup of the access control
     */
    public void setWorkGroup( String workgroup )
    {
        _strWorkgroup = workgroup;
    }

    /**
     *
     * @return true if the filter contaion access control criteria
     */
    public boolean containsWorkgroupCriteria( )
    {
        return ( !_strWorkgroup.equals( ALL_STRING ) );
    }

    public boolean containsName( )
    {

        return StringUtils.isNotBlank( _strName );
    }
}
