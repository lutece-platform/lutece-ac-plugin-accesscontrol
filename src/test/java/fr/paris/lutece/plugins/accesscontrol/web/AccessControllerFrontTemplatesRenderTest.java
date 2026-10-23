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
package fr.paris.lutece.plugins.accesscontrol.web;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import fr.paris.lutece.portal.service.editor.RichTextContentMethod;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;

/**
 * Renders the front office forms of the user code and Lutece user code controllers with the real cLabel macro of the
 * core (read from the exploded webapp): the label text is given in label, the required parameter of cLabel; a call
 * with labelKey (a parameter of the back office formGroup) fails to render.
 */
public class AccessControllerFrontTemplatesRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String CORE_TEMPLATES_ROOT = "target/lutece/WEB-INF/templates";
    private static final String TEMPLATE_USER_CODE = "skin/plugins/accesscontrol/controller/user_code_controller_template.html";
    private static final String TEMPLATE_LUTECEUSER_CODE = "skin/plugins/accesscontrol/controller/luteceuser_code_controller_template.html";
    private static final String LABEL_USER_ID = "#i18n{accesscontrol.controller.userCodeAccessController.labelUserId}";
    private static final String LABEL_CODE = "#i18n{accesscontrol.controller.userCodeAccessController.labelCode}";

    /**
     * The real cLabel of the core; stand-ins for the layout macros and cInput, which write their nested content and the
     * field, and for the helpers cLabel calls (commonAttrs writes the id only).
     */
    private static final String MACROS = "accesscontrol_front_test_macros.ftl";
    private static final String MACROS_SOURCE = "<#macro deprecatedWarning args rest...></#macro>"
            + "<#macro commonAttrs id='' rest...><#if id?has_content> id=\"${id}\"</#if></#macro>"
            + "<#include '/skin/themes/macros/forms/inputs/cLabel.ftl'>"
            + "<#macro cTpl rest...><#nested></#macro><#macro cRow rest...><#nested></#macro><#macro cCol rest...><#nested></#macro>"
            + "<#macro cInput name id='' rest...><input name=\"${name}\" id=\"${id}\"></#macro>";

    /**
     * Renders a template of the plugin.
     *
     * @param strTemplate
     *            the template path
     * @return the HTML, white space collapsed
     * @throws Exception
     *             if the rendering fails
     */
    private static String render( String strTemplate ) throws Exception
    {
        assertTrue( new File( CORE_TEMPLATES_ROOT, "skin/themes/macros/forms/inputs/cLabel.ftl" ).isFile( ),
                "the core templates are read from the exploded webapp: run lutece:exploded before the tests" );
        StringTemplateLoader stubs = new StringTemplateLoader( );
        stubs.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setSharedVariable( "richText", new RichTextContentMethod( ) );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader[] {
                stubs, new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), new FileTemplateLoader( new File( CORE_TEMPLATES_ROOT ) )
        } ) );
        cfg.addAutoInclude( MACROS );
        Map<String, Object> config = new HashMap<>( );
        config.put( "comment", "Please enter your code" );
        Map<String, Object> model = new HashMap<>( );
        model.put( "config", config );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );

        return writer.toString( ).replaceAll( "\\s+", " " );
    }

    /**
     * The user code form renders a label for the user id and for the code, each tied to its field.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testUserCodeTemplateRendersLabels( ) throws Exception
    {
        String strHtml = render( TEMPLATE_USER_CODE );

        assertTrue( strHtml.contains( "Please enter your code" ), strHtml );
        assertTrue( strHtml.contains( "<label class=\"form-label\" for=\"userId\"> " + LABEL_USER_ID + " </label>" ), strHtml );
        assertTrue( strHtml.contains( "<input name=\"userId\" id=\"userId\">" ), strHtml );
        assertTrue( strHtml.contains( "<label class=\"form-label\" for=\"code\"> " + LABEL_CODE + " </label>" ), strHtml );
        assertTrue( strHtml.contains( "<input name=\"code\" id=\"code\">" ), strHtml );
    }

    /**
     * The Lutece user code form renders a label for the code, tied to its field.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testLuteceUserCodeTemplateRendersLabel( ) throws Exception
    {
        String strHtml = render( TEMPLATE_LUTECEUSER_CODE );

        assertTrue( strHtml.contains( "Please enter your code" ), strHtml );
        assertTrue( strHtml.contains( "<label class=\"form-label\" for=\"code\"> " + LABEL_CODE + " </label>" ), strHtml );
        assertTrue( strHtml.contains( "<input name=\"code\" id=\"code\">" ), strHtml );
    }
}
