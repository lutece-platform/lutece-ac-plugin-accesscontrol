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
package fr.paris.lutece.plugins.accesscontrol.web;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.editor.RichTextContentMethod;
import fr.paris.lutece.test.LuteceTestCase;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders the front office forms of the controller types with HTML auto-escaping (LUT-33153): the comment of a controller
 * is rich text an administrator entered with the editor of the back office, written with richText(): its formatting is
 * kept, its scripts and event handlers are removed by the sanitizer of the core, hence the CDI container of
 * LuteceTestCase. The core skin macros are replaced by stand-ins.
 */
public class ControllerCommentRenderTest extends LuteceTestCase
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String [ ] TEMPLATES = {
            "skin/plugins/accesscontrol/controller/age_controller_template.html",
            "skin/plugins/accesscontrol/controller/comment_controller_template.html",
            "skin/plugins/accesscontrol/controller/luteceuser_code_controller_template.html",
            "skin/plugins/accesscontrol/controller/tos_controller_template.html",
            "skin/plugins/accesscontrol/controller/user_code_controller_template.html"
    };
    /** Stand-ins of the core skin macros the forms call. */
    private static final String MACROS = "accesscontrol_test_skin_macros.ftl";
    private static final String MACROS_SOURCE = "<#macro cTpl rest...><#nested></#macro><#macro cRow rest...><#nested></#macro>"
            + "<#macro cCol rest...><#nested></#macro><#macro cLabel rest...></#macro><#macro cInput rest...></#macro>"
            + "<#macro cCheckbox rest...></#macro><#macro getDate rest...></#macro>";

    private static final String KEPT = "<p>Enter your <strong>code</strong> &amp; validate</p>";
    private static final String COMMENT = KEPT + "<script>alert(1)</script><p onclick=\"alert(2)\">Help</p>";

    /**
     * Renders a form with the configuration of AbstractFreeMarkerTemplateService for HTML templates and the richText()
     * method of the core.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the HTML
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), macros
        } ) );
        cfg.addAutoInclude( MACROS );
        cfg.setSharedVariable( "richText", new RichTextContentMethod( ) );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * Every form keeps the formatting of the comment and removes its scripts.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testCommentIsRichTextWithoutScripts( ) throws Exception
    {
        for ( String strTemplate : TEMPLATES )
        {
            String strHtml = render( strTemplate, Map.of( "config", Map.of( "comment", COMMENT ), "language", "fr" ) );

            assertTrue( strTemplate + " : " + strHtml, strHtml.contains( KEPT ) );
            assertTrue( strTemplate + " : " + strHtml, strHtml.contains( "<p>Help</p>" ) );
            assertFalse( strTemplate + " : " + strHtml, strHtml.contains( "alert(" ) );
            assertFalse( strTemplate + " : " + strHtml, strHtml.contains( "onclick" ) );
            assertFalse( strTemplate + " : " + strHtml, strHtml.contains( "&lt;" ) );
        }
    }

    /**
     * A configuration without comment writes nothing.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testNoComment( ) throws Exception
    {
        String strHtml = render( TEMPLATES [1], Map.of( "config", Map.of( ) ) );

        assertEquals( "", strHtml.trim( ) );
    }
}
