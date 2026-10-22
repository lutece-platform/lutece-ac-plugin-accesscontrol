/*
 * Copyright (c) 2002-2026, City of Paris
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
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
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
package fr.paris.lutece.plugins.accesscontrol.web;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * The page titles and paths used by the access control screens are defined in the bundles of the plugin
 */
public class AccessControlMessagesTest extends LuteceTestCase
{
    private static final String [ ] KEYS = {
            AccessControlXPage.MESSAGE_PAGE_TITLE, AccessControlXPage.MESSAGE_PATH, "accesscontrol.create_accesscontrol.pageTitle",
            "accesscontrol.modify_accesscontrol.pageTitle", "accesscontrol.permission.label.resourceType"
    };

    /**
     * Every key is translated in English and in French: a missing title or path makes the front office page fail and leaves the back office titles empty
     */
    @Test
    public void testPageTitlesAreTranslated( )
    {
        for ( Locale locale : new Locale [ ] {
                Locale.ENGLISH, Locale.FRENCH
        } )
        {
            for ( String strKey : KEYS )
            {
                String strValue = I18nService.getLocalizedString( strKey, locale );
                assertTrue( strKey + " " + locale, strValue != null && !strValue.isEmpty( ) );
            }
        }
    }
}
