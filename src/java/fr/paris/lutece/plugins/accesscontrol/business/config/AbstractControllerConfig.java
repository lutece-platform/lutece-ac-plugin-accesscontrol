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
package fr.paris.lutece.plugins.accesscontrol.business.config;

/**
 * Abstract implementation of {@link IAccessControllerConfig}. <br />
 * Attributes:<br />
 * <ul>
 * <li>Id Controller</li>
 * <li>Comment to Display</li>
 * <li>Error Message</li>
 * </ul>
 */
public abstract class AbstractControllerConfig implements IAccessControllerConfig
{
    private int _nIdAccessController;
    private String _strComment;
    private String _strErrorMessage;
    private String _dataHandler;

    /**
     * @return the strErrorMessage
     */
    public String getErrorMessage( )
    {
        return _strErrorMessage;
    }

    /**
     * @param strErrorMessage
     *            the strErrorMessage to set
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
     * @param strComment
     *            the strComment to set
     */
    public void setComment( String strComment )
    {
        _strComment = strComment;
    }

    /**
     * @return the dataHandler
     */
    public String getDataHandler( )
    {
        return _dataHandler;
    }

    /**
     * @param dataHandler the dataHandler to set
     */
    public void setDataHandler( String dataHandler )
    {
        _dataHandler = dataHandler;
    }
    
}
