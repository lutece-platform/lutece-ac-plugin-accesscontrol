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

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.accesscontrol.service.rbac.AccessControlRbacAction;
import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;

/**
 * This is the business class for the object AccessControl
 */
public class AccessControl implements Serializable, AdminWorkgroupResource, RBACResource
{
    private static final long serialVersionUID = 1L;

    public static final String RESOURCE_TYPE = "ACCESCONTROL_ACCESCONTROL";

    // Variables declarations
    private int _nId;

    private String _strName;
    private String _strDescription;
    private Date _dateCreationDate;
    private boolean _bIsEnabled;
    private String _strWorkgroupKey;
    private String _strReturnUrl;

    private List<AccessControlRbacAction> _actionList;

    @Override
    public String getResourceTypeCode( )
    {
        return RESOURCE_TYPE;
    }

    @Override
    public String getResourceId( )
    {
        return StringUtils.EMPTY + _nId;
    }

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
     * Returns the Name
     * 
     * @return The Name
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Sets the Name
     * 
     * @param strName
     *            The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Description
     * 
     * @return The Description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     * 
     * @param strDescription
     *            The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the CreationDate
     * 
     * @return The CreationDate
     */
    public Date getCreationDate( )
    {
        return _dateCreationDate;
    }

    /**
     * Sets the CreationDate
     * 
     * @param dateCreationDate
     *            The CreationDate
     */
    public void setCreationDate( Date dateCreationDate )
    {
        _dateCreationDate = dateCreationDate;
    }

    /**
     * Returns the IsEnabled
     * 
     * @return The IsEnabled
     */
    public boolean isEnabled( )
    {
        return _bIsEnabled;
    }

    /**
     * Sets the IsEnabled
     * 
     * @param bIsEnabled
     *            The IsEnabled
     */
    public void setEnabled( boolean bIsEnabled )
    {
        _bIsEnabled = bIsEnabled;
    }

    /**
     *
     * @return the work group associate to the workflow
     */
    public String getWorkgroup( )
    {
        return _strWorkgroupKey;
    }

    /**
     * set the work group associate to the workflow
     * 
     * @param workGroup
     *            the work group associate to the workflow
     */
    public void setWorkgroup( String workGroup )
    {
        _strWorkgroupKey = workGroup;
    }

    /**
     * @return the strReturnUrl
     */
    public String getReturnUrl( )
    {
        return _strReturnUrl;
    }

    /**
     * @param strReturnUrl
     *            the strReturnUrl to set
     */
    public void setReturnUrl( String strReturnUrl )
    {
        _strReturnUrl = strReturnUrl;
    }

    /**
     * @return the actionList
     */
    public List<AccessControlRbacAction> getActionList( )
    {
        return _actionList;
    }

    /**
     * @param actionList
     *            the actionList to set
     */
    public void setActionList( List<AccessControlRbacAction> actionList )
    {
        _actionList = actionList;
    }
}
